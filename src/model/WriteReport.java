/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.*;
import DBcontroller.ScriptDB;
import DBcontroller.TestCampaignDB;
import controller.util.CommonFunctions;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * @author tmorin
 */
public class WriteReport {

    /**
     * Hard-coded Header for Excel Report.
     */
    private static final String[] tempHeader = {"Overall Result", "EventList Result", "Data Type", "Register", "Register Offset", "Triggering State"};
    private static final String[] tempHeaderCIP = {"Overall Result", "EventList Result", "Data Type", "Program Tag", "Full Tag", "Triggering State"};
    private static final String[] tempReportHeader = {"Register_Address/File", "Bit_offset", "Result", "Associated defect (PCR ID)", "Comment on result", "System version under test", "Date", "Tester"};
    private static final String[] tempReportHeaderCIP = {"Program Tag", "Full Tag", "Result", "Associated defect (PCR ID)", "Comment on result", "System version under test", "Date", "Tester"};
    private static final HashMap<String, int[]> STDInformation = new HashMap<>();
    private static final HashMap<String, int[]> authorInformation = new HashMap<>();
    private static final HashMap<String, int[]> STRResults = new HashMap<>();
    private static final HashMap<String, int[]> STRInformation = new HashMap<>();
    private static final HashMap<String, Integer> caseExeResult = new HashMap<>();
    private static final String[] scriptTypeName = {"DI2", "DI", "AI", "DO", "IEC", "CIP"};      //DI2 has to be the first element (It will iterate through each element)
    private static final Integer[] scriptTypeMaxStep = {4, 2, 2, 2, 4, 8};
    private final String colStationKey = "Station";
    private final String colEqpt_CodeKey = "EQP Code";
    private final String colEqpt_DescriptionKey = "EQP Description";
    private final String colEqpt_IdentifierKey = "EQP Number";
    private final String colAttribute_DescriptionKey = "Attribute Description";
    private final String colStateKey = "State";
    //Initialize CCS.PSD Variables
    private final int colStation = 0;
    private final int colEqpt_Code = 1;
    private final int colEqpt_Description = 2;
    private final int colEqpt_Identifier = 3;
    private final int colAttribute_Description = 4;
    private final int colDC_Data_Type = 5;
    private final int colv0_label0 = 6;
    private final int colv0_Severity = 7;
    private final int colv0_State = 8;
    private final int colRegister_Address = 9;
    private final int colBit_offset = 10;
    private final int colResult = 11;
    private final int colAssociatedDefect = 12;
    private final int colCommentOnResult = 13;
    private final int colSystemVersionUnderTest = 14;
    private final int colDate = 15;
    private final int colTester = 16;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private XSSFSheet reportSheet;
    private XSSFSheet summarySheet;
    private TestCampaignDB testCampaignController = new TestCampaignDB();
    private int campaignID;
    private String baselineID;
    private String campaignReference;
    private String campainWriter;
    private String campainCreationDate;
    /**
     * File Name to write to.
     */
    private String FILE_NAME;
    /**
     * Current Row in Excel workbook.
     */
    private int currentRow = 0;
    private int currentRowGlobal = 0;
    private int reportCurrentRow = 1;
    /**
     * Number of Iterations.
     */
    private int numIt;
    private CreationHelper createHelper;
    private int reportMaxStep = 0;
    private String scriptTypeGlobal = "";
    //Initialize paramSearchListIndex for SummarySheet
    private int paramIndex_Station;
    private int paramIndex_EQPCode;
    private int paramIndex_EQPNumber;
    private int paramIndex_EQPDescription;
    private int paramIndex_AttrDescription;
    private int paramIndex_State;
    private int paramIndex_AlarmSeverity;

    //Initialize Cell Styles
    private CellStyle cellStyle1;
    private CellStyle cellStyle2;
    private CellStyle cellStyle3;
    private CellStyle cellStyle4;
    private CellStyle cellStyle5;

    /**
     *
     */
    public WriteReport() {
        this.workbook = new XSSFWorkbook();
    }

    /**
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
            if (words.length > 3 && words[2].equalsIgnoreCase("NOK")) {           //NOK Mismatch
                i += 2;
                paramFound.add(split[i].substring(split[i].indexOf("=") + 1).trim());
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
     * @param workbook
     * @return
     */
    private static CellStyle getRedCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        return style;
    }

    private static CellStyle getDarkBlueCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        return style;
    }

    private static CellStyle getGreyCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        return style;
    }

    /**
     * @param it
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void readTemplateReport(Iterations it) throws FileNotFoundException, IOException {
        String template = "Report_Temp.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(template));
        this.workbook = new XSSFWorkbook(inputStream);
        this.workbook.getSheetAt(0).getRow(18).getCell(7).setCellValue(it.getDate());
        this.sheet = this.workbook.createSheet("Raw Result");       //It should be automatically put at the end.
        this.reportSheet = this.workbook.getSheet("Report");
        this.summarySheet = this.workbook.getSheet("Summary");
        this.createHelper = this.workbook.getCreationHelper();
    }

    private void initHashMaps() {
        //Initialize Pre-defined CellStyle from Template
        this.cellStyle1 = this.reportSheet.getRow(0).getCell(this.colStation).getCellStyle();
        this.cellStyle2 = this.reportSheet.getRow(0).getCell(this.colAttribute_Description).getCellStyle();
        this.cellStyle3 = this.reportSheet.getRow(0).getCell(this.colResult).getCellStyle();
        this.cellStyle4 = this.reportSheet.getRow(1).getCell(this.colResult).getCellStyle();
        this.cellStyle5 = this.reportSheet.getRow(1).getCell(this.colAssociatedDefect).getCellStyle();

        WriteReport.STDInformation.put("STD ID", new int[]{4, 3 - 1});

        WriteReport.authorInformation.put("Written by", new int[]{7, 3 - 1});
        WriteReport.authorInformation.put("Writing date", new int[]{7, 5 - 1});

        WriteReport.STRInformation.put("IO schedule version", new int[]{4, 20 - 1});
        WriteReport.STRInformation.put("Total steps", new int[]{4, 23 - 1});

        //Initialize Summary Sheet's Specific Cell Place. (Zero-based coordinate)
        WriteReport.STRResults.put("Not Tested", new int[]{4, 26 - 1});
        WriteReport.STRResults.put("OK", new int[]{4, 27 - 1});
        WriteReport.STRResults.put("OKWC", new int[]{4, 28 - 1});
        WriteReport.STRResults.put("NOK", new int[]{4, 29 - 1});
        WriteReport.STRResults.put("Not Testable", new int[]{4, 30 - 1});
        WriteReport.STRResults.put("Out Of Scope", new int[]{4, 31 - 1});
        WriteReport.STRResults.put("Test case result", new int[]{4, 32 - 1});

        WriteReport.caseExeResult.put("NExec", 0);
        WriteReport.caseExeResult.put("OK", 0);
        WriteReport.caseExeResult.put("OKWC", 0);
        WriteReport.caseExeResult.put("NOK", 0);
        WriteReport.caseExeResult.put("Not Testable", 0);
        WriteReport.caseExeResult.put("OS", 0);
        WriteReport.caseExeResult.put("Test case result", 0);
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
            CommonFunctions.debugLog.error("", ex);
        }
        this.initHashMaps();
        this.reportSheetOffsetInit(it);
        this.setHeaderFileRows(scriptTypeGlobal);
        this.setReportHeaderFileRows(scriptTypeGlobal);
        this.set(it);
        this.updateSTDInformation();
        this.updateAuthorInformation();
        this.updateSTRInformation();
        this.updateSTRResults();

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            this.workbook.write(outputStream);
            this.workbook.close();
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        System.out.println("Report Created");
    }

    /**
     *
     */
    private void setHeaderFileRows(String scriptType) {
        //Fill in First Header Row
        CellStyle style = this.workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Row row = this.sheet.createRow(0);
        int colNum = 0;
        String[] header;
        switch (scriptType) {
            case "CIP":
                header = tempHeaderCIP;
                break;
            default:
                header = tempHeader;
                break;
        }
        for (int i = 0; i < header.length; i++) {
            Cell cell = row.createCell(colNum);
            cell.setCellValue(header[i]);
            cell.setCellStyle(style);
            colNum++;
            this.sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
        }
        this.currentRow += 2; //because we merge cells

        //this.reportMaxStep records the maximum step it contians. If it is 4, the we should add 3 * (4-1) columns to the worksheet.
    }

    private void setReportHeaderFileRows(String scriptType) {
        Row row = this.reportSheet.getRow(0);
//        this.cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 1; i <= this.reportMaxStep; i++) {
            Cell cell = row.createCell(this.colv0_label0 + (i - 1) * 3);
            cell.setCellValue("v" + (i - 1) + "_label(" + i + ")");
            cell.setCellStyle(this.cellStyle1);
            cell = row.createCell(this.colv0_Severity + (i - 1) * 3);
            cell.setCellValue("v" + (i - 1) + "_Severity");
            cell.setCellStyle(this.cellStyle1);
            cell = row.createCell(this.colv0_State + (i - 1) * 3);
            cell.setCellValue("v" + (i - 1) + "_State");
            cell.setCellStyle(this.cellStyle1);
        }
        String[] header;
        switch (scriptType) {
            case "CIP":
                header = tempReportHeaderCIP;
                break;
            default:
                header = tempReportHeader;
                break;
        }
        for (int i = 0; i < header.length; i++) {
            Cell cell = row.createCell(this.colRegister_Address + (this.reportMaxStep - 1) * 3 + i);
            cell.setCellValue(header[i]);
            if (i > 1) {     //If greater than 1, means it is after "Result" Column
                cell.setCellStyle(cellStyle3);
            } else {
                cell.setCellStyle(cellStyle1);
            }
        }
    }

    /**
     * Obtain the maximum step in this iterations.
     *
     * @param iteration
     */
    private void reportSheetOffsetInit(Iterations iteration) {
        TestCasesExecution testCaseExecution = new TestCasesExecution();
        this.campaignID = iteration.getTestCampaign().getIdtestCampaign();
        this.baselineID = iteration.getBaselineId();
        ArrayList<CaseExecutions> caseExecutionsList = testCaseExecution.PrepareCaseDisplayResults(baselineID, iteration.getIterationNumber());
        for (CaseExecutions currCaseEx : caseExecutionsList) {
            testCaseExecution.PrepareStepsScriptsParametersDisplayResults(currCaseEx, iteration);
            testCaseExecution.prepareCaseDisplay(currCaseEx);
            Set<StepExecutions> stepExSet = currCaseEx.getStepExecutionses();
            for (StepExecutions currStepEx : stepExSet) {
                Set<ScriptExecutions> scriptExSet = currStepEx.getScriptExecutionses();
                for (ScriptExecutions scriptEx : scriptExSet) {
                    Script script = scriptEx.getScript();
                    for (int i = 0; i < scriptTypeName.length; i++) {
                        if (script.getName().contains("CIP") && script.getName().contains("DI")) {
                            this.reportMaxStep = Math.max(8, this.reportMaxStep);
                        }
                        if (script.getName().contains("Modbus") && script.getName().contains("DO")) {
                            this.reportMaxStep = 2;
                        }
                        if (script.getName().contains("Modbus") && script.getName().contains("DOCMB")) {
                            this.reportMaxStep = 1;
                        }
                        if (script.getName().contains("Modbus") && script.getName().contains("AO")) {
                            this.reportMaxStep = 1;
                        }
                        if (script.getName().contains(scriptTypeName[i])) {
                            this.scriptTypeGlobal = scriptTypeName[i];
                            this.reportMaxStep = Math.max(scriptTypeMaxStep[i], this.reportMaxStep);
                        }
                    }
                }
            }
        }
        System.out.println("Maximum Step: " + this.reportMaxStep);
        CommonFunctions.debugLog.info("Maximum Step Number of Baseline: " + this.reportMaxStep);
    }

    /**
     * @param iteration
     */
    public void set(Iterations iteration) {

        ArrayList<CaseExecutions> caseExecutionsList;
        this.campaignID = iteration.getTestCampaign().getIdtestCampaign();
        TestCampaign testCampaign = testCampaignController.getTestCampaignFromID(this.campaignID);
        this.campaignReference = testCampaign.getReference();
        this.campainWriter = testCampaign.getWritter();
        this.campainCreationDate = testCampaign.getCreationDate();
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
            boolean DO_FLAG = false;

            Row reportRow = this.reportSheet.createRow(this.reportCurrentRow);

            CaseExecutions currCaseEx = itCaseEx.next();

            //List of Register and Regiser Offsets
            List<String> registerList = new ArrayList<>();

            testCaseExecution.PrepareStepsScriptsParametersDisplayResults(currCaseEx, iteration);
            testCaseExecution.prepareCaseDisplay(currCaseEx);

            String res = currCaseEx.getSimpleStringResultProperty();
            Cell cell;

            //Update Result Value
            WriteReport.caseExeResult.put(res, WriteReport.caseExeResult.get(res) + 1);
            WriteReport.caseExeResult.put("Test case result", WriteReport.caseExeResult.get("Test case result") + 1);

            System.out.println("Overall Case Result: " + res + "\n");

            if (!this.scriptTypeGlobal.contains("DI") || (caseNum != 0)) {
                //Manipulate result in both Report worksheet & Raw Result Worksheet.
                this.currentRowGlobal = this.currentRow;
                Row row = this.sheet.createRow(this.currentRow);
                cell = row.createCell(0);
                cell.setCellValue(res);
                Cell reportCell = reportRow.createCell(this.colResult + (this.reportMaxStep - 1) * 3);
                reportCell.setCellValue(res.equals("NExec") ? "NT" : res);      //Need to modify
                switch (res) {
                    case "OK":
                        cellStyle4.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
                        break;
                    case "NExec":
                        cellStyle4.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
                        break;
                    case "NOK":
                        cellStyle4.setFillForegroundColor(IndexedColors.RED.getIndex());
                        break;
                    case "OS":
                        cellStyle4.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
                        break;
                }
                reportCell.setCellStyle(cellStyle4);

                switch (res) {
                    case "NOK":
                        cell.setCellStyle(getRedCellStyle(this.workbook));
                        reportCell.setCellStyle(getRedCellStyle(this.workbook));
                        break;
                    case "OS":
                        cell.setCellStyle(getDarkBlueCellStyle(this.workbook));
                        reportCell.setCellStyle(getDarkBlueCellStyle(this.workbook));
                        break;
                    case "NExec":
                        cell.setCellStyle(getGreyCellStyle(this.workbook));
                        reportCell.setCellStyle(getGreyCellStyle(this.workbook));
                        break;
                }
                System.out.println("OverallCase Result put in excel sheet at row: " + this.currentRow);
                this.currentRow++;
            }

            Set<StepExecutions> stepExSet = currCaseEx.getStepExecutionses();

            Iterator<StepExecutions> itStepEx = stepExSet.iterator();

            int totalSteps = 0;
            while (itStepEx.hasNext()) {
                StepExecutions currStepEx = itStepEx.next();
                totalSteps++;
            }
            itStepEx = stepExSet.iterator();

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
                //either 1 or 2 script per step
                int scriptNum = 0;
                String scriptType = "";
                boolean DO_POINT_ADDRESS_ERROR = false;
                boolean DO_VALUE_ERROR = false;
                while (itScriptEx.hasNext()) {

                    ScriptExecutions scriptEx = itScriptEx.next();

                    System.out.println("CaseNum: " + caseNum + " StepNum: " + stepNumber + " scriptNum :" + scriptNum + " ScriptName: " + scriptEx.getScript().getName());
                    System.out.println();
                    //Check if ScriptExecution is a stimuli
                    //if getIsStimuli = 1 ---> script on left side
                    //if getIsStimuli = 0 ---> script on right side
                    boolean isStimuli;
                    isStimuli = scriptEx.getIsStimuli() != 0;

                    Script script = scriptEx.getScript();
                    System.out.println(script.getName());
                    if (script.getName().contains("DI2")) {     //Refer to Modbus DI2
                        scriptType = "DI2";
                        this.scriptTypeGlobal = "DI2";
                        maxStep = 4;
                    } else if (script.getName().contains("DI") && !script.getName().contains("CIP")) {      //Refer to Modbus DI
                        scriptType = "DI";
                        this.scriptTypeGlobal = "DI";
                        maxStep = 2;
                    } else if (script.getName().contains("AI") && !script.getName().contains("CIP")) {
                        scriptType = "AI";
                        this.scriptTypeGlobal = "AI";
                        maxStep = 2;
                    } else if (script.getName().contains("DO2")) {      //Refer to IEC-104 DO2
                        scriptType = "DO2";
                        this.scriptTypeGlobal = "DO";
                        maxStep = 4;
                        DO_FLAG = true;
                    } else if (script.getName().contains("CIP") && script.getName().contains("DO")) {  //Assume it is CIP DO
                        scriptType = "CIPDO";
                        this.scriptTypeGlobal = "CIPDO";
                        maxStep = 8;
                        DO_FLAG = true;
                    } else if (script.getName().contains("CIP") && script.getName().contains("AO")) {   //Assume it is CIP AO
                        scriptType = "CIPAO";
                        this.scriptTypeGlobal = "CIPAO";
                        maxStep = 1;
                        DO_FLAG = true;
                    } else if (script.getName().contains("DO") && !script.getName().contains("Modbus")) {       //Refer to IEC-104 DO
                        scriptType = "DO";
                        this.scriptTypeGlobal = "DO";
                        maxStep = 1;
                        DO_FLAG = true;
                    } else if (script.getName().contains("DOCMB") && script.getName().contains("Modbus")) {     //Refer to Modbus DOCMB
                        scriptType = "MODBUSDOCMB";
                        this.scriptTypeGlobal = "MODBUSDOCMB";
                        maxStep = 4;
                        DO_FLAG = true;
                    } else if (script.getName().contains("DO") && script.getName().contains("Modbus")) {        //Refer to Modbus DO
                        scriptType = "MODBUSDO";
                        this.scriptTypeGlobal = "MODBUSDO";
                        maxStep = 2;
                        DO_FLAG = true;
                    } else if (script.getName().contains("AO") && script.getName().contains("Modbus")) {        //Refer to Modbus AO
                        scriptType = "MODBUSAO";
                        this.scriptTypeGlobal = "MODBUSAO";
                        maxStep = 1;
                        DO_FLAG = true;
                    } else if (script.getName().contains("IEC104")) {       //Refer to IEC-104 DI/AI SOE1 SOE2
                        scriptType = "SOE";
                        this.scriptTypeGlobal = "SOE";
                        maxStep = 4;
                    } else if (script.getName().contains("CIP") && (script.getName().contains("DI") || script.getName().contains("AI"))) {
                        scriptType = "CIPDI";
                        this.scriptTypeGlobal = "CIPDI";
                        maxStep = 8;
                    }
                    if (!DO_FLAG)
                        this.reportMaxStep = maxStep > this.reportMaxStep ? maxStep : this.reportMaxStep;

                    Set<ParametersExecution> paramExSet = scriptEx.getParametersExecutions();
                    Iterator<ParametersExecution> itParamEx = paramExSet.iterator();
                    int numParameter = 0;
                    //number of parameters inputed in TAT on each script in one step
                    while (itParamEx.hasNext()) {
                        ParametersExecution paramEx = itParamEx.next();
                        String paramSearched = paramEx.getValue();
                        System.out.println("numParameter: " + numParameter + " paramSearched: " + paramSearched);
                        if (!isStimuli) {       //For verifying the result
                            if (paramEx.getParameters().getName().equalsIgnoreCase("searched value")) {
                                System.out.println("Script is NOT stimuli, parameter " + paramSearched + " added in paramSearchList");
                                //This specific scripts is only used in Search Occurance.
                                paramSearchList.add(paramSearched);
                            }
                            if ("DO".equals(scriptType) || "DO2".equals(scriptType)) {
                                if (numParameter == 0) {
                                    try {
                                        registerList.add(String.valueOf((int) Math.round(Double.parseDouble(paramSearched))));
                                        registerList.add("0");
                                    } catch (NumberFormatException ex) {
                                        System.out.println("Should be last step. Do nothing.");
                                    }
                                }
                            } else if ("CIPDO".equals(scriptType) || "CIPAO".equals(scriptType)) {
                                if (numParameter == 2 || numParameter == 3) {        // Assume the SLOT refers to the Register Information.
                                    registerList.add(paramSearched);
                                }
                            } else if ("MODBUSDO".equals(scriptType) || "MODBUSDOCMB".equals(scriptType) || "MODBUSAO".equals(scriptType)) {
                                if (numParameter == 2 || numParameter == 3) {
                                    registerList.add(paramSearched);
                                }
                            }
                        } else if (isStimuli) {        //For Step Description (Stimuli only)
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
                            } else if (scriptType.contains("SOE")) {
                                if (stepNumber > 1 && numParameter == 1) {
                                    try {
                                        registerList.add(String.valueOf((int) Math.round(Double.parseDouble(paramSearched))));
                                        registerList.add("0");
                                    } catch (NumberFormatException ex) {
                                        System.out.println("Should be last step. Do nothing.");
                                    }
                                } else if (stepNumber > 1 && numParameter == 3) {
                                    if (paramSearched.contains("SOE")) {
                                        switch (paramSearched) {
                                            case "SOE1":
                                                scriptType = "SOE1";
                                                maxStep = 2;
                                                break;
                                            case "AI":
                                                scriptType = "AI";
                                                maxStep = 2;
                                                break;
                                            case "SOE2":
                                                scriptType = "SOE2";
                                                maxStep = 4;
                                                break;
                                        }
                                    }
                                }
                            } else if (scriptType.equals("CIPDI")) {
                                if (numParameter == 3 || numParameter == 4) {
                                    registerList.add(paramSearched);
                                }
                            }
                        }
                        numParameter++;
                    }

                    //Only if isStimuli = 0
                    if (!isStimuli) {
                        String scriptExComment = scriptEx.getScriptExecutionComment();
                        if (DO_FLAG && scriptExComment.contains("Received Incorrect value")) {
                            DO_VALUE_ERROR = true;
                        } else if (DO_FLAG && scriptExComment.contains("Received Incorrect Point Address")) {
                            DO_POINT_ADDRESS_ERROR = true;
                        }
                        //Either 2 for DI, 4 for DI2
                        paramFoundList = getParamFound(paramSearchList, scriptExComment);
                        Iterator it = paramFoundList.iterator();
                        int c = 0;
                        while (it.hasNext()) {
                            System.out.println("paramFoundList " + c++ + ": " + it.next());
                        }
                    }

                    //getting param script macro
                    if (
                            ((caseNum != 0 && scriptType.contains("DI")) || scriptType.contains("SOE")) && scriptNum != 0 ||
                                    scriptType.contains("DO") && scriptNum != 0 ||
                                    scriptType.contains("CIPDO") && scriptNum != 0 ||
                                    scriptType.contains("CIPAO") && scriptNum != 0 ||
                                    scriptType.contains("CIPDI") && scriptNum != 0 ||
                                    scriptType.contains("DO2") && scriptNum != 0 ||
                                    (scriptType.contains("MODBUSDO") && scriptNum != 0 && caseNum != 0) ||
                                    (scriptType.contains("MODBUSDOCMB") && scriptNum != 0 && caseNum != 0) ||
                                    (scriptType.contains("MODBUSAO") && scriptNum != 0 && caseNum != 0)
                    ) {
                        ScriptDB scDB = new ScriptDB();
                        scDB.getAllFromParamScriptMacro(script);

                        Iterator<Macro> it2 = scriptEx.getScript().getMacrosForScriptIdScript().iterator();
                        ParamScriptMacro temp;
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
                                    System.out.println("paramScriptMacro Value: " + param);
                                }
                                enteredWhile = true;
                            }
                            i++;
                        }
                        if (enteredWhile) {
                            hasSearchOccParamConf = true;
                        }
                        System.out.println("\n");
                    }
                    scriptNum++;
                }

                //Write check Number of Event if possible.
                if (!scriptValidation(totalSteps, stepNumber) && res.equals("NOK")) {
                    cell = this.sheet.getRow(this.currentRowGlobal).createCell(20);
                    cell.setCellValue("Several events generated per trigger");
                    cell = this.sheet.getRow(0).createCell(20);
                    cell.setCellValue("Check Number of Event");
                }

                //write register and offset
                if ((scriptType.contains("DI") && caseNum != 0 && stepNumber != 0 && scriptValidation(totalSteps, stepNumber)) ||
                        ((stepNumber > 1 && (stepNumber != totalSteps - 1)) && scriptType.contains("SOE")) ||
                        ((stepNumber > 0 && (stepNumber != totalSteps - 1)) && scriptType.contains("DO")) ||
                        (scriptType.contains("CIPDO")) || (scriptType.contains("CIPAO")) ||
                        (scriptType.contains("CIPDI") && stepNumber != 0) ||
                        (scriptType.contains("MODBUSDO") && stepNumber > 0) ||
                        (scriptType.contains("MODBUSDOCMB")) ||
                        (scriptType.contains("MODBUSAO"))
                ) {
                    Row row = this.sheet.createRow(this.currentRow);
                    Cell cellR = row.createCell(1); //column = 1
                    cellR.setCellValue(overallStepResult);
                    if (overallStepResult.equals("NOK")) {
                        CellStyle red = getRedCellStyle(this.workbook);
                        red.setWrapText(true);
                        red.setAlignment(CellStyle.ALIGN_LEFT);
                        cellR.setCellStyle(red);
                    }
                    cell = row.createCell(3);
                    cell.setCellValue(registerList.get(0));         //Register
                    if (reportDuplicateCheck(reportRow, this.colRegister_Address + (this.reportMaxStep - 1) * 3)) {
                        cell = reportRow.createCell(this.colRegister_Address + (this.reportMaxStep - 1) * 3);
                        cell.setCellValue(registerList.get(0));
                        if (DO_POINT_ADDRESS_ERROR) {
                            CellStyle cellStyle = getRedCellStyle(this.workbook);
                            cellStyle.setWrapText(true);
                            cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
                            cell.setCellStyle(cellStyle);
                        }
                    }

                    Cell cell2 = row.createCell(4);
                    cell2.setCellValue(registerList.get(1));        //Register Offset
                    if (reportDuplicateCheck(reportRow, this.colBit_offset + (this.reportMaxStep - 1) * 3)) {
                        cell = reportRow.createCell(this.colBit_offset + (this.reportMaxStep - 1) * 3);
                        cell.setCellValue(registerList.get(1));

                    }

                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(scriptType);                 //Data Type
                    if (reportDuplicateCheck(reportRow, this.colDC_Data_Type)) {
                        cell = reportRow.createCell(this.colDC_Data_Type);
                        cell.setCellValue(scriptType);
                    }

                    Cell cell4 = row.createCell(5);
                    int cellOffset = stepNumber % maxStep;
                    if (scriptType.equals("DO2")) {
                        cellOffset = (stepNumber - 1) % maxStep;
                    }
                    cell4.setCellValue(String.valueOf(cellOffset));         //Triggering State
                    if (DO_VALUE_ERROR) {
                        CellStyle cellStyle = getRedCellStyle(this.workbook);
                        cellStyle.setWrapText(true);
                        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
                        cell4.setCellStyle(cellStyle);
                    }

                    if (reportDuplicateCheck(reportRow, this.colAssociatedDefect + (this.reportMaxStep - 1) * 3)) {       //Associated Defect
                        cell = reportRow.createCell(this.colAssociatedDefect + (this.reportMaxStep - 1) * 3);
                        cell.setCellStyle(cellStyle5);
                    }

                    if (reportDuplicateCheck(reportRow, this.colCommentOnResult + (this.reportMaxStep - 1) * 3)) {       //Comment on Result
                        cell = reportRow.createCell(this.colCommentOnResult + (this.reportMaxStep - 1) * 3);
                        cell.setCellStyle(cellStyle5);
                        Hyperlink rawReportLink = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
                        rawReportLink.setAddress("'Raw Result'!" + (this.currentRow) + ":" + (this.currentRow));
                        cell.setHyperlink(rawReportLink);
                        cell.setCellValue("Link to Raw Result");
                    }

                    if (reportDuplicateCheck(reportRow, this.colSystemVersionUnderTest + (this.reportMaxStep - 1) * 3)) {       //System Version Under Test
                        cell = reportRow.createCell(this.colSystemVersionUnderTest + (this.reportMaxStep - 1) * 3);
                        cell.setCellStyle(cellStyle5);
                    }

                    if (reportDuplicateCheck(reportRow, this.colDate + (this.reportMaxStep - 1) * 3)) {       //Operation Date
                        cell = reportRow.createCell(this.colDate + (this.reportMaxStep - 1) * 3);
                        cell.setCellValue(iteration.getDate());
                        cell.setCellStyle(cellStyle5);
                    }

                    if (reportDuplicateCheck(reportRow, this.colTester + (this.reportMaxStep - 1) * 3)) {       //Tester
                        cell = reportRow.createCell(this.colTester + (this.reportMaxStep - 1) * 3);
                        cell.setCellValue("TAT");
                        cell.setCellStyle(cellStyle5);
                    }

                    //write parameters in Raw Report
                    for (int i = 0; i < paramSearchList.size(); i++) {
                        Cell cellS = row.createCell(tempHeader.length + 2 * i);
                        Cell cellF = row.createCell(tempHeader.length + 2 * i + 1);
                        String search = "";
                        String found = "";
                        try {
                            search = "-1".equals(paramSearchList.get(i)) ? "N/A" : paramSearchList.get(i);
                            found = DO_FLAG ? "N/A" : paramFoundList.get(i);
                        } catch (IndexOutOfBoundsException ex) {
                            found = "N/A";
                        }
                        cellS.setCellValue(search);
                        if ("-1".equals(paramSearchList.get(i))) {
                            cellS.setCellStyle(getGreyCellStyle(this.workbook));
                        }
                        cellF.setCellValue(found);
                        if (DO_FLAG) {
                            cellF.setCellStyle(getGreyCellStyle(this.workbook));
                        } else if (!search.equals(found)) {
                            CellStyle red = getRedCellStyle(this.workbook);
                            cellS.setCellStyle(red);
                            cellF.setCellStyle(red);
                        }
                    }
                    this.currentRow++;

                    try {
                        //write parameters in Report. Assume there's always 6 in paramSearchList.  These are before v0 columns.
                        if (reportDuplicateCheck(reportRow, this.colStation)) {
                            cell = reportRow.createCell(this.colStation);
                            cell.setCellValue(paramSearchList.get(getColIndex(searchOccParamList, colStationKey)));
                        }
                        if (reportDuplicateCheck(reportRow, this.colEqpt_Code)) {
                            cell = reportRow.createCell(this.colEqpt_Code);
                            cell.setCellValue(paramSearchList.get(getColIndex(searchOccParamList, colEqpt_CodeKey)));
                        }
                        if (reportDuplicateCheck(reportRow, this.colEqpt_Description)) {
                            cell = reportRow.createCell(this.colEqpt_Description);
                            cell.setCellValue(paramSearchList.get(getColIndex(searchOccParamList, colEqpt_DescriptionKey)));
                        }
                        if (reportDuplicateCheck(reportRow, this.colEqpt_Identifier)) {
                            cell = reportRow.createCell(this.colEqpt_Identifier);
                            cell.setCellValue(paramSearchList.get(getColIndex(searchOccParamList, colEqpt_IdentifierKey)));

                        }
                        if (reportDuplicateCheck(reportRow, this.colAttribute_Description)) {
                            cell = reportRow.createCell(this.colAttribute_Description);
                            cell.setCellValue(paramSearchList.get(getColIndex(searchOccParamList, colAttribute_DescriptionKey)));

                        }

                        int offset = stepNumber % maxStep;  //In unit of 3
                        if (scriptType.equals("DO2") || scriptType.equals("MODBUSDO")) {
                            offset = (stepNumber - 1) % maxStep;
                        }
                        cell = reportRow.createCell(this.colv0_label0 + offset * 3);
                        cell.setCellValue("-1".equals(paramSearchList.get(getColIndex(searchOccParamList, colStateKey))) ? "" : paramSearchList.get(getColIndex(searchOccParamList, colStateKey)));
                        cell = reportRow.createCell(this.colv0_Severity + offset * 3);
                        double severity = Double.parseDouble(paramSearchList.get(6));
                        cell.setCellValue((int) severity == -1 ? "" : String.valueOf((int) severity));
                        cell = reportRow.createCell(this.colv0_State + offset * 3);
                        cell.setCellValue((int) severity == -1 ? "" : severity > 0 ? "A" : "N");
                    } catch (IndexOutOfBoundsException ex) {
                        CommonFunctions.debugLog.debug("Cannot find the keyword in parameters. Please ensure columnName used matched with pre-defined column key.", ex);
                    }
                }
                System.out.println("Step result of above step:" + overallStepResult + "\n");
                stepNumber++;
            }
            caseNum++;
            this.reportCurrentRow++;
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

    private boolean reportDuplicateCheck(Row sheetRow, int colNum) {
        return (sheetRow.getCell(colNum) == null || sheetRow.getCell(colNum).getStringCellValue().equals(""));
    }

    /*
     *To validate whether this Trigger DI/DI2/etc event contains SSH Commands for checking multi-event report
     */
    private boolean scriptValidation(int totalSteps, int currentStep) {
        for (int i = 0; i < scriptTypeName.length; i++) {
            if (this.scriptTypeGlobal.equals(scriptTypeName[i]) && scriptTypeMaxStep[i] < totalSteps - 2) {
                if (currentStep == 1 || currentStep == (totalSteps - 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateSTDInformation() {
        Cell cell;
        CellStyle cellStyle = this.summarySheet.getRow(STDInformation.get("STD ID")[1]).getCell(STDInformation.get("STD ID")[0]).getCellStyle();

        cell = this.summarySheet.getRow(STDInformation.get("STD ID")[1]).getCell(STDInformation.get("STD ID")[0]);
        cell.setCellValue(this.campaignReference);
        cell.setCellStyle(cellStyle);
    }

    private void updateAuthorInformation() {
        Cell cell;
        CellStyle cellStyle = this.summarySheet.getRow(authorInformation.get("Written by")[1]).getCell(authorInformation.get("Written by")[0]).getCellStyle();

        cell = this.summarySheet.getRow(authorInformation.get("Written by")[1]).getCell(authorInformation.get("Written by")[0]);
        cell.setCellValue(this.campainWriter);
        cell.setCellStyle(cellStyle);

        cell = this.summarySheet.getRow(authorInformation.get("Writing date")[1]).getCell(authorInformation.get("Writing date")[0]);
        cell.setCellValue(this.campainCreationDate);
        cell.setCellStyle(cellStyle);
    }

    private void updateSTRInformation() {
        Cell cell;
        CellStyle cellStyle = this.summarySheet.getRow(STRInformation.get("IO schedule version")[1]).getCell(STRInformation.get("IO schedule version")[0]).getCellStyle();

        cell = this.summarySheet.getRow(STRInformation.get("IO schedule version")[1]).createCell(STRInformation.get("IO schedule version")[0]);
        cell.setCellValue(this.baselineID);
        cell.setCellStyle(cellStyle);

        cell = this.summarySheet.getRow(STRInformation.get("Total steps")[1]).createCell(STRInformation.get("Total steps")[0]);
        cell.setCellValue(WriteReport.caseExeResult.get("Test case result"));
        cell.setCellStyle(cellStyle);

    }

    private void updateSTRResults() {
        Cell cell;
        CellStyle cellStyle = this.summarySheet.getRow(STRResults.get("OK")[1]).getCell(STRResults.get("OK")[0]).getCellStyle();

        for (String key : WriteReport.caseExeResult.keySet()) {
            switch (key) {
                case "OK":
                    cell = this.summarySheet.getRow(STRResults.get("OK")[1]).createCell(STRResults.get("OK")[0]);
                    cell.setCellValue(WriteReport.caseExeResult.get(key));
                    cell.setCellStyle(cellStyle);
                    break;
                case "NOK":
                    cell = this.summarySheet.getRow(STRResults.get("NOK")[1]).createCell(STRResults.get("NOK")[0]);
                    cell.setCellValue(WriteReport.caseExeResult.get(key));
                    cell.setCellStyle(cellStyle);
                    break;
                case "OS":
                    cell = this.summarySheet.getRow(STRResults.get("Out Of Scope")[1]).createCell(STRResults.get("Out Of Scope")[0]);
                    cell.setCellValue(WriteReport.caseExeResult.get(key));
                    cell.setCellStyle(cellStyle);
                    break;
                case "NExec":
                    cell = this.summarySheet.getRow(STRResults.get("Not Tested")[1]).createCell(STRResults.get("Not Tested")[0]);
                    cell.setCellValue(WriteReport.caseExeResult.get(key));
                    cell.setCellStyle(cellStyle);
                    break;
            }
        }
        //Update Test Case Result
        cell = this.summarySheet.getRow(STRResults.get("Test case result")[1]).createCell(STRResults.get("Test case result")[0]);
        cell.setCellValue(WriteReport.caseExeResult.get("Test case result"));
        cell.setCellStyle(cellStyle);

    }

    /**
     * @return
     */
    public String getFileName() {
        return FILE_NAME;
    }

    private int getColIndex(List<String> searchOccParamList, String checkStr) {
        for (int i = 0; i < searchOccParamList.size(); i++) {
            if (searchOccParamList.get(i).contains(checkStr)) return i;
        }
        return -1;
    }

}
