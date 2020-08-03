package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Currency;
import com.hyva.restopos.rest.pojo.CurrencyPojo;

import java.util.ArrayList;
import java.util.List;

public class CurrecncyMapper {

    public static Currency  mapPojoToEntity (CurrencyPojo currencyPojo){
        Currency currency = new Currency();
        currency.setCurrencyId(currencyPojo.getCurrency_id());
        currency.setCountry_id(currencyPojo.getCountry_id());
        currency.setCurrencyName(currencyPojo.getCurrency_name());
        currency.setCurrencyCode(currencyPojo.getCurrency_code());
        currency.setCurrencySymbol(currencyPojo.getCurrency_symbol());
        currency.setStatus(currencyPojo.getCurrency_status());
        return currency;
    }

    public static List<CurrencyPojo> mapEntityToPojo(List<Currency> List) {
        List<CurrencyPojo> list = new ArrayList<>();
        for (Currency config : List) {
            CurrencyPojo currencyPojo = new CurrencyPojo();
            currencyPojo.setCurrency_id(config.getCurrencyId());
            currencyPojo.setCountry_id(config.getCountry_id());
            currencyPojo.setCurrency_name(config.getCurrencyName());
            currencyPojo.setCurrency_code(config.getCurrencyCode());
            currencyPojo.setCurrency_symbol(config.getCurrencySymbol());
            currencyPojo.setCurrency_status(config.getStatus());
            list.add(currencyPojo);
        }
        return list;
    }

}
