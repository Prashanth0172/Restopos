package com.hyva.restopos.rest.pojo;


public class ZomatoPojo {

    private Long custNotiId;
    private Long orderId;
    private Long externalOrderId;
    private String rejectionMessageId;
    private String deliveryTime;
    private String prepareTime;
    private String riderName;
    private String riderPhoneNo;
    private String vendorRejectionMessage;
    private String customerArrivalTime;
    private String type;
    private String status;
    private String message;
    private String restaurantId;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCustNotiId() {
        return custNotiId;
    }

    public void setCustNotiId(Long custNotiId) {
        this.custNotiId = custNotiId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(Long externalOrderId) {
        this.externalOrderId = externalOrderId;
    }

    public String getRejectionMessageId() {
        return rejectionMessageId;
    }

    public void setRejectionMessageId(String rejectionMessageId) {
        this.rejectionMessageId = rejectionMessageId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(String prepareTime) {
        this.prepareTime = prepareTime;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderPhoneNo() {
        return riderPhoneNo;
    }

    public void setRiderPhoneNo(String riderPhoneNo) {
        this.riderPhoneNo = riderPhoneNo;
    }

    public String getVendorRejectionMessage() {
        return vendorRejectionMessage;
    }

    public void setVendorRejectionMessage(String vendorRejectionMessage) {
        this.vendorRejectionMessage = vendorRejectionMessage;
    }

    public String getCustomerArrivalTime() {
        return customerArrivalTime;
    }

    public void setCustomerArrivalTime(String customerArrivalTime) {
        this.customerArrivalTime = customerArrivalTime;
    }


}
