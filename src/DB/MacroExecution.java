package DB;
// Generated Jun 19, 2015 5:06:37 PM by Hibernate Tools 4.3.1


/**
 * MacroExecution generated by hbm2java
 */
public class MacroExecution implements java.io.Serializable {


    private Integer idmacroExecution;
    private ScriptExecutions scriptExecutionsByScriptExecutionsIdscriptExecutions;
    private ScriptExecutions scriptExecutionsByScriptExecutionsIdscriptExecutions1;
    private Byte scriptOrder;

    /**
     *
     */
    public MacroExecution() {
    }

    /**
     * @param scriptExecutionsByScriptExecutionsIdscriptExecutions
     * @param scriptExecutionsByScriptExecutionsIdscriptExecutions1
     */
    public MacroExecution(ScriptExecutions scriptExecutionsByScriptExecutionsIdscriptExecutions, ScriptExecutions scriptExecutionsByScriptExecutionsIdscriptExecutions1) {
        this.scriptExecutionsByScriptExecutionsIdscriptExecutions = scriptExecutionsByScriptExecutionsIdscriptExecutions;
        this.scriptExecutionsByScriptExecutionsIdscriptExecutions1 = scriptExecutionsByScriptExecutionsIdscriptExecutions1;
    }

    /**
     * @param scriptExecutionsByScriptExecutionsIdscriptExecutions
     * @param scriptExecutionsByScriptExecutionsIdscriptExecutions1
     * @param scriptOrder
     */
    public MacroExecution(ScriptExecutions scriptExecutionsByScriptExecutionsIdscriptExecutions, ScriptExecutions scriptExecutionsByScriptExecutionsIdscriptExecutions1, Byte scriptOrder) {
        this.scriptExecutionsByScriptExecutionsIdscriptExecutions = scriptExecutionsByScriptExecutionsIdscriptExecutions;
        this.scriptExecutionsByScriptExecutionsIdscriptExecutions1 = scriptExecutionsByScriptExecutionsIdscriptExecutions1;
        this.scriptOrder = scriptOrder;
    }

    /**
     * @return
     */
    public Integer getIdmacroExecution() {
        return this.idmacroExecution;
    }

    /**
     * @param idmacroExecution
     */
    public void setIdmacroExecution(Integer idmacroExecution) {
        this.idmacroExecution = idmacroExecution;
    }

    /**
     * @return
     */
    public ScriptExecutions getScriptExecutionsByScriptExecutionsIdscriptExecutions() {
        return this.scriptExecutionsByScriptExecutionsIdscriptExecutions;
    }

    /**
     * @param scriptExecutionsByScriptExecutionsIdscriptExecutions
     */
    public void setScriptExecutionsByScriptExecutionsIdscriptExecutions(ScriptExecutions scriptExecutionsByScriptExecutionsIdscriptExecutions) {
        this.scriptExecutionsByScriptExecutionsIdscriptExecutions = scriptExecutionsByScriptExecutionsIdscriptExecutions;
    }

    /**
     * @return
     */
    public ScriptExecutions getScriptExecutionsByScriptExecutionsIdscriptExecutions1() {
        return this.scriptExecutionsByScriptExecutionsIdscriptExecutions1;
    }

    /**
     * @param scriptExecutionsByScriptExecutionsIdscriptExecutions1
     */
    public void setScriptExecutionsByScriptExecutionsIdscriptExecutions1(ScriptExecutions scriptExecutionsByScriptExecutionsIdscriptExecutions1) {
        this.scriptExecutionsByScriptExecutionsIdscriptExecutions1 = scriptExecutionsByScriptExecutionsIdscriptExecutions1;
    }

    /**
     * @return
     */
    public Byte getScriptOrder() {
        return this.scriptOrder;
    }

    /**
     * @param scriptOrder
     */
    public void setScriptOrder(Byte scriptOrder) {
        this.scriptOrder = scriptOrder;
    }


}


