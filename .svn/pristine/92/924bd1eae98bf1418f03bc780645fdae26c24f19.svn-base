/*
 * To change this template; choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "salesinvoice", uniqueConstraints = @UniqueConstraint(columnNames = {"SINo","serialNumber"}))
@Data
public class SalesInvoice implements Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    Employee userId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long sIId;
    private String serialNumber;
    private String sINo;
    @Temporal(TemporalType.DATE)
    private Date sIDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateWithTime;
    @OneToOne
    private Customer customerId;
    private String custPONo;
    private String referenceno;
    private String ShipTo;
    @Column(name = "memo", columnDefinition = "LONGTEXT")
    private String Memo;
    private String flag;
    @Column(length = 5000)
    private String termsDesc;
    private boolean gstflag;
    @OneToOne(fetch = FetchType.LAZY)
    private CompanyInfo companyId;
    private double freightCharge;
    private double totalAmount;
    private double totalamountsalesretun;
    private double totalReceived;
    private double salesTotalTaxAmt;
    private double totalReceivable;
    private String sIStatus;
    private BigDecimal ARBalance;
    private BigDecimal totCommissionAmount;
    private double advancereceived;
    private double advancetaxamount;
    @OneToOne(fetch = FetchType.LAZY)
    private Currency currencyId;
    @OneToOne(fetch = FetchType.LAZY)
    private Agent agentId;
    private String showReport;
    private double exchangeRateValue;
    private String posting;
    private String badDeptRelief;
    private double totalDiscountAmount;
    private String taxInvoice;
    private double advancetotalAmount;
    @OneToOne
    private UserAccountSetup useraccount_id;
    private String hiConnectStatus;
    private String companyLocation;
    private String customerLocation;
    private String shippingReferenceNo;
    @Temporal(TemporalType.DATE)
    private Date shippingDate;
    @Column(columnDefinition = "TINYINT(1)", length = 1)
    @ColumnDefault(value = "0")
    private int alertStatus;
    private String otherDetailsId;
    private String tcsAmount;
    private String tdsAmount;
    private String advancePayment;
    private String invoiceType;
    private String otherContactId;
    private double serviceChargeAmt;
    private double serviceChargePer;
    private String discountCode;
    private String discountType;
    private double discountConfig;

    private double actualWeightTotalAmt;
    private double sellablrWeightTotalAmt;
    private double differenceInWeight;
    private double cessTaxAmt;



}
