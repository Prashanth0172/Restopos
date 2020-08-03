package com.hyva.restopos.rest.pojo;
import lombok.Data;
@Data
public class CustomerPojo {
    private Long customer_id;
    private String Restaurant_id;
    private String location_connect_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String telephone;
    private String address_1;
    private String city;
    private String state;
    private String status;
    private String postcode;
    private String country_id;
    private String countryName;

    public CustomerPojo(Long customer_id, String first_name, String telephone) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.telephone = telephone;
    }

    public CustomerPojo() {
    }


    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
