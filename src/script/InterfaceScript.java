/**
 *
 */
package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import engine.Result;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Define the shell of all script class.
 *
 * @author Morin Thomas.
 * @version 0.1
 *
 */
public interface InterfaceScript {

    /**
     * Run the script.
     *
     * @param parameters available in input and to use for the script execution
     * @param hashMap in which it is possible to return objects
     * @return the result of the test. Object result is composed by a comment and a result (Result can be : OK,MOK,OKWC,NT,IC,OS). In case of a stimuli, the object result returned 
     * can be "null". In this case put "return null" at the end of the run function.
     */
    Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception;

    /**
     * close the script, all the connections opened for example. This function is called at the end of each test case.
     */
    void close();

    /**
     * Return all the parameters needed for the script with their name, type and
     * description. This Script is useful to add new scripts in the TAT, all the information of the parameters are automatically filled for the user.
     *
     * @return the ArrayList with all the parameters for the script
     * 
     */
    ArrayList<Parameters> parameters();

    /**
     * Return the script with all its information
     * This Function is useful to add new scripts in the TAT, All the information of the script are automatically filled for the user.
     *
     * @return the script with its information
     */
    Script scriptInfos();
    
}
