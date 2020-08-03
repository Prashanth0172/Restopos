/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Admin
 */
@Entity

public class GLTransactions implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    @OneToOne
    private FormSetUp Tran_type;
    private String Tran_no;
    private String suppInNo;
    private String flagForGlTrans;
    @Temporal(TemporalType.DATE)
    private Date Tran_date;
    @ManyToOne
    private AccountMaster account;
    private String acc_code;
    private String acc_name;
    @Column(name = "memo", columnDefinition = "LONGTEXT")
    private String memo;
    private BigDecimal amount;
    @ManyToOne
    private Employee EmployeeId;
    private double bankCharge;
    private String currencyId;
    private String currencyValue;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }

    //parameterized constructor
    public GLTransactions(long id){this.id = id;}
    //default constructor
    public GLTransactions(){}


    public Employee getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(Employee EmployeeId) {
        this.EmployeeId = EmployeeId;
    }

    public Date getTran_date() {
        return Tran_date;
    }

    public void setTran_date(Date Tran_date) {
        this.Tran_date = Tran_date;
    }

    public String getTran_no() {
        return Tran_no;
    }

    public void setTran_no(String Tran_no) {
        this.Tran_no = Tran_no;
    }

    public FormSetUp getTran_type() {
        return Tran_type;
    }

    public void setTran_type(FormSetUp Tran_type) {
        this.Tran_type = Tran_type;
    }

    public AccountMaster getAccount() {
        return account;
    }

    public void setAccount(AccountMaster account) {
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAcc_code() {
        return acc_code;
    }

    public void setAcc_code(String acc_code) {
        this.acc_code = acc_code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getAcc_name() {
        return acc_name;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }

    public double getBankCharge() {
        return bankCharge;
    }

    public void setBankCharge(double bankCharge) {
        this.bankCharge = bankCharge;
    }

    public String getSuppInNo() {
        return suppInNo;
    }

    public void setSuppInNo(String suppInNo) {
        this.suppInNo = suppInNo;
    }

    public String getFlagForGlTrans() {
        return flagForGlTrans;
    }

    public void setFlagForGlTrans(String flagForGlTrans) {
        this.flagForGlTrans = flagForGlTrans;
    }


}
