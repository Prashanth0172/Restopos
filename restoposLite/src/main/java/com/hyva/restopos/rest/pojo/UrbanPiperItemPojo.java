package com.hyva.restopos.rest.pojo;

import lombok.Data;

import java.util.List;

@Data
public class UrbanPiperItemPojo {
    private String ref_id;
    private String title;
    private boolean sold_at_store;
    private boolean available;
    private String description;
    private float price;
    private int current_stock;
    private String food_type;
    private Long sort_order;
    private boolean recommended;
    private int weight;
    private String img_url;
    private List<String> category_ref_ids;

    public List<String> getCategory_ref_ids() {
        return category_ref_ids;
    }

    public void setCategory_ref_ids(List<String> category_ref_ids) {
        this.category_ref_ids = category_ref_ids;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSold_at_store() {
        return sold_at_store;
    }

    public void setSold_at_store(boolean sold_at_store) {
        this.sold_at_store = sold_at_store;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCurrent_stock() {
        return current_stock;
    }

    public void setCurrent_stock(int current_stock) {
        this.current_stock = current_stock;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public Long getSort_order() {
        return sort_order;
    }

    public void setSort_order(Long sort_order) {
        this.sort_order = sort_order;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}