package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.SMSServer;
import com.hyva.restopos.rest.pojo.SmsServerDto;

public class SmsServerMapper {
    public static SMSServer MapsmsserverPojoToEntity(SmsServerDto smsServerDto){
        SMSServer smsServer = new SMSServer();
        smsServer.setApiKey(smsServerDto.getApiKey());
        smsServer.setMessageToSend(smsServerDto.getMessageToSend());
        smsServer.setSenderId(smsServerDto.getSenderId());
        smsServer.setNotification(smsServerDto.getNotification());
        smsServer.setServerUrl(smsServerDto.getServerUrl());
        smsServer.setStatus(smsServerDto.getStatus());
        smsServer.setId(smsServerDto.getId());
        return smsServer;
    }
}
