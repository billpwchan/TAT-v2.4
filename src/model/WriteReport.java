/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.CaseExecutions;
import DB.Iterations;
import DB.Macro;
import DB.ParamScriptMacro;
import DB.ParametersExecution;
import DB.Script;
import DB.ScriptExecutions;
import DB.StepExecutions;
import DBcontroller.ScriptDB;
import DBcontroller.TestExecution;
import DBcontroller.sessionFactorySingleton;
import controller.tabtestexecution.TabTestCampaignExecutionRepositoryBaselineController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author tmorin
 */
public class WriteReport {

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    private int campaignID;

    private String baselineID;

    /**
     * File Name to write to.
     */
    private String FILE_NAME;

    /**
     * Current Row in Excel workbook.
     */
    private int currentRow = 0;

    /**
     * Current Iteration.
     */
    private static int currIt = 0;

    /**
     * Number of Iterations.
     */
    private int numIt;

    /**
     * Hard-coded Header for Excel Report.
     */
    private static final String[] tempHeader = {"Overall Result", "EventList Result", "Data Type", "Register", "Register Offset", "Triggering State"};

    /**
     * HashMap that maps Cell Coordinates to CellStyle.
     */
    private HashMap<Coordinate, CellStyle> styleMap = new HashMap<Coordinate, CellStyle>();

    /**
     *
     */
    public WriteReport() {
        this.workbook = new XSSFWorkbook();
    }

    /**
     *
     * @param it
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void readTemplateReport(Iterations it) throws FileNotFoundException, IOException {
        String template = "Report_Temp.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(template));
        this.workbook = new XSSFWorkbook(inputStream);
        this.workbook.getSheetAt(0).getRow(18).getCell(7).setCellValue(it.getDate());
        this.sheet = this.workbook.createSheet("Report");
    }

    /**
     * Create Report Function (For Successfully executed report only
     *
     * @param it Iterations DataType
     */
    public void createReport(Iterations it) {

        this.FILE_NAME = "./Reports/" + it.getBaselineId() + "_Report_Iteration_" + it.getIterationNumber() + ".xlsx";
        try {
            this.readTemplateReport(it);
        } catch (Exception ex) {
            Logger.getLogger(WriteReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setHeaderFileRows();
        this.set(it);
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            this.workbook.write(outputStream);
            this.workbook.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WriteReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WriteReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Report Created");
    }

    /**
     *
     * @param iteration
     */
    public void getStepExecutions(Iterations iteration) {

        Iterator<CaseExecutions> itCaseExecutions = iteration.getCaseExecutionses().iterator();

    }

    /**
     *
     */
    public void setHeaderFileRows() {

        //XSSFSheet sheet = this.workbook.createSheet("Report");
        //Fill in First Header Row
        Row row = this.sheet.createRow(0);
        int colNum = 0;
        for (int i = 0; i < tempHeader.length; i++) {
            Cell cell = row.createCell(colNum);
            cell.setCellValue(tempHeader[i]);
            CellStyle style = this.workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(style);
            colNum++;
            this.sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
        }

        this.currentRow += 2; //because we merge cells

    }

    /**
     *
     * @param iteration
     */
    public void set(Iterations iteration) {

        TestExecution testExecHandler = new TestExecution();
        ArrayList<CaseExecutions> caseExecutionsList;
        this.campaignID = iteration.getTestCampaign().getIdtestCampaign();
        this.baselineID = iteration.getBaselineId();
        TestCasesExecution testCaseExecution = new TestCasesExecution();

        caseExecutionsList = testCaseExecution.PrepareCaseDisplayResults(baselineID, iteration.getIterationNumber());

        // caseExecutionsList = testCaseExecution.PrepareCaseDisplayResults(this.baselineID, this.campaignID);
        Iterator<CaseExecutions> itCaseEx = caseExecutionsList.listIterator();

        int caseNum = 0;

        List<String> searchOccParamList = new ArrayList<>();
        boolean hasSearchOccParamConf = false;

        //number of Test Cases
        while (itCaseEx.hasNext()) {

            CaseExecutions currCaseEx = new CaseExecutions();
            currCaseEx = itCaseEx.next();

            //List of Register and Regiser Offsets
            List<String> registerList = new ArrayList<>();

            testCaseExecution.PrepareStepsScriptsParametersDisplayResults(currCaseEx, iteration);
            testCaseExecution.prepareCaseDisplay(currCaseEx);

            String res = currCaseEx.getSimpleStringResultProperty();
            System.out.println("                            Overall Case Result: " + res + "\n");
            if (caseNum != 0) {
                Row row = this.sheet.createRow(this.currentRow);
                Cell cell = row.createCell(0);
                cell.setCellValue(res);
                if (res.equals("NOK")) {
                    CellStyle red = getRedCellStyle(this.workbook);
                    cell.setCellStyle(red);
                }
                System.out.println("OverallCase Result put in excel sheet at row: " + this.currentRow);
                this.currentRow++;

            }

            Set<StepExecutions> stepExSet = new HashSet<>();
            stepExSet = currCaseEx.getStepExecutionses();

            Iterator<StepExecutions> itStepEx = stepExSet.iterator();

            int stepNumber = 0;

            //List for stepParameters for !isStimuli scripts
            List<String> stepParameters = new ArrayList<>();

            //number of Steps in Test Case
            while (itStepEx.hasNext()) {

                List<String> paramSearchList = new ArrayList<>();
                List<String> paramFoundList = new ArrayList<>();

                StepExecutions currStepEx = itStepEx.next();
                //stepExResultSet -> has all the EventLResults: eg. OK, NOK, OS...
                String overallStepResult = currStepEx.getStepExecutionResult();

                int maxStep = 0;

//                ScriptExecutions bufferScriptEx = new ScriptExecutions();
//                List<String> parametersOfBufferScriptEx = new ArrayList<String>();
                Set<ScriptExecutions> scriptExSet = currStepEx.getScriptExecutionses();
                Iterator<ScriptExecutions> itScriptEx = scriptExSet.iterator();
                boolean isFirstStimuliScript = true;
                //either 1 or 2 script per step
                int scriptNum = 0;
                String scriptType = "";
                while (itScriptEx.hasNext()) {

                    ScriptExecutions scriptEx = itScriptEx.next();

                    System.out.println("CaseNum: " + caseNum + " StepNum: " + stepNumber + " scriptNum :" + scriptNum + " ScriptName: " + scriptEx.getScript().getName());
                    System.out.println("");
                    //Check if ScriptExecution is a stimuli
                    //if getIsStimuli = 1 ---> script on left side
                    //if getIsStimuli = 0 ---> script on right side
                    boolean isStimuli;
                    if (scriptEx.getIsStimuli() == 0) {
                        isStimuli = false;
                    } else {
                        isStimuli = true;
                    }

                    Script script = scriptEx.getScript();
                    switch (script.getName()) {
                        case "Trigger of DI (Modbus)":
                            scriptType = "DI";
                            maxStep = 2;
                            break;
                        case "Trigger Modbus (DI2)":
                            scriptType = "DI2";
                            maxStep = 4;
                            break;
                        case "Trigger of AI (Modbus)":
                            scriptType = "AI";
                            maxStep = 2;
                            break;
                    }

                    Set<ParametersExecution> paramExSet = scriptEx.getParametersExecutions();
                    Iterator<ParametersExecution> itParamEx = paramExSet.iterator();
//                    Hibernate.initialize(script.getMacrosForScriptIdScript());
//                    Iterator<Macro> itMacro = script.getMacrosForScriptIdScript().iterator();
//                    Integer macroParameterIndex = 0;
//                    ArrayList<Integer> tempIndex = new ArrayList<>();
//                    while (itMacro.hasNext()) {
//                        Macro setMacro = itMacro.next();
//                        
//                        Hibernate.initialize(setMacro.getScriptByScriptIdScript1());
//                        
//                        if (setMacro.getScriptByScriptIdScript1().getName().equalsIgnoreCase("Search occurrence")) {
//                            System.out.println("Entered first: "+ macroParameterIndex);
//                            tempIndex.add(macroParameterIndex);
//                        }
//                        macroParameterIndex++;
//
//                    }
                    int numParameter = 0;
//                    macroParameterIndex = 0;
                    //number of parameters inputed in TAT on each script in one step
                    while (itParamEx.hasNext()) {
                        ParametersExecution paramEx = itParamEx.next();
                        String paramSearched = paramEx.getValue();
                        System.out.println("numParameter: " + numParameter + " paramSearched: " + paramSearched);
                        if (!isStimuli) {
                            System.out.println("Script is NOT stimuli, parameter " + paramSearched + " added in paramSearchList");
                            if (paramEx.getParameters().getName().equalsIgnoreCase("searched value")) {     //This specific scripts is only used in Search Occurance. 
                                paramSearchList.add(paramSearched);
                            }
                            //Parameters searched
                        } else {
                            if ("DI".equals(scriptType) || "DI2".equals(scriptType)) {
                                if (stepNumber == 0 && numParameter != 0) {
                                    double reg = Double.parseDouble(paramSearched);
                                    int regInt = (int) Math.round(reg);
                                    registerList.add(String.valueOf(regInt));
                                    System.out.println("Register/RegOffset: " + paramSearched + " added to registerList");
                                } else if (numParameter == 2 && "DI".equals(scriptType)) {
                                    try {
                                        double regOff = Double.parseDouble(paramSearched);
                                        if (regOff > Double.parseDouble(registerList.get(numParameter - 1))) {
                                            registerList.remove(numParameter - 1);
                                            int regOffInt = (int) Math.round(regOff);
                                            registerList.add(numParameter - 1, String.valueOf(regOffInt));
                                            System.out.println("RegisterOffset: " + regOff + " added and replaced previous offset");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                } else if (numParameter == 4 && "DI2".equals(scriptType)) {
                                    try {
                                        double regOff = Double.parseDouble(paramSearched);
                                        if (regOff > Double.parseDouble(registerList.get(numParameter - 1))) {
                                            registerList.remove(numParameter - 1);
                                            registerList.add(numParameter, String.valueOf(regOff));
                                            System.out.println("RegisterOffset: " + regOff + " added and replaced previous offset");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        numParameter++;
                    }
//                    if (isStimuli) {
//                        parametersOfBufferScriptEx = paramSearchList;
//                    }

                    //Only if isStimuli = 0
                    if (!isStimuli) {
                        String scriptExResult = scriptEx.getScriptExecutionResult();
                        String scriptExComment = scriptEx.getScriptExecutionComment();
                        //Either 2 for DI, 4 for DI2
                        paramFoundList = getParamFound(paramSearchList, scriptExComment);
                        Iterator it = paramFoundList.iterator();
                        int c = 0;
                        while (it.hasNext()) {
                            System.out.println("paramFoundList " + c++ + ": " + it.next());
                        }

                    }

                    //getting param script macro
                    if (caseNum != 0 && scriptNum != 0) {
                        ScriptDB scDB = new ScriptDB();
                        scDB.getAllFromParamScriptMacro(script);

                        Iterator<Macro> it2 = scriptEx.getScript().getMacrosForScriptIdScript().iterator();
                        ParamScriptMacro temp = new ParamScriptMacro();
                        int i = 0;
                        boolean enteredWhile = false;
                        while (it2.hasNext()) {
                            Iterator<ParamScriptMacro> paramIt = it2.next().getParamScriptMacros().iterator();
                            while (paramIt.hasNext() && i != 0 && !hasSearchOccParamConf) {
                                temp = paramIt.next();
                                if (temp.getParamOrder() == 0) {
                                    String[] tempStr = temp.getValue().split("Search");
                                    String param = tempStr[1].trim();
                                    searchOccParamList.add(param);
                                    System.out.println("            paramScriptMacro Value: " + param);
                                }
                                enteredWhile = true;
                            }
                            i++;
                        }
                        if (enteredWhile == true) {
                            hasSearchOccParamConf = true;
                        }
                        System.out.println("\n");
                    }
                    scriptNum++;
                }

                //write register and offset
                if (caseNum != 0 && stepNumber != 0) { //
                    Row row = this.sheet.createRow(this.currentRow);
                    Cell cellR = row.createCell(1); //column = 1
                    cellR.setCellValue(overallStepResult);
                    if (overallStepResult.equals("NOK")) {
                        CellStyle red = getRedCellStyle(this.workbook);
                        cellR.setCellStyle(red);
                    }

                    Cell cell = row.createCell(3);
                    cell.setCellValue(registerList.get(0));         //Register Offset
                    Cell cell2 = row.createCell(4);
                    cell2.setCellValue(registerList.get(1));        //Register

                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(scriptType);                 //Data Type

                    Cell cell4 = row.createCell(5);
                    cell4.setCellValue(String.valueOf((stepNumber) % maxStep));         //Triggering State

                    //write parameters
                    for (int i = 0; i < paramSearchList.size(); i++) {

                        Cell cellS = row.createCell(tempHeader.length + 2 * i);
                        Cell cellF = row.createCell(tempHeader.length + 2 * i + 1);
                        String search = "";
                        String found = "";
                        try {
                            search = paramSearchList.get(i);
                            found = paramFoundList.get(i);
                        } catch (IndexOutOfBoundsException ex) {
                            found = "N/A";
                        }
                        cellS.setCellValue(search);
                        cellF.setCellValue(found);
                        if (!search.equals(found)) {
                            CellStyle red = getRedCellStyle(this.workbook);
                            cellS.setCellStyle(red);
                            cellF.setCellStyle(red);
                        }

                    }
                    this.currentRow++;

                }

                System.out.println("Step result of above step:" + overallStepResult + "\n");

                if (caseNum == 1 && stepNumber == 1) {

                }
                stepNumber++;
            }
            caseNum++;
        }

        //Create Header with (Search & Found)
        Row row = this.sheet.getRow(0);
        Row row2 = this.sheet.createRow(1);
        for (int i = 0; i < searchOccParamList.size(); i++) {
            Cell cell = row.createCell(tempHeader.length + 2 * i);
            cell.setCellValue(searchOccParamList.get(i));
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(style);
            this.sheet.addMergedRegion(new CellRangeAddress(0, 0, tempHeader.length + 2 * i, tempHeader.length + 2 * i + 1));
            Cell cellSearch = row2.createCell(tempHeader.length + 2 * i);
            cellSearch.setCellValue("Searched");
            Cell cellFound = row2.createCell(tempHeader.length + 2 * i + 1);
            cellFound.setCellValue("Found");
        }

    }

    /**
     *
     * @param paramSearch
     * @param comment
     * @return
     */
    public static List<String> getParamFound(List<String> paramSearch, String comment) {

        String[] split = comment.split("\n");
        List<String> paramFound = new ArrayList<>();

        System.out.println("getParamFound split string length: " + split.length);
        int count = 0;
        for (int i = 2; i < split.length; i++) {
            String[] words = split[i].split(" ");
            if (words[2].equalsIgnoreCase("NOK") && words.length > 3) {           //NOK Mismatch
                i += 2;
                paramFound.add(split[i].substring(split[i].indexOf("=")+1).trim());
                count++;
                continue;
            }

            try {
                paramFound.add(paramSearch.get(count));
            } catch (IndexOutOfBoundsException ex) {
                paramFound.add("N/A");
            }
            count++;
        }

        return paramFound;

    }

    /**
     *
     * @param workbook
     * @return
     */
    public static CellStyle getRedCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return FILE_NAME;
    }

}
