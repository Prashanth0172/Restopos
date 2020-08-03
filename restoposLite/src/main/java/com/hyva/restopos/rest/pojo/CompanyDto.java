package com.hyva.restopos.rest.pojo;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author sarita.a
 */
public class CompanyDto implements Serializable {
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
    private String locationName;
    private String connectNo;
    private String gstRegister;
    private String gstNo;
    private String fileName;
    private String panNumber;
    private String pincode;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
