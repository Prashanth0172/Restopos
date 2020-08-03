/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Nataraj t
 */
@Data
@Entity
public class RestaurantTempData implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String tableName;
    private String tableId;
    @Column(columnDefinition = "longtext")
    private String selectedItemsList;
    private long customerId;
    private String locationId;
    private String orderNo;
    private String agentId;
    private String useraccount_id;
    @Column(columnDefinition = "text")
    private String removedItemsList;
    private boolean customerBill;

}
