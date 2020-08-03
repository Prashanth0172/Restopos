package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class EmployeePojo {
    private Long staff_id;
    private String staff_name;
    private String staff_group_id;
    private String staff_status;
    private String username;
    private String password;
    private String staff_email;
    private String incentives;
    private String permission;
    private String Restaurant_id;
    private String location_connect_id;
}
