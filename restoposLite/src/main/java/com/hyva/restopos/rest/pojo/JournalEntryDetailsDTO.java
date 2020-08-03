package com.hyva.restopos.rest.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;


@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalEntryDetailsDTO {
    private Long jeDId;
    private String jeId;
    private String formNo;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String remark;
    private Long accountid;
    private AccountMasterDTO am_accountid;
    private String accountname;
    private String account_code;
/*
    private AccountGroupDTO ag_id;
*/
    private String stringAccountCode;
    private boolean flag;
    private String codecaoId;
    private String aparcode;
    private String status;
    private String locationId;
    private String useraccount_id;
    private String parentAccount_name;
    private String accountGroup;
    private double amtinclusivetax;
    private String accountDescription;
    private String taxName;
    private double amtexclusivetax;
    private long    taxid;
    private double taxpercent;
    private double taxamt;

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public Long getAccountid() {
        return accountid;
    }

    public void setAccountid(Long accountid) {
        this.accountid = accountid;
    }

    public AccountMasterDTO getAm_accountid() {
        return am_accountid;
    }

    public void setAm_accountid(AccountMasterDTO am_accountid) {
        this.am_accountid = am_accountid;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccount_code() {
        return account_code;
    }

    public void setAccount_code(String account_code) {
        this.account_code = account_code;
    }

//    public AccountGroupDTO getAg_id() {
//        return ag_id;
//    }
//
//    public void setAg_id(AccountGroupDTO ag_id) {
//        this.ag_id = ag_id;
//    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParentAccount_name() {
        return parentAccount_name;
    }

    public void setParentAccount_name(String parentAccount_name) {
        this.parentAccount_name = parentAccount_name;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public double getAmtinclusivetax() {
        return amtinclusivetax;
    }

    public void setAmtinclusivetax(double amtinclusivetax) {
        this.amtinclusivetax = amtinclusivetax;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public double getAmtexclusivetax() {
        return amtexclusivetax;
    }

    public void setAmtexclusivetax(double amtexclusivetax) {
        this.amtexclusivetax = amtexclusivetax;
    }

    public long getTaxid() {
        return taxid;
    }

    public void setTaxid(long taxid) {
        this.taxid = taxid;
    }

    public double getTaxpercent() {
        return taxpercent;
    }

    public void setTaxpercent(double taxpercent) {
        this.taxpercent = taxpercent;
    }

    public double getTaxamt() {
        return taxamt;
    }

    public void setTaxamt(double taxamt) {
        this.taxamt = taxamt;
    }

    public Long getJeDId() {
        return jeDId;
    }

    public void setJeDId(Long jeDId) {
        this.jeDId = jeDId;
    }

    public String getJeId() {
        return jeId;
    }

    public void setJeId(String jeId) {
        this.jeId = jeId;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getUseraccount_id() {
        return useraccount_id;
    }

    public void setUseraccount_id(String useraccount_id) {
        this.useraccount_id = useraccount_id;
    }
}
