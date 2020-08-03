package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.City;
import com.hyva.restopos.rest.entities.Country;
import com.hyva.restopos.rest.entities.State;
import com.hyva.restopos.rest.pojo.CityPojo;
import com.hyva.restopos.rest.pojo.CountryPojo;
import com.hyva.restopos.rest.pojo.StatePojo;

import java.util.ArrayList;
import java.util.List;

public class CityMapper {

    public static City MapPojoToEntity(CityPojo cityPojo){
        City city = new City();
        city.setId(cityPojo.getId());
        city.setStateid(cityPojo.getState_id());
        city.setName(cityPojo.getName());
        city.setCountryid(cityPojo.getCountry_id());
        city.setStatus(cityPojo.getStatus());
        return city;
    }

    public static List<CityPojo> MapCityEntityToPojo(List<City> List) {
        List<CityPojo> list = new ArrayList<>();
        for (City city : List) {
            CityPojo cityPojo = new CityPojo();
            cityPojo.setId(city.getId());
            cityPojo.setState_id(city.getStateid());
            cityPojo.setName(city.getName());
            cityPojo.setCountry_id(city.getCountryid());
            cityPojo.setStatus(city.getStatus());
            list.add(cityPojo);
        }
        return list;
    }



}
