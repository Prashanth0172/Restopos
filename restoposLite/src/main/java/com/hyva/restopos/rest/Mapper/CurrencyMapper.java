package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Currency;
import com.hyva.restopos.rest.entities.State;
import com.hyva.restopos.rest.pojo.CurrencyPojo;
import com.hyva.restopos.rest.pojo.StatePojo;

import java.util.ArrayList;
import java.util.List;

public class CurrencyMapper {

    public static Currency MapCurrencyPojoToEntity(CurrencyPojo currencyPojo){
        Currency currency = new Currency();
        currency.setCurrencyId(currencyPojo.getCurrency_id());
        currency.setCurrencyName(currencyPojo.getCurrency_name());
        currency.setCountry_id(currencyPojo.getCountry_id());
        currency.setCurrencyCode(currencyPojo.getCurrency_code());
        currency.setCurrencySymbol(currencyPojo.getCurrency_symbol());
        currency.setStatus(currencyPojo.getCurrency_status());
        return currency;
    }

    public static List<CurrencyPojo> mapCurrencyEntityToPojo(List<Currency> List) {
        List<CurrencyPojo> list = new ArrayList<>();
        for (Currency currency : List) {
            CurrencyPojo currencyPojo = new CurrencyPojo();
            currencyPojo.setCurrency_id(currency.getCurrencyId());
            currencyPojo.setCountry_id(currency.getCountry_id());
            currencyPojo.setStatus(currency.getStatus());
            currencyPojo.setCurrency_name(currency.getCurrencyName());
            list.add(currencyPojo);
        }
        return list;
    }

}
