/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.pojo;


import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 *
 * @author admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSync {
    public String last_name;
    public String location_name;
    public String order_time;
    public String table_id;
    public String total_items;
    public String location_id;
    public String cart;
    public String notify;
    public String invoice_date;
    public String location_email;
    public String status_id;
    public String payment;
    public String first_name;
    public String order_type;
    public String email;
    public String user_agent;
    public String invoice_no;
    public String address_id;
    private long itemId;
    private double order_total;
    private long customer_id;
    private double quantity;
    List<SelectedItemsSync> selectedItem;

   }
