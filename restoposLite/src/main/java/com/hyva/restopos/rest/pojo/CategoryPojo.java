package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class CategoryPojo {
    private Long category_id;
    private String name;
    private String code;
    private String description;
    private String status;
    private String image;
    private String Restaurant_id;
    private String location_connect_id;
}
