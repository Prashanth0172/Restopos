package com.hyva.restopos.rest.pojo;

import javax.persistence.Column;

public class RestaurantTempDataPojo {
    private Long id;

    private String tableName;
    private String tableId;
    @Column(columnDefinition = "text")
    private String selectedItemsList;
    private long customerId;
    //locationId is used to store employee Id
    private String locationId;
    private String orderNo;
    private String agentId;
    private String useraccount_id;
    @Column(columnDefinition = "text")
    private String removedItemsList;
    private boolean customerBill;
    private String pax;
    private String customerNo;
    private String customerName;


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getSelectedItemsList() {
        return selectedItemsList;
    }

    public void setSelectedItemsList(String selectedItemsList) {
        this.selectedItemsList = selectedItemsList;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getUseraccount_id() {
        return useraccount_id;
    }

    public void setUseraccount_id(String useraccount_id) {
        this.useraccount_id = useraccount_id;
    }

    public String getRemovedItemsList() {
        return removedItemsList;
    }

    public void setRemovedItemsList(String removedItemsList) {
        this.removedItemsList = removedItemsList;
    }

    public boolean isCustomerBill() {
        return customerBill;
    }

    public void setCustomerBill(boolean customerBill) {
        this.customerBill = customerBill;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }
}
