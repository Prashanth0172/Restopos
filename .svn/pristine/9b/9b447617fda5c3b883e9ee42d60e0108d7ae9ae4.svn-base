package com.hyva.restopos.rest.entities;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="RestaurantTokenRecord")
public class RestaurantTokenRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long restaurantTokenId;
    @Column(columnDefinition="longtext")
    private String itemDeteails;
    private String tableName;
    private String tableId;
    @OneToOne
    private SalesInvoice siId;
    private String siNo;
    private String token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String time;
    private String status;
    private String useraccount_id;
    private String inventoryLocation;
    private String dayEndStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dayEndDate;
    @Column(columnDefinition = "text")
    private String removedItemsList;
    private String waiterName;
    private String orderNo;
    private String productStatus;
    private Date kitchenTokenStart;
    private Date kitchenTokenEnd;
    private Date waiterTokenEnd;
    @Column(columnDefinition="text")
    private String personalDetails;
    private String pax;
    @Column(columnDefinition="text")
    private String orderRemarks;

}
