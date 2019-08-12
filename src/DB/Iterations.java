package DB;
// Generated Jun 22, 2015 3:06:53 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Iterations generated by hbm2java
 */
public class Iterations implements java.io.Serializable {

    private int iditerations;
    private TestCampaign testCampaign;
    private String baselineId;
    private int iterationNumber;
    private String date;
    private String result;
    private Set caseExecutionses = new HashSet(0);
    private String type;
    private Double iterationRestult;
    private String user;

    /**
     *
     */
    public Iterations() {
    }

    /**
     * @param testCampaign
     * @param baselineId
     * @param iterationNumber
     * @param date
     */
    public Iterations(TestCampaign testCampaign, String baselineId, int iterationNumber, String date) {
        this.date = date;
        this.testCampaign = testCampaign;
        this.baselineId = baselineId;
        this.iterationNumber = iterationNumber;
    }

    /**
     * @param iditerations
     * @param testCampaign
     * @param baselineId
     * @param iterationNumber
     */
    public Iterations(int iditerations, TestCampaign testCampaign, String baselineId, int iterationNumber) {
        this.iditerations = iditerations;
        this.testCampaign = testCampaign;
        this.baselineId = baselineId;
        this.iterationNumber = iterationNumber;
    }

    /**
     * @param iditerations
     * @param testCampaign
     * @param baselineId
     * @param iterationNumber
     * @param date
     * @param caseExecutionses
     * @param user
     */
    public Iterations(int iditerations, TestCampaign testCampaign, String baselineId, int iterationNumber, String date, Set caseExecutionses, String user) {
        this.iditerations = iditerations;
        this.testCampaign = testCampaign;
        this.baselineId = baselineId;
        this.iterationNumber = iterationNumber;
        this.date = date;
        this.caseExecutionses = caseExecutionses;
    }

    /**
     * @return
     */
    public int getIditerations() {
        return this.iditerations;
    }

    /**
     * @param iditerations
     */
    public void setIditerations(int iditerations) {
        this.iditerations = iditerations;
    }

    /**
     * @return
     */
    public TestCampaign getTestCampaign() {
        return this.testCampaign;
    }

    /**
     * @param testCampaign
     */
    public void setTestCampaign(TestCampaign testCampaign) {
        this.testCampaign = testCampaign;
    }

    /**
     * @return
     */
    public String getBaselineId() {
        return this.baselineId;
    }

    /**
     * @param baselineId
     */
    public void setBaselineId(String baselineId) {
        this.baselineId = baselineId;
    }

    /**
     * @return
     */
    public int getIterationNumber() {
        return this.iterationNumber;
    }

    /**
     * @param iterationNumber
     */
    public void setIterationNumber(int iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    /**
     * @return
     */
    public String getDate() {
        return this.date;
    }

    /**
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return
     */
    public Set getCaseExecutionses() {
        return this.caseExecutionses;
    }

    /**
     * @param caseExecutionses
     */
    public void setCaseExecutionses(Set caseExecutionses) {
        this.caseExecutionses = caseExecutionses;
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
    public String getType() {
        return this.type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return
     */
    public Double getIterationResult() {
        return this.iterationRestult;
    }

    /**
     * @param iterationResult
     */
    public void setIterationResult(Double iterationResult) {
        this.iterationRestult = iterationResult;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

}
