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
 * @author admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditCardPayment {
    private double totalCCPAmountRefunded;
    private double totalCCPDiscount;
    private double totalCCPAmountTendered;
    private Date chequeDate;
    private String chequeBankName;
    private String transactionNo;
    private String totalCCPAfterDiscount;
    private String paymentType;
    private List<MultiCardPayment> cardPaymentList;


}
