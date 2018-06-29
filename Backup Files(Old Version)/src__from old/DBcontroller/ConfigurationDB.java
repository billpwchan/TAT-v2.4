/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import DB.Iterations;
import DB.ParametersExecution;
import DB.ScriptExecutions;
import DB.ScriptHasBeenConfigured;
import DB.StepExecutions;
import DB.TestCampaign;
import DB.TestCase;
import DB.TestStep;
import DB.TestStepHasScript;
import configuration.settings;
import controller.tabtestexecution.TabTestCampaignExecutionBaselineCampaignController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import javafx.application.Platform;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Martinth
 */
public class ConfigurationDB {

    int caseIterationExecution = 1;

    private String string;

    /**
     * Default constructor of this class.
     */
    public ConfigurationDB() {
        //init();
    }

    private void init() {
    }

    public int configureTestCase(Iterations iteration, TestCase testCase, File exceFile, int range, String sheetNumber, int caseNumber, String excelCategoryInstantiation, String excelLocationInstantiation) throws FileNotFoundException, IOException, InterruptedException {
        //final Stage dialog2 = dialog;
        SessionFactory factory = sessionFactorySingleton.getInstance();
        String splitOn = ((char) 007) + "";
        Session session = factory.openSession();
        boolean excelFilePresent = false;
        HSSFSheet sheet = null;

        if (exceFile != null) {
            FileInputStream file = new FileInputStream(exceFile);
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            excelFilePresent = true;
            if (sheetNumber.contains("@&Number_")) {
                sheet = workbook.getSheetAt(Integer.parseInt(sheetNumber.replace("@&Number_", "").trim()) - 1);
            } else if (sheetNumber.contains("@&Name_")) {
                System.out.println("HERE");
                System.out.println("STRING = " + sheetNumber.replace("@&Name_", "").trim());
                sheet = workbook.getSheet(sheetNumber.replace("@&Name_", "").trim());
                System.out.println("sheetName= " + sheet.getSheetName());
            }

            System.out.println("sheet numberAAAA = " + sheetNumber);
        } else {

        }
        Short caseOrder =(short) caseNumber;
        Byte stepOrder = 0;
        Byte scriptOrder = 0;
        Byte paramOrder = 0;
        //HSSFSheet sheet = workbook.getSheet(sheetName);
        int indexLine = 0;
        String value = null;
        while (range > 0 || range == -1) {
            CaseExecutions caseExecution = new CaseExecutions(iteration, testCase, caseOrder);
            if (excelCategoryInstantiation != null) {
                String[] category = excelCategoryInstantiation.split(splitOn);
                caseExecution.setTestCategory(this.getExcelValue(sheet, Integer.parseInt(category[0]), Integer.parseInt(category[1]), indexLine));
            }
            if (excelLocationInstantiation != null) {
                String[] location = excelLocationInstantiation.split(splitOn);
                caseExecution.setLocation(this.getExcelValue(sheet, Integer.parseInt(location[0]), Integer.parseInt(location[1]), indexLine));
            }
            Iterator<TestStep> itSteps = testCase.getTestSteps().iterator();
            stepOrder = 0;
            while (itSteps.hasNext()) {
                TestStep testStep = itSteps.next();
                StepExecutions stepExecutions = new StepExecutions(caseExecution, testStep, stepOrder);
                caseExecution.getStepExecutionses().add(stepExecutions);
                Iterator<TestStepHasScript> itTestStepHasScript = testStep.getTestStepHasScripts().iterator();
                scriptOrder = 0;
                while (itTestStepHasScript.hasNext()) {
                    TestStepHasScript testStepHasScriptObject = itTestStepHasScript.next();
                    ScriptExecutions scriptExecution = new ScriptExecutions(testStepHasScriptObject.getScript(), stepExecutions, scriptOrder, testStepHasScriptObject.getScript().getIsStimuli());
                    stepExecutions.getScriptExecutionses().add(scriptExecution);
                    Iterator<ScriptHasBeenConfigured> itScHasBeenConfigured = testStepHasScriptObject.getScriptHasBeenConfigureds().iterator();
                    paramOrder = 0;
                    while (itScHasBeenConfigured.hasNext()) {
                        ScriptHasBeenConfigured Param = itScHasBeenConfigured.next();
                        if (Param.getValuePath().equals("Excel file")) {
                            caseExecution.setExcelPath(settings.scriptsPaht + "\\" + iteration.getTestCampaign().getReference() + "\\" + iteration.getBaselineId() + "\\" + exceFile.toPath().getFileName());
                            String[] params = Param.getValue().split(splitOn);
                            int x = Integer.parseInt(params[3]);
                            int y = Integer.parseInt(params[4]);
                            try {
                                //System.out.println("HERE AVANT BUG");
                                sheet.getRow(y + indexLine - 1).getCell(x - 1).getCellType();
                            } catch (Exception e) {
                                Platform.runLater(() -> {
                                    TabTestCampaignExecutionBaselineCampaignController.errorBox(x, y);
                                });

                                return -1;
                            }
                            value = this.getExcelValue(sheet, x, y, indexLine);
                        } else if (Param.getValuePath().equals("Constant") || Param.getValuePath().equals("Buffer list")|| Param.getValuePath().equals("HMI")|| Param.getValuePath().equals("Classes")) {
                            String subString = Param.getValue();
                            System.out.println("JE SUIS DANS BASELINE ET CONFIG= "+subString);
                            subString = subString.substring(1, subString.length() - 1);
                            value = subString;
                            System.out.println("JE SUIS DANS BASELINE ET CONFIG= "+value);
                        } else if (Param.getValuePath().equals("Property")) {
                            String[] property = Param.getValue().split(splitOn);
                            //System.out.println("VALUE = "+property);
                            value=property[4];
                        }
                        ParametersExecution paramExecution = new ParametersExecution(Param.getParameters(), scriptExecution, value, paramOrder);
                        scriptExecution.getParametersExecutions().add(paramExecution);
                        paramOrder++;
                    }
                    scriptOrder++;
                }
                stepOrder++;
            }
            //System.out.println("JE VAIS SAVE");
            try {
                session.save(caseExecution);
            } catch (Exception e) {
                System.out.println("EXCEPTION in session.save = " + e);
            }
            caseNumber++;
            indexLine++;
            caseOrder++;
            range--;
            System.out.println("CASE NUMBER = "+caseOrder);
        }
        session.beginTransaction().commit();
        session.close();
        Platform.runLater(() -> {
            //TabTestCampaignExecutionBaselineCampaignController.closeAlert();
        });
        return caseNumber;
    }

    public long checkConfigurationExistence(String baselineName) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query query = session.createQuery("select count(*) from Iterations as IT where IT.baselineId=:baselineName");
        query.setString("baselineName", baselineName);
        Long count = (Long) query.uniqueResult();
        session.close();
        return count;
    }

    public void deleteConfiguration(Iterations baseline) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("delete Iterations IT where IT.baselineId=:baselineID");
        qry.setString("baselineID", baseline.getBaselineId());
        qry.executeUpdate();
        session.beginTransaction().commit();
        session.close();
    }

    public Iterations createBaseline(String baselineId, TestCampaign campaignToBaseline) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();

        Iterations baseline = new Iterations(campaignToBaseline, baselineId, 0, main.Main.df.format(new Date()));
        session.save(baseline);
        session.beginTransaction().commit();
        session.close();
        return baseline;
    }

    public String getExcelValue(HSSFSheet sheet, int x, int y, int indexLine) {
        String value = null;
        switch (sheet.getRow(y + indexLine - 1).getCell(x - 1).getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = sheet.getRow(y + indexLine - 1).getCell(x - 1).getRichStringCellValue().getString();
                //System.out.println(sheet.getRow(y + indexLine - 1).getCell(x - 1).getRichStringCellValue().getString());
                //System.out.println("VALUE = " + value);
                break;
            case Cell.CELL_TYPE_NUMERIC:
                value = String.valueOf(sheet.getRow(y + indexLine - 1).getCell(x - 1).getNumericCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                try {

                    value = String.valueOf(sheet.getRow(y + indexLine - 1).getCell(x - 1).getStringCellValue());
                } catch (Exception e) {
                    value = String.valueOf(sheet.getRow(y + indexLine - 1).getCell(x - 1).getNumericCellValue());
                }
                //System.out.println("VALUE = " + value);
                break;
            default:
                value = String.valueOf(0);
        }
        return value;
    }

}
