package com.hyva.restopos.rest.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AirPayDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long airPayId;
    private String transactionID;
    private String apTransactionId;
    private String invoiceNo;
    private String merchantId;
    private String terminalId;
    private String cardIssuer;
    private String chMod;
    private String currenctCode;
    private String transactionStatus;
    private String message;
    private String rrn;
    private String authCode;
    private String customer;
    private String customerPhone;
    private String cardNumber;
    private String customerEmail;
    private String isRisk;
    private String ipnId;
    private String billedAmount;
    private String surCharge;
    private String token;
    private String cardType;
    private String transactionType;
    private String ccExpiry;
    private String customVar;
    private String walletBalance;
    private String cardCategory;
    private String ap_SecureHash;
    private String paymentId;
    private  String transactionTime;
    private String cardUniqueCode;
    private String bankName;
    private String cardCountry;
    private String txnMode;
    private String posEntryMode;
    private String merchantName;



    public Long getAirPayId() {
        return airPayId;
    }

    public void setAirPayId(Long airPayId) {
        this.airPayId = airPayId;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getApTransactionId() {
        return apTransactionId;
    }

    public void setApTransactionId(String apTransactionId) {
        this.apTransactionId = apTransactionId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getChMod() {
        return chMod;
    }

    public void setChMod(String chMod) {
        this.chMod = chMod;
    }

    public String getCurrenctCode() {
        return currenctCode;
    }

    public void setCurrenctCode(String currenctCode) {
        this.currenctCode = currenctCode;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getIsRisk() {
        return isRisk;
    }

    public void setIsRisk(String isRisk) {
        this.isRisk = isRisk;
    }

    public String getIpnId() {
        return ipnId;
    }

    public void setIpnId(String ipnId) {
        this.ipnId = ipnId;
    }

    public String getBilledAmount() {
        return billedAmount;
    }

    public void setBilledAmount(String billedAmount) {
        this.billedAmount = billedAmount;
    }

    public String getSurCharge() {
        return surCharge;
    }

    public void setSurCharge(String surCharge) {
        this.surCharge = surCharge;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCcExpiry() {
        return ccExpiry;
    }

    public void setCcExpiry(String ccExpiry) {
        this.ccExpiry = ccExpiry;
    }

    public String getCustomVar() {
        return customVar;
    }

    public void setCustomVar(String customVar) {
        this.customVar = customVar;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getAp_SecureHash() {
        return ap_SecureHash;
    }

    public void setAp_SecureHash(String ap_SecureHash) {
        this.ap_SecureHash = ap_SecureHash;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getCardUniqueCode() {
        return cardUniqueCode;
    }

    public void setCardUniqueCode(String cardUniqueCode) {
        this.cardUniqueCode = cardUniqueCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardCountry() {
        return cardCountry;
    }

    public void setCardCountry(String cardCountry) {
        this.cardCountry = cardCountry;
    }

    public String getTxnMode() {
        return txnMode;
    }

    public void setTxnMode(String txnMode) {
        this.txnMode = txnMode;
    }

    public String getPosEntryMode() {
        return posEntryMode;
    }

    public void setPosEntryMode(String posEntryMode) {
        this.posEntryMode = posEntryMode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
