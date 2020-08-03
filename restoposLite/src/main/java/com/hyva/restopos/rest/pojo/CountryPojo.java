package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class CountryPojo {
    private Long country_id;
    private String country_name;
    private String status;
    private String Restaurant_id;
}
