/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity

public class PaymentMethod implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long paymentmethodId;
    private String paymentmethodName;
    private String paymentmethodDescription;
    private String paymentmethodType;
    private String status;
    private String defaultType;
    @OneToOne
    private CompanyInfo companyId;
    private String locationId;
    private String useraccount_id;

//    private AccountMaster accountMaster;
    private String validateVoucher;
    private String merchantId;
    private String uniqueId;
    private String secretKey;
    public String getValidateVoucher() {
        return validateVoucher;
    }

    public void setValidateVoucher(String validateVoucher) {
        this.validateVoucher = validateVoucher;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getPaymentmethodId() {
        return paymentmethodId;
    }

    public void setPaymentmethodId(Long paymentmethodId) {
        this.paymentmethodId = paymentmethodId;
    }

    public String getPaymentmethodName() {
        return paymentmethodName;
    }

    public void setPaymentmethodName(String paymentmethodName) {
        this.paymentmethodName = paymentmethodName;
    }

    public String getPaymentmethodDescription() {
        return paymentmethodDescription;
    }

    public void setPaymentmethodDescription(String paymentmethodDescription) {
        this.paymentmethodDescription = paymentmethodDescription;
    }

    public String getPaymentmethodType() {
        return paymentmethodType;
    }

    public void setPaymentmethodType(String paymentmethodType) {
        this.paymentmethodType = paymentmethodType;
    }

    public CompanyInfo getCompanyId() {
        return companyId;
    }

    public void setCompanyId(CompanyInfo companyId) {
        this.companyId = companyId;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
