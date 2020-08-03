package com.hyva.restopos.rest.pojo;

import lombok.Data;


@Data
public class TablesPosPojo {
    private Long table_id;
    private String table_name;
    private Long zone_id;
    private String table_status;
    private String min_capacity;
    private String max_capacity;
    private String Restaurant_id;
    private String location_connect_id;
}
