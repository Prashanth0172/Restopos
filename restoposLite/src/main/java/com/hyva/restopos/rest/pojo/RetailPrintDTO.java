/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.pojo;

import com.hyva.restopos.rest.entities.Company;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Data
public class RetailPrintDTO {

    private String result;
    private String date;
    private String restaurantDate;
    private String time;
    private List<ItemDTO> itemDetils;
    private UserAccountSetUpDTO userAccountSetupData;
    private LocationDto inventoryLocationData;
    private RetailDTO siData;
    private String recieptFooter;
    private String companyLogoPath;
    private String userName;
    private String footer;
    private String hsnCode;
    private String cmpyLocation;
    private String custLocation;
    private String billToGST;
    private double totalIncludingTax;
    private double taxAmt;
    private double cessTaxAmt;
    private double totalExcludingTax;
    private double totalPaid;
    private BigDecimal balance;
    private String soNo;
    private String printType;
    private String taxInvoice;
    private String OtherContactsName;
    private String status;
    private  String exchangeRateValue;
    private Currency currencyObj;
    private String Memo;
    private double hiposServiceChargeAmt;
    private double hiPosServiceCharge;
    private PrinterSelectionDisplayPojo printerSelectionDisplayPojo;
    private Company companyData;
    private String airPayMerchantId;
    private String paymentId;

}