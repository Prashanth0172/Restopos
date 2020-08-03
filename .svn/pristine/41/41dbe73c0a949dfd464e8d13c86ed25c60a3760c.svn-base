package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.DeliveryArea;
import com.hyva.restopos.rest.pojo.DeliveryAreaPojo;

import java.util.ArrayList;
import java.util.List;


public class DeliveryMapper {

    public static DeliveryArea mapPojoToEntity (DeliveryAreaPojo deliveryAreaPojo){
        DeliveryArea deliveryArea = new DeliveryArea();
        deliveryArea.setId(deliveryAreaPojo.getId());
        deliveryArea.setName(deliveryAreaPojo.getName());
        deliveryArea.setStatus(deliveryAreaPojo.getStatus());
        deliveryArea.setType(deliveryAreaPojo.getType());
        return deliveryArea;
    }

    public static List<DeliveryAreaPojo> mapEntityToPojo(List<DeliveryArea> List) {
        List<DeliveryAreaPojo> list = new ArrayList<>();
        for (DeliveryArea deliveryArea : List) {
            DeliveryAreaPojo deliveryAreaPojo = new DeliveryAreaPojo();
            deliveryAreaPojo.setId(deliveryArea.getId());
            deliveryAreaPojo.setName(deliveryArea.getName());
            deliveryAreaPojo.setStatus(deliveryArea.getStatus());
            deliveryAreaPojo.setType(deliveryArea.getType());
            list.add(deliveryAreaPojo);
        }
        return list;
    }

}
