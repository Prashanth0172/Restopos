package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class AccountGroupPojo {

    private Long accountId;
    private String accountName;
    private String accountDescription;
    private String accountType;
    private String accountCode;
    private String status;

}
