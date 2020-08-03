package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.KitchenMaster;
import com.hyva.restopos.rest.pojo.KitchenMasterPOJO;

import java.util.ArrayList;
import java.util.List;

public class KitchenMapper {
    public static KitchenMaster MapPojoToEntity(KitchenMasterPOJO kitchenMasterPOJO){
        KitchenMaster kitchenMaster = new KitchenMaster();
        kitchenMaster.setKitchenId(kitchenMasterPOJO.getKitchenId());
        kitchenMaster.setKitchenName(kitchenMasterPOJO.getKitchenName());
        kitchenMaster.setDescription(kitchenMasterPOJO.getDescription());
        kitchenMaster.setStatus(kitchenMasterPOJO.getStatus());
        return kitchenMaster;
    }

    public static List<KitchenMasterPOJO> mapEntityToPojo(List<KitchenMaster> List) {
        List<KitchenMasterPOJO> list = new ArrayList<>();
        for (KitchenMaster kitchenMaster : List) {
            KitchenMasterPOJO pojo = new KitchenMasterPOJO();
            pojo.setKitchenId(kitchenMaster.getKitchenId());
            pojo.setStatus(kitchenMaster.getStatus());
            pojo.setKitchenName(kitchenMaster.getKitchenName());
            pojo.setDescription(kitchenMaster.getDescription());
            list.add(pojo);
        }
        return list;
    }
}
