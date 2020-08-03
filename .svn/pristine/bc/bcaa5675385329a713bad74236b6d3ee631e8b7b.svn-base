package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.CustomerNotifications;
import com.hyva.restopos.rest.pojo.CustomerNotificationsPojo;

import java.util.ArrayList;
import java.util.List;

public class CustomerNotificationMapper {



    public static List<CustomerNotificationsPojo> mapEntityToPojo(List<CustomerNotifications> List) {
        List<CustomerNotificationsPojo> list = new ArrayList<>();
        for (CustomerNotifications customerNotifications : List) {
            CustomerNotificationsPojo customerNotificationsPojo = new CustomerNotificationsPojo();
            customerNotificationsPojo.setCustNotiId(customerNotifications.getCustNotiId());
            customerNotificationsPojo.setFromRegno(customerNotifications.getFromRegno());
            customerNotificationsPojo.setToRegno(customerNotifications.getToRegno());
            customerNotificationsPojo.setWaiter(customerNotifications.getWaiter());
            customerNotificationsPojo.setTableName(customerNotifications.getTableName());
            customerNotificationsPojo.setStatus(customerNotifications.getStatus());
            list.add(customerNotificationsPojo);
        }
        return list;
    }
}
