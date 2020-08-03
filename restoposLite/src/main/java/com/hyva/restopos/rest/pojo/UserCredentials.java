package com.hyva.restopos.rest.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @Author udaybhaskar
 * Created on 4/1/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCredentials {
    private String companyEmail;
    private String userName;
    private String password;

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
