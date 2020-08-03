package com.hyva.restopos.rest.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountMasterDTO {
    private Long accountid;
    private AccountMasterDTO am_accountid;
    private String accountname;
    private String accountcode;
    private AccountGroupPojo agid;
    private String stringAccountCode;
    private boolean flag;
    private String codecaoId;
    private String aparcode;
    private String status;
    private String parentAccountname;
    private String accountTypeName;
    private String accountGroup;
    private double amtinclusivetax;
    private String accountDescription;
    private String invoiceNumber;
    private String taxName;
    private double amtexclusivetax;
    private long    taxid;
    private double taxpercent;
    private double taxamt;
    private String creditAmount;
    private String debitAmount;
    private BigDecimal gtAmountExcTax;
    private BigDecimal gtAmountIncTax;
    private BigDecimal taxAmt;
    private String taxRate;
    private Long accountTypeId;
    private Long accountGroupId;
    private Long amaccountid;
    private BigDecimal debit;
    private BigDecimal credit;
    private String remark;
    private String formNo;

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public Long getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Long accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public Long getAccountGroupId() {
        return accountGroupId;
    }

    public void setAccountGroupId(Long accountGroupId) {
        this.accountGroupId = accountGroupId;
    }

    public Long getAmaccountid() {
        return amaccountid;
    }

    public void setAmaccountid(Long amaccountid) {
        this.amaccountid = amaccountid;
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

    public String getAccountcode() {
        return accountcode;
    }

    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode;
    }

    public AccountGroupPojo getAgid() {
        return agid;
    }

    public void setAgid(AccountGroupPojo agid) {
        this.agid = agid;
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

    public String getParentAccountname() {
        return parentAccountname;
    }

    public void setParentAccountname(String parentAccountname) {
        this.parentAccountname = parentAccountname;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getGtAmountExcTax() {
        return gtAmountExcTax;
    }

    public void setGtAmountExcTax(BigDecimal gtAmountExcTax) {
        this.gtAmountExcTax = gtAmountExcTax;
    }

    public BigDecimal getGtAmountIncTax() {
        return gtAmountIncTax;
    }

    public void setGtAmountIncTax(BigDecimal gtAmountIncTax) {
        this.gtAmountIncTax = gtAmountIncTax;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
}



