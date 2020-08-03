package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class TableConfigPojo {
    private Long id;
    private int no_of_rows;
    private String zone_name;
    private String status;
    private String discription;
    private int no_of_col;
    private String Restaurant_id;
    private String location_connect_id;
}
