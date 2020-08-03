package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class ShiftPojo {
    private Long mealtime_id;
    private String mealtime_name;
    private String start_time;
    private String end_time;
    private String mealtime_status;
    private String location_connect_id;
    private String Restaurant_id;



}
