/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Admin
 */
@Entity

public class JournalEntryDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long jeDId;
    @OneToOne
    private JournalEntry jeId;
    @OneToOne
    private AccountMaster accountid;
    private BigDecimal debit;
    private BigDecimal credit;
    private String remark;
    private String locationId;
    private String formNo;
    private String useraccount_id;

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
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

    public Long getJeDId() {
        return jeDId;
    }

    public void setJeDId(Long jeDId) {
        this.jeDId = jeDId;
    }

    public JournalEntry getJeId() {
        return jeId;
    }

    public void setJeId(JournalEntry jeId) {
        this.jeId = jeId;
    }

    public AccountMaster getAccountid() {
        return accountid;
    }

    public void setAccountid(AccountMaster accountid) {
        this.accountid = accountid;
    }

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
}
