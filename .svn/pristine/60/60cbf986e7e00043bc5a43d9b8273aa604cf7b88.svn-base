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

public class JournalEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long jeId;
    private String jeNo;
    @Temporal(TemporalType.DATE)
    private Date jeDate;
    private String memo;
    private String status;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getJeId() {
        return jeId;
    }

    public void setJeId(Long jeId) {
        this.jeId = jeId;
    }

    public String getJeNo() {
        return jeNo;
    }

    public void setJeNo(String jeNo) {
        this.jeNo = jeNo;
    }

    public Date getJeDate() {
        return jeDate;
    }

    public void setJeDate(Date jeDate) {
        this.jeDate = jeDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }
}
