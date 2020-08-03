package com.hyva.restopos.rest.pojo;

import java.util.List;

public class DashBoardDto {
    private double cashInHand;
    private double cashInBank;
    private double totalRecievable;
    private  double totalPayable;
    private  double taxamount;
    private  double amount;
    private  double discount;
    private List<SalesDashBoardDataDto> salesDashBoardDataDtoList;



    public double getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(double taxamount) {
        this.taxamount = taxamount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getCashInHand() {
        return cashInHand;
    }

    public void setCashInHand(double cashInHand) {
        this.cashInHand = cashInHand;
    }

    public double getCashInBank() {
        return cashInBank;
    }

    public void setCashInBank(double cashInBank) {
        this.cashInBank = cashInBank;
    }

    public double getTotalRecievable() {
        return totalRecievable;
    }

    public void setTotalRecievable(double totalRecievable) {
        this.totalRecievable = totalRecievable;
    }

    public double getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(double totalPayable) {
        this.totalPayable = totalPayable;
    }

    public List<SalesDashBoardDataDto> getSalesDashBoardDataDtoList() {
        return salesDashBoardDataDtoList;
    }

    public void setSalesDashBoardDataDtoList(List<SalesDashBoardDataDto> salesDashBoardDataDtoList) {
        this.salesDashBoardDataDtoList = salesDashBoardDataDtoList;
    }
}

