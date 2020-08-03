package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.AirPayDetails;
import com.hyva.restopos.rest.entities.InvoicePayDetails;
import com.hyva.restopos.rest.pojo.AirPayDetailsPojo;
import com.hyva.restopos.rest.pojo.InvoicePayDetailsPojo;

public class OnlineInvoiceMapper {

    public static AirPayDetailsPojo MapAirPayEntitytoPOJO(AirPayDetails airPayDetails){
        AirPayDetailsPojo pojo = new AirPayDetailsPojo();
        pojo.setAirPayId(airPayDetails.getAirPayId());
        pojo.setTransactionID(airPayDetails.getTransactionID());
        pojo.setApTransactionId(airPayDetails.getApTransactionId());
        pojo.setInvoiceNo(airPayDetails.getInvoiceNo());
        pojo.setMerchantId(airPayDetails.getMerchantId());
        pojo.setTerminalId(airPayDetails.getTerminalId());
        pojo.setCardIssuer(airPayDetails.getCardIssuer());
        pojo.setChMod(airPayDetails.getChMod());
        pojo.setCurrenctCode(airPayDetails.getCurrenctCode());
        pojo.setTransactionStatus(airPayDetails.getTransactionStatus());
        pojo.setMessage(airPayDetails.getMessage());
        pojo.setRrn(airPayDetails.getRrn());
        pojo.setAuthCode(airPayDetails.getAuthCode());
        pojo.setCustomer(airPayDetails.getCustomer());
        pojo.setCustomerPhone(airPayDetails.getCustomerPhone());
        pojo.setCustomerEmail(airPayDetails.getCustomerEmail());
        pojo.setIsRisk(airPayDetails.getIsRisk());
        pojo.setIpnId(airPayDetails.getIpnId());
        pojo.setBilledAmount(airPayDetails.getBilledAmount());
        pojo.setToken(airPayDetails.getToken());
        pojo.setSurCharge(airPayDetails.getSurCharge());
        pojo.setCardType(airPayDetails.getCardType());
        pojo.setTransactionTime(airPayDetails.getTransactionTime());
        pojo.setCcExpiry(airPayDetails.getCcExpiry());
        pojo.setCustomVar(airPayDetails.getCustomVar());
        pojo.setWalletBalance(airPayDetails.getWalletBalance());
        pojo.setCardCategory(airPayDetails.getCardCategory());
        pojo.setAp_SecureHash(airPayDetails.getAp_SecureHash());
        pojo.setCardUniqueCode(airPayDetails.getCardUniqueCode());
        pojo.setBankName(airPayDetails.getBankName());
        pojo.setCardCountry(airPayDetails.getCardCountry());
        pojo.setTxnMode(airPayDetails.getTxnMode());
        pojo.setPosEntryMode(airPayDetails.getPosEntryMode());
        pojo.setMerchantName(airPayDetails.getMerchantName());
        return pojo;
    }


    public static InvoicePayDetailsPojo MapOLINVEntitytoPOJO(InvoicePayDetails details){
        InvoicePayDetailsPojo pojo = new InvoicePayDetailsPojo();
        pojo.setInvoicePayId(details.getInvoicePayId());
        pojo.setTransactionPaymentStatus(details.getTransactionPaymentStatus());
        pojo.setTransactionId(details.getTransactionId());
        pojo.setApTransactionId(details.getApTransactionId());
        pojo.setTxn_mode(details.getTxn_mode());
        pojo.setChMod(details.getChMod());
        pojo.setAmount(details.getAmount());
        pojo.setCurrencyCode(details.getCurrencyCode());
        pojo.setTransactionStatus(details.getTransactionStatus());
        pojo.setMessage(details.getMessage());
        pojo.setCustomer(details.getCustomer());
        pojo.setCustomerEmail(details.getCustomerEmail());
        pojo.setCustomerPhone(details.getCustomerPhone());
        pojo.setCustomerVar(details.getCustomerVar());
        pojo.setTransactionType(details.getTransactionType());
        pojo.setRisk(details.getRisk());
        pojo.setTransactionTime(details.getTransactionTime());
        pojo.setBilledAmount(details.getBilledAmount());
        pojo.setCardIssuer(details.getCardIssuer());
        pojo.setBankName(details.getBankName());
        pojo.setCardCountry(details.getCardCountry());
        pojo.setCardType(details.getCardType());
        pojo.setMerchant_Name(details.getMerchant_Name());
        pojo.setAp_SecureHash(details.getAp_SecureHash());
        pojo.setCardUniqueCode(details.getCardUniqueCode());

        return pojo;
    }
}
