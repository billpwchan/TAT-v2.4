/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.*;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author T0155040
 */
public class createOrchestra {

    private final List<Integer> positionSteps = new ArrayList<>();
    File source;
    File dest;
    XSSFWorkbook wb;
    // copy file using FileStreams
    long start = System.nanoTime();
    long end;
    private String pathOrchestra = "src/file/HK - IVVQ - Template - STD - v16.xlsx";
    //private final String pathOrchestra = "D:\\Users\\t0155040\\Desktop\\HK - IVVQ - Template - STD - v16.xlsx";
    //private final String pathOrchestra = "D:\\Users\\t0155040\\Desktop\\HK - IVVQ - Template - STD - v16.xlsx";
    //private final String pathOrchestra = "D:\\Users\\t0155040\\Desktop\\New Microsoft Excel Worksheet.xlsx";
    private TestCampaign testCampaign;//Iteration should be replaced by that
    private Iterations iteration;
    private boolean hasBeenCOnfigured = false;
    private int postitionRow = 0;
    private int positionColumnScriptPreparation = 14;
    private int positionWritingScript = 14;
    private int mergeCellScriptName = 14;
    private Cell cell = null;
    private XSSFSheet sheetSummary;
    private XSSFRichTextString richString;
    private ParametersExecution paramExe;
    private CellStyle currentStyle;
    private StepExecutions currentStepExecution;
    private ScriptExecutions currentScriptExecution;
    private TestStep currentTestStep;
    //    Point[] cellsSummary = {
//        //TEST SHEET DEFINITION
//        new Point(3, 5),//E3 : Test sheet identification
//        new Point(4, 5),//E4 : Version of the test sheet
//        new Point(5, 5),//E5 : Project
//        new Point(6, 5),//E6 : Type of test
//        new Point(7, 5),//E7 : Category of test
//        new Point(8, 5),//E8 : Location
//        new Point(9, 5),//E9 : Test title
//        new Point(10, 5),//E10 : Test objective
//        //GENERAL DESCRIPTION
//        new Point(8, 3),//H3 : Regression thread
//        new Point(8, 4),//H4 : Written by
//        new Point(8, 5),//H5 : Writter email
//        new Point(8, 6),//H6 : Writing status
//        new Point(8, 7),//H7 : Test method (IADT)
//    //        //TEST CONFIGURATION
//    //        new Point(13, 5),//E13 : Test environment
//    //        new Point(14, 5),//E14 : MCS Software Version
//    //        new Point(15, 5),//E15 : IO Schedule Version
//    //        new Point(16, 5),//E16 : Hardwire Termination Schedule Version
//    //        new Point(17, 5),//E17 : RSLogix5000 Version
//    //        new Point(18, 5),//E18 : Cubicle ID
//    //        new Point(19, 5),//E19 : Manufacturing drawing version
//    //        //EXECUTION SUMMARY
//    //        new Point(13, 5),//H23 : Instance of the test sheet execution
//    //        new Point(14, 5),//H24 : Run by
//    //        new Point(15, 5),//H25 : Runner email
//    //        new Point(16, 5),//H26 : Test state
//    //        new Point(17, 5),//H27 : Last run date
//    //        new Point(18, 5),//H28 : Approximate preparation duration
//    //        new Point(19, 5),//H29 : Approximate running duration
//    };
    private FileInputStream file;
    private Row row;
    private Iterator<ParametersExecution> iteParametersExecution;
    private Iterator<StepExecutions> iteStepExecutions;
    private Iterator<ScriptExecutions> iteScriptExecutions;

    /**
     *
     */
    public createOrchestra() {

    }

    private static void copyFileUsingFileChannels(File source, File dest)
            throws IOException {
//        FileChannel inputChannel = null;
//        FileChannel outputChannel = null;
//        try {
//            inputChannel = new FileInputStream(source).getChannel();
//            outputChannel = new FileOutputStream(dest).getChannel();
//            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
//        } finally {
//            inputChannel.close();
//            outputChannel.close();
        Files.copy(source.toPath(), dest.toPath());
//        }
    }

    /**
     * @param iteration
     * @param file
     * @param pathOrche
     */
    public void generateExcelRapport(Iterations iteration, File file, String pathOrche) {

        //System.out.println("Start excel generation file");
        pathOrchestra = pathOrche;
        this.testCampaign = iteration.getTestCampaign();
        this.iteration = iteration;

        //System.out.println("Taille de l'object iteration : "+a);
        Iterator<CaseExecutions> iteCaseExecution = iteration.getCaseExecutionses().iterator();
        CaseExecutions currentTestCaseExecution = iteCaseExecution.next();
        //Current Test Case
        TestCase currentTestCase = currentTestCaseExecution.getTestCase();
        openOrchestra(pathOrchestra, currentTestCase.getTestCaseIdentification(), file);
        sheetSummary = wb.getSheetAt(0);
        this.writeSummary(currentTestCase, this.testCampaign);
        sheetSummary = wb.getSheetAt(2);
        System.out.println("Taille de l'array :" + currentTestCaseExecution.getStepExecutionses().size());
        this.readyHeader(currentTestCaseExecution);
        writeTestCase(currentTestCaseExecution.getStepExecutionses());

        int i = 0;
        while (iteCaseExecution.hasNext()) {
            currentTestCaseExecution = iteCaseExecution.next();
//            if (!hasBeenCOnfigured) {
//                this.readyHeader(currentTestCaseExecution);
//                hasBeenCOnfigured = true;
//            }
            writeTestCase(currentTestCaseExecution.getStepExecutionses());
            //System.out.println("Test case : " + i + " Case execution oject reference : " + currentTestCaseExecution.getTestCase().getIdtestCase());
            i++;
        }
        closeExcel();
    }

    private void openOrchestra(String pathOrchestra, String nameTestCase, File file) {

//        File file = new File("C:\\Directory1");
//	if (!file.exists()) {
//		if (file.mkdir()) {
//			System.out.println("Directory is created!");
//		} else {
//			System.out.println("Failed to create directory!");
//		}
//	}
        source = new File(pathOrchestra);
        //System.out.println(file.getAbsolutePath()+"\\" + nameTestCase + ".xlsx");
        dest = new File(file.getAbsolutePath() + "\\" + nameTestCase + ".xlsx");
        try {
            copyFileUsingFileChannels(source, dest);
        } catch (IOException ex) {
            Logger.getLogger(createOrchestra.class.getName()).error("", ex);
        }
        openFile();

    }

    private void openFile() {
        try {
            file = new FileInputStream(dest);
            //wb = new SXSSFWorkbook(new XSSFWorkbook(file));
            wb = new XSSFWorkbook(file);
        } catch (IOException ex) {
            Logger.getLogger(createOrchestra.class.getName()).error("", ex);
        }
        //printInstrumentationSize(wb);

    }

    /**
     * Print basic details including size of provided object to standard output.
     *
     * @param object Object whose value and size are to be printed to standard
     *               output.
     */
//    public static void printInstrumentationSize(final Object object) {
//        System.out.println(
//                "Object of type '" + object.getClass() + "' has size of "
//                + InstrumentationAgent.getObjectSize(object) + " bytes.");
//    }
    private void writeTestCase(Set<StepExecutions> testExe) {

//        cell = sheetSummary.getRow(18).getCell(5);
//        currentStyle = cell.getCellStyle();
        iteStepExecutions = testExe.iterator();
        int i = 0;
        while (iteStepExecutions.hasNext()) {
            currentStepExecution = iteStepExecutions.next();
            currentTestStep = currentStepExecution.getTestStep();

            //TESTS
            row = sheetSummary.createRow(17 + postitionRow);

            //cell = sheetSummary.getRow(17 + postitionRow).getCell(1);
            cell = row.createCell(1);
            cell.setCellValue(postitionRow);

            //cell = sheetSummary.getRow(17 + postitionRow).getCell(2);
            cell = row.createCell(2);
            cell.setCellValue(currentTestStep.getHumanStimuli());

            //cell = sheetSummary.getRow(17 + postitionRow).getCell(3);
            cell = row.createCell(3);
            cell.setCellValue(currentTestStep.getHumanCheck());

            //cell = sheetSummary.getRow(17 + postitionRow).getCell(5);
            cell = row.createCell(5);
            cell.setCellValue(currentStepExecution.getStepExecutionResult());

            //cell = sheetSummary.getRow(17 + postitionRow).getCell(7);
            //System.out.println("Mon comment est : " + currentStepExecution.getStepExecutionComment());
            cell = row.createCell(7);
            cell.setCellValue(currentStepExecution.getStepExecutionComment());

//            cell = sheetSummary.getRow(17 + postitionRow).getCell(10);
//            cell.setCellValue(this.iteration.getTesterName());
            //cell = sheetSummary.getRow(17 + postitionRow).getCell(12);
            cell = row.createCell(12);
            cell.setCellValue(this.iteration.getDate());
            positionWritingScript = positionSteps.get(i);
            iteScriptExecutions = currentStepExecution.getScriptExecutionses().iterator();
            while (iteScriptExecutions.hasNext()) {
                currentScriptExecution = iteScriptExecutions.next();

                iteParametersExecution = currentScriptExecution.getParametersExecutions().iterator();
                while (iteParametersExecution.hasNext()) {
                    paramExe = iteParametersExecution.next();
                    //cell = sheetSummary.getRow(17 + postitionRow).getCell(positionWritingScript);
                    cell = row.createCell(positionWritingScript);
                    cell.setCellValue(paramExe.getValue());
                    positionWritingScript++;
                }
                //cell = sheetSummary.getRow(17 + postitionRow).getCell(positionWritingScript);
                cell = row.createCell(positionWritingScript);
                cell.setCellValue(currentScriptExecution.getScriptExecutionResult());
                //cell.setCellStyle(currentStyle);
                positionWritingScript++;
                //cell = sheetSummary.getRow(17 + postitionRow).getCell(positionWritingScript);
                cell = row.createCell(positionWritingScript);
                cell.setCellValue(currentScriptExecution.getScriptExecutionComment());
                positionWritingScript++;
            }
            //positionWritingScript = 14;
            hasBeenCOnfigured = true;
            postitionRow++;
            //System.out.println("Write line number : " + postitionRow);
            i++;
        }

        //listStepExecution.
        //List<ScriptExecution> listStepExecution = new ArrayList<>(testExe.getScriptExecutions());
        //System.out.println("J ai tant de sctript dans mon step : "+listStepExecution.size());
        //int position = testExe.getId().getStepExecutionOrder() + testExe.getId().getCaseExecutionOrder();
        //System.out.println("Mon stimuli est : "+currentStep.getHumanStimuli());
    }

    private void writeSummary(TestCase testcase, TestCampaign testcampaign) {

        /**
         * TEST CONFIGURATION
         *
         */
        //System.out.println("is Summary nhull ? " + sheetSummary);
        // declare a Cell object
        /**
         * TEST SHEET DEFINITION
         */
        //Set Test sheet identification
        cell = sheetSummary.getRow(2).getCell(4);
        cell.setCellValue(testcase.getTestCaseIdentification());
        //Set test case version
        cell = sheetSummary.getRow(3).getCell(4);
        cell.setCellValue(testcase.getTestCaseVersion());
        //Set test case project
        cell = sheetSummary.getRow(4).getCell(4);
        cell.setCellValue(testcase.getProject());
        //Set type of test
        cell = sheetSummary.getRow(5).getCell(4);
        cell.setCellValue(testcase.getTypeOfTest());
        //Set category
        cell = sheetSummary.getRow(6).getCell(4);
        cell.setCellValue(testcase.getCategoryOfTest());
        //Set location
        cell = sheetSummary.getRow(7).getCell(4);
        cell.setCellValue(testcase.getLocation());
        //Set test title
        cell = sheetSummary.getRow(8).getCell(4);
        cell.setCellValue(testcase.getTestCaseTitle());
        //Set test objectives
        cell = sheetSummary.getRow(9).getCell(4);
        cell.setCellValue(testcase.getTestObjective());
        //
        cell = sheetSummary.getRow(37).getCell(3);
        cell.setCellValue(testcase.getInternalComments());

        /**
         *  //GENERAL DESCRIPTION new Point(8, 3),//H3 : Regression thread new
         * Point(8, 4),//H4 : Written by new Point(8, 5),//H5 : Writter email
         * new Point(8, 6),//H6 : Writing status new Point(8, 7),//H7 : Test
         * method (IADT)
         */
        cell = sheetSummary.getRow(2).getCell(7);
        cell.setCellValue(testcampaign.getRegressionThread());
        //
        cell = sheetSummary.getRow(3).getCell(7);
        cell.setCellValue(testcase.getWritter());
        //
        cell = sheetSummary.getRow(4).getCell(7);
        cell.setCellValue(testcase.getWritterEmail());
        //
        cell = sheetSummary.getRow(5).getCell(7);
        cell.setCellValue(testcase.getWritingStatus());
        //
        cell = sheetSummary.getRow(6).getCell(7);
        cell.setCellValue(testcase.getTestMethodIadt());

//        //TEST CONFIGURATION
//        new Point(13, 5),//E13 : Test environment
//        new Point(14, 5),//E14 : MCS Software Version
//        new Point(15, 5),//E15 : IO Schedule Version
//        new Point(16, 5),//E16 : Hardwire Termination Schedule Version
//        new Point(17, 5),//E17 : RSLogix5000 Version
//        new Point(18, 5),//E18 : Cubicle ID
//        new Point(19, 5),//E19 : Manufacturing drawing version
        cell = sheetSummary.getRow(12).getCell(4);
        cell.setCellValue(testcampaign.getSoftwareSutRelease());
        cell = sheetSummary.getRow(13).getCell(4);
        cell.setCellValue(testcase.getEnvironment());
        //

//        //EXECUTION SUMMARY
//        new Point(13, 5),//H23 : Instance of the test sheet execution
//        new Point(14, 5),//H24 : Run by
//        new Point(15, 5),//H25 : Runner email
//        new Point(16, 5),//H26 : Test state
//        new Point(17, 5),//H27 : Last run date
//        new Point(18, 5),//H28 : Approximate preparation duration
//        new Point(19, 5),//H29 : Approximate running duration
    }

    private void closeExcel() {
        //System.out.println("Start to write file into disk");

        FileOutputStream fileOut;
        try {
            file.close();
            fileOut = new FileOutputStream(dest);
            wb.write(fileOut);
            fileOut.close();
            wb.close();

            cell = null;
            sheetSummary = null;
            paramExe = null;
            currentStyle = null;
            currentStepExecution = null;
            source = null;
            dest = null;
            wb = null;
            this.testCampaign = null;
            this.iteration = null;
            //System.gc();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(createOrchestra.class.getName()).error("", ex);
        } catch (IOException ex) {
            Logger.getLogger(createOrchestra.class.getName()).error("", ex);
        }
    }

    private void prepareScriptView(ScriptExecutions currentScriptExecution, XSSFSheet sheetSummary) {
        int numberOfCellsToMerge = currentScriptExecution.getParametersExecutions().size() + 1;

        //cell = sheetSummary.getRow(14).getCell(mergeCellScriptName);
        //row = 
        cell = sheetSummary.getRow(14).createCell(positionWritingScript);
        richString = new XSSFRichTextString("SCRIPT : " + currentScriptExecution.getScript().getName());
        cell.setCellValue(richString);
        //System.out.println("Script Name is : " + currentScriptExecution.getScript().getName());
        //setCellValue("Script : " + currentScriptExecution.getScript().getName());
        //System.out.println("Total de cellule a merger : " + numberOfCellsToMerge + " From column : " + mergeCellScriptName + " to column : " + (mergeCellScriptName + numberOfCellsToMerge));
        sheetSummary.addMergedRegion(new CellRangeAddress(
                14, //first row (0-based)
                14, //last row  (0-based)
                mergeCellScriptName, //first column (0-based)
                mergeCellScriptName + numberOfCellsToMerge //last column  (0-based)
        ));

        mergeCellScriptName += numberOfCellsToMerge + 1;

        //System.out.println("Script name is :"+currentScriptExecution.getScript().getName());
        iteParametersExecution = currentScriptExecution.getParametersExecutions().iterator();
        while (iteParametersExecution.hasNext()) {
            paramExe = iteParametersExecution.next();
            //cell = sheetSummary.getRow(15).getCell(positionColumnScriptPreparation);
            cell = sheetSummary.getRow(15).createCell(positionColumnScriptPreparation);
            cell.setCellValue(paramExe.getParameters().getName());
            positionColumnScriptPreparation++;
        }
        //cell = sheetSummary.getRow(15).getCell(positionColumnScriptPreparation);
        cell = sheetSummary.getRow(15).createCell(positionColumnScriptPreparation);
        cell.setCellValue("Result");
        positionColumnScriptPreparation++;
        //cell = sheetSummary.getRow(15).getCell(positionColumnScriptPreparation);
        cell = sheetSummary.getRow(15).createCell(positionColumnScriptPreparation);
        cell.setCellValue("Comments");
        positionColumnScriptPreparation++;
    }

    private void readyHeader(CaseExecutions currentTestCase) {

        int stepNumber = 1;

        int numberOfCellsScriptTOMerge = 0;
        int numberOfCellsStepTOMerge = 0;

        int startingCellsSteps = 14;
        int startingCellsScript = 14;
        iteStepExecutions = currentTestCase.getStepExecutionses().iterator();
        while (iteStepExecutions.hasNext()) {
            currentStepExecution = iteStepExecutions.next();

            iteScriptExecutions = currentStepExecution.getScriptExecutionses().iterator();
            while (iteScriptExecutions.hasNext()) {
                currentScriptExecution = iteScriptExecutions.next();

                iteParametersExecution = currentScriptExecution.getParametersExecutions().iterator();
                while (iteParametersExecution.hasNext()) {
                    paramExe = iteParametersExecution.next();
                    cell = sheetSummary.getRow(16).getCell(positionColumnScriptPreparation);
                    //cell = sheetSummary.getRow(16).createCell(positionColumnScriptPreparation);
                    cell.setCellValue(paramExe.getParameters().getName());
                    positionColumnScriptPreparation++;
                    numberOfCellsScriptTOMerge++;
                }
                //cell = sheetSummary.getRow(16).createCell(positionColumnScriptPreparation);
                cell = sheetSummary.getRow(16).getCell(positionColumnScriptPreparation);
                cell.setCellValue("Result");
                positionColumnScriptPreparation++;
                numberOfCellsScriptTOMerge++;
                cell = sheetSummary.getRow(16).getCell(positionColumnScriptPreparation);
                //cell = sheetSummary.getRow(16).createCell(positionColumnScriptPreparation);
                cell.setCellValue("Comments");
                positionColumnScriptPreparation++;
                numberOfCellsScriptTOMerge++;
                cell = sheetSummary.getRow(15).getCell(positionColumnScriptPreparation - numberOfCellsScriptTOMerge);
                //cell = sheetSummary.getRow(15).createCell(startingCellsScript);
                richString = new XSSFRichTextString("SCRIPT : " + currentScriptExecution.getScript().getName());
                cell.setCellValue(richString);
                sheetSummary.addMergedRegion(new CellRangeAddress(
                        15, //first row (0-based)
                        15, //last row  (0-based)
                        positionColumnScriptPreparation - numberOfCellsScriptTOMerge, //first column (0-based)
                        (positionColumnScriptPreparation - 1) //last column  (0-based)
                ));
                //System.out.println("Number of cells to merge : " + numberOfCellsScriptTOMerge + " Merge Cells for script between : " + startingCellsScript + " and " + (startingCellsScript + numberOfCellsScriptTOMerge - 1) + " Script name is : " + currentScriptExecution.getScript().getName());
                numberOfCellsStepTOMerge += numberOfCellsScriptTOMerge;
                numberOfCellsScriptTOMerge = 0;
                startingCellsScript++;
            }
            sheetSummary.addMergedRegion(new CellRangeAddress(
                    14, //first row (0-based)
                    14, //last row  (0-based)
                    positionColumnScriptPreparation - numberOfCellsStepTOMerge, //first column (0-based)
                    (positionColumnScriptPreparation - 1) //last column  (0-based)
            ));

            //cell = sheetSummary.getRow(14).createCell(startingCellsSteps);
            positionSteps.add(positionColumnScriptPreparation - numberOfCellsStepTOMerge);
            cell = sheetSummary.getRow(14).getCell(positionColumnScriptPreparation - numberOfCellsStepTOMerge);
            richString = new XSSFRichTextString("STEP : " + stepNumber);
            cell.setCellValue(richString);

            //System.out.println("Merge Cells for Steps between : " + startingCellsSteps + " and " + (startingCellsSteps + numberOfCellsStepTOMerge - 1) + " Step number is : " + stepNumber);
            stepNumber++;
            numberOfCellsStepTOMerge = 0;
            startingCellsSteps++;
        }
    }

}
