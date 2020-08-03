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
 * @author Niteen
 */
@Entity

public class OcOtherReceiptWithoutItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long otherChargesId;
    private BigDecimal amount;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;
    @Column(name = "orName", columnDefinition = "LONGTEXT")
    private String orName;
    private String otherContactID;

    private BigDecimal taxAmount;
    private BigDecimal amountIncludeTax;
    @OneToOne
    private AccountMaster accountid;

    @ManyToOne
    private OtherReceipt ocdetails;
    @Column(name = "claimableTax", columnDefinition = "LONGTEXT")
    private String claimableTax;

    private String invoiceNumber;



    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }



    public Long getOtherChargesId() {
        return otherChargesId;
    }

    public void setOtherChargesId(Long otherChargesId) {
        this.otherChargesId = otherChargesId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public AccountMaster getAccountid() {
        return accountid;
    }

    public void setAccountid(AccountMaster accountid) {
        this.accountid = accountid;
    }


    public OtherReceipt getOcdetails() {
        return ocdetails;
    }

    public void setOcdetails(OtherReceipt ocdetails) {
        this.ocdetails = ocdetails;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getAmountIncludeTax() {
        return amountIncludeTax;
    }

    public void setAmountIncludeTax(BigDecimal amountIncludeTax) {
        this.amountIncludeTax = amountIncludeTax;
    }

    public String getOrName() {
        return orName;
    }

    public void setOrName(String orName) {
        this.orName = orName;
    }

    public String getClaimableTax() {
        return claimableTax;
    }

    public void setClaimableTax(String claimableTax) {
        this.claimableTax = claimableTax;
    }

    public String getOtherContactID() {
        return otherContactID;
    }

    public void setOtherContactID(String otherContactID) {
        this.otherContactID = otherContactID;
    }
}
