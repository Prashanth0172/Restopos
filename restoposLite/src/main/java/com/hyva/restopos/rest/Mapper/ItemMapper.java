package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Item;
import com.hyva.restopos.rest.pojo.AddNewItemDTO;
import com.hyva.restopos.rest.pojo.ItemPojo;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static Item mapPojoToEntity(ItemPojo itemPojo){
        Item item = new Item();
        item.setItemName(itemPojo.getMenu_name());
        item.setMenu_price(itemPojo.getMenu_price());
        item.setItemId(itemPojo.getMenu_id());
        item.setItemtype(itemPojo.getItemType());
        item.setStock(itemPojo.getStock_qty());
        if(itemPojo.getImage()!=null) {
            item.setImageFile(itemPojo.getImage());
        }
        item.setItemStatus(itemPojo.getMenu_status());
        item.setCategoryId(itemPojo.getMenu_category_id());
        item.setShiftId(itemPojo.getMealtime_id());
        item.setItemDesc(itemPojo.getMenu_description());
        item.setMenu_category_id(itemPojo.getMenu_category_id());
        item.setMealtime_id(itemPojo.getMealtime_id());
        item.setTaxpercent(itemPojo.getTaxpercent());
        if(itemPojo.getTaxtype() == "true"){
            item.setTaxtype("Inclusive");
        }else{
            item.setTaxtype("Exclusive");
        }
        return item;
    }

    public static List<ItemPojo> mapItemEntityToPojo(List<Item> List) {
        List<ItemPojo> list = new ArrayList<>();
        for (Item config : List) {
            ItemPojo itemPojo = new ItemPojo();
            itemPojo.setMenu_id(config.getItemId());
            itemPojo.setMenu_name(config.getItemName());
            itemPojo.setMenu_description(config.getItemDesc());
            itemPojo.setMenu_status(config.getItemStatus());
            itemPojo.setItemType(config.getItemtype());
            itemPojo.setImage(config.getImageFile());
            itemPojo.setMenu_price(config.getMenu_price());
            itemPojo.setMenu_category_id(config.getMenu_category_id());
            itemPojo.setMealtime_id(config.getMealtime_id());
            itemPojo.setTaxpercent(config.getTaxpercent());
            itemPojo.setTaxtype(config.getTaxtype());
            itemPojo.setMealtime_id(config.getMealtime_id());
            itemPojo.setMenu_price(config.getMenu_price());
            list.add(itemPojo);
        }
        return list;
    }

}
