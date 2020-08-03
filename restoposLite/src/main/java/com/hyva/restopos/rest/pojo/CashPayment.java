/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.pojo;

import lombok.Data;

import java.util.List;

/**
 *
 * @author admin
 */
@Data
public class CashPayment {
    private String paymentType;
    private double totalCPAmountRefunded;
    private double totalCPDiscount;
    private double totalCPAmountTendered;
    private List<MultiCashPayment> multiCashPaymentList;

}
