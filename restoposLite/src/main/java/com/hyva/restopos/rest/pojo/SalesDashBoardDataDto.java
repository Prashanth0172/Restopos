package com.hyva.restopos.rest.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Admin on 7/14/2017.
 */
public class SalesDashBoardDataDto {
    private Date salesDate;
    private  double totalRecievable;
    private BigDecimal arBalance;
    private double totalAmount;
    private double salesTotalTaxAmt;
    private double totalDiscountAmount;


    public double getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(double totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getSalesTotalTaxAmt() {
        return salesTotalTaxAmt;
    }

    public void setSalesTotalTaxAmt(double salesTotalTaxAmt) {
        this.salesTotalTaxAmt = salesTotalTaxAmt;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public double getTotalRecievable() {
        return totalRecievable;
    }

    public void setTotalRecievable(double totalRecievable) {
        this.totalRecievable = totalRecievable;
    }

    public BigDecimal getArBalance() {
        return arBalance;
    }

    public void setArBalance(BigDecimal arBalance) {
        this.arBalance = arBalance;
    }
}
