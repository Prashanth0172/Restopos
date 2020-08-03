/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Admin
 */
@Entity
public class AccountMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long accountid;

    @OneToOne
//    @Cascade(CascadeType.ALL)
    private AccountMaster amaccountid;
    private String accountname;
    private String accountcode;
    @OneToOne
    private AccountGroup agid;
    private String stringAccountCode;
    private boolean flag;
    private String codecaoId;
    private String aparcode;
    private String status;
    @OneToOne
    private AccountType accountTypeId;
    private String reportvalue;

    public String getReportvalue() {
        return reportvalue;
    }

    public void setReportvalue(String reportvalue) {
        this.reportvalue = reportvalue;
    }

    public AccountType getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(AccountType accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public AccountMaster(long accountid){this.accountid=accountid;}

    public AccountMaster(){

    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountcode() {
        return accountcode;
    }

    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode;
    }

    public AccountGroup getAgid() {
        return agid;
    }

    public void setAgid(AccountGroup agid) {
        this.agid = agid;
    }

    public AccountMaster getAmaccountid() {
        return amaccountid;
    }

    public void setAmaccountid(AccountMaster amaccountid) {
        this.amaccountid = amaccountid;
    }

    public Long getAccountid() {
        return accountid;
    }

    public void setAccountid(Long accountid) {
        this.accountid = accountid;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStringAccountCode() {
        return stringAccountCode;
    }

    public void setStringAccountCode(String stringAccountCode) {
        this.stringAccountCode = stringAccountCode;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

//    @Override
//    public String toString() {
////        String res = null;
////        switch (this.account_code.length()) {
////            case 2:
////                res = this.ag_id.getAccount_code1() + c + this.am_accountid.account_code + c + this.account_code + ": " + this.account_name;
////                break;
////            case 3:
////                res = this.ag_id.getAccount_code1() + c + this.account_code + c + "00: " + this.account_name;
////                break;
////            case 4:
////                res = this.account_code + c + "000" + c + "00: " + this.account_name;
////                break;
////        }
//        String res = this.stringAccountCode + " : " + this.account_name;
//        return res;
//
//    }

//    public String geCodeInFormat() {
//        String s = (this.getAg_id().getAccount_code2() == null) ? this.getAg_id().getAccount_code1() : this.getAg_id().getAccount_code2();
//        return s + "/" + this.account_code;
//    }

//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof AccountMaster)) {
//            return false;
//        }
//        AccountMaster other = (AccountMaster) object;
//        if ((this.accountid == null && other.accountid != null) || (this.accountid != null && !this.accountid.equals(other.accountid))) {
//            return false;
//        }
//        return true;
//    }

//    public String getCode() {
//        char c = "/".charAt(0);
//        try {
//            // c=CompanyDAO.getCodeSep();
//        } catch (NullPointerException ne) {
//        }
//        String res = null;
//        switch (this.account_code.length()) {
//            case 2:
//                res = this.ag_id.getAccount_code1() + c + this.am_accountid.account_code + c + this.account_code + ": " + this.account_name;
//                break;
//            case 3:
//                res = this.ag_id.getAccount_code1() + c + this.account_code + c + "00: " + this.account_name;
//                break;
//            case 4:
//                res = this.account_code + c + "000" + c + "00: " + this.account_name;
//                break;
//        }
//        return res;
//    }

//    public String getCodeNEW() {
//        char c = "/".charAt(0);
//        try {
//            //   c=CompanyDAO.getCodeSep();
//        } catch (NullPointerException ne) {
//        }
//        String res = null;
//        switch (this.account_code.length()) {
//            case 2:
//                res = this.ag_id.getAccount_code1() + c + this.am_accountid.account_code + c + this.account_code;
//                break;
//            case 3:
//                res = this.ag_id.getAccount_code1() + c + this.account_code + c + "00";
//                break;
//            case 4:
//                res = this.account_code + c + "000" + c + "00";
//                break;
//        }
//        return res;
//    }


    public String getCodecaoId() {
        return codecaoId;
    }

    public void setCodecaoId(String codecaoId) {
        this.codecaoId = codecaoId;
    }

    public String getAparcode() {
        return aparcode;
    }

    public void setAparcode(String aparcode) {
        this.aparcode = aparcode;
    }
}
