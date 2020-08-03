package com.hyva.restopos.rest.pojo;

import java.util.List;

public class AirPayPayment {
    private String paymentType;
    private double totalAPAmountRefunded;
    private double totalAPDiscount;
    private double totalAPAmountTendered;
    private List<MultiAirPayPayment> airPayPaymentList;


    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getTotalAPAmountRefunded() {
        return totalAPAmountRefunded;
    }

    public void setTotalAPAmountRefunded(double totalAPAmountRefunded) {
        this.totalAPAmountRefunded = totalAPAmountRefunded;
    }

    public double getTotalAPDiscount() {
        return totalAPDiscount;
    }

    public void setTotalAPDiscount(double totalAPDiscount) {
        this.totalAPDiscount = totalAPDiscount;
    }

    public double getTotalAPAmountTendered() {
        return totalAPAmountTendered;
    }

    public void setTotalAPAmountTendered(double totalAPAmountTendered) {
        this.totalAPAmountTendered = totalAPAmountTendered;
    }

    public List<MultiAirPayPayment> getAirPayPaymentList() {
        return airPayPaymentList;
    }

    public void setAirPayPaymentList(List<MultiAirPayPayment> airPayPaymentList) {
        this.airPayPaymentList = airPayPaymentList;
    }
}
