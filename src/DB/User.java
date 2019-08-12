package DB;
// Generated Jun 19, 2015 5:06:37 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {


    private Integer idUser;
    private String login;
    private String password;
    private String right;
    private String email;
    private String name;
    private Set testCampaigns = new HashSet(0);
    /**
     *
     */

    /**
     *
     */
    public User() {
    }

    /**
     * @param login
     * @param password
     * @param right
     * @param email
     * @param name
     * @param testCampaigns
     */

    public User(String login, String password, String right, String email, String name, Set testCampaigns) {
        this.login = login;
        this.password = password;
        this.right = right;
        this.email = email;
        this.name = name;
        this.testCampaigns = testCampaigns;
    }

    /**
     * @return
     */

    public Integer getIdUser() {
        return this.idUser;
    }

    /**
     * @param idUser
     */

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    /**
     * @return
     */

    public String getLogin() {
        return this.login;
    }

    /**
     * @param login
     */

    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return
     */

    public String getPassword() {
        return this.password;
    }

    /**
     * @param password
     */

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return
     */

    public String getRight() {
        return this.right;
    }

    /**
     * @param right
     */

    public void setRight(String right) {
        this.right = right;
    }

    /**
     * @return
     */

    public String getEmail() {
        return this.email;
    }

    /**
     * @param email
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return
     */

    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */

    public Set getTestCampaigns() {
        return this.testCampaigns;
    }

    /**
     * @param testCampaigns
     */

    public void setTestCampaigns(Set testCampaigns) {
        this.testCampaigns = testCampaigns;
    }


}


