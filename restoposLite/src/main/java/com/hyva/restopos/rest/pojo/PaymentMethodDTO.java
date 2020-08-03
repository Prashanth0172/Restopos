package com.hyva.restopos.rest.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethodDTO {
    private Long paymentmethodId;
    private String paymentmethodName;
    private String paymentmethodDescription;
    private String paymentmethodType;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}