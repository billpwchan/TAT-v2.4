package DB;
// Generated Jun 19, 2015 5:06:37 PM by Hibernate Tools 4.3.1


/**
 * StepExecutionsResultId generated by hbm2java
 */
public class StepExecutionsResultId implements java.io.Serializable {


    private int stepExecutionsIdstepExecutions;
    private byte iterationNumber;

    /**
     *
     */
    public StepExecutionsResultId() {
    }

    /**
     * @param stepExecutionsIdstepExecutions
     * @param iterationNumber
     */
    public StepExecutionsResultId(int stepExecutionsIdstepExecutions, byte iterationNumber) {
        this.stepExecutionsIdstepExecutions = stepExecutionsIdstepExecutions;
        this.iterationNumber = iterationNumber;
    }

    /**
     * @return
     */
    public int getStepExecutionsIdstepExecutions() {
        return this.stepExecutionsIdstepExecutions;
    }

    /**
     * @param stepExecutionsIdstepExecutions
     */
    public void setStepExecutionsIdstepExecutions(int stepExecutionsIdstepExecutions) {
        this.stepExecutionsIdstepExecutions = stepExecutionsIdstepExecutions;
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
        if (!(other instanceof StepExecutionsResultId)) return false;
        StepExecutionsResultId castOther = (StepExecutionsResultId) other;

        return (this.getStepExecutionsIdstepExecutions() == castOther.getStepExecutionsIdstepExecutions())
                && (this.getIterationNumber() == castOther.getIterationNumber());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getStepExecutionsIdstepExecutions();
        result = 37 * result + this.getIterationNumber();
        return result;
    }


}


