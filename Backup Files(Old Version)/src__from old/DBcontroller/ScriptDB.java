/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.Iterations;
import DB.Macro;
import DB.ParametersExecution;
import DB.Script;
import DB.ScriptExecutionResult;
import DB.ScriptExecutions;
import DB.ScriptHasParameters;
import DB.StepExecutions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author tmorin
 */
public class ScriptDB {
    //private Configuration cfg=new Configuration();

    public ScriptDB() {
    }

    public ArrayList<Script> getAllScripts() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("from Script S order by s.name ASC");
        List l = qry.list();
        ArrayList<Script> scripts = (ArrayList) l;
        session.close();
        return scripts;
    }

    public Script getTestScriptFromID(int id) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        Script script = new Script();
        script = (Script) session.get(Script.class, id);
        session.getTransaction().commit();
        session.close();
        return script;
    }

    public void getScriptAndParametersExecutionFromScriptExecution(ScriptExecutions scriptExecution) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(scriptExecution);
        Hibernate.initialize(scriptExecution.getScript());
        Hibernate.initialize(scriptExecution.getParametersExecutions());
        session.close();
    }

    public void getScriptParametersAndResults(Session session, StepExecutions stepExecution, Iterations iteration) {
        Query qryStep = session.createQuery("from ScriptExecutionResult SER where SER.id.scriptExecutionsIdscriptExecutions=:idScript and SER.id.iterationNumber=:iterationNumber");
        Iterator<ScriptExecutions> itScriptExecutions = stepExecution.getScriptExecutionses().iterator();
        while (itScriptExecutions.hasNext()) {
            ScriptExecutions scriptExecution = itScriptExecutions.next();
            if (iteration.getIterationNumber() == 0) {
                scriptExecution.setScriptExecutionResult("");
                scriptExecution.setScriptExecutionComment("");
            } else {
                qryStep.setInteger("idScript", scriptExecution.getIdscriptExecutions());
                qryStep.setInteger("iterationNumber", iteration.getIterationNumber());
                List l = qryStep.list();
                if (!l.isEmpty()) {
                    ScriptExecutionResult scriptExecutionResult = (ScriptExecutionResult) l.get(0);
                    scriptExecution.setScriptExecutionResult(scriptExecutionResult.getResult());
                    scriptExecution.setScriptExecutionComment(scriptExecutionResult.getComment());
                } else {
                    scriptExecution.setScriptExecutionResult("NExec");
                    scriptExecution.setScriptExecutionComment("");
                }
            }

            Hibernate.initialize(scriptExecution.getScript());
            Iterator<ParametersExecution> itParamExecution = scriptExecution.getParametersExecutions().iterator();
            while (itParamExecution.hasNext()) {
                Hibernate.initialize(itParamExecution.next().getParameters());
            }
        }
    }

    public HashSet<Script> getScriptListWithParameters() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("from Script S");
        HashSet<Script> scripts = new HashSet(qry.list());
        Iterator<Script> itScript = scripts.iterator();
        while (itScript.hasNext()) {
            Script script = itScript.next();
            Hibernate.initialize(script.getScriptHasParameterses());
            Set<ScriptHasParameters> hashScriptHasParameters = script.getScriptHasParameterses();
            Iterator<ScriptHasParameters> iteScriptHP = hashScriptHasParameters.iterator();
            while (iteScriptHP.hasNext()) {
                ScriptHasParameters scriptHasParameters = iteScriptHP.next();
                Hibernate.initialize(scriptHasParameters.getParameters());
            }
        }
        session.close();
        return scripts;
    }

    public HashSet<Script> getScriptListAndParameters() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("from Script S where S.isMacro=0 order by S.name ASC");
        HashSet<Script> scripts = new HashSet(qry.list());
        Iterator<Script> itScript = scripts.iterator();
        while (itScript.hasNext()) {
            Script script = itScript.next();
            Hibernate.initialize(script.getScriptHasParameterses());
            Set<ScriptHasParameters> hashScriptHasParameters = script.getScriptHasParameterses();
            Iterator<ScriptHasParameters> iteScriptHP = hashScriptHasParameters.iterator();
            while (iteScriptHP.hasNext()) {
                ScriptHasParameters scriptHasParameters = iteScriptHP.next();
                Hibernate.initialize(scriptHasParameters.getParameters());
            }
        }
        session.close();
        return scripts;
    }

    public void getAllFromScript(Script script) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(script);
        Iterator<ScriptHasParameters> itParams = script.getScriptHasParameterses().iterator();
        while (itParams.hasNext()) {
            ScriptHasParameters schp = itParams.next();
            Hibernate.initialize(schp.getParameters());
        }
        session.close();
    }
    
    public void getAllFromParamScriptMacro(Script script) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(script);
        Iterator<Macro> itParams = script.getMacrosForScriptIdScript().iterator();
        while (itParams.hasNext()) {
            Macro schp = itParams.next();
            Hibernate.initialize(schp.getParamScriptMacros());
        }
        session.close();
    }

    public void fillDB() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        //session.beginTransaction();
        Query q = session.createSQLQuery("INSERT INTO `parameters` VALUES (1,'Search Occurence input Buffer','buffer','Buffer in which are stored the lines to search in.'),(10,'Script purpose','string','The purpose of the script'),(11,'ShhCmd IP','ip','IP of the equipment to connect to.'),(12,'UserName','string','UserName for the connection to the targeted equipment'),(13,'Password','string','Password for the connection to the targeted equipment'),(14,'SSHcmd','string','Command to execute on the targeted equipment.'),(15,'SSHcmd Output buffer','buffer','Name of the buffer created to store the output of the command.'),(16,'searched value','string','value to be found in the line.'),(17,'regular expression','string','expression of the pattern allowing the identification of the searched value.'),(18,'retrieve line input Buffer','buffer','Buffer in which are stored the lines to search in.'),(19,'retrieve line output Buffer','buffer','Name of the buffer created to store the lines identified.'),(20,'port','integer','port of connection.'),(21,'Slave ID','integer','Slave ID (put 1 by default).'),(22,'IO Type','string','Type (HR,AI) of register used for the modbus address.'),(23,'Own IP','ip','The IP of your computer.'),(24,'Image path 1','string','Path of the original image.'),(25,'Image path 2','string','Path of motif.'),(26,'Thresold','string','comparison criteria for image comparison validation.'),(27,'Class','string','Class of the equipment.'),(28,'Equipment','string','Equipment to move to'),(29,'Register','string','Register address of the targeted equipment attribute to trigger via modbus.'),(30,'Value','string','value to input to trigger the targeted stat22us.'),(31,'Low value','string','Minimum physical value.'),(32,'Max value','string','Maximum physical value.'),(33,'Scalling factor','string','Scalling factor'),(34,'Output buffer','buffer','Name of the buffer created to store the value triggered.'),(35,'Message to display','string','Message to display.'),(36,'Time to pause','integer','Time to pause'),(37,'Image path','string','Path of the image on which move the cursor.'),(38,'x','integer','coordinate'),(39,'y','integer','coordinate'),(40,'String 1','string','String 1 to be joined in a single text string.'),(41,'String 2','string','String 2 to be joined in a single text string.'),(42,'Concatenate output buffer','buffer','name of the buffer created to store the concatenation of the 2 strings.'),(43,'Value 1','integer','Bit 0'),(44,'Value 2','integer','Bit 1'),(45,'Register Bit','integer','Register bit'),(48,'Color','color','This is a color'),(49,'String','string','Parameter with the value string.'),(50,'equipment','buffer','equipment in parameter'),(51,'Path','string','Path to save the screenshot.'),(52,'HMI','string','Name of the HMI to load information from.'),(53,'Classe','string','Name of the class to load information from.'),(54,'Equipment','string','Name of the equipment to load information from.'),(55,'Image path','string','path of the image.'),(56,'Accuracy','string','Minimum acceptance rate.'),(57,'Software path','string','Path of the software to launch.'),(58,'String to save','string','String to save in the file'),(59,'File path','string','Path where store the file'),(60,'File name','string','Name of the file in which store the string.'),(61,'Folder save','string','Path of the folder to save the screenshot in'),(62,'Equipment name','string','Name of the equipment screenshoted'),(63,'Buffer string','buffer','Buffer to save the path of the image into.');");
        q.executeUpdate();

        q = session.createSQLQuery("INSERT INTO `script` VALUES (11,'SSH command','Execute a SSH command. The outptut is stored in a buffer.',1,'26-08-2015',NULL,1,0,'ExecuteSSHCommand'),(12,'retrieve line','return in a buffer the lines which contain the substring given as parameter',1,'26-08-2015',NULL,1,0,'IdentifyLine'),(13,'Connect TCP modbus srv','Connect to the TCP server for modbus',1,'26-08-2015',NULL,1,0,'LaunchTCPServerModbus'),(14,'Image comparison (match and bit)','Image comparison',1,'26-08-2015',NULL,0,0,'MatchAndBit'),(15,'Search occurrence','Find if a string contains a substring given as parameter',1,'26-08-2015',NULL,0,0,'SearchOccurence'),(17,'Trigger of DI (Modbus)','Trigger a DI point via modbus protocol.',1,'26-08-2015',NULL,1,0,'TriggerModbusPointDI'),(18,'Trigger of AI (Modbus)','Trigger an AI point via modbus protocol.\\n',1,'26-08-2015',NULL,1,0,'TriggerModbusPointAI'),(19,'Mouse Click','perfom a mouse click.',1,'26-08-2015',NULL,1,0,'clickSouris'),(20,'Manual check (PopUp)','Display a popUp to the user in order to perform a check.',1,'26-08-2015',NULL,1,0,'popUpCheck'),(21,'Manual action (PopUp)','Display a popUp to the user in order to perform an action.',1,'26-08-2015',NULL,1,0,'popUpStimuli'),(22,'Pause time','Break during a certain amount of time.',1,'26-08-2015',NULL,1,0,'wait'),(23,'Move cursor to image','Move the cursor on an image present on the screen.',1,'26-08-2015',NULL,1,0,'MoveMouseOnImages'),(24,'move cursor to coordinates','Move the cursor on coordinates of the screen.',1,'26-08-2015',NULL,1,0,'MoveMouseCoordinates'),(25,'Concatenate','Join 2 texts string into one text string.',1,'26-08-2015',NULL,1,0,'Concat'),(27,'Trigger Modbus (DI2)','Trigger a DI2 point via modbus protocol',1,'27-08-2015',NULL,1,0,'TriggerModbusPointDI2'),(35,'Color pourcentage','Find the color pourcentage in the image.',1,'07-09-2015',NULL,0,0,'testColorImage'),(37,'Click on equipment','Prerequisit : Move to equipment Layout. Click on the equipment given in parameter',1,'04-09-2015',NULL,1,0,'ClickOnEquipment'),(38,'Keyboard inputs','Simulate the keyboard and write the string given in parameter',1,'04-09-2015',NULL,1,0,'KeyboardInput'),(39,'Screenshot','Take a screenshot of the current physic display',1,'04-09-2015',NULL,1,0,'TakeScreenshot'),(40,'Screenshot equipment','Prerequisit : Move to equipment Layout. Take a screenshot of the equipment given in parameter',1,'04-09-2015',NULL,1,0,'TakeScreenShotEQP'),(41,'Go to equipment layout','Go to the layout of the equipement given in parameters.',1,'04-09-2015',NULL,1,0,'TriggerEquipment'),(42,'CheckSSHEvent','Connect to the scada server and retrive the last line. Compare the infromation found in this line with the infromation (Station, EQP code, EQP number ...) found in the excel file.',1,'09-09-2015',NULL,0,1,NULL),(43,'ClickAndTriggerStateEQP','Click on one equipment given in input and trigger the wanted state',1,'09-09-2015',NULL,1,1,NULL),(44,'launch software','Launch a software',1,'09-09-2015',NULL,1,0,'launchSoftware'),(45,'Save in file','save the value given as parameter in a file',1,'09-09-2015',NULL,1,0,'SaveInFile'),(46,'Screenshot equipment buffer','Take a screenshot of an equipment and return it into a buffer',1,'09-09-2015',NULL,1,0,'TakeSCEQPBuffer'),(47,'Wait without popup','Wait script without having a popup',1,'09-09-2015',NULL,1,0,'WaitW');");
        q.executeUpdate();

        q = session.createSQLQuery("INSERT INTO `script_has_parameters` VALUES (25,10,11,0),(26,11,11,1),(27,12,11,2),(28,13,11,3),(29,14,11,4),(30,15,11,5),(31,10,12,0),(32,16,12,1),(33,17,12,2),(34,18,12,3),(35,19,12,4),(36,10,13,0),(37,23,13,1),(38,20,13,2),(39,21,13,3),(40,22,13,4),(41,10,14,0),(42,24,14,1),(43,25,14,2),(44,26,14,3),(45,10,15,0),(46,16,15,1),(47,17,15,2),(48,1,15,3),(52,10,17,0),(53,29,17,1),(54,30,17,2),(55,10,18,0),(56,29,18,1),(57,31,18,2),(58,32,18,3),(59,33,18,4),(60,34,18,5),(61,10,19,0),(62,10,20,0),(63,35,20,1),(64,10,21,0),(65,35,21,1),(66,10,22,0),(67,36,22,1),(68,10,23,0),(69,37,23,1),(70,10,24,0),(71,38,24,1),(72,39,24,2),(73,10,25,0),(74,40,25,1),(75,41,25,2),(76,42,25,3),(77,10,27,0),(78,29,27,1),(79,43,27,2),(80,44,27,3),(81,45,27,4),(86,10,35,0),(87,10,38,0),(88,49,38,1),(89,10,37,0),(90,50,37,1),(93,51,39,1),(94,10,39,0),(95,10,40,0),(96,51,40,1),(97,50,40,2),(98,10,41,0),(99,52,41,1),(100,53,41,2),(101,54,41,3),(102,55,35,1),(103,56,35,3),(104,48,35,2),(105,50,41,4),(107,57,44,1),(108,10,44,0),(109,10,45,0),(110,58,45,1),(111,59,45,2),(112,60,45,3),(113,61,46,1),(114,62,46,2),(115,50,46,3),(116,63,46,4),(117,10,47,0),(118,36,47,1);");
        q.executeUpdate();

        q = session.createSQLQuery("INSERT INTO `macro` VALUES (1,42,11,0),(2,42,15,1),(3,42,15,2),(4,42,15,3),(5,42,15,4),(6,42,15,5),(7,42,15,6),(8,43,37,0),(9,43,22,1),(10,43,23,2),(11,43,22,4),(12,43,23,5),(13,43,22,7),(14,43,23,8),(15,43,22,10),(16,43,23,11),(17,43,22,13),(18,43,23,14),(19,43,22,16),(20,43,23,17),(21,43,19,3),(22,43,19,6),(23,43,19,9),(24,43,19,12),(25,43,19,15),(26,43,19,18);");
        q.executeUpdate();

        q = session.createSQLQuery("INSERT INTO `param_script_macro` VALUES (1,1,NULL,25,0,'Constant','Send SSHLine',0),(2,1,NULL,27,0,'Constant','scada',2),(3,1,NULL,30,0,'Buffer list','@&Buffer_Buffer_A',5),(4,1,NULL,29,0,'Constant','scsolsshow -lEventList -r | tail -f -n 1',4),(5,1,NULL,26,0,'Constant','128.59.8.206',1),(6,1,NULL,28,0,'Constant','scada',3),(7,2,NULL,46,1,'','',1),(8,2,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(9,2,NULL,45,0,'Constant','Search EQP number',0),(10,2,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){14}',2),(11,3,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(12,3,NULL,45,0,'Constant','Search Attibute description',0),(13,3,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){7}',2),(14,3,NULL,46,1,'','',1),(15,4,NULL,45,0,'Constant','Search State',0),(16,4,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){8}',2),(17,4,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(18,4,NULL,46,1,'','',1),(19,5,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){6}',2),(20,5,NULL,45,0,'Constant','Search EQP description',0),(21,5,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(22,5,NULL,46,1,'','',1),(23,6,NULL,45,0,'Constant','Search EQP code',0),(24,6,NULL,46,1,'','',1),(25,6,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(26,6,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){12}',2),(27,7,NULL,45,0,'Constant','Search station',0),(28,7,NULL,47,0,'Constant',':(.+?):',2),(29,7,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(30,7,NULL,46,1,'','',1),(31,8,NULL,90,1,'','',1),(32,8,NULL,89,0,'Constant','Click on given EQP',0),(33,9,NULL,66,0,'Constant','Wait',0),(34,9,NULL,67,0,'Constant','1',1),(35,10,NULL,68,0,'Constant','Click on image given',0),(36,10,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\advance.png',1),(37,11,34,67,0,'2 (Pause time)','Time to pause',1),(38,11,33,66,0,'2 (Pause time)','Script purpose',0),(39,12,NULL,68,0,'Constant','Click on image',0),(40,12,NULL,69,1,'','',1),(41,13,33,66,0,'2 (Pause time)','Script purpose',0),(42,13,NULL,67,0,'Constant','1',1),(43,14,35,68,0,'3 (Move cursor to image)','Script purpose',0),(44,14,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\apply.png',1),(45,15,33,66,0,'2 (Pause time)','Script purpose',0),(46,15,NULL,67,0,'Constant','1',1),(47,16,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\information.png',1),(48,16,35,68,0,'3 (Move cursor to image)','Script purpose',0),(49,17,NULL,67,0,'Constant','1',1),(50,17,NULL,66,0,'Constant','Wait',0),(51,18,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\alarmack.png',1),(52,18,NULL,68,0,'Constant','Click on image',0),(53,19,NULL,67,0,'Constant','1',1),(54,19,NULL,66,0,'Constant','Wait',0),(55,20,NULL,68,0,'Constant','Click on image',0),(56,20,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\close2.png',1),(57,21,NULL,61,0,'Constant','Click',0),(58,22,NULL,61,0,'Constant','Click',0),(59,23,NULL,61,0,'Constant','Click',0),(60,24,NULL,61,0,'Constant','Click',0),(61,25,NULL,61,0,'Constant','Click',0),(62,26,NULL,61,0,'Constant','Click',0);");
        q.executeUpdate();

//        q = session.createSQLQuery("INSERT INTO `param_script_macro` VALUES (1,1,NULL,25,0,'Constant','Send SSHLine',0),(2,1,NULL,27,0,'Constant','scada',2),(3,1,NULL,30,0,'Buffer list','@&Buffer_Buffer_A',5),(4,1,NULL,29,0,'Constant','scsolsshow -lEventList -r | tail -f -n 1',4),(5,1,NULL,26,0,'Constant','128.59.8.206',1),(6,1,NULL,28,0,'Constant','scada',3),(7,2,NULL,46,1,'','',1),(8,2,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(9,2,NULL,45,0,'Constant','Search EQP number',0),(10,2,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){14}',2),(11,3,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(12,3,NULL,45,0,'Constant','Search Attibute description',0),(13,3,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){7}',2),(14,3,NULL,46,1,'','',1),(15,4,NULL,45,0,'Constant','Search State',0),(16,4,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){8}',2),(17,4,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(18,4,NULL,46,1,'','',1),(19,5,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){6}',2),(20,5,NULL,45,0,'Constant','Search EQP description',0),(21,5,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(22,5,NULL,46,1,'','',1),(23,6,NULL,45,0,'Constant','Search EQP code',0),(24,6,NULL,46,1,'','',1),(25,6,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(26,6,NULL,47,0,'Constant','(?:\\|(^$|([^\\|]*))){12}',2),(27,7,NULL,45,0,'Constant','Search station',0),(28,7,NULL,47,0,'Constant',':(.+?):',2),(29,7,NULL,48,0,'Buffer list','@&Buffer_Buffer_A',3),(30,7,NULL,46,1,'','',1),(31,8,NULL,90,1,'','',1),(32,8,NULL,89,0,'Constant','Click on given EQP',0),(33,9,NULL,66,0,'Constant','Wait',0),(34,9,NULL,67,0,'Constant','1',1),(35,10,NULL,68,0,'Constant','Click on image given',0),(36,10,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\advance.png',1),(37,11,34,67,0,'2 (Pause time)','Time to pause',1),(38,11,33,66,0,'2 (Pause time)','Script purpose',0),(39,12,NULL,68,0,'Constant','Click on image',0),(40,12,NULL,69,1,'','',1),(41,13,33,66,0,'2 (Pause time)','Script purpose',0),(42,13,NULL,67,0,'Constant','1',1),(43,14,35,68,0,'3 (Move cursor to image)','Script purpose',0),(44,14,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\apply.png',1),(45,15,33,66,0,'2 (Pause time)','Script purpose',0),(46,15,NULL,67,0,'Constant','1',1),(47,16,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\information.png',1),(48,16,35,68,0,'3 (Move cursor to image)','Script purpose',0),(49,17,NULL,67,0,'Constant','1',1),(50,17,NULL,66,0,'Constant','Wait',0),(51,18,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\alarmack.png',1),(52,18,NULL,68,0,'Constant','Click on image',0),(53,19,NULL,67,0,'Constant','1',1),(54,19,NULL,66,0,'Constant','Wait',0),(55,20,NULL,68,0,'Constant','Click on image',0),(56,20,NULL,69,0,'Constant','C:\\Users\\tmartinez\\Desktop\\inspectoPanel\\close2.png',1),(57,21,NULL,61,0,'Constant','Click',0),(58,22,NULL,61,0,'Constant','Click',0),(59,23,NULL,61,0,'Constant','Click',0),(60,24,NULL,61,0,'Constant','Click',0),(61,25,NULL,61,0,'Constant','Click',0),(62,26,NULL,61,0,'Constant','Click',0);");
//        q.executeUpdate();

        session.beginTransaction().commit();
        session.close();
    }
}
