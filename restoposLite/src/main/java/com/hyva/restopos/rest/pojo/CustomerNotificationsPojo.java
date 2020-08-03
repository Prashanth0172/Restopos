package com.hyva.restopos.rest.pojo;
import lombok.Data;

import java.sql.Timestamp;


@Data
public class CustomerNotificationsPojo {
    private long custNotiId;
    private String fromRegno;
    private String toRegno;
    private String status;
    private Timestamp timestamp;
    private String typeDoc;
    private String customerNotification;
    private String userId;
    private String toCompname;
    private String toEmail;
    private String contact;
    private String address;
    private String fromCompname;
    private String fromEmail;
    private String fromContact;
    private String fromAddress;
    private String typeFlag;
    private String viewStatus;
    private String gstIn;
    private String state;
    private String country;
    private String currency;
    private String objectdata;
    private String orderId;
    private String totalcheckoutamt;
    private String totaltax;
    private String siNo;
    private String piNo;
    private Long transactionId;
    private Long notificationId;
    private String subscriptionType;
    private String destinationmap;
    private String restaurantId;
    private String completeData;
    private String restaurantName;
    private String tableName;
    private String waiter;
    private String tableid;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCompleteData() {
        return completeData;
    }

    public void setCompleteData(String completeData) {
        this.completeData = completeData;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getDestinationmap() {
        return destinationmap;
    }

    public void setDestinationmap(String destinationmap) {
        this.destinationmap = destinationmap;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getPiNo() {
        return piNo;
    }

    public void setPiNo(String piNo) {
        this.piNo = piNo;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getSiNo() {
        return siNo;
    }

    public void setSiNo(String siNo) {
        this.siNo = siNo;
    }

    public String getTotaltax() {
        return totaltax;
    }

    public void setTotaltax(String totaltax) {
        this.totaltax = totaltax;
    }

    public String getTotalcheckoutamt() {
        return totalcheckoutamt;
    }

    public void setTotalcheckoutamt(String totalcheckoutamt) {
        this.totalcheckoutamt = totalcheckoutamt;
    }

    public String getObjectdata() {
        return objectdata;
    }

    public void setObjectdata(String objectdata) {
        this.objectdata = objectdata;
    }

    public long getCustNotiId() {
        return custNotiId;
    }

    public void setCustNotiId(long custNotiId) {
        this.custNotiId = custNotiId;
    }

    public String getFromRegno() {
        return fromRegno;
    }

    public void setFromRegno(String fromRegno) {
        this.fromRegno = fromRegno;
    }

    public String getToRegno() {
        return toRegno;
    }

    public void setToRegno(String toRegno) {
        this.toRegno = toRegno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getCustomerNotification() {
        return customerNotification;
    }

    public void setCustomerNotification(String customerNotification) {
        this.customerNotification = customerNotification;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToCompname() {
        return toCompname;
    }

    public void setToCompname(String toCompname) {
        this.toCompname = toCompname;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFromCompname() {
        return fromCompname;
    }

    public void setFromCompname(String fromCompname) {
        this.fromCompname = fromCompname;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromContact() {
        return fromContact;
    }

    public void setFromContact(String fromContact) {
        this.fromContact = fromContact;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(String typeFlag) {
        this.typeFlag = typeFlag;
    }

    public String getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(String viewStatus) {
        this.viewStatus = viewStatus;
    }

    public String getGstIn() {
        return gstIn;
    }

    public void setGstIn(String gstIn) {
        this.gstIn = gstIn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
