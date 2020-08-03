/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Admin
 */
@Entity
@Table(name = "otherreceipt", uniqueConstraints = @UniqueConstraint(columnNames = {"orno"}))

public class OtherReceipt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long orId;
    private String orno;
    @Temporal(TemporalType.DATE)
    private Date orDate;
    private String referNo;
    //    @OneToOne
//    private Supplier SupplierId;
    private String supplierName;
    private String otherContactID;
    private String customerID;
    private String accountNo;
    private String accountDescription;
    @Column(name = "address", columnDefinition = "LONGTEXT")
    private String address;
    @ManyToOne
    private AccountMaster accountid;
    @OneToOne
    private PaymentMethod paymentmethodId;
    private BigDecimal totalAllocated;
    private BigDecimal totalPaid;
    private BigDecimal amountPaid;
    private BigDecimal balance;
    private BigDecimal totalTaxAmount;
    private BigDecimal outofBalance;
    private double amount2;
    private String tax;
    private double taxAmount;
    private String status;
    private String receiptStatus;
    private BigDecimal totalApplied;
    @Column(name = "memo", columnDefinition = "LONGTEXT")
    private String memo;
    @ManyToOne
    private AccountMaster accountid2;
    private String posting;
    private String taxInvoice;
    private String referenceNo;
    private String shippingReferenceNo;
    @Temporal(TemporalType.DATE)
    private Date shippingDate;
    @OneToMany(mappedBy = "ocdetails")
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<OcOtherReceiptWithoutItem> OCdetails;

    public String getOrno() {
        return orno;
    }

    public void setOrno(String orno) {
        this.orno = orno;
    }

    public PaymentMethod getPaymentmethodId() {
        return paymentmethodId;
    }

    public void setPaymentmethodId(PaymentMethod paymentmethodId) {
        this.paymentmethodId = paymentmethodId;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }



    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getShippingReferenceNo() {
        return shippingReferenceNo;
    }

    public void setShippingReferenceNo(String shippingReferenceNo) {
        this.shippingReferenceNo = shippingReferenceNo;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public AccountMaster getAccountid() {
        return accountid;
    }

    public void setAccountid(AccountMaster accountid) {
        this.accountid = accountid;
    }

    public Long getOrId() {
        return orId;
    }

    public void setOrId(Long orId) {
        this.orId = orId;
    }

    public Date getOrDate() {
        return orDate;
    }

    public void setOrDate(Date orDate) {
        this.orDate = orDate;
    }

    public String getReferNo() {
        return referNo;
    }

    public void setReferNo(String referNo) {
        this.referNo = referNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(BigDecimal totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getOutofBalance() {
        return outofBalance;
    }

    public void setOutofBalance(BigDecimal outofBalance) {
        this.outofBalance = outofBalance;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalApplied() {
        return totalApplied;
    }

    public void setTotalApplied(BigDecimal totalApplied) {
        this.totalApplied = totalApplied;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public AccountMaster getAccountid2() {
        return accountid2;
    }

    public void setAccountid2(AccountMaster accountid2) {
        this.accountid2 = accountid2;
    }


    public List<OcOtherReceiptWithoutItem> getOCdetails() {
        return OCdetails;
    }

    public void setOCdetails(List<OcOtherReceiptWithoutItem> OCdetails) {
        this.OCdetails = OCdetails;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getTaxInvoice() {
        return taxInvoice;
    }

    public void setTaxInvoice(String taxInvoice) {
        this.taxInvoice = taxInvoice;
    }

    public String getOtherContactID() {
        return otherContactID;
    }

    public void setOtherContactID(String otherContactID) {
        this.otherContactID = otherContactID;
    }

    public double getAmount2() {
        return amount2;
    }

    public void setAmount2(double amount2) {
        this.amount2 = amount2;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }
}
