/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Data
@Entity
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long companyId;
    private String lang;
    private Long countryId;
    private Long stateId;
    private Long currencyId;
    private String companyName;
    private String companyNo;
    private String address;
    private String phone;
    private String fax;
    private String email;
    private String web;
    @Temporal(TemporalType.DATE)
    private Date incdate;
    private String logo;
    @Temporal(TemporalType.DATE)
    private Date gstRegisteredDate;
    private String imageFile;
    private String locationName;
    private String connectNo;
    private String gstRegister;
    private String gstNo;
    private String panNumber;
    private String pincode;
    private String orderId;
    private String status;
    private String createdDate;
    private String expiryDate;
    private String validityStatus;


    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Date getIncdate() {
        return incdate;
    }

    public void setIncdate(Date incdate) {
        this.incdate = incdate;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Date getGstRegisteredDate() {
        return gstRegisteredDate;
    }

    public void setGstRegisteredDate(Date gstRegisteredDate) {
        this.gstRegisteredDate = gstRegisteredDate;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getConnectNo() {
        return connectNo;
    }

    public void setConnectNo(String connectNo) {
        this.connectNo = connectNo;
    }

    public String getGstRegister() {
        return gstRegister;
    }

    public void setGstRegister(String gstRegister) {
        this.gstRegister = gstRegister;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
