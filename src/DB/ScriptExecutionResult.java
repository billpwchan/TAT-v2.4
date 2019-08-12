package DB;
// Generated Jun 19, 2015 5:06:37 PM by Hibernate Tools 4.3.1


/**
 * ScriptExecutionResult generated by hbm2java
 */
public class ScriptExecutionResult implements java.io.Serializable {


    private ScriptExecutionResultId id;
    private ScriptExecutions scriptExecutions;
    private String result;
    private String comment;
    private String baselineId;

    /**
     *
     */
    public ScriptExecutionResult() {
    }

    /**
     * @param id
     * @param scriptExecutions
     * @param result
     */
    public ScriptExecutionResult(ScriptExecutionResultId id, ScriptExecutions scriptExecutions, String result) {
        this.id = id;
        this.scriptExecutions = scriptExecutions;
        this.result = result;
    }

    /**
     * @param id
     * @param scriptExecutions
     * @param result
     * @param comment
     */
    public ScriptExecutionResult(ScriptExecutionResultId id, ScriptExecutions scriptExecutions, String result, String comment) {
        this.id = id;
        this.scriptExecutions = scriptExecutions;
        this.result = result;
        this.comment = comment;
    }

    /**
     * @param id
     * @param scriptExecutions
     * @param result
     * @param comment
     * @param baselineId
     */
    public ScriptExecutionResult(ScriptExecutionResultId id, ScriptExecutions scriptExecutions, String result, String comment, String baselineId) {
        this.id = id;
        this.scriptExecutions = scriptExecutions;
        this.result = result;
        this.comment = comment;
        this.baselineId = baselineId;
    }

    public String getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(String baselineId) {
        this.baselineId = baselineId;
    }

    /**
     * @return
     */
    public ScriptExecutionResultId getId() {
        return this.id;
    }

    /**
     * @param id
     */
    public void setId(ScriptExecutionResultId id) {
        this.id = id;
    }

    /**
     * @return
     */
    public ScriptExecutions getScriptExecutions() {
        return this.scriptExecutions;
    }

    /**
     * @param scriptExecutions
     */
    public void setScriptExecutions(ScriptExecutions scriptExecutions) {
        this.scriptExecutions = scriptExecutions;
    }

    /**
     * @return
     */
    public String getResult() {
        return this.result;
    }

    /**
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }


}


