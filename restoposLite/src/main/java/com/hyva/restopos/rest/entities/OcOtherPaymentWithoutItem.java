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

public class OcOtherPaymentWithoutItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long otherChargesId;
    private BigDecimal amount;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;
    @Column(name = "glName", columnDefinition = "LONGTEXT")
    private String glName;
    private BigDecimal taxAmount;
    private BigDecimal amountIncludeTax;
    @OneToOne
    private AccountMaster accountid;
    @Column(name = "claimableTax", columnDefinition = "LONGTEXT")
    private String claimableTax;
    //     @ManyToOne
//    @JoinColumn(name = "ocdetails")
//    private PurchaseInvoice ocdetails;
    @ManyToOne
    @JoinColumn(name = "ocdetails")
    private OtherPayment ocdetails;

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


    public OtherPayment getOcdetails() {
        return ocdetails;
    }

    public void setOcdetails(OtherPayment ocdetails) {
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

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        System.out.println("glName--->" + glName);
        this.glName = glName;
    }

    public String getClaimableTax() {
        return claimableTax;
    }

    public void setClaimableTax(String claimableTax) {
        this.claimableTax = claimableTax;
    }

}
