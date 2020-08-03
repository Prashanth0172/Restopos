/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.pojo;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Nataraj t
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherPayment {

    private double totalVPBeforDiscount;
    private double totalVPDiscount;
    private String voucherNo;
    private Date   voucherDate;
    private double totalVoucherAmt;
    private double totalVPAfterAllDeductions;
    private double totalVPAmountTendered;
    private double totalVPAmountRefunded;
    private double cardAmount;
    private String paymentType;
    private List<MultiVoucherPayment> multiVoucherPayments;

}
