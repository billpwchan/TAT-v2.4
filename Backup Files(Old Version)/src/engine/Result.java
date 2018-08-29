/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

/**
 *Class of a result of a script, step and case
 * @author Thomas M.
 */
public class Result {

    //the result
    private String result;
    
    //the comment of the test
    private String comment;

    //constructor of a result

    /**
     *
     */
        public Result() {
        this.result = "";
        this.comment = "";
    }

    /**
     * setter of the result of a Result
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Getter of a result of a Result
     * @return 
     */
    public String getResult() {
        return this.result;
    }

    /**
     * setter of a comment of a Result
     * @param comment the comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * getter of a comment of a Result
     * @return 
     */
    public String getComment() {
        return this.comment;
    }
}
