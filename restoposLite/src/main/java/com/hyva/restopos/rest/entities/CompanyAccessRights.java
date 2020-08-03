package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Data
@Entity
public class CompanyAccessRights implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    @OneToOne
    private Company company;
    //masters
    private boolean country;
    private boolean state;
    private boolean currency;
    private boolean tableZone;
    private boolean tablePos;
    private boolean agent;
    private boolean employee;
    private boolean customer;
    private boolean category;
    private boolean item;
    private boolean paymentMethod;
    private boolean userAccountSetUp;
    private boolean accountType;
    private boolean accountGroup;
    private boolean chartOfAccounts;
    private boolean contact;
    private boolean configuration;
    private boolean shift;
    private boolean paymentVoucher;
    private boolean smsService;


    private boolean restaurantInvReg;
    private boolean monthEndReport;
    private boolean itemSalesReport;
    private boolean shiftReport;
    private boolean cancelledItemReg;
    private boolean freeMealReport;
    private boolean discountReport;
    private boolean tableWiseReport;
    private boolean agentReport;
    private boolean cancelledInv;
    private boolean dayEndReport;
    private boolean onlineInvReport;
    private boolean waiterReport;

    //receipt
    private boolean receiptAddCnct;
    private boolean receiptSelectAcnt;
    private boolean receiptRemoveAcnt;
    private boolean receiptClearAll;
    private boolean receiptSave;
    private boolean receiptPrintlist;

    //expense
    private boolean expenseAddCnct;
    private boolean expenseSelectAcnt;
    private boolean expenseRemoveAcnt;
    private boolean expenseClearAll;
    private boolean expenseSave;
    private boolean expensePrintlist;

    //journalentry
    private boolean jESelectAcnt;
    private boolean jEPrintlist;
    private boolean jERemoveAcnt;
    private boolean jESave;
    private boolean jEDraft;
    private boolean jECancel;

    //Restaurant
    private boolean restDineIn;
    private boolean restTakeaway;
    private boolean restDelivery;
    private boolean restTableReservation;
    private boolean restDigiOrders;
    private boolean restOnlineDelivery;
    private boolean restTable;
    private boolean restWaiter;
    private boolean restItemSearch;
    private boolean restItemAdd;
    private boolean restAgent;
    private boolean restCustomer;
    private boolean restCustomerAdd;
    private boolean restPax;
    private boolean restSave;
    private boolean restSavePrint;
    private boolean restSaveSms;
    private boolean restCustomerBill;
    private boolean restClearall;
    private boolean restSplitbill;
    private boolean restChangeTable;
    private boolean restMergeTable;
    private boolean restPrintList;
    private boolean restDailyReport;
    private boolean restVoucher;
    private boolean restOffers;

    //Tokens
    private boolean kitchenTokens;
    private boolean waiterTokens;

    //Main Headings
    private boolean restDashboard;
    private boolean restaurant;
    private boolean tokens;
    private boolean masters;
    private boolean finance;
    private boolean companyInfo;
    private boolean reports;
    private boolean restaurantReports;
    private boolean receipt;
    private boolean expense;
    private boolean journalEntry;
    private boolean pandlreport;

}
