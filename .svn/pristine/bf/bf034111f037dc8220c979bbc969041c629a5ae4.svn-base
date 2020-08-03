package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class StaffGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long staffgroupid;
    private String staffgroupname;
    private String permission;
    private String Restaurantid;
    private String status;
    private String locationconnectid;

    public Long getStaffgroupid() {
        return staffgroupid;
    }

    public void setStaffgroupid(Long staffgroupid) {
        this.staffgroupid = staffgroupid;
    }

    public String getStaffgroupname() {
        return staffgroupname;
    }

    public void setStaffgroupname(String staffgroupname) {
        this.staffgroupname = staffgroupname;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRestaurantid() {
        return Restaurantid;
    }

    public void setRestaurantid(String restaurantid) {
        Restaurantid = restaurantid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocationconnectid() {
        return locationconnectid;
    }

    public void setLocationconnectid(String locationconnectid) {
        this.locationconnectid = locationconnectid;
    }
}
