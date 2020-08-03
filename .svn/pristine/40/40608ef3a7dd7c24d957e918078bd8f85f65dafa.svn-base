package com.hyva.restopos.rest.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Pusher  extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long PusherId;
    @Column(name = "a0")
    private boolean active;
    @Column(name = "a1", columnDefinition = "mediumblob")
    private String typeDoc;
    @Column(name = "a2", columnDefinition = "mediumblob")
    private String Url;
    @Column(name = "a3", columnDefinition = "mediumblob")
    private String kafkaServerAddress;
    @Column(name = "a4", columnDefinition = "mediumblob")
    private String kafkaConsumerGroupId;
    @Column(name = "a5", columnDefinition = "mediumblob")
    private String kafkaTopic;
    @Column(name = "a6", columnDefinition = "mediumblob")
    private String HttpMethodType;
    @Column(name = "a7", columnDefinition = "mediumblob")
    private String jsonData;


    public Long getPusherId() {
        return PusherId;
    }

    public void setPusherId(Long pusherId) {
        PusherId = pusherId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getKafkaServerAddress() {
        return kafkaServerAddress;
    }

    public void setKafkaServerAddress(String kafkaServerAddress) {
        this.kafkaServerAddress = kafkaServerAddress;
    }

    public String getKafkaConsumerGroupId() {
        return kafkaConsumerGroupId;
    }

    public void setKafkaConsumerGroupId(String kafkaConsumerGroupId) {
        this.kafkaConsumerGroupId = kafkaConsumerGroupId;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public String getHttpMethodType() {
        return HttpMethodType;
    }

    public void setHttpMethodType(String httpMethodType) {
        HttpMethodType = httpMethodType;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
