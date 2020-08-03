package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class POSKOTItemOrderDTO {

    int  uomValue;
    double itemQty;

    String tableName;
    String itemName;
    String type;
    String itemCategoryName;
    String uomConvertedName;
    String itemDescription;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemQty() {
        return itemQty;
    }

    public void setItemQty(double itemQty) {
        this.itemQty = itemQty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public int getUomValue() {
        return uomValue;
    }

    public void setUomValue(int uomValue) {
        this.uomValue = uomValue;
    }

    public String getUomConvertedName() {
        return uomConvertedName;
    }

    public void setUomConvertedName(String uomConvertedName) {
        this.uomConvertedName = uomConvertedName;
    }

    public String getItemDescription() {return itemDescription;}

    public void setItemDescription(String itemDescription) {this.itemDescription = itemDescription;}
}
