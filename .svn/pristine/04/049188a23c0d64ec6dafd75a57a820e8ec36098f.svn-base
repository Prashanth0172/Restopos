package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Entity
public class CustomerNotifications implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private Long custNotiId;
  private String fromRegno;
  private String toRegno;
  private String status;
  private java.sql.Timestamp timestamp;
  private String typeDoc;
  private String customerNotification;
  private String userId;
  private String toCompname;
  private String toEmail;
  private String contact;
  private String address;
  private String fromCompname;
  private String fromEmail;
  private String fromContact;
  private String fromAddress;
  private String typeFlag;
  private String viewStatus;
  private String gstIn;
  private String state;
  private String country;
  private String currency;
  private String tableid;
  private Long transactionId;
  @Column(name = "objectdata", columnDefinition = "LONGTEXT")
  private String objectdata;
  @Column(name = "completeData", columnDefinition = "LONGTEXT")
  private String completeData;
  @Column(name = "statusChangeData", columnDefinition = "LONGTEXT")
  private String statusChangeData;
  @Column(name = "riderChangeData", columnDefinition = "LONGTEXT")
  private String riderChangeData;
  private Long notificationId;
  private String totaltax;
  private String totalcheckoutamt;
  private String piNo;
  private String siNo;
  private String destinationmap;
  private String restaurantId;
  private String restaurantName;
  private String tableName;
  private String waiter;

}