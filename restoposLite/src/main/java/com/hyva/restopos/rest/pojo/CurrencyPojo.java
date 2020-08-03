package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class CurrencyPojo {
    private Long currency_id;
    private String currency_name;
    private Long country_id;
    private String country_name;
    private String currency_code;
    private String currency_symbol;
    private String currency_status;
    private String status;

}

