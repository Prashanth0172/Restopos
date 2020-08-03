package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Company;
import com.hyva.restopos.rest.entities.Country;
import com.hyva.restopos.rest.entities.Discount;
import com.hyva.restopos.rest.pojo.CompanyDto;
import com.hyva.restopos.rest.pojo.CountryPojo;
import com.hyva.restopos.rest.pojo.DiscountPojo;

import java.util.ArrayList;
import java.util.List;

public class DiscountMapper {

    public static Discount MapDiscountPojoToEntity(DiscountPojo discountPojo){
        Discount discount = new Discount();
        discount.setId(discountPojo.getId());
        discount.setMenuid(discountPojo.getMenu_id());
        discount.setDiscounttype(discountPojo.getDiscount_type());
        discount.setDiscount(discountPojo.getDiscount());
        discount.setFromdate(discountPojo.getFrom_date());
        discount.setTodate(discountPojo.getTo_date());
        discount.setFromtime(discountPojo.getFrom_time());
        discount.setTotime(discountPojo.getTo_time());
        discount.setStatus(discountPojo.getStatus());
        return discount;
    }

    public static List<DiscountPojo> mapdiscountEntityToPojo(List<Discount> List) {
        List<DiscountPojo> list = new ArrayList<>();
        for (Discount discount : List) {
            DiscountPojo discountPojo = new DiscountPojo();
            discountPojo.setId(discount.getId());
            discountPojo.setMenu_id(discount.getMenuid());
            discountPojo.setDiscount_type(discount.getDiscounttype());
            discountPojo.setDiscount(discount.getDiscount());
            discountPojo.setFrom_date(discount.getFromdate());
            discountPojo.setTo_date(discount.getTodate());
            discountPojo.setFrom_time(discount.getFromtime());
            discountPojo.setTo_time(discount.getTotime());
            discountPojo.setStatus(discount.getStatus());
            list.add(discountPojo);
        }
        return list;
    }



}
