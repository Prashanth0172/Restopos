/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Admin
 */
@Data
@Entity

public class SalesInvoiceDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    protected Long sIItemId;
    @OneToOne
    protected SalesInvoice sIId;
    protected String sINo;
    @OneToOne
    protected Item itemId;
    protected String itemCode;
    @Column(name = "ItemDesc", columnDefinition = "LONGTEXT")
    protected String itemDesc;
    protected double price;
    protected double qtyOrdered;
    protected double qtySent;
    protected double discPercent;
    protected double itemAmountExcTax;
    protected double itemTaxPer;
    protected double itemTax;
    protected double itemAmountIncTax;
    protected String recStatus;
    protected String hiConnectStatus;
    protected String concateItemStockId;
    protected double fifoAmount;
    private double qtyRemain;
    private double discountAmount;
    private Double discountconfigamount;
    private double negaraOnPayAmt;
    private double actualWeight;
    private double makingCharge;
    private double itemCommisionAmount;
    private Double itemCommisionConfigAmount;
    private double cessPer;
    private double cessTaxAmt;
    private double convertedQuantity;

}
