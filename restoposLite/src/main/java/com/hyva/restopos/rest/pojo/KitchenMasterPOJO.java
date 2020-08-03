package com.hyva.restopos.rest.pojo;

public class KitchenMasterPOJO {
    private Long kitchenId;
    private String kitchenName;
    private String description;
    private String status;
    private String Restaurant_id;
    private String location_connect_id;

    public String getRestaurant_id() {
        return Restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        Restaurant_id = restaurant_id;
    }

    public String getLocation_connect_id() {
        return location_connect_id;
    }

    public void setLocation_connect_id(String location_connect_id) {
        this.location_connect_id = location_connect_id;
    }

    public Long getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public String getKitchenName() {
        return kitchenName;
    }

    public void setKitchenName(String kitchenName) {
        this.kitchenName = kitchenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
