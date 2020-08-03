
package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.CustomerNotifications;
import com.hyva.restopos.rest.entities.Item;
import com.hyva.restopos.rest.pojo.CustomerNotificationsPojo;
import com.hyva.restopos.rest.pojo.ItemPojo;
import com.hyva.restopos.util.FileSystemOperations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class HiposMapper {

    public static CustomerNotifications mapPojoToEntity(CustomerNotificationsPojo customerNotificationsPojo){
        CustomerNotifications customerNotifications=new CustomerNotifications();
//        customerNotifications.setCustNotiId(customerNotificationsPojo.getCustNotiId());
        customerNotifications.setFromRegno(customerNotificationsPojo.getFromRegno());
        customerNotifications.setToRegno(customerNotificationsPojo.getToRegno());
        customerNotifications.setStatus(customerNotificationsPojo.getStatus());
        customerNotifications.setTimestamp(customerNotificationsPojo.getTimestamp());
        customerNotifications.setTypeDoc(customerNotificationsPojo.getTypeDoc());
        customerNotifications.setUserId(customerNotificationsPojo.getUserId());
        customerNotifications.setNotificationId(Long.parseLong(customerNotificationsPojo.getOrderId()));
        customerNotifications.setToCompname(customerNotificationsPojo.getToCompname());
        customerNotifications.setToEmail(customerNotificationsPojo.getToEmail());
        customerNotifications.setContact(customerNotificationsPojo.getContact());
        customerNotifications.setAddress(customerNotificationsPojo.getAddress());
        customerNotifications.setFromCompname(customerNotificationsPojo.getFromCompname());
        customerNotifications.setFromEmail(customerNotificationsPojo.getFromEmail());
        customerNotifications.setFromContact(customerNotificationsPojo.getFromContact());
        customerNotifications.setFromAddress(customerNotificationsPojo.getFromAddress());
        customerNotifications.setTypeFlag(customerNotificationsPojo.getTypeFlag());
        customerNotifications.setViewStatus(customerNotificationsPojo.getViewStatus());
        customerNotifications.setGstIn(customerNotificationsPojo.getGstIn());
        customerNotifications.setState(customerNotificationsPojo.getState());
        customerNotifications.setCountry(customerNotificationsPojo.getCountry());
        customerNotifications.setCurrency(customerNotificationsPojo.getCurrency());
        customerNotifications.setObjectdata(customerNotificationsPojo.getObjectdata());
        customerNotifications.setPiNo(customerNotificationsPojo.getPiNo());
        customerNotifications.setTotaltax(customerNotificationsPojo.getTotaltax());
        customerNotifications.setTotalcheckoutamt(customerNotificationsPojo.getTotalcheckoutamt());
        customerNotifications.setTransactionId(customerNotificationsPojo.getTransactionId());
        customerNotifications.setSiNo(customerNotificationsPojo.getSiNo());
        customerNotifications.setDestinationmap(customerNotificationsPojo.getDestinationmap());
        customerNotifications.setCompleteData(customerNotificationsPojo.getCompleteData());
        customerNotifications.setCustomerNotification(customerNotificationsPojo.getCustomerNotification());
        customerNotifications.setRestaurantId(customerNotificationsPojo.getRestaurantId());
        customerNotifications.setRestaurantName(customerNotificationsPojo.getRestaurantName());
        customerNotifications.setTableName(customerNotificationsPojo.getTableName());
        return customerNotifications;
    }
//    public static List<ItemPojo> mapEntitytoPojo(List<Item> items){
//        List<ItemPojo> itemPojos=new ArrayList<>();
//        for(Item item:items){
//            ItemPojo itemPojo=new ItemPojo();
//            itemPojo.setItemCategoryName(item.getIdItemCategory().getName());
//            itemPojo.setItemId(item.getItemId());
//            itemPojo.setItemCode(item.getItemCode());
//            itemPojo.setItemName(item.getItemName());
//            itemPojo.setItemPrice(item.getItemPrice());
//            itemPojo.setItemDesc(item.getItemDesc());
//            if (!org.apache.commons.lang3.StringUtils.isEmpty(item.getImageFile())) {
//                if (!item.getImageFile().equalsIgnoreCase("")) {
//                    String imageLocation = FileSystemOperations.getImagesDirItem() + File.separator + item.getItemName() + ".png";
//
//                    String fileDirectory = File.separator;
//                    if (fileDirectory.equals("\\"))//Windows OS
//                        imageLocation = imageLocation.substring(imageLocation.indexOf("\\image")).replaceAll("\\\\", "/");
//                    else//Linux or MAC
//                        imageLocation = imageLocation.substring(imageLocation.indexOf("/image"));
//                    itemPojo.setImageFile(imageLocation);
//                }
//            }
////            itemPojo.setImageFile(item.getImageFile());
//            itemPojo.setFoodtype(item.getFoodtype());
//            itemPojo.setItemStatus(item.getItemStatus());
//            itemPojo.setInclusiveJSON(item.getInclusiveJSON());
//            itemPojos.add(itemPojo);
//        }
//        return itemPojos;
//    }


    public static List<CustomerNotificationsPojo> mapEntityToPojo(List<CustomerNotifications> customerNotificationsList){
        List<CustomerNotificationsPojo> customerNotificationsPojoList =new ArrayList<>();
        for (CustomerNotifications customerNotifications:customerNotificationsList){
            CustomerNotificationsPojo customerNotificationsPojo = new CustomerNotificationsPojo();
            customerNotificationsPojo.setFromRegno(customerNotifications.getFromRegno());
            customerNotificationsPojo.setToRegno(customerNotifications.getToRegno());
            customerNotificationsPojo.setStatus(customerNotifications.getStatus());
            customerNotificationsPojo.setTimestamp(customerNotifications.getTimestamp());
            customerNotificationsPojo.setTypeDoc(customerNotifications.getTypeDoc());
            customerNotificationsPojo.setUserId(customerNotifications.getUserId());
            customerNotificationsPojo.setNotificationId(customerNotifications.getNotificationId());
            customerNotificationsPojo.setToCompname(customerNotifications.getToCompname());
            customerNotificationsPojo.setToEmail(customerNotifications.getToEmail());
            customerNotificationsPojo.setAddress(customerNotifications.getAddress());
            customerNotificationsPojo.setFromCompname(customerNotifications.getFromCompname());
            customerNotificationsPojo.setFromEmail(customerNotifications.getFromEmail());
            customerNotificationsPojo.setFromContact(customerNotifications.getFromContact());
            customerNotificationsPojo.setFromAddress(customerNotifications.getFromAddress());
            customerNotificationsPojo.setTypeFlag(customerNotifications.getTypeFlag());
            customerNotificationsPojo.setViewStatus(customerNotifications.getViewStatus());
            customerNotificationsPojo.setGstIn(customerNotifications.getGstIn());
            customerNotificationsPojo.setState(customerNotifications.getState());
            customerNotificationsPojo.setCountry(customerNotifications.getCountry());
            customerNotificationsPojo.setCurrency(customerNotifications.getCurrency());
            customerNotificationsPojo.setObjectdata(customerNotifications.getObjectdata());
            customerNotificationsPojo.setPiNo(customerNotifications.getPiNo());
            customerNotificationsPojo.setTotaltax(customerNotifications.getTotaltax());
            customerNotificationsPojo.setTotalcheckoutamt(customerNotifications.getTotalcheckoutamt());
            customerNotificationsPojo.setTransactionId(customerNotifications.getTransactionId());
            customerNotificationsPojo.setSiNo(customerNotifications.getSiNo());
            customerNotificationsPojo.setDestinationmap(customerNotifications.getDestinationmap());
            customerNotificationsPojo.setCompleteData(customerNotifications.getCompleteData());
            customerNotificationsPojo.setCustomerNotification(customerNotifications.getCustomerNotification());
            customerNotificationsPojo.setRestaurantId(customerNotifications.getRestaurantId());
            customerNotificationsPojo.setRestaurantName(customerNotifications.getRestaurantName());
            customerNotificationsPojoList.add(customerNotificationsPojo);
        }
        return customerNotificationsPojoList;
    }


}
