package com.hyva.restopos.rest.entities;


//import com.hyva.util.HiNextConstants;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chinmay.b on 3/20/2017.
 */
@Data
@Entity
public class PosPaymentTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long paymenetId;
    @Column(length = 5000)
    private String voucherPayment;
    @Column(length = 5000)
    private String cardPayment;
    private double totalCashPayment;
    private double totalVoucherPayment;
    private double totalCardPayment;
    private double totalAmount;
    private String duplicatePrint;
    @OneToOne
    private SalesInvoice salesInvoice;
    private int count;
    private double totalTaxAmount;
    private double roundingAdjustment;
    @OneToOne
    private Customer customer;
    private String locationId;
    private String useraccount_id;
    private transient String company_no;
    private transient String registerNo;
    private transient String typePrefix;
    private transient String nextref;
    private String fileName;
    @Column(length = 5000)
    private String bankPayment;
    @Column(length = 5000)
    private String cashPayment;
    private double totalBankAmt;
    private double amountReturn;
    @Temporal(TemporalType.DATE)
    private Date transactionDate;
    private String custPaymentNo;
    private String onlineInvoiceId;
    private String airPaydetailsId;
    @Column(length = 5000)
    private String onlinePayment;
    private double olPayAmount;


    public String getCustPaymentNo() {
        return custPaymentNo;
    }

    public void setCustPaymentNo(String custPaymentNo) {
        this.custPaymentNo = custPaymentNo;
    }

    public String getCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(String cashPayment) {
        this.cashPayment = cashPayment;
    }

    public double getAmountReturn() {
        return amountReturn;
    }

    public void setAmountReturn(double amountReturn) {
        this.amountReturn = amountReturn;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getBankPayment() {
        return bankPayment;
    }

    public void setBankPayment(String bankPayment) {
        this.bankPayment = bankPayment;
    }

    public double getTotalBankAmt() {
        return totalBankAmt;
    }

    public void setTotalBankAmt(double totalBankAmt) {
        this.totalBankAmt = totalBankAmt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getUseraccount_id() {
        return useraccount_id;
    }

    public void setUseraccount_id(String useraccount_id) {
        this.useraccount_id = useraccount_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public double getRoundingAdjustment() {
        return roundingAdjustment;
    }

    public void setRoundingAdjustment(double roundingAdjustment) {
        this.roundingAdjustment = roundingAdjustment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SalesInvoice getSalesInvoice() {
        return salesInvoice;
    }

    public void setSalesInvoice(SalesInvoice salesInvoice) {
        this.salesInvoice = salesInvoice;
    }

    public Long getPaymenetId() {
        return paymenetId;
    }

    public void setPaymenetId(Long paymenetId) {
        this.paymenetId = paymenetId;
    }

    public String getVoucherPayment() {
        return voucherPayment;
    }

    public void setVoucherPayment(String voucherPayment) {
        this.voucherPayment = voucherPayment;
    }

    public String getCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(String cardPayment) {
        this.cardPayment = cardPayment;
    }

    public double getTotalCashPayment() {
        return totalCashPayment;
    }

    public void setTotalCashPayment(double totalCashPayment) {
        this.totalCashPayment = totalCashPayment;
    }

    public double getTotalVoucherPayment() {
        return totalVoucherPayment;
    }

    public void setTotalVoucherPayment(double totalVoucherPayment) {
        this.totalVoucherPayment = totalVoucherPayment;
    }

    public double getTotalCardPayment() {
        return totalCardPayment;
    }

    public void setTotalCardPayment(double totalCardPayment) {
        this.totalCardPayment = totalCardPayment;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDuplicatePrint() {
        return duplicatePrint;
    }

    public void setDuplicatePrint(String duplicatePrint) {
        this.duplicatePrint = duplicatePrint;
    }

    public String getCompany_no() {
        return company_no;
    }

    public void setCompany_no(String company_no) {
        this.company_no = company_no;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getTypePrefix() {
        return typePrefix;
    }

    public void setTypePrefix(String typePrefix) {
        this.typePrefix = typePrefix;
    }

    public String getNextref() {
        return nextref;
    }

    public void setNextref(String nextref) {
        this.nextref = nextref;
    }

    public String getOnlineInvoiceId() {
        return onlineInvoiceId;
    }

    public void setOnlineInvoiceId(String onlineInvoiceId) {
        this.onlineInvoiceId = onlineInvoiceId;
    }

    public String getAirPaydetailsId() {
        return airPaydetailsId;
    }

    public void setAirPaydetailsId(String airPaydetailsId) {
        this.airPaydetailsId = airPaydetailsId;
    }

    public String getOnlinePayment() {
        return onlinePayment;
    }

    public void setOnlinePayment(String onlinePayment) {
        this.onlinePayment = onlinePayment;
    }

    public double getOlPayAmount() {
        return olPayAmount;
    }

    public void setOlPayAmount(double olPayAmount) {
        this.olPayAmount = olPayAmount;
    }
}
