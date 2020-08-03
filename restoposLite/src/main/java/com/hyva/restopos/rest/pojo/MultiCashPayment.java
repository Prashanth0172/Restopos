package com.hyva.restopos.rest.pojo;
public class MultiCashPayment {
    private Long paymentType;
    private double totalCPAmountRefunded;
    private double totalCPDiscount;
    private double cashAmt;
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

    public double getTotalCPAmountRefunded() {
        return totalCPAmountRefunded;
    }

    public void setTotalCPAmountRefunded(double totalCPAmountRefunded) {
        this.totalCPAmountRefunded = totalCPAmountRefunded;
    }

    public double getTotalCPDiscount() {
        return totalCPDiscount;
    }

    public void setTotalCPDiscount(double totalCPDiscount) {
        this.totalCPDiscount = totalCPDiscount;
    }

    public double getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(double cashAmt) {
        this.cashAmt = cashAmt;
    }
}
