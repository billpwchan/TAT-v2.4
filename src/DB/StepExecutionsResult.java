package DB;
// Generated Jun 19, 2015 5:06:37 PM by Hibernate Tools 4.3.1



/**
 * StepExecutionsResult generated by hbm2java
 */
public class StepExecutionsResult  implements java.io.Serializable {    

     private StepExecutionsResultId id;
     private StepExecutions stepExecutions;
     private String result;
     private String comment;

    public String getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(String baselineId) {
        this.baselineId = baselineId;
    }

    private String baselineId;

    /**
     *
     */
    public StepExecutionsResult() {
    }

    /**
     *
     * @param id
     * @param stepExecutions
     * @param result
     */
    public StepExecutionsResult(StepExecutionsResultId id, StepExecutions stepExecutions, String result) {
        this.id = id;
        this.stepExecutions = stepExecutions;
        this.result = result;
    }

    /**
     *
     * @param id
     * @param stepExecutions
     * @param result
     * @param comment
     */
    public StepExecutionsResult(StepExecutionsResultId id, StepExecutions stepExecutions, String result, String comment) {
       this.id = id;
       this.stepExecutions = stepExecutions;
       this.result = result;
       this.comment = comment;
    }

    /**
     *
     * @param id
     * @param stepExecutions
     * @param result
     * @param comment
     * @param baselineId
     */
    public StepExecutionsResult(StepExecutionsResultId id, StepExecutions stepExecutions, String result, String comment, String baselineId) {
        this.id = id;
        this.stepExecutions = stepExecutions;
        this.result = result;
        this.comment = comment;
        this.baselineId = baselineId;
    }
   
    /**
     *
     * @return
     */
    public StepExecutionsResultId getId() {
        return this.id;
    }
    
    /**
     *
     * @param id
     */
    public void setId(StepExecutionsResultId id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public StepExecutions getStepExecutions() {
        return this.stepExecutions;
    }
    
    /**
     *
     * @param stepExecutions
     */
    public void setStepExecutions(StepExecutions stepExecutions) {
        this.stepExecutions = stepExecutions;
    }

    /**
     *
     * @return
     */
    public String getResult() {
        return this.result;
    }
    
    /**
     *
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     *
     * @return
     */
    public String getComment() {
        return this.comment;
    }
    
    /**
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }




}


