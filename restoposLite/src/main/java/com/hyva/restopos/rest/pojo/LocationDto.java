package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class LocationDto {
    private String hiconnectNo;

    public String getHiconnectNo() {
        return hiconnectNo;
    }

    public void setHiconnectNo(String hiconnectNo) {
        this.hiconnectNo = hiconnectNo;
    }
}
