package com.hyva.restopos.util;

import com.hyva.restopos.rest.entities.SMSServer;
import com.hyva.restopos.rest.repository.SmsServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SmsService {
    @Autowired
    SmsServerRepository smsServerRepository;

    public SMSServer getSmsServer(Long id){
        SMSServer smsServer=smsServerRepository.findAllById(id);
        return smsServer;
    }
    public  String sendSmsRestaurant(String amt, String customerName, String mobileNumber,String invNo) throws IOException {
        String sentance = "Hi "+customerName+","+"Your total bill for the Invoice No "+invNo+" is " + amt + "Rs                                 Thank You!!!Visit Again           "+"";
        SMSServer smsServer = new SMSServer();
        smsServer=getSmsServer(1L);
        if(smsServer!=null) {
            URL url = new URL( smsServer.getServerUrl() + "?method=sms" + "&api_key=" + smsServer.getApiKey() + "&to=" + mobileNumber + "&sender=" + smsServer.getSenderId() + "&message=" + sentance);
            URLConnection conn = url.openConnection();
            conn.getInputStream();
        }
        return null;
    }
    public  String sendSmsRestaurantCancelled(String amt, String customerName, String mobileNumber,String invNo) throws IOException {
        String sentance = "Hi "+customerName+","+"Your total bill for the Invoice No "+invNo+" is Cancelled " + amt + "Rs        "+"";
        SMSServer smsServer = new SMSServer();
        smsServer=getSmsServer(1L);
        if(smsServer!=null) {
            URL url = new URL( smsServer.getServerUrl() + "?method=sms" + "&api_key=" + smsServer.getApiKey() + "&to=" + mobileNumber + "&sender=" + smsServer.getSenderId() + "&message=" + sentance);
            URLConnection conn = url.openConnection();
            conn.getInputStream();
        }
        return null;
    }
    public  String sendSmsRestaurantForKot(String customerName, String mobileNumber,String tokenNo,String steward,String description) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy @hh:mm a");
        String date = simpleDateFormat.format(new Date());
        String sentance = "Hi, "+""+", "+"Kot No. "+"'"+tokenNo+"' "+"was Cancelled by"+" "+steward+"."+" On"+" "+date+"                               "+"For:"+description;
        SMSServer smsServer = new SMSServer();
        smsServer=getSmsServer(1L);
        if(smsServer!=null) {
            URL url = new URL( smsServer.getServerUrl() + "?method=sms" + "&api_key=" + smsServer.getApiKey() + "&to=" + mobileNumber + "&sender=" + smsServer.getSenderId() + "&message=" + sentance);
            URLConnection conn = url.openConnection();
            conn.getInputStream();
        }
        return null;
    }


}
