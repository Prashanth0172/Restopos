package com.hyva.restopos.rest.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by chinmay.b on 3/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiVoucherPayment {
    private String voucherNo;
    private double voucherAmt;
    private String transactionNo;
    private Long paymentType;
    private String paymentName;

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Long getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Long paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public double getVoucherAmt() {
        return voucherAmt;
    }

    public void setVoucherAmt(double voucherAmt) {
        this.voucherAmt = voucherAmt;
    }
}
