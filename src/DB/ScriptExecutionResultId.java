package DB;
// Generated Jun 19, 2015 5:06:37 PM by Hibernate Tools 4.3.1


/**
 * ScriptExecutionResultId generated by hbm2java
 */
public class ScriptExecutionResultId implements java.io.Serializable {


    private int scriptExecutionsIdscriptExecutions;
    private byte iterationNumber;

    /**
     *
     */
    public ScriptExecutionResultId() {
    }

    /**
     * @param scriptExecutionsIdscriptExecutions
     * @param iterationNumber
     */
    public ScriptExecutionResultId(int scriptExecutionsIdscriptExecutions, byte iterationNumber) {
        this.scriptExecutionsIdscriptExecutions = scriptExecutionsIdscriptExecutions;
        this.iterationNumber = iterationNumber;
    }

    /**
     * @return
     */
    public int getScriptExecutionsIdscriptExecutions() {
        return this.scriptExecutionsIdscriptExecutions;
    }

    /**
     * @param scriptExecutionsIdscriptExecutions
     */
    public void setScriptExecutionsIdscriptExecutions(int scriptExecutionsIdscriptExecutions) {
        this.scriptExecutionsIdscriptExecutions = scriptExecutionsIdscriptExecutions;
    }

    /**
     * @return
     */
    public byte getIterationNumber() {
        return this.iterationNumber;
    }

    /**
     * @param iterationNumber
     */
    public void setIterationNumber(byte iterationNumber) {
        this.iterationNumber = iterationNumber;
    }


    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof ScriptExecutionResultId)) return false;
        ScriptExecutionResultId castOther = (ScriptExecutionResultId) other;

        return (this.getScriptExecutionsIdscriptExecutions() == castOther.getScriptExecutionsIdscriptExecutions())
                && (this.getIterationNumber() == castOther.getIterationNumber());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getScriptExecutionsIdscriptExecutions();
        result = 37 * result + this.getIterationNumber();
        return result;
    }


}


