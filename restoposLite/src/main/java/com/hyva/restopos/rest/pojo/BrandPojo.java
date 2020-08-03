package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class BrandPojo {
    private Long brandId;
    private String brandName;
    private String brandDescription;

    private String locationId;

    private String useraccount_id;
    private String status;
}
