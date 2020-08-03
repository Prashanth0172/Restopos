package com.hyva.restopos.rest.Hiposservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.pojo.*;
import com.hyva.restopos.rest.repository.*;
import org.hibernate.HibernateException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.valueOf;

@Component
public class GTService {

    @Autowired
    AccountMasterRepository accountMasterRepository;
    @Autowired
    OtherReceiptRepository otherReceiptRepository;
    @Autowired
    OcOtherReceiptWOItemRep ocOtherReceiptWOItemRep;
    @Autowired
    OtherPaymentRepository otherPaymentRepository;
    @Autowired
    OcOtherPaymentWOItemRep ocOtherPaymentWOItemRep;
    @Autowired
    GLTransactionRepository glTransactionRepository;
    @Autowired
    PosReceiptPayRepository posReceiptPayRepository;
    @Autowired
    PosExpensePayRepository posExpensePayRepository;
    @Autowired
    ChequeReportDetailsRepository chequeReportDetailsRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    FormSetupRepository formSetupRepository;
    @Autowired
    JournalEntryRepository journalEntryRepository;
    @Autowired
    JournalEntryDetailsRepository journalEntryDetailsRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Transactional
    public OtherRecieptGtDTO createOtherRecieptNPayment(OtherRecieptGtDTO orgtDTO) throws IOException, JSONException,ParseException {
        OtherRecieptGtDTO printDTO = new OtherRecieptGtDTO();
            OtherReceipt otherReceipt = createPOSSI(orgtDTO);
            List<OcOtherReceiptWithoutItem> sidList = createPOSSIDetails(orgtDTO, otherReceipt);
            persistOtherReceipt(otherReceipt, sidList);
            PosReceiptPaymentTypes posReceiptPaymentTypes = createPosReceiptPayment(otherReceipt, orgtDTO);
            writingGtforSalesLedger(otherReceipt, sidList, orgtDTO,posReceiptPaymentTypes);
            printDTO = getPrintDetailsOtherRecipt(orgtDTO, otherReceipt, sidList);
        return printDTO;
    }
    public FormSetUp getFooter(String formSetupType) {
        FormSetUp formSetUp = new FormSetUp();
        try {
            formSetUp = formSetupRepository.findAllByTypename(formSetupType);
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return formSetUp;
    }
    public PosReceiptPaymentTypes createPosReceiptPayment(OtherReceipt otherReceipt, OtherRecieptGtDTO opgtDTO) {
        PosReceiptPaymentTypes posReceiptPaymentTypes = new PosReceiptPaymentTypes();
        try{
            List<MultiVoucherPayment> multiVoucherPaymentsList = new ArrayList<>();
            List<MultiCardPayment> multiCardPaymentsList = new ArrayList<>();
            List<MultiBankPayment> multiBankPayments = new ArrayList<>();
            double totalVoucherAmount = 0.00;
            double totalCreditCardAmount = 0.00;
            double totalbankAmount = 0.00;

            if (opgtDTO.getVoucherPayment().getMultiVoucherPayments().size() != 0) {
                for (MultiVoucherPayment dto : opgtDTO.getVoucherPayment().getMultiVoucherPayments()) {
                    MultiVoucherPayment m = new MultiVoucherPayment();
                    m.setVoucherNo(dto.getVoucherNo());
                    m.setVoucherAmt(dto.getVoucherAmt());
                    totalVoucherAmount += dto.getVoucherAmt();
                    multiVoucherPaymentsList.add(m);
                }
            }
            if (opgtDTO.getBankPayment().getMultiBankPaymentList().size() != 0) {
                for (MultiBankPayment dto : opgtDTO.getBankPayment().getMultiBankPaymentList()) {
                    MultiBankPayment b = new MultiBankPayment();
                    b.setBankAccount(dto.getBankAccount());
                    b.setAmount(dto.getAmount());
                    b.setInvoiceNo(dto.getInvoiceNo());
                    b.setBankAccountId(dto.getBankAccountId());
                    b.setTransactionNo(dto.getTransactionNo());
                    Date date = new Date();
                    if (dto.getDate() == null) {
                        b.setDate(date);
                        dto.setDate(new Date());
                    } else {
                        b.setDate(dto.getDate());
                    }
                    b.setBankName(dto.getBankName());
                    b.setPaymentType(dto.getPaymentType());
                    totalbankAmount += dto.getAmount();
                    multiBankPayments.add(b);
                    if (StringUtils.pathEquals(dto.getPaymentType(), "Cheque")) {
                        ChequeReportDetails chequeReportDetails = new ChequeReportDetails();
                        chequeReportDetails.setChequeDate(dto.getDate());
                        if (dto.getDate().after(new Date())) {
                            chequeReportDetails.setChequeStatus("Not Deposited");
                        }
                        else {
                            chequeReportDetails.setChequeStatus("Deposited");
                        }
//                    chequeReportDetails.setSupplierid(otherPayment.getOtherContactID());
                        chequeReportDetails.setChequeNo(dto.getInvoiceNo());
                        chequeReportDetails.setChequeAmount(dto.getAmount());
                        chequeReportDetails.setInvoiceNumber(otherReceipt.getOrno());
                        chequeReportDetails.setStatus("Active");
                        chequeReportDetailsRepository.save(chequeReportDetails);
                    }
                }
            }
            if (opgtDTO.getCreditPayment().getCardPaymentList().size() != 0) {
                for (MultiCardPayment cardPayment : opgtDTO.getCreditPayment().getCardPaymentList()) {
                    MultiCardPayment multiCardPayment = new MultiCardPayment();
                    multiCardPayment.setCardAmt(cardPayment.getCardAmt());
                    multiCardPayment.setCardNo(cardPayment.getCardNo());
                    multiCardPayment.setCardBankName(cardPayment.getCardBankName());
                    if(cardPayment.getCardDate()!=null) {
                        multiCardPayment.setCardDate(cardPayment.getCardDate());
                    }
                    else {
                        multiCardPayment.setCardDate(new Date().toString());
                    }
                    totalCreditCardAmount += cardPayment.getCardAmt();
                    multiCardPaymentsList.add(multiCardPayment);
                }
            }
            posReceiptPaymentTypes.setOtherReceipt(otherReceipt);
            posReceiptPaymentTypes.setTotalVoucherPayment(totalVoucherAmount);
            posReceiptPaymentTypes.setTotalCardPayment(totalCreditCardAmount);
            posReceiptPaymentTypes.setTotalBankAmt(totalbankAmount);
            String json1 = new Gson().toJson(multiVoucherPaymentsList);
            String jsonBank = new Gson().toJson(multiBankPayments);
            String jsoncardpayment = new Gson().toJson(multiCardPaymentsList);
            posReceiptPaymentTypes.setVoucherPayment(json1);
            posReceiptPaymentTypes.setCardPayment(jsoncardpayment);
            posReceiptPaymentTypes.setBankPayment(jsonBank);
            if (opgtDTO.getCashPayment().getMultiCashPaymentList().size() != 0) {
                posReceiptPaymentTypes.setTotalCashPayment(opgtDTO.getCashPayment().getMultiCashPaymentList().get(0).getCashAmt());
            }
//            harsh commit
//            posReceiptPaymentTypes.setContact(hiposDAO.getOtherContactsObject(Long.parseLong(otherReceipt.getOtherContactID())));
            posReceiptPaymentTypes.setTotalAmount(opgtDTO.getDiscountAmount());
            posReceiptPaymentTypes.setCount(opgtDTO.getItemCount());
            posReceiptPayRepository.save(posReceiptPaymentTypes);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return posReceiptPaymentTypes;
    }
    public OtherRecieptGtDTO getPrintDetailsOtherRecipt(OtherRecieptGtDTO orgtDTO,OtherReceipt otherReceipt,List<OcOtherReceiptWithoutItem> sidList)throws ParseException{
        SimpleDateFormat sfmtDate = new SimpleDateFormat("dd/MM/yyyy");
        Company company=companyRepository.findAllByStatus("Active");
        orgtDTO.setCompany(company);
        orgtDTO.setTaxInvoice(otherReceipt.getTaxInvoice());
        BigDecimal deci= new BigDecimal(String.valueOf(otherReceipt.getTotalTaxAmount()));
        orgtDTO.setTotalTaxAmt(deci.doubleValue());
        orgtDTO.setDateOfInvoice(sfmtDate.format(otherReceipt.getOrDate()));
        orgtDTO.setOrNo(otherReceipt.getOrno());
        orgtDTO.setAmountPaid(otherReceipt.getAmountPaid());
        BigDecimal decibalance= new BigDecimal(String.valueOf(otherReceipt.getBalance()));
        orgtDTO.setBalanceAmount(decibalance.doubleValue());
//        for(AccountMasterDTO selectedAccount1:orgtDTO.getSelectedAccountList()){
//            Tax tax=hiposDAO.getTax(null,null,selectedAccount1.getTaxid());
//            selectedAccount1.setTaxRate(tax.getTaxCode());
//        }
        if(orgtDTO.getCreditPayment().getCardPaymentList().size()>0){
            orgtDTO.setPaymentType("Card");
        }
        if(orgtDTO.getBankPayment().getMultiBankPaymentList().size()>0){
            orgtDTO.setPaymentType("Bank");
        }
        if(orgtDTO.getVoucherPayment().getMultiVoucherPayments().size()>0){
            orgtDTO.setPaymentType("Voucher");
        }
        if(orgtDTO.getCashPayment().getMultiCashPaymentList().size()>0){
            orgtDTO.setPaymentType("Cash");
        }
        return orgtDTO;
    }

    public void writingGtforSalesLedger( OtherReceipt otherReceipt ,
                                         List<OcOtherReceiptWithoutItem> sidList,  OtherRecieptGtDTO orgtDTO,
                                         PosReceiptPaymentTypes posReceiptPaymentTypes) {
        double totalPaymentBankAmt=0;
        double totalPaymentCashAmt=0;
        double totalPaymentVoucherAmt=0;
        Map map = new HashMap();
        FormSetUp formSetUp =null;
        if(Long.valueOf(orgtDTO.getCustomerId())!=null) {
            getFooter("OtherCustReceipt");
        }
        else{
            getFooter("OtherCustReceipt");

        }
        AccountMaster accountMaster=accountMasterRepository.findByAccountname("Petty Cash");
//        AccountSetup accountSetup = hiposDAO.getAccountSetup(userId);
        if(orgtDTO.getCashPayment().getMultiCashPaymentList()!=null) {
            if (orgtDTO.getCashPayment().getMultiCashPaymentList().size() > 0) {
                BigDecimal cardVal = new BigDecimal(posReceiptPaymentTypes.getTotalCashPayment());
                createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(), formSetUp, otherReceipt.getOrno(), accountMaster, "cashPayment", cardVal, "", orgtDTO.getCutomerName(), orgtDTO.getCurrencyId(), orgtDTO.getExchangerateValue());
                totalPaymentCashAmt = otherReceipt.getAmountPaid().doubleValue();
                map.put("42/200", totalPaymentCashAmt);//Cash Ledger
            }
        }
        if(orgtDTO.getBankPayment().getMultiBankPaymentList().size()>0){
            for (MultiBankPayment bankPayment : orgtDTO.getBankPayment().getMultiBankPaymentList()) {
//                if (!bankPayment.getDate().after(new Date())) {
//                AccountMaster bankPaymentACC = accountMasterRepository.findOne(bankPayment.getBankAccountId());
                BigDecimal bankVal = new BigDecimal(bankPayment.getAmount());
                createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(), accountMaster,"bankPayment", bankVal,"",orgtDTO.getCutomerName(),orgtDTO.getCurrencyId(),orgtDTO.getExchangerateValue());
                totalPaymentBankAmt=otherReceipt.getAmountPaid().doubleValue();
                map.put("42/100", totalPaymentBankAmt);//Bank Ledger
//                }
            }
        }
        if(orgtDTO.getCreditPayment().getCardPaymentList().size()>0){
            BigDecimal cardVal = new BigDecimal(posReceiptPaymentTypes.getTotalCardPayment());
            createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(), accountMaster, "bankPayment", cardVal,"",orgtDTO.getCutomerName(),orgtDTO.getCurrencyId(),orgtDTO.getExchangerateValue());
            totalPaymentBankAmt=otherReceipt.getAmountPaid().doubleValue();
            map.put("42/100", totalPaymentBankAmt);//Bank Ledger
        }
        if(orgtDTO.getVoucherPayment().getMultiVoucherPayments().size()>0){
            BigDecimal vouVal = new BigDecimal(posReceiptPaymentTypes.getTotalVoucherPayment());
            AccountMaster accountMaster1 = accountMasterRepository.findByAccountname("Discounts Given");
            createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(), accountMaster1, "bankPayment", vouVal,"",orgtDTO.getCutomerName(),orgtDTO.getCurrencyId(),orgtDTO.getExchangerateValue());
            totalPaymentVoucherAmt=otherReceipt.getAmountPaid().doubleValue();
            map.put("84", totalPaymentVoucherAmt);//Voucher Ledger
        }
        for (OcOtherReceiptWithoutItem sd : sidList) {
            createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(),  sd.getAccountid(), "bankPayment", (sd.getAmountIncludeTax().subtract(sd.getTaxAmount())).negate(),"",orgtDTO.getCutomerName(),orgtDTO.getCurrencyId(),orgtDTO.getExchangerateValue());
//            Tax tax=hiposDAO.getTax(companyName,  companyInfo,  sd.getTaxId().getTaxId());
//            if(tax.getLinkedId()!=null){
//                createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrNo(), sd.getTaxId().getLinkedId(), HiposConstants.PAYMENTTYPE_BANK, sd.getTaxAmount().negate(), "",orgtDTO.getCutomerName(),orgtDTO.getCurrencyId(),orgtDTO.getExchangerateValue());
//            }
            String[] str =  sd.getAccountid().getStringAccountCode().split("/");
            String fourDigit = str[0];
            String threeDigit = str[1];
            map.put(fourDigit, (sd.getAmountIncludeTax().subtract((sd.getTaxAmount()))).negate());
            map.put("46", sd.getTaxAmount());//Tax Ledger Amt
//            accountGroupService.upDateTotalBalanceAccountGroup(map);
        }
    }
    public void createGeneralLedgerForGTPurchase(Date dt,FormSetUp Tran_type,
                                                 String Tran_no, AccountMaster account,
                                                 String memo, BigDecimal amountWithoutExchangeRate, String PLRNo,String flag,String currencyId,String currencyValue) {
        String currency;
        BigDecimal amount;
        GLTransactions gl = new GLTransactions();
        gl.setTran_type(Tran_type);
        gl.setTran_no(Tran_no);
        gl.setTran_date(dt);
        gl.setAccount(account);
        gl.setAcc_code(account != null ? account.getAccountcode() : null);
        gl.setMemo(memo);
        gl.setCurrencyValue(currencyValue);
        gl.setSuppInNo(PLRNo);
        gl.setAmount(amountWithoutExchangeRate);
        gl.setFlagForGlTrans(flag);
        glTransactionRepository.save(gl);
//        hiposLedgerDAO.createGTofRespectiveTable( dt,Tran_type, Tran_no,account, memo,amount, PLRNo,flag,fiscalYear,this.userAccountSetup,currency,currencyValue);
    }
    private OtherReceipt createPOSSI(OtherRecieptGtDTO orgtDTO) {
        OtherReceipt orgt = new OtherReceipt();
        if(orgtDTO.getOrNo()!=null){
            orgt=otherReceiptRepository.findByOrno(orgtDTO.getOrNo());
            List<OcOtherReceiptWithoutItem> list=ocOtherReceiptWOItemRep.findAllByOcdetails(orgt);
            for(OcOtherReceiptWithoutItem ocOtherReceiptWithoutItem:list){
                ocOtherReceiptWOItemRep.delete(ocOtherReceiptWithoutItem);
            }
            List<PosReceiptPaymentTypes> list2=posReceiptPayRepository.findAllByOtherReceipt(orgt);
            for(PosReceiptPaymentTypes posReceiptPaymentTypes:list2){
                posReceiptPayRepository.delete(posReceiptPaymentTypes);
            }
        }
        else {
            FormSetUp formSetUp = getFormSetUp("OtherReceipt");
            orgt.setOrno(getNextRefInvoice(formSetUp.getTypeprefix(), formSetUp.getNextref()));
        }

//        AccountMaster accSetup = accountMasterRepository.findOne(orgtDTO.get);
        orgt.setStatus("WithoutItem");
        orgt.setOrDate(parseDate(orgtDTO.getDateOfInvoice()));
        orgt.setPosting("Yes");
        if(!StringUtils.isEmpty(orgtDTO.getCutomerName())) {
            orgt.setSupplierName(orgtDTO.getCutomerName());
        }else {
            orgt.setSupplierName(orgtDTO.getOtherCustName());
            orgtDTO.setCutomerName(orgtDTO.getOtherCustName());
        }
        orgt.setOtherContactID(String.valueOf(orgtDTO.getCustomerId()));
        orgt.setCustomerID(String.valueOf(orgtDTO.getOtherCustId()));
        orgt.setTaxInvoice(orgtDTO.getTaxType());
        orgt.setTaxInvoice(orgtDTO.getTaxType());
        orgt.setTotalTaxAmount(BigDecimal.valueOf(orgtDTO.getTotalTaxAmt()));
        double balanceAmt =  orgtDTO.getTotalCheckOutamt() - orgtDTO.getTotalTenderedAmount();
        orgt.setBalance(BigDecimal.valueOf(balanceAmt));

        if(orgtDTO.getCashPayment()!=null){
            double amtPaid = orgtDTO.getTotalCheckOutamt() - orgtDTO.getCashPayment().getTotalCPDiscount();
//            orgt.setAccountDescription(ac.getAccount_name());
            orgt.setAmountPaid(BigDecimal.valueOf(amtPaid));
//            orgt.setAccountid(accSetup.getPosCashAccount());
//            orgt.setAccountNo(accSetup.getPosCashAccount().getAccount_name());
        }

        if(orgtDTO.getCreditPayment()!=null){
            double   amtPaid = orgtDTO.getTotalCheckOutamt() - orgtDTO.getCreditPayment().getTotalCCPDiscount();
//            orgt.setAccountDescription(accSetup.getPosCashAccount().getAccount_name());
            orgt.setAmountPaid(BigDecimal.valueOf(amtPaid));
//            orgt.setAccountid(accSetup.getBankCharges());
//            orgt.setAccountNo(accSetup.getPosCashAccount().getAccount_name());
        }

        switch (orgtDTO.getPaymentType()) {
            case "cashPayment":
                double amtPaid = orgtDTO.getTotalCheckOutamt() - orgtDTO.getCashPayment().getTotalCPDiscount();
//                orgt.setAccountDescription(accSetup.getPosCashAccount().getAccount_name());
                orgt.setAmountPaid(BigDecimal.valueOf(amtPaid));
//                orgt.setAccountid(accSetup.getPosCashAccount());
//                orgt.setAccountNo(accSetup.getPosCashAccount().getAccount_name());
                break;
            case "creditPayment":
                amtPaid = orgtDTO.getTotalCheckOutamt() - orgtDTO.getCreditPayment().getTotalCCPDiscount();
//                orgt.setAccountDescription(accSetup.getPosCashAccount().getAccount_name());
                orgt.setAmountPaid(BigDecimal.valueOf(amtPaid));
//                orgt.setAccountid(accSetup.getBankCharges());
//                orgt.setAccountNo(accSetup.getPosCashAccount().getAccount_name());
                break;
            case "voucherPayment":
                amtPaid = orgtDTO.getVoucherPayment().getTotalVPBeforDiscount() - orgtDTO.getVoucherPayment().getTotalVPDiscount();
                amtPaid = orgtDTO.getTotalCheckOutamt() - orgtDTO.getCreditPayment().getTotalCCPDiscount();
//                orgt.setAccountDescription(accSetup.getPosCashAccount().getAccount_name());
                orgt.setAmountPaid(BigDecimal.valueOf(amtPaid));
//                orgt.setAccountid(accSetup.getBankCharges());
//                orgt.setAccountNo(accSetup.getPosCashAccount().getAccount_name());
                break;
        }
        return orgt;
    }
    public static Date parseDate(String strDate){
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        f.setTimeZone(utc);
        GregorianCalendar cal = new GregorianCalendar(utc);
        try {
            cal.setTime(f.parse(strDate));
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return cal.getTime();

    }
    public static String getNextRefInvoice(String prefix, String nextRef) {
        StringBuilder sb = new StringBuilder();
        return sb.append(prefix).append(nextRef).toString();

    }
    @Transactional
    public  void persistOtherReceipt(OtherReceipt otherReceipt, List<OcOtherReceiptWithoutItem> sidList) {
        try {
//            harsha commit
                Long id = Long.parseLong(otherReceipt.getOtherContactID());
                OtherContacts otherContacts=contactRepository.findOne(id);
                otherReceipt.setSupplierName(otherContacts.getFullName());
                for (OcOtherReceiptWithoutItem invoiceDetails : sidList) {
                    invoiceDetails.setOrName(otherContacts.getOtherContactId().toString());
                    ocOtherReceiptWOItemRep.save(invoiceDetails);
                }
            double amount=0;
            double tax=0;
            DecimalFormat df = new DecimalFormat("#.00");
            for (OcOtherReceiptWithoutItem calamount : sidList) {
                amount=calamount.getAmount().doubleValue()+amount;
                tax=calamount.getTaxAmount().doubleValue()+tax;
            }
            otherReceipt.setAmount2(Double.parseDouble(df.format(amount)));
            otherReceipt.setTaxAmount(Double.parseDouble(df.format(tax)));
            otherReceiptRepository.save(otherReceipt);

        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }
    private List<OcOtherReceiptWithoutItem> createPOSSIDetails(OtherRecieptGtDTO orgtDTO, OtherReceipt otherReceipt) {
        List<OcOtherReceiptWithoutItem> sidList = new ArrayList<>();
        for (AccountMasterDTO selectedAccount : orgtDTO.getSelectedAccountList()) {
            OcOtherReceiptWithoutItem sid = new OcOtherReceiptWithoutItem();
            // TODO
            sid.setOrName(orgtDTO.getCutomerName());
            sid.setOtherContactID((String.valueOf(orgtDTO.getCustomerId())));
            sid.setInvoiceNumber(selectedAccount.getInvoiceNumber());
            sid.setOcdetails(otherReceipt);
            sid.setTaxAmount(BigDecimal.valueOf(selectedAccount.getTaxamt()));
            AccountMaster accountMaster = accountMasterRepository.findOne(selectedAccount.getAccountid());
            sid.setAccountid(accountMaster);
            sid.setAmount(BigDecimal.valueOf(selectedAccount.getAmtexclusivetax()));
            sid.setAmountIncludeTax(BigDecimal.valueOf(selectedAccount.getAmtinclusivetax()));
            sid.setDescription(selectedAccount.getAccountDescription());
//            Tax tax=hiposDAO.getTax(companyName, companyInfo, selectedAccount.getTaxid());
//            sid.setTaxId(tax);
            sidList.add(sid);

        }
        return sidList;
    }
    private List<OcOtherPaymentWithoutItem> createPOSSIDetails(OtherPurchaseGtDTO opgtDTO, OtherPayment otherPayment) {
        List<OcOtherPaymentWithoutItem> pidList = new ArrayList<>();
        for (AccountMasterDTO selectedAccount : opgtDTO.getSelectedAccountList()) {
            OcOtherPaymentWithoutItem pid = new OcOtherPaymentWithoutItem();
            // TODO
            pid.setOcdetails(otherPayment);
            pid.setGlName(opgtDTO.getSupplierName());
            pid.setTaxAmount(BigDecimal.valueOf(selectedAccount.getTaxamt()));
            AccountMaster accountMaster = accountMasterRepository.findOne(selectedAccount.getAccountid());
            pid.setAccountid(accountMaster);
            pid.setAmount(BigDecimal.valueOf(selectedAccount.getAmtexclusivetax()));
            pid.setAmountIncludeTax(BigDecimal.valueOf(selectedAccount.getAmtinclusivetax()));
            pid.setInvoiceNumber(selectedAccount.getInvoiceNumber());
            pid.setDescription(selectedAccount.getAccountDescription());
//            Tax tax=hiposDAO.getTax(selectedAccount.getTaxid());
//            pid.setTaxId(tax);
            pidList.add(pid);
        }
        return pidList;
    }
    public List<PosInvoiceDTO> getOtherReceiptDuplicateList(String searchText) {
        List<OtherReceipt> otherReceipt=new ArrayList<>();
        List<String> statusList=new ArrayList<>();
        statusList.add("Cancelled Invoice");
        statusList.add("Draft Cancelled Invoice");
        if (org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
            otherReceipt = otherReceiptRepository.findAllByStatusNotInAndOrnoContaining(statusList,searchText);
        } else {
            otherReceipt = otherReceiptRepository.findAllByStatusNotIn(statusList);
        }
      List<PosInvoiceDTO> posInvDTO = GTMapper.getOtherReceiptEntityToPojo(otherReceipt);
      return posInvDTO;
    }
    public OtherRecieptGtDTO duplicateReceiptPrint(String paymentId){
        OtherRecieptGtDTO opgtDTO = new OtherRecieptGtDTO();
        SimpleDateFormat sfmtDate = new SimpleDateFormat("dd/MM/yyyy");
        OtherReceipt otherReceipt =otherReceiptRepository.findByOrno(paymentId);
        List<OcOtherReceiptWithoutItem> ocOtherReceiptWithoutItem=ocOtherReceiptWOItemRep.findByOcdetails(otherReceipt);
        List<AccountMasterDTO> accountList = GTMapper.getOcReceiptDetailsEntityToPojo(ocOtherReceiptWithoutItem);
        ArrayList<AccountMasterDTO> list = new ArrayList<>(accountList);
        opgtDTO.setSelectedAccountList(list);
        opgtDTO.setTaxInvoice(otherReceipt.getTaxInvoice());
        BigDecimal deci= new BigDecimal(String.valueOf(otherReceipt.getTotalTaxAmount()));
        opgtDTO.setTotalTaxAmt(deci.doubleValue());
        if(otherReceipt.getOtherContactID()!=null&&!StringUtils.pathEquals(otherReceipt.getOtherContactID(),"0")) {
            OtherContacts contacts =contactRepository.findOne(Long.parseLong(otherReceipt.getOtherContactID()));
            opgtDTO.setCutomerName(contacts.getFullName());
        }
        Company company=companyRepository.findAllByStatus("Active");
        opgtDTO.setCompany(company);
        opgtDTO.setDateOfInvoice(sfmtDate.format(otherReceipt.getOrDate()));
        opgtDTO.setOrNo(otherReceipt.getOrno());
        opgtDTO.setAmountPaid(otherReceipt.getAmountPaid());
        opgtDTO.setBalanceAmount(otherReceipt.getBalance().doubleValue());
        //pos print data
        PosReceiptPaymentTypes payments=posReceiptPayRepository.findByOtherReceipt(otherReceipt);
        VoucherPayment v=new VoucherPayment();
        CreditCardPayment  cp=new CreditCardPayment();
        Gson json1 = new Gson();
        BankPayment bp=new BankPayment();
        Type bankType = new TypeToken<ArrayList<MultiBankPayment>>() {}.getType();
        Type type1 = new TypeToken<ArrayList<MultiCardPayment>>() {}.getType();
        Type type = new TypeToken<ArrayList<MultiVoucherPayment>>() {}.getType();
        Type cashType = new TypeToken<ArrayList<MultiCashPayment>>() {}.getType();
        List<MultiVoucherPayment> multiVoucherPayments=new ArrayList<>();
        if(!StringUtils.isEmpty(payments.getVoucherPayment())) {
            multiVoucherPayments = json1.fromJson(payments.getVoucherPayment(), type);
            v.setMultiVoucherPayments(multiVoucherPayments);
            opgtDTO.setVoucherPayment(v);
        }
        List<MultiCardPayment> multiCardPayments=new ArrayList<>();
        if(!StringUtils.isEmpty(payments.getCardPayment())) {
            multiCardPayments = json1.fromJson(payments.getCardPayment(), type1);
            cp.setCardPaymentList(multiCardPayments);
            opgtDTO.setCreditPayment(cp);
        }
        if(!StringUtils.isEmpty(payments.getBankPayment())) {
            List<MultiBankPayment> multiBankPayment = json1.fromJson(payments.getBankPayment(), bankType);
            bp.setMultiBankPaymentList(multiBankPayment);
            opgtDTO.setBankPayment(bp);
        }
        List<MultiCashPayment>multiCashPayments = new ArrayList<>();
        CashPayment cashPayment = new CashPayment();
        if(!StringUtils.isEmpty(payments.getTotalCashPayment())) {
            cashPayment.setTotalCPAmountTendered(payments.getTotalCashPayment());
            opgtDTO.setCashPayment(cashPayment);
        }
        return opgtDTO;
    }
    @Transactional
    public OtherPurchaseGtDTO createOtherPurchaseNPayment(@RequestBody OtherPurchaseGtDTO opgtDTO) throws IOException, JSONException, ParseException {
        OtherPurchaseGtDTO printDTO = new OtherPurchaseGtDTO();
        OtherPayment otherPayment = createPOSSI(opgtDTO);
            List<OcOtherPaymentWithoutItem> pidList = createPOSSIDetails(opgtDTO, otherPayment);
            persistPOSSI( otherPayment, pidList);
            PosExpensePaymentTypes posExpensePaymentTypes = createPosExpensePayment(otherPayment,opgtDTO);
            writingGtforPurchaseLedger(otherPayment, pidList, opgtDTO,posExpensePaymentTypes);
            printDTO = getPrintDetailsOtherPurchase(opgtDTO,otherPayment,pidList);
            return printDTO;
    }
    private OtherPayment createPOSSI(OtherPurchaseGtDTO opgtDTO){
        OtherPayment opgt = new OtherPayment();
        if(opgtDTO.getOpNo()!=null){
            opgt=otherPaymentRepository.findByOpNo(opgtDTO.getOpNo());
            List<OcOtherPaymentWithoutItem> list=ocOtherPaymentWOItemRep.findByOcdetails(opgt);
            for(OcOtherPaymentWithoutItem ocOtherPaymentWithoutItem:list){
                ocOtherPaymentWOItemRep.delete(ocOtherPaymentWithoutItem);
            }
            List<PosExpensePaymentTypes> list2=posExpensePayRepository.findAllByOtherPayment(opgt);
            for(PosExpensePaymentTypes posExpensePaymentTypes:list2){
                posExpensePayRepository.delete(posExpensePaymentTypes);
            }

        }
        else {
            FormSetUp formSetUp = getFormSetUp("OtherPurchase");
            opgt.setOpNo(getNextRefInvoice(formSetUp.getTypeprefix(), formSetUp.getNextref()));
        }
//        AccountSetup accSetup = hiposDAO.getAccountSetup(userId);
        opgt.setOpDate(parseDate(opgtDTO.getDateOfInvoice()));
        opgt.setStatus("WithoutItem");
        opgt.setOpDate(parseDate(opgtDTO.getDateOfInvoice()));
        opgt.setPosting("Yes");
        opgt.setSupplierName(opgtDTO.getSupplierName());
        opgt.setGstApprovalNumber(opgtDTO.getGstApprovalNumber());
        opgt.setOtherContactID(String.valueOf(opgtDTO.getSupplierId()));
        opgt.setReferNo(opgtDTO.getSupplierInvNo());
        opgt.setTaxInvoice(opgtDTO.getTaxType());
        opgt.setTotalTaxAmount(BigDecimal.valueOf(opgtDTO.getTotalTaxAmt()));
        double balanceAmt =  opgtDTO.getTotalCheckOutamt() - opgtDTO.getTotalTenderedAmount();
        opgt.setBalance(BigDecimal.valueOf(balanceAmt));
        if(opgtDTO.getCashPayment()!=null){   double amtPaid = opgtDTO.getTotalCheckOutamt() - opgtDTO.getCashPayment().getTotalCPDiscount();
//            opgt.setAccountDescription(accSetup.getPosCashAccount().getAccount_name());
            opgt.setAmountPaid(BigDecimal.valueOf(amtPaid));
//            opgt.setAccountid(accSetup.getPosCashAccount());
//            opgt.setAccountNo(accSetup.getPosCashAccount().getAccount_name());
        }
        if(opgtDTO.getCreditPayment()!=null){
            double   amtPaid = opgtDTO.getTotalCheckOutamt() - opgtDTO.getCreditPayment().getTotalCCPDiscount();
//            opgt.setAccountDescription(accSetup.getPosCashAccount().getAccount_name());
            opgt.setAmountPaid(BigDecimal.valueOf(amtPaid));
//            opgt.setAccountid(accSetup.getBankCharges());
//            opgt.setAccountNo(accSetup.getPosCashAccount().getAccount_name());
        }
        return opgt;
    }
    @Transactional
    public  void persistPOSSI(OtherPayment otherPayment, List<OcOtherPaymentWithoutItem> pidList) {
        try {
            Long id=Long.parseLong(otherPayment.getOtherContactID())    ;
            double amount=0;
            double tax=0;
            DecimalFormat df = new DecimalFormat("#.00");
            OtherContacts otherContacts=contactRepository.findOne(id);
            for (OcOtherPaymentWithoutItem calamount : pidList) {
                amount=calamount.getAmount().doubleValue()+amount;
                tax=calamount.getTaxAmount().doubleValue()+tax;
            }
            otherPayment.setSupplierName(otherContacts.getFullName());
            otherPayment.setAmount2(Double.parseDouble(df.format(amount)));
            otherPayment.setTaxAmount(Double.parseDouble(df.format(tax)));
            otherPaymentRepository.save(otherPayment);
            for (OcOtherPaymentWithoutItem invoiceDetails : pidList) {
                invoiceDetails.setGlName(otherContacts.getOtherContactId().toString());
                ocOtherPaymentWOItemRep.save(invoiceDetails);
            }
        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }
    public PosExpensePaymentTypes createPosExpensePayment(OtherPayment otherPayment, OtherPurchaseGtDTO opgtDTO)  {
        PosExpensePaymentTypes posExpensePaymentTypes = new PosExpensePaymentTypes();
        try {
            List<MultiVoucherPayment> multiVoucherPaymentsList = new ArrayList<>();
            List<MultiCardPayment> multiCardPaymentsList = new ArrayList<>();
            List<MultiBankPayment> multiBankPayments = new ArrayList<>();
            double totalVoucherAmount = 0.00;
            double totalCreditCardAmount = 0.00;
            double totalbankAmount = 0.00;
            if (opgtDTO.getVoucherPayment().getMultiVoucherPayments().size() != 0) {
                for (MultiVoucherPayment dto : opgtDTO.getVoucherPayment().getMultiVoucherPayments()) {
                    MultiVoucherPayment m = new MultiVoucherPayment();
                    m.setVoucherNo(dto.getVoucherNo());
                    m.setVoucherAmt(dto.getVoucherAmt());
                    m.setTransactionNo(dto.getTransactionNo());
                    totalVoucherAmount += dto.getVoucherAmt();
                    multiVoucherPaymentsList.add(m);
                }
            }
            if (opgtDTO.getBankPayment().getMultiBankPaymentList().size() != 0) {
                for (MultiBankPayment dto : opgtDTO.getBankPayment().getMultiBankPaymentList()) {
                    MultiBankPayment b = new MultiBankPayment();
                    b.setBankAccount(dto.getBankAccount());
                    b.setAmount(dto.getAmount());
                    b.setInvoiceNo(dto.getInvoiceNo());
                    b.setBankAccountId(dto.getBankAccountId());
                    Date date = new Date();
                    if (dto.getDate() == null) {
                        b.setDate(date);
                        dto.setDate(new Date());
                    } else {
                        b.setDate(dto.getDate());
                    }
                    b.setBankName(dto.getBankName());
                    b.setTransactionNo(dto.getTransactionNo());
                    b.setPaymentType(dto.getPaymentType());
                    totalbankAmount += dto.getAmount();
                    multiBankPayments.add(b);
                    if (StringUtils.pathEquals(dto.getPaymentType(), "Cheque")) {
                        ChequeReportDetails chequeReportDetails = new ChequeReportDetails();
                        chequeReportDetails.setChequeDate(dto.getDate());
                        if (dto.getDate().after(new Date())) {
                            chequeReportDetails.setChequeStatus("Not Deposited");
                        }
                        else {
                            chequeReportDetails.setChequeStatus("Deposited");
                        }
//                    chequeReportDetails.setSupplierid(otherPayment.getOtherContactID());
                        chequeReportDetails.setChequeNo(dto.getInvoiceNo());
                        chequeReportDetails.setChequeAmount(dto.getAmount());
                        chequeReportDetails.setInvoiceNumber(otherPayment.getOpNo());
                        chequeReportDetails.setStatus("Active");
                        chequeReportDetailsRepository.save(chequeReportDetails);
                    }
                }
            }
            if (opgtDTO.getCreditPayment().getCardPaymentList().size() != 0) {
                for (MultiCardPayment cardPayment : opgtDTO.getCreditPayment().getCardPaymentList()) {
                    MultiCardPayment multiCardPayment = new MultiCardPayment();
                    multiCardPayment.setCardAmt(cardPayment.getCardAmt());
                    multiCardPayment.setCardNo(cardPayment.getCardNo());
                    multiCardPayment.setTransactionNo(cardPayment.getTransactionNo());
                    multiCardPayment.setCardBankName(cardPayment.getCardBankName());
                    if(cardPayment.getCardDate()!=null) {
                        multiCardPayment.setCardDate(cardPayment.getCardDate());
                    }
                    else {
                        multiCardPayment.setCardDate(new Date().toString());
                    }                    totalCreditCardAmount += cardPayment.getCardAmt();
                    multiCardPaymentsList.add(multiCardPayment);
                }
            }
            posExpensePaymentTypes.setOtherPayment(otherPayment);
            posExpensePaymentTypes.setTotalVoucherPayment(totalVoucherAmount);
            posExpensePaymentTypes.setTotalCardPayment(totalCreditCardAmount);
            posExpensePaymentTypes.setTotalBankAmt(totalbankAmount);
            String json1 = new Gson().toJson(multiVoucherPaymentsList);
            String jsonBank = new Gson().toJson(multiBankPayments);
            String jsoncardpayment = new Gson().toJson(multiCardPaymentsList);
            posExpensePaymentTypes.setVoucherPayment(json1);
            posExpensePaymentTypes.setCardPayment(jsoncardpayment);
            posExpensePaymentTypes.setBankPayment(jsonBank);
            if (opgtDTO.getCashPayment().getMultiCashPaymentList().size() != 0) {
                posExpensePaymentTypes.setTotalCashPayment(opgtDTO.getCashPayment().getMultiCashPaymentList().get(0).getCashAmt());
            }
            posExpensePaymentTypes.setContact(contactRepository.findOne(Long.parseLong(otherPayment.getOtherContactID())));
            posExpensePaymentTypes.setTotalAmount(opgtDTO.getDiscountAmount());
            posExpensePaymentTypes.setCount(opgtDTO.getItemCount());
            posExpensePayRepository.save(posExpensePaymentTypes);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return posExpensePaymentTypes;
    }
    public void writingGtforPurchaseLedger( OtherPayment otherPayment ,
                                            List<OcOtherPaymentWithoutItem> pidList,  OtherPurchaseGtDTO opgtDTO
            ,PosExpensePaymentTypes posExpensePaymentTypes) {

        double totalPaymentBankAmt=0;
        double totalPaymentCashAmt=0;
        double totalPaymentVoucherAmt=0;
        Map map = new HashMap();
        FormSetUp formSetUp = getFooter("OtherPurchase");
        AccountMaster accountMaster = accountMasterRepository.findByAccountname("Petty Cash");

        if (opgtDTO.getCashPayment().getMultiCashPaymentList().size()>0) {
            BigDecimal cashVal = new BigDecimal(posExpensePaymentTypes.getTotalCashPayment());
            createGeneralLedgerForGTPurchase(otherPayment.getOpDate(),formSetUp, otherPayment.getOpNo(), accountMaster, "cashPayment", cashVal.negate(),opgtDTO.getSupplierInvNo(),opgtDTO.getSupplierName(),opgtDTO.getCurrencyId(),opgtDTO.getExchangerateValue());
            totalPaymentCashAmt= otherPayment.getAmountPaid().doubleValue();
            map.put("42/200", -totalPaymentCashAmt);//Cash Ledger
        }
        if(opgtDTO.getBankPayment().getMultiBankPaymentList().size()>0) {
            for (MultiBankPayment bankPayment : opgtDTO.getBankPayment().getMultiBankPaymentList()) {
//                if (!bankPayment.getDate().after(new Date())) {
//                AccountMaster bankPaymentACC = accountMasterRepository.findOne(bankPayment.getBankAccountId());
                BigDecimal bankVal = new BigDecimal(bankPayment.getAmount());
                createGeneralLedgerForGTPurchase(otherPayment.getOpDate(),formSetUp, otherPayment.getOpNo(),accountMaster, "bankPayment", bankVal.negate(),opgtDTO.getSupplierInvNo(),opgtDTO.getSupplierName(),opgtDTO.getCurrencyId(),opgtDTO.getExchangerateValue());
                totalPaymentBankAmt=otherPayment.getAmountPaid().doubleValue();
                map.put("42/100", -totalPaymentBankAmt);//Bank Ledger
//                }
            }
        }
        if(opgtDTO.getCreditPayment().getCardPaymentList().size()>0) {
            BigDecimal cardVal = new BigDecimal(posExpensePaymentTypes.getTotalCardPayment());
            createGeneralLedgerForGTPurchase(otherPayment.getOpDate(),formSetUp, otherPayment.getOpNo(), accountMaster, "bankPayment", cardVal.negate(),opgtDTO.getSupplierInvNo(),opgtDTO.getSupplierName(),opgtDTO.getCurrencyId(),opgtDTO.getExchangerateValue());
            totalPaymentBankAmt=otherPayment.getAmountPaid().doubleValue();
            map.put("42/100", -totalPaymentBankAmt);//Bank Ledger
        }
        if(opgtDTO.getVoucherPayment().getMultiVoucherPayments().size()>0) {
            BigDecimal vouVal = new BigDecimal(posExpensePaymentTypes.getTotalVoucherPayment());
            AccountMaster accountMaster1 = accountMasterRepository.findByAccountname("Discount Received");
            createGeneralLedgerForGTPurchase(otherPayment.getOpDate(),formSetUp, otherPayment.getOpNo(), accountMaster1, "voucherPayment", vouVal.negate(),opgtDTO.getSupplierInvNo(),opgtDTO.getSupplierName(),opgtDTO.getCurrencyId(),opgtDTO.getExchangerateValue());
            totalPaymentVoucherAmt= otherPayment.getAmountPaid().doubleValue();
            map.put("62", -totalPaymentVoucherAmt);//Voucher Ledger
        }
        for (OcOtherPaymentWithoutItem pd : pidList) {
            totalPaymentCashAmt= otherPayment.getAmountPaid().doubleValue();
//            Tax tax = hiposDAO.getTax(companyName, companyInfo, pd.getTaxId().getTaxId());
//            if(tax.getTaxTypeId()!=null) {
//                if (!tax.getTaxTypeId().getTaxTypeName().equalsIgnoreCase("BLOCKED CAPITAL GOODS")) {
//                    if (tax.getLinkedId() != null) {
//                        createGeneralLedgerForGTPurchase(otherPayment.getOpDate(), formSetUp, otherPayment.getOpNo(), tax.getLinkedId(), HiposConstants.PAYMENTTYPE_BANK, pd.getTaxAmount(), opgtDTO.getSupplierInvNo(), opgtDTO.getSupplierName(), opgtDTO.getCurrencyId(), opgtDTO.getExchangerateValue());
//                        totalPaymentCashAmt = otherPayment.getAmountPaid().doubleValue();
//                    }
//                    createGeneralLedgerForGTPurchase(otherPayment.getOpDate(), formSetUp, otherPayment.getOpNo(), pd.getAccountid(), HiposConstants.PAYMENTTYPE_BANK, (pd.getAmountIncludeTax().subtract((pd.getTaxAmount()))), opgtDTO.getSupplierInvNo(), opgtDTO.getSupplierName(), opgtDTO.getCurrencyId(), opgtDTO.getExchangerateValue());
//                } else {
//                    createGeneralLedgerForGTPurchase(otherPayment.getOpDate(), formSetUp, otherPayment.getOpNo(), pd.getAccountid(), HiposConstants.PAYMENTTYPE_BANK, (pd.getAmountIncludeTax()), opgtDTO.getSupplierInvNo(), opgtDTO.getSupplierName(), opgtDTO.getCurrencyId(), opgtDTO.getExchangerateValue());
//                }
//            }else {
                createGeneralLedgerForGTPurchase(otherPayment.getOpDate(), formSetUp, otherPayment.getOpNo(), pd.getAccountid(),  "bankPayment", (pd.getAmountIncludeTax()), opgtDTO.getSupplierInvNo(), opgtDTO.getSupplierName(), opgtDTO.getCurrencyId(), opgtDTO.getExchangerateValue());
//            }
//            String[] str =  pd.getAccountid().getStringAccountCode().split("/");
//            String fourDigit = str[0];
//            String threeDigit = str[1];
//            map.put(fourDigit, (pd.getAmountIncludeTax().abs().subtract((pd.getTaxAmount()))));
//            map.put(AccountGroupConstants.TAX_CLAIMABLE,  pd.getTaxAmount());//Tax Ledger Amt
//            accountGroupService.upDateTotalBalanceAccountGroup(map);
        }
    }
    public OtherPurchaseGtDTO getPrintDetailsOtherPurchase(OtherPurchaseGtDTO opgtDTO, OtherPayment otherPayment, List<OcOtherPaymentWithoutItem> pidList)throws ParseException{
        SimpleDateFormat sfmtDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sfmtTime = new SimpleDateFormat("HH:mm:ss");
        opgtDTO.setTaxInvoice(otherPayment.getTaxInvoice());
        Company company=companyRepository.findAllByStatus("Active");
        opgtDTO.setCompany(company);
        BigDecimal deci= new BigDecimal(String.valueOf(otherPayment.getTotalTaxAmount()));
        opgtDTO.setTotalTaxAmt(deci.doubleValue());
        opgtDTO.setOpDate(sfmtDate.format(otherPayment.getOpDate()));
        opgtDTO.setOpNo(otherPayment.getOpNo());
        opgtDTO.setAmountPaid(otherPayment.getAmountPaid());
        BigDecimal decibalance= new BigDecimal(String.valueOf(otherPayment.getBalance()));
        opgtDTO.setBalanceAmount(decibalance.doubleValue());
        if(opgtDTO.getCreditPayment().getCardPaymentList().size()>0){
            opgtDTO.setPaymentType("Card");
        }
        if(opgtDTO.getBankPayment().getMultiBankPaymentList().size()>0){
            opgtDTO.setPaymentType("Bank");
        }
        if(opgtDTO.getVoucherPayment().getMultiVoucherPayments().size()>0){
            opgtDTO.setPaymentType("Voucher");
        }
        if(opgtDTO.getCashPayment().getMultiCashPaymentList().size()>0){
            opgtDTO.setPaymentType("Cash");
        }
        return opgtDTO;
    }
    @Transactional
    public FormSetUp getFormSetUp(String formSetupType) {
        FormSetUp formSetUp = new FormSetUp();
        try {
            formSetUp = formSetupRepository.findAllByTypename(formSetupType);
            int incValue = Integer.parseInt(formSetUp.getNextref());
            synchronized (formSetUp) {
                formSetUp.setNextref(String.format("%05d", ++incValue));
            }

            formSetupRepository.save(formSetUp);
        } catch (HibernateException he) {
            he.printStackTrace();
        }

        return formSetUp;
    }

    @Transactional
    public JournalEntryDTO createJournalEntryNPayment(JournalEntryDTO jegtDTO) throws IOException, JSONException {
        FormSetUp formSetUp = null;
        if (StringUtils.isEmpty(jegtDTO.getJeNo())) {
            formSetUp = getFormSetUp("JournalEntry");

        }
        JournalEntryDTO printDTO = new JournalEntryDTO();
        JournalEntry journalEntry = createJournalEntry(jegtDTO, formSetUp);
        List<JournalEntryDetails> jeList = createJournalEntryDetails(jegtDTO, journalEntry);
        journalEntryRepository.save(journalEntry);
        for (JournalEntryDetails invoiceDetails : jeList) {
            journalEntryDetailsRepository.save(invoiceDetails);
        }
        if (formSetUp != null) {
            formSetupRepository.save(formSetUp);
        }
        if (StringUtils.pathEquals(jegtDTO.getStatus(), "Prepared")) {
            writingJournalEntryLedger(journalEntry, jegtDTO, jeList);
        }
        printDTO = getPrintDetailsJournalEntry(jegtDTO, journalEntry, jeList);
        printDTO.setStatus("success");
        return printDTO;
    }

    private JournalEntry createJournalEntry(JournalEntryDTO jegtDTO,FormSetUp formSetUp) {
        JournalEntry jegt = null;
        if(StringUtils.isEmpty(jegtDTO.getJeNo())) {
            jegt=new JournalEntry();
            jegt.setJeNo(getNextRefInvoice(formSetUp.getTypeprefix(), formSetUp.getNextref()));
        }
        else{
            jegt=journalEntryRepository.findByJeNo(jegtDTO.getJeNo());
            jegt.setJeNo(jegtDTO.getJeNo());
        }
        jegt.setJeDate(jegtDTO.getJeDate());

        jegt.setMemo(jegtDTO.getMemo());
        jegt.setTotalCredit(jegtDTO.getTotalCredit());
        jegt.setTotalDebit(jegtDTO.getTotalDebit());
        jegt.setStatus(jegtDTO.getStatus());
        return jegt;
    }
    private List<JournalEntryDetails> createJournalEntryDetails(JournalEntryDTO jegtDTO, JournalEntry journalEntry) {
        List<JournalEntryDetails> jedList = new ArrayList<>();
        JournalEntry journalEntry1 = new JournalEntry();
        if(!StringUtils.isEmpty(jegtDTO.getJeNo())){
            journalEntry1=journalEntryRepository.findByJeNo(jegtDTO.getJeNo());
            List<JournalEntryDetails> list=journalEntryDetailsRepository.findAllByJeId(journalEntry1);
            for(JournalEntryDetails journalEntryDetails:list){
                journalEntryDetailsRepository.delete(journalEntryDetails);
            }
        }
        for (JournalEntryDetailsDTO selectedAccount : jegtDTO.getJournalEntryDetailsList()) {
            JournalEntryDetails jed = new JournalEntryDetails();
            jed.setRemark(selectedAccount.getAccountDescription());
            jed.setDebit(selectedAccount.getDebitAmount());
            jed.setCredit(selectedAccount.getCreditAmount());
            jed.setFormNo(journalEntry.getJeNo());
            jed.setJeId(journalEntry);
            AccountMaster accountMaster = accountMasterRepository.findByAccountname(selectedAccount.getAccountname());
            jed.setAccountid(accountMaster);
            jedList.add(jed);

        }
        return jedList;
    }
    public JournalEntryDTO getPrintDetailsJournalEntry(JournalEntryDTO jegtDTO,JournalEntry journalEntry, List<JournalEntryDetails> jeList){
        SimpleDateFormat sfmtDate = new SimpleDateFormat("dd/MM/yyyy");
        FormSetUp formterms = getFooter("JournalEntry");
        jegtDTO.setFooter(formterms.getTermsDesc());
        jegtDTO.setTotalCredit(journalEntry.getTotalCredit());
        jegtDTO.setTotalDebit(journalEntry.getTotalDebit());
        jegtDTO.setJepDate(sfmtDate.format(journalEntry.getJeDate()));
        jegtDTO.setJeNo(journalEntry.getJeNo());
        Company company=companyRepository.findAllByStatus("Active");
        jegtDTO.setCompany(company);
        return jegtDTO;
    }
    public void writingJournalEntryLedger(JournalEntry journalEntry,
                                          JournalEntryDTO journalEntryDTO,  List<JournalEntryDetails> jeList) {
        FormSetUp formSetUp = getFooter("JournalEntry");
        AccountMaster accountSetup = accountMasterRepository.findByAccountname("Petty Cash");
        for (JournalEntryDetails pd : jeList) {
            String[] str =  pd.getAccountid().getStringAccountCode().split("/");
            String fourDigit = str[0];
            Map map = new HashMap();
            if (pd.getDebit().compareTo(BigDecimal.ZERO) > 0) {
                createGeneralLedgerForJournalEntry(journalEntry.getJeDate(),formSetUp, journalEntry.getJeNo(), pd.getAccountid(), journalEntry.getMemo(), pd.getDebit(), journalEntry.getJeNo(),pd.getRemark(),"1","1");
                map.put(fourDigit, journalEntry.getTotalDebit());
            } else {
                map.put(fourDigit, journalEntry.getTotalCredit().negate());
                createGeneralLedgerForJournalEntry(journalEntry.getJeDate(),formSetUp, journalEntry.getJeNo(), pd.getAccountid(), journalEntry.getMemo(), pd.getCredit().negate(), journalEntry.getJeNo(),pd.getRemark(),"1","1");
            }
        }
    }
    public void createGeneralLedgerForJournalEntry(Date dt,FormSetUp Tran_type, String Tran_no, AccountMaster account, String memo, BigDecimal amountWithoutExchangeRate, String PLRNo,String flag,String currencyId,String currencyValue) {
        BigDecimal amount;
        amount=amountWithoutExchangeRate;
        GLTransactions gl = new GLTransactions();
        gl.setTran_type(Tran_type);
        gl.setTran_no(Tran_no);
        gl.setTran_date(dt);
        gl.setAccount(account);
        gl.setCurrencyValue(currencyValue);
        gl.setAcc_code(account != null ? account.getAccountcode() : null);
        gl.setMemo(memo);
        gl.setSuppInNo(PLRNo);
        gl.setAmount(amount);
        gl.setFlagForGlTrans(flag);
        glTransactionRepository.save(gl);
//        FiscalYearDTO fiscalYear=hiposDAO.getCurrentFiscalYearList(null,null,0);
//        hiposLedgerDAO.createGTofRespectiveTable(dt,Tran_type, Tran_no,account, memo,amount, PLRNo,flag,fiscalYear,this.userAccountSetup,currency,currencyValue);
    }
    public  List<JournalEntry> getOtherJournalEntryList(String searchText) {
        List<JournalEntry> otherPaymentList = new ArrayList<>();
//        List<String> statusList=new ArrayList<>();
//        statusList.add("Cancelled Invoice");
//        statusList.add("Draft Cancelled Invoice");
        if (org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
            otherPaymentList = journalEntryRepository.findAllByJeNoContaining(searchText);
        } else {
            otherPaymentList = journalEntryRepository.findAll();
        }
        return otherPaymentList;
    }

    public JournalEntryDTO duplicateJournalEntryPrint(String paymentId){
        JournalEntryDTO jeDTO = new JournalEntryDTO();
        SimpleDateFormat sfmtDate = new SimpleDateFormat("dd/MM/yyyy");
        JournalEntry journalEntry = journalEntryRepository.findByJeNo(paymentId);
        List<JournalEntryDetails> journalEntryDetailsList = journalEntryDetailsRepository.findAllByJeId(journalEntry);
        List<JournalEntryDetailsDTO> journalEntryDetailsDTOS=new ArrayList<>();
        for(JournalEntryDetails journalEntryDetails:journalEntryDetailsList){
            JournalEntryDetailsDTO journalEntryDetailsDTO=new JournalEntryDetailsDTO();
            journalEntryDetailsDTO.setAccountname(journalEntryDetails.getAccountid().getAccountname());
            journalEntryDetailsDTO.setDebitAmount(journalEntryDetails.getDebit());
            journalEntryDetailsDTO.setCreditAmount(journalEntryDetails.getCredit());
            journalEntryDetailsDTO.setAccountDescription(journalEntryDetails.getRemark());
            journalEntryDetailsDTOS.add(journalEntryDetailsDTO);
        }
        jeDTO.setJournalEntryDetailsList(journalEntryDetailsDTOS);
        jeDTO.setJeNo(journalEntry.getJeNo());
        jeDTO.setJepDate(sfmtDate.format(journalEntry.getJeDate()));
        jeDTO.setTotalDebit(journalEntry.getTotalDebit());
        jeDTO.setTotalCredit(journalEntry.getTotalCredit());
        Company company=companyRepository.findAllByStatus("Active");
        jeDTO.setCompany(company);
        return jeDTO;
    }
    public List<OtherPayment> getOtherPaymentDuplicateList(String searchText) {
        List<OtherPayment> otherPaymentList = new ArrayList<>();
        List<String> statusList=new ArrayList<>();
        statusList.add("Cancelled Invoice");
        statusList.add("Draft Cancelled Invoice");
        if (org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
            otherPaymentList = otherPaymentRepository.findAllByStatusNotInAndOpNoContaining(statusList,searchText);
        } else {
            otherPaymentList = otherPaymentRepository.findAllByStatusNotIn(statusList);
        }
        return otherPaymentList;
    }

    public OtherPurchaseGtDTO duplicateVoucherPrint(String paymentId){
        OtherPurchaseGtDTO opgtDTO = new OtherPurchaseGtDTO();
        SimpleDateFormat sfmtDate = new SimpleDateFormat("dd/MM/yyyy");
        OtherPayment otherPayment = otherPaymentRepository.findByOpNo(paymentId);
        ArrayList<AccountMasterDTO> accountMasterDTOS=new ArrayList<>();
        List<OcOtherPaymentWithoutItem> accountList = ocOtherPaymentWOItemRep.findByOcdetails(otherPayment);
        for(OcOtherPaymentWithoutItem ocOtherPaymentWithoutItem:accountList){
            AccountMasterDTO accountMasterDTO=new AccountMasterDTO();
            accountMasterDTO.setAccountname(ocOtherPaymentWithoutItem.getAccountid().getAccountname());
            accountMasterDTO.setAccountcode(ocOtherPaymentWithoutItem.getAccountid().getAccountcode());
            accountMasterDTO.setAccountid(ocOtherPaymentWithoutItem.getAccountid().getAccountid());
            accountMasterDTO.setAccountDescription(ocOtherPaymentWithoutItem.getDescription());
            accountMasterDTO.setGtAmountExcTax(ocOtherPaymentWithoutItem.getAmount());
            accountMasterDTO.setInvoiceNumber(ocOtherPaymentWithoutItem.getInvoiceNumber());
            accountMasterDTO.setGtAmountIncTax(ocOtherPaymentWithoutItem.getAmountIncludeTax());
            accountMasterDTO.setAmtexclusivetax(ocOtherPaymentWithoutItem.getAmountIncludeTax().doubleValue());
            accountMasterDTOS.add(accountMasterDTO);
        }
        opgtDTO.setSelectedAccountList(accountMasterDTOS);
        opgtDTO.setTaxInvoice(otherPayment.getTaxInvoice());
        BigDecimal deci= new BigDecimal(String.valueOf(otherPayment.getTotalTaxAmount()));
        opgtDTO.setTotalTaxAmt(deci.doubleValue());
        opgtDTO.setSupplierName(otherPayment.getSupplierName());
        Company company=companyRepository.findAllByStatus("Active");
        opgtDTO.setCompany(company);
        //---------------end---------
        opgtDTO.setOpDate(sfmtDate.format(otherPayment.getOpDate()));
        opgtDTO.setOpNo(otherPayment.getOpNo());
        opgtDTO.setAmountPaid(otherPayment.getAmountPaid());
        opgtDTO.setBalanceAmount(otherPayment.getBalance().doubleValue());
        opgtDTO.setGstApprovalNumber(otherPayment.getGstApprovalNumber());
        //pos print data
        PosExpensePaymentTypes payments=posExpensePayRepository.findByOtherPayment(otherPayment);
        VoucherPayment v=new VoucherPayment();
        CreditCardPayment  cp=new CreditCardPayment();
        Gson json1 = new Gson();
        BankPayment bp=new BankPayment();
        Type bankType = new TypeToken<ArrayList<MultiBankPayment>>() {}.getType();
        Type type1 = new TypeToken<ArrayList<MultiCardPayment>>() {}.getType();
        Type type = new TypeToken<ArrayList<MultiVoucherPayment>>() {}.getType();
        Type cashType = new TypeToken<ArrayList<MultiCashPayment>>() {}.getType();
        List<MultiVoucherPayment> multiVoucherPayments=new ArrayList<>();
        if(!StringUtils.isEmpty(payments.getVoucherPayment())) {
            multiVoucherPayments = json1.fromJson(payments.getVoucherPayment(), type);
            v.setMultiVoucherPayments(multiVoucherPayments);
            opgtDTO.setVoucherPayment(v);
        }
        List<MultiCardPayment> multiCardPayments=new ArrayList<>();
        if(!StringUtils.isEmpty(payments.getCardPayment())) {
            multiCardPayments = json1.fromJson(payments.getCardPayment(), type1);
            cp.setCardPaymentList(multiCardPayments);
            opgtDTO.setCreditPayment(cp);
        }
        if(!StringUtils.isEmpty(payments.getBankPayment())) {
            List<MultiBankPayment> multiBankPayment = json1.fromJson(payments.getBankPayment(), bankType);
            bp.setMultiBankPaymentList(multiBankPayment);
            opgtDTO.setBankPayment(bp);
        }
        CashPayment cashPayment=new CashPayment();
        if(!StringUtils.isEmpty(payments.getTotalCashPayment())) {
            cashPayment.setTotalCPAmountTendered(payments.getTotalCashPayment());
            opgtDTO.setCashPayment(cashPayment);
        }
        return opgtDTO;
    }
    @Transactional
    public OtherRecieptGtDTO createDraftGtSalesPayment(OtherRecieptGtDTO orgtDTO)throws IOException, JSONException, ParseException {
        OtherRecieptGtDTO printDTO = new OtherRecieptGtDTO();
        OtherReceipt otherReceipt = createPOSSI(orgtDTO);
            otherReceipt.setStatus("Draft");
            otherReceipt.setPosting("No");
            List<OcOtherReceiptWithoutItem> sidList = createPOSSIDetails(orgtDTO, otherReceipt);
            persistOtherReceipt(otherReceipt, sidList);
            createPosReceiptPayment(otherReceipt, orgtDTO);
            printDTO = getPrintDetailsOtherRecipt(orgtDTO, otherReceipt, sidList);
            return printDTO;
    }
    public OtherRecieptGtDTO retrieveGtSalesForEditPopulate(String invoiceNo) {
        OtherReceipt  receipt = otherReceiptRepository.findByOrno(invoiceNo);
        System.out.println("invoice found with number "+invoiceNo+" -> "+receipt);
        OtherRecieptGtDTO otherRecieptGtDTO = new OtherRecieptGtDTO();
        List<OcOtherReceiptWithoutItem> ocOtherReceiptWithoutItem=ocOtherReceiptWOItemRep.findByOcdetails(receipt);
        List<AccountMasterDTO> accountList = GTMapper.getOcReceiptDetailsEntityToPojo(ocOtherReceiptWithoutItem);
        ArrayList<AccountMasterDTO> selectedAccounts = new ArrayList<>(accountList);
        otherRecieptGtDTO.setSelectedAccountList(selectedAccounts);
        PosReceiptPaymentTypes receipts=retriveReceipt(invoiceNo);
        otherRecieptGtDTO.setSiNo(invoiceNo);
        VoucherPayment v=new VoucherPayment();
        CreditCardPayment  cp=new CreditCardPayment();
        Gson json1 = new Gson();
        BankPayment bp=new BankPayment();
        Type bankType = new TypeToken<ArrayList<MultiBankPayment>>() {}.getType();
        Type type1 = new TypeToken<ArrayList<MultiCardPayment>>() {}.getType();
        Type type = new TypeToken<ArrayList<MultiVoucherPayment>>() {}.getType();
        List<MultiVoucherPayment> multiVoucherPayments=new ArrayList<>();
        multiVoucherPayments=json1.fromJson(receipts.getVoucherPayment(), type);
        v.setMultiVoucherPayments(multiVoucherPayments);
        otherRecieptGtDTO.setVoucherPayment(v);
        List<MultiCardPayment> multiCardPayments=new ArrayList<>();
        multiCardPayments=json1.fromJson(receipts.getCardPayment(), type1);
        cp.setCardPaymentList(multiCardPayments);
        otherRecieptGtDTO.setCreditPayment(cp);
        List<MultiBankPayment> multiBankPayment=json1.fromJson(receipts.getBankPayment(), bankType);
        bp.setMultiBankPaymentList(multiBankPayment);
        otherRecieptGtDTO.setBankPayment(bp);
        CashPayment cashPayment=new CashPayment();
        cashPayment.setTotalCPAmountTendered(receipts.getTotalCashPayment());
        otherRecieptGtDTO.setCashPayment(cashPayment);
        otherRecieptGtDTO.setBalanceAmount(receipts.getTotalAmount());
        otherRecieptGtDTO.setOperation( "Draft");
        otherRecieptGtDTO.setOrNo(receipt.getOrno());
        if(receipt.getOtherContactID()!=null&&Long.parseLong(receipt.getOtherContactID())!=0) {
            OtherContacts otherContacts = contactRepository.findOne(Long.parseLong(receipt.getOtherContactID()));
            otherRecieptGtDTO.setSupplierName(otherContacts.getFullName());
            otherRecieptGtDTO.setCustomerId(otherContacts.getOtherContactId());
        }
        otherRecieptGtDTO.setOrDate(receipt.getOrDate());
        return otherRecieptGtDTO;
    }
    public PosReceiptPaymentTypes retriveReceipt(String invoiceNo) {
        OtherReceipt invoice = otherReceiptRepository.findByOrno(invoiceNo);
        PosReceiptPaymentTypes vd=null;
        if (invoice != null) {
            vd= posReceiptPayRepository.findByOtherReceipt(invoice);
        }
        return vd;
    }
    public  String makeCancelledGtReceipt(String invoiceId){
        String status= null;
        String failedmsg="Canot Able To  Cancel Invoice Please Try Once Again Or Please Contact Your Service Provider ";
        String success="Invoice Cancelled SuccessFully";
        status=cancelGtReceiptInvoice(invoiceId,success,status);
        return status;
    }
    public void writingGtforSalesLedgerForCancellation( OtherReceipt otherReceipt ,
                                                        List<OcOtherReceiptWithoutItem> sidList,  PosReceiptPaymentTypes orgtDTO) {
        double totalPaymentBankAmt=0;
        double totalPaymentCashAmt=0;
        double totalPaymentVoucherAmt=0;
        Map map = new HashMap();
        FormSetUp formSetUp =null;
        if(!StringUtils.isEmpty(otherReceipt.getOtherContactID())) {
            formSetUp = getFooter( "OtherReceipt");
        }
        else{
            formSetUp = getFooter("OtherReceipt");

        }
        AccountMaster accountSetup = accountMasterRepository.findByAccountname("Petty Cash");
        if (orgtDTO.getTotalCashPayment()>0) {
            createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(), accountSetup, "CancelledGTReceipt", new BigDecimal(orgtDTO.getTotalCashPayment()).negate(),"",otherReceipt.getSupplierName(),"1","1");
            totalPaymentCashAmt= otherReceipt.getAmountPaid().doubleValue();
            map.put("42/200", totalPaymentCashAmt);//Cash Ledger
        }
        if(orgtDTO.getTotalBankAmt()>0){
            Gson json1 = new Gson();
            Type bankType = new TypeToken<ArrayList<MultiBankPayment>>() {}.getType();
            List<MultiBankPayment> multiBankPayment=json1.fromJson(orgtDTO.getBankPayment(), bankType);
            for (MultiBankPayment bankPayment : multiBankPayment) {
                AccountMaster bankPaymentACC = accountMasterRepository.findOne(bankPayment.getBankAccountId());
                createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(),bankPaymentACC, "CancelledGTReceipt", new BigDecimal(bankPayment.getAmount()).negate(),"",otherReceipt.getSupplierName(),"1","1");
                totalPaymentBankAmt=otherReceipt.getAmountPaid().doubleValue();
                map.put("42/100", totalPaymentBankAmt);//Bank Ledger
//                }
            }
        }
        if(orgtDTO.getTotalCardPayment()>0){
            createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(), accountSetup, "CancelledGTReceipt", new BigDecimal(orgtDTO.getTotalCardPayment()).negate(),"",otherReceipt.getSupplierName(),"1","1");
            totalPaymentBankAmt=otherReceipt.getAmountPaid().doubleValue();
            map.put("42/100", totalPaymentBankAmt);//Bank Ledger
        }
        if(orgtDTO.getTotalVoucherPayment()>0){
            AccountMaster accountMaster = accountMasterRepository.findByAccountname("Discounts Given");
            createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(), accountMaster,"CancelledGTReceipt", new BigDecimal(orgtDTO.getTotalVoucherPayment()).negate(),"",otherReceipt.getSupplierName(),"1","1");
            totalPaymentVoucherAmt=otherReceipt.getAmountPaid().doubleValue();
            map.put("84", totalPaymentVoucherAmt);//Bank Ledger
        }
        for (OcOtherReceiptWithoutItem sd : sidList) {
            createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(),  sd.getAccountid(), "CancelledGTReceipt", (sd.getAmountIncludeTax().subtract(sd.getTaxAmount())),"",otherReceipt.getSupplierName(),"1","1");
            totalPaymentCashAmt= otherReceipt.getAmountPaid().doubleValue();
//            Tax tax=hiposDAO.getTax(companyName,  companyInfo,  sd.getTaxId().getTaxId());
//            if(tax.getLinkedId()!=null){
//                createGeneralLedgerForGTPurchase(otherReceipt.getOrDate(),formSetUp, otherReceipt.getOrno(), sd.getTaxId().getLinkedId(), "CancelledGTReceipt", sd.getTaxAmount(), "",otherReceipt.getSupplierName(),"1","1");
//                totalPaymentCashAmt= otherReceipt.getAmountPaid().doubleValue();
//            }
            String[] str =  sd.getAccountid().getStringAccountCode().split("/");
            String fourDigit = str[0];
            String threeDigit = str[1];
            map.put(fourDigit, (sd.getAmountIncludeTax().subtract((sd.getTaxAmount()))).negate());
//            map.put(AccountGroupConstants.TAX_CLAIMABLE, sd.getTaxAmount());//Tax Ledger Amt
//            accountGroupService.upDateTotalBalanceAccountGroup(map);
        }
        otherReceipt.setStatus("Cancelled Invoice");

        String invno=otherReceipt.getOrno();
        List<OtherReceipt> data = otherReceiptRepository.findAllByOrno(invno);
        otherReceipt.setOrno(otherReceipt.getOrno()+"c"+data.size());
        otherReceiptRepository.save(otherReceipt);

    }
    public String cancelGtReceiptInvoice(String invoiceId,String success,String status){
        OtherReceipt otherReceipt=otherReceiptRepository.findByOrno(invoiceId);
        List<OcOtherReceiptWithoutItem> invoiceDetails = ocOtherReceiptWOItemRep.findByOcdetails(otherReceipt);
        PosReceiptPaymentTypes posReceiptPaymentTypes =posReceiptPayRepository.findByOtherReceipt(otherReceipt);
        writingGtforSalesLedgerForCancellation(otherReceipt,invoiceDetails ,posReceiptPaymentTypes);
        status = success;
        return status;
    }
    public OtherRecieptGtDTO postGTSales(String invoice){
        OtherReceipt otherReceipt=otherReceiptRepository.findByOrno(invoice);
        List<OcOtherReceiptWithoutItem> sidList=ocOtherReceiptWOItemRep.findByOcdetails(otherReceipt);
        PosReceiptPaymentTypes posReceiptPaymentTypes=posReceiptPayRepository.findByOtherReceipt(otherReceipt);
        OtherRecieptGtDTO otherRecieptGtDTO=duplicateReceiptPrint(otherReceipt.getOrno());
        otherReceipt.setPosting("Yes");
        otherReceipt.setStatus("WithoutItem");
        otherReceiptRepository.save(otherReceipt);
        if(otherRecieptGtDTO.getCashPayment().getTotalCPAmountTendered()>0){
            MultiCashPayment cashPayment=new MultiCashPayment();
            cashPayment.setPaymentType(1L);
            cashPayment.setCashAmt(otherRecieptGtDTO.getCashPayment().getTotalCPAmountTendered());
            List<MultiCashPayment> multiCashPayments=new ArrayList<>();
            multiCashPayments.add(cashPayment);
            otherRecieptGtDTO.getCashPayment().setMultiCashPaymentList(multiCashPayments);
        }
        writingGtforSalesLedger(otherReceipt, sidList,otherRecieptGtDTO,posReceiptPaymentTypes);
        return otherRecieptGtDTO;
    }
    public OtherPurchaseGtDTO retrievePIDetailsForEditPopulate(String invoiceNo) {
        OtherPayment payment = otherPaymentRepository.findByOpNo(invoiceNo);
        System.out.println("invoice found with number "+invoiceNo+" -> "+payment);
        OtherPurchaseGtDTO otherPurchaseGtDTO = new OtherPurchaseGtDTO();
        ArrayList<AccountMasterDTO> accountMasterDTOS=new ArrayList<>();
        List<OcOtherPaymentWithoutItem> accountList = ocOtherPaymentWOItemRep.findByOcdetails(payment);
        for(OcOtherPaymentWithoutItem ocOtherPaymentWithoutItem:accountList){
            AccountMasterDTO accountMasterDTO=new AccountMasterDTO();
            accountMasterDTO.setAccountname(ocOtherPaymentWithoutItem.getAccountid().getAccountname());
            accountMasterDTO.setAccountcode(ocOtherPaymentWithoutItem.getAccountid().getAccountcode());
            accountMasterDTO.setAccountDescription(ocOtherPaymentWithoutItem.getDescription());
            accountMasterDTO.setGtAmountExcTax(ocOtherPaymentWithoutItem.getAmount());
            accountMasterDTO.setInvoiceNumber(ocOtherPaymentWithoutItem.getInvoiceNumber());
            accountMasterDTO.setGtAmountIncTax(ocOtherPaymentWithoutItem.getAmountIncludeTax());
            accountMasterDTO.setAmtinclusivetax(ocOtherPaymentWithoutItem.getAmountIncludeTax().doubleValue());
            accountMasterDTO.setAmtexclusivetax(ocOtherPaymentWithoutItem.getAmountIncludeTax().doubleValue());
            accountMasterDTO.setAccountid(ocOtherPaymentWithoutItem.getAccountid().getAccountid());
            accountMasterDTOS.add(accountMasterDTO);
        }
        otherPurchaseGtDTO.setSelectedAccountList(accountMasterDTOS);
        PosExpensePaymentTypes payments=retrive(invoiceNo);
        otherPurchaseGtDTO.setPiNo(invoiceNo);
        VoucherPayment v=new VoucherPayment();
        CreditCardPayment cp=new CreditCardPayment();
        Gson json1 = new Gson();
        BankPayment bp=new BankPayment();
        Type bankType = new TypeToken<ArrayList<MultiBankPayment>>() {}.getType();
        Type type1 = new TypeToken<ArrayList<MultiCardPayment>>() {}.getType();
        Type type = new TypeToken<ArrayList<MultiVoucherPayment>>() {}.getType();
        List<MultiVoucherPayment> multiVoucherPayments=new ArrayList<>();
        multiVoucherPayments=json1.fromJson(payments.getVoucherPayment(), type);
        v.setMultiVoucherPayments(multiVoucherPayments);
        otherPurchaseGtDTO.setVoucherPayment(v);
        List<MultiCardPayment> multiCardPayments=new ArrayList<>();
        multiCardPayments=json1.fromJson(payments.getCardPayment(), type1);
        cp.setCardPaymentList(multiCardPayments);
        otherPurchaseGtDTO.setCreditPayment(cp);
        List<MultiBankPayment> multiBankPayment=json1.fromJson(payments.getBankPayment(), bankType);
        bp.setMultiBankPaymentList(multiBankPayment);
        otherPurchaseGtDTO.setBankPayment(bp);
        CashPayment cashPayment=new CashPayment();
        cashPayment.setTotalCPAmountTendered(payments.getTotalCashPayment());
        otherPurchaseGtDTO.setCashPayment(cashPayment);
        otherPurchaseGtDTO.setBalanceAmount(payments.getTotalAmount());
        otherPurchaseGtDTO.setOperation("Draft");
        otherPurchaseGtDTO.setOpNo(payment.getOpNo());
        otherPurchaseGtDTO.setGstApprovalNumber(payment.getGstApprovalNumber());
        OtherContacts otherContacts =contactRepository.findOne(Long.parseLong(payment.getOtherContactID()));
        otherPurchaseGtDTO.setSupplierName(otherContacts.getFullName());
        otherPurchaseGtDTO.setSupplierId(otherContacts.getOtherContactId());
        otherPurchaseGtDTO.setDate(payment.getOpDate());
        return otherPurchaseGtDTO;
    }
    public PosExpensePaymentTypes retrive(String invoiceNo) {
        OtherPayment invoice = otherPaymentRepository.findByOpNo(invoiceNo);
        PosExpensePaymentTypes vd=null;
        if (invoice != null) {
            vd= posExpensePayRepository.findByOtherPayment(invoice);
        }
        return vd;
    }
    @Transactional
    public OtherPurchaseGtDTO createDraftGtPurchasePayment(OtherPurchaseGtDTO opgtDTO)throws IOException, JSONException, ParseException {
        OtherPurchaseGtDTO printDTO = new OtherPurchaseGtDTO();
        OtherPayment otherPayment = createPOSSI(opgtDTO);
        otherPayment.setStatus("Draft");
        otherPayment.setPosting("No");
        List<OcOtherPaymentWithoutItem> pidList = createPOSSIDetails(opgtDTO, otherPayment);
        persistPOSSI( otherPayment, pidList);
        createPosExpensePayment(otherPayment,opgtDTO);
        printDTO = getPrintDetailsOtherPurchase(opgtDTO,otherPayment,pidList);
        return printDTO;
    }
    public OtherPurchaseGtDTO postGTPurchase(String invoice){
        OtherPayment otherPayment=otherPaymentRepository.findByOpNo(invoice);
        List<OcOtherPaymentWithoutItem> pidList=ocOtherPaymentWOItemRep.findByOcdetails(otherPayment);
        PosExpensePaymentTypes posExpensePaymentTypes=posExpensePayRepository.findByOtherPayment(otherPayment);
        OtherPurchaseGtDTO otherPurchaseGtDTO=duplicateVoucherPrint(otherPayment.getOpNo());
        otherPayment.setStatus("WithoutItem");
        otherPayment.setPosting("Yes");
        otherPaymentRepository.save(otherPayment);
        if(otherPurchaseGtDTO.getCashPayment().getTotalCPAmountTendered()>0){
            MultiCashPayment cashPayment=new MultiCashPayment();
            cashPayment.setPaymentType(1L);
            cashPayment.setCashAmt(otherPurchaseGtDTO.getCashPayment().getTotalCPAmountTendered());
            List<MultiCashPayment> multiCashPayments=new ArrayList<>();
            multiCashPayments.add(cashPayment);
            otherPurchaseGtDTO.getCashPayment().setMultiCashPaymentList(multiCashPayments);
        }else {
            List<MultiCashPayment> multiCashPayments=new ArrayList<>();
            otherPurchaseGtDTO.getCashPayment().setMultiCashPaymentList(multiCashPayments);
        }
        writingGtforPurchaseLedger(otherPayment, pidList,otherPurchaseGtDTO,posExpensePaymentTypes);
        return otherPurchaseGtDTO;
    }
    public String cancelGtforExpense(String invoiceNo){
        cancelGtPurchase(invoiceNo);
        return null;
    }
    @Transactional
    public void cancelGtPurchase(String invoiceNo){
        OtherPayment otherPayment=otherPaymentRepository.findByOpNo(invoiceNo);
        List<OtherPayment> otherPaymentList=otherPaymentRepository.findAllByOpNo(invoiceNo);
        otherPayment.setOpNo(otherPayment.getOpNo()+"c"+otherPaymentList.size());
        otherPayment.setStatus("Draft Cancelled Invoice");
        otherPaymentRepository.save(otherPayment);
    }
    public  String makeCancelledGtExpense(String formNo){
        String status= null;
        String failedmsg="Canot Able To  Cancel Invoice Please Try Once Again Or Please Contact Your Service Provider ";
        String success="Invoice Cancelled SuccessFully";
        status=cancelGtExpenseInvoice(formNo,success,status);
        return status;
    }
    public String cancelGtExpenseInvoice(String formNo,String success,String status){
        OtherPayment otherPayment=otherPaymentRepository.findByOpNo(formNo);
        List<OcOtherPaymentWithoutItem> invoiceDetails = ocOtherPaymentWOItemRep.findByOcdetails(otherPayment);
        PosExpensePaymentTypes posExpensePaymentTypes =posExpensePayRepository.findByOtherPayment(otherPayment);
        writingGtforCancelPurchaseLedger(otherPayment,invoiceDetails ,posExpensePaymentTypes);
        status = success;
        return status;
    }
    public void writingGtforCancelPurchaseLedger( OtherPayment otherPayment ,
                                                  List<OcOtherPaymentWithoutItem> pidList,  PosExpensePaymentTypes opgtDTO) {
        double totalPaymentBankAmt=0;
        double totalPaymentCashAmt=0;
        double totalPaymentVoucherAmt=0;
        Map map = new HashMap();
        FormSetUp formSetUp = getFooter("OtherPurchase");
        AccountMaster accountSetup = accountMasterRepository.findByAccountname("Petty Cash");
        if (opgtDTO.getTotalCashPayment()>0) {
            createGeneralLedgerForGTPurchase(otherPayment.getOpDate(),formSetUp, otherPayment.getOpNo(), accountSetup, "CancelledGTExpense", new BigDecimal(opgtDTO.getTotalCashPayment()), otherPayment.getReferNo(),otherPayment.getSupplierName(),valueOf(1),valueOf(1));
            totalPaymentCashAmt= otherPayment.getAmountPaid().doubleValue();
            map.put("42/200", -totalPaymentCashAmt);//Cash Ledger
        }
        if(opgtDTO.getTotalBankAmt()>0) {
            Gson json1 = new Gson();
            Type bankType = new TypeToken<ArrayList<MultiBankPayment>>() {}.getType();
            List<MultiBankPayment> multiBankPayment=json1.fromJson(opgtDTO.getBankPayment(), bankType);
            for (MultiBankPayment bankPayment : multiBankPayment) {
//                if (!bankPayment.getDate().after(new Date())) {
                AccountMaster bankPaymentACC = accountMasterRepository.findOne(bankPayment.getBankAccountId());
                createGeneralLedgerForGTPurchase(otherPayment.getOpDate(),formSetUp, otherPayment.getOpNo(),bankPaymentACC, "CancelledGTExpense", new BigDecimal(bankPayment.getAmount()),otherPayment.getReferNo(),otherPayment.getSupplierName(),valueOf(1),valueOf(1));
                totalPaymentBankAmt=otherPayment.getAmountPaid().doubleValue();
                map.put("42/100", -totalPaymentBankAmt);//Bank Ledger
//                }
            }
        }
        if(opgtDTO.getTotalCardPayment()>0) {
            createGeneralLedgerForGTPurchase(otherPayment.getOpDate(),formSetUp, otherPayment.getOpNo(), accountSetup, "CancelledGTExpense", new BigDecimal(opgtDTO.getTotalCardPayment()),otherPayment.getReferNo(),otherPayment.getSupplierName(),valueOf(1),valueOf(1));
            totalPaymentCashAmt= otherPayment.getAmountPaid().doubleValue();
            totalPaymentBankAmt=otherPayment.getAmountPaid().doubleValue();
            map.put("42/100", -totalPaymentBankAmt);//Bank Ledger
        }
        if(opgtDTO.getTotalVoucherPayment()>0) {
            AccountMaster accountMaster = accountMasterRepository.findByAccountname("Discount Received");
            createGeneralLedgerForGTPurchase(otherPayment.getOpDate(),formSetUp, otherPayment.getOpNo(),accountMaster, "CancelledGTExpense", new BigDecimal(opgtDTO.getTotalVoucherPayment()),otherPayment.getReferNo(),otherPayment.getSupplierName(),valueOf(1),valueOf(1));
            totalPaymentVoucherAmt= otherPayment.getAmountPaid().doubleValue();
            map.put("62", -totalPaymentVoucherAmt);//Bank Ledger
        }
        for (OcOtherPaymentWithoutItem pd : pidList) {
            totalPaymentCashAmt= otherPayment.getAmountPaid().doubleValue();
            createGeneralLedgerForGTPurchase(otherPayment.getOpDate(), formSetUp, otherPayment.getOpNo(), pd.getAccountid(), "CancelledGTExpense", (pd.getAmountIncludeTax()).negate(),otherPayment.getReferNo(),otherPayment.getSupplierName(),valueOf(1),valueOf(1));
            String[] str =  pd.getAccountid().getStringAccountCode().split("/");
            String fourDigit = str[0];
            String threeDigit = str[1];
            map.put(fourDigit, (pd.getAmountIncludeTax().abs().subtract((pd.getTaxAmount()))));
            map.put("46",  pd.getTaxAmount());//Tax Ledger Amt
        }
        otherPayment.setStatus("Cancelled Invoice");
        String invno=otherPayment.getOpNo();
        List<OtherPayment> otherPayment1=otherPaymentRepository.findAllByOpNo(invno);
        otherPayment.setOpNo(otherPayment.getOpNo()+"c"+otherPayment1.size());
        otherPayment.setStatus(otherPayment.getStatus());
        otherPaymentRepository.save(otherPayment1);
    }
    public String cancelGtforSales(String invoiceNo){
        cancelGtSales(invoiceNo);
        return null;
    }
    @Transactional
    public void cancelGtSales(String invoiceNo){
        OtherReceipt otherReceipt=otherReceiptRepository.findByOrno(invoiceNo);
        List<OtherReceipt> data=otherReceiptRepository.findAllByOrno(otherReceipt.getOrno());
        otherReceipt.setOrno(otherReceipt.getOrno()+"c"+data.size());
        otherReceipt.setStatus("Cancelled Invoice");
        otherReceiptRepository.save(otherReceipt);
    }

    @Transactional
    public JournalEntryDTO createJournalEntryNPayment(JournalEntryDTO jegtDTO, int userId) throws IOException, JSONException {
        FormSetUp formSetUp = null;
        if (StringUtils.isEmpty(jegtDTO.getJeNo())) {
            formSetUp = getFormSetUp("JournalEntry");
        }
        String failedmsg = "Canot Able To  Cancel Invoice Please Try Once Again Or Please Contact Your Service Provider ";
        String success = "GtPurchase has been Modified SuccessFully";
        JournalEntryDTO printDTO = new JournalEntryDTO();
        JournalEntry journalEntry = createJournalEntry(jegtDTO, formSetUp);
        List<JournalEntryDetails> jeList = createJournalEntryDetails(jegtDTO, journalEntry);
        for (JournalEntryDetails invoiceDetails : jeList) {
            journalEntryDetailsRepository.save(invoiceDetails);
        }
        if (formSetUp != null) {
            formSetupRepository.save(formSetUp);
        }
        if (StringUtils.pathEquals(jegtDTO.getStatus(), "Prepared")) {
            writingJournalEntryLedger(journalEntry, jegtDTO, jeList);
        }
        printDTO = getPrintDetailsJournalEntry(jegtDTO, journalEntry, jeList);
        printDTO.setStatus(success);
        return printDTO;
    }
    public JournalEntryDTO retrieveJREForEditPopulate(String invoiceNo) {
        JournalEntry  journalEntry = journalEntryRepository.findByJeNo(invoiceNo);
        System.out.println("invoice found with number "+invoiceNo+" -> "+journalEntry);
        JournalEntryDTO journalEntryDTO = new JournalEntryDTO();
        ArrayList<AccountMasterDTO> selectedAccounts = new ArrayList<>();
        List<JournalEntryDetails> accountList = journalEntryDetailsRepository.findAllByJeId(journalEntry);
        for(JournalEntryDetails journalEntryDetails:accountList){
            AccountMasterDTO accountMasterDTO=new AccountMasterDTO();
            accountMasterDTO.setAccountname(journalEntryDetails.getAccountid().getAccountname());
            accountMasterDTO.setAccountcode(journalEntryDetails.getAccountid().getAccountcode());
            accountMasterDTO.setAccountDescription(journalEntryDetails.getRemark());
            accountMasterDTO.setDebit(journalEntryDetails.getDebit());
            accountMasterDTO.setCredit(journalEntryDetails.getCredit());
            accountMasterDTO.setDebitAmount(journalEntryDetails.getDebit().toString());
            accountMasterDTO.setCreditAmount(journalEntryDetails.getCredit().toString());
            accountMasterDTO.setFormNo(journalEntryDetails.getFormNo());
            selectedAccounts.add(accountMasterDTO);
        }

        journalEntryDTO.setSelectedAccountList(selectedAccounts);
        journalEntryDTO.setSelectedAccountList(selectedAccounts);
        journalEntryDTO.setJeNo(invoiceNo);
        journalEntryDTO.setStatus("Draft");
        journalEntryDTO.setJeNo(journalEntry.getJeNo());
        journalEntryDTO.setJeId(journalEntry.getJeId());
        journalEntryDTO.setStatus(journalEntry.getStatus());
        journalEntryDTO.setJeDate(journalEntry.getJeDate());
        journalEntryDTO.setMemo(journalEntry.getMemo());
        journalEntryDTO.setMemo(journalEntry.getMemo());
        return journalEntryDTO;
    }
    public JournalEntryDTO postJournalEntry(String invoice){
        JournalEntry journalEntry=journalEntryRepository.findByJeNo(invoice);
        List<JournalEntryDetails> pidList=journalEntryDetailsRepository.findAllByJeId(journalEntry);
        JournalEntryDTO journalEntryDTO=duplicateJournalEntryPrint(journalEntry.getJeNo());
        journalEntry.setStatus("Prepared");
        writingJournalEntryLedger(journalEntry, journalEntryDTO, pidList);
        journalEntryRepository.save(journalEntry);
        return journalEntryDTO;
    }
}
