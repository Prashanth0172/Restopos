/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.pojo;

/**
 *
 * @author admin
 */
public class TaxSummary {
    private String taxName;
    private long taxId;
    private double taxAmount;
    private double taxPercent;
    private double amt;
    private double taxPercentageSplit;
    private double cessAmt;
    private double taxableAmt;
    private double cessPercent;
    public double getCessPercent() {
        return cessPercent;
    }
    public void setCessPercent(double cessPercent) {
        this.cessPercent = cessPercent;
    }
    public double getTaxableAmt() {
        return taxableAmt;
    }
    public void setTaxableAmt(double taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public double getCessAmt() {
        return cessAmt;
    }

    public void setCessAmt(double cessAmt) {
        this.cessAmt = cessAmt;
    }

    public double getTaxPercentageSplit() {
        return taxPercentageSplit;
    }

    public void setTaxPercentageSplit(double taxPercentageSplit) {
        this.taxPercentageSplit = taxPercentageSplit;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public long getTaxId() {
        return taxId;
    }

    public void setTaxId(long taxId) {
        this.taxId = taxId;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }
}
