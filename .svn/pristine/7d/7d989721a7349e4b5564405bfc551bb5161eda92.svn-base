package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Company;
import com.hyva.restopos.rest.entities.Country;
import com.hyva.restopos.rest.pojo.CompanyDto;
import com.hyva.restopos.rest.pojo.CountryPojo;

import java.util.ArrayList;
import java.util.List;

public class CountryMapper {

    public static Country MapPojoToEntity(CountryPojo countryPojo){
        Country country = new Country();
        country.setCountryId(countryPojo.getCountry_id());
        country.setCountryName(countryPojo.getCountry_name());
        country.setStatus(countryPojo.getStatus());
        return country;
    }

    public static List<CountryPojo> MapcountryEntityToPojo(List<Country> List) {
        List<CountryPojo> list = new ArrayList<>();
        for (Country country : List) {
            CountryPojo countryPojo = new CountryPojo();
            countryPojo.setCountry_id(country.getCountryId());
            countryPojo.setStatus(country.getStatus());
            countryPojo.setCountry_name(country.getCountryName());
            list.add(countryPojo);
        }
        return list;
    }

    public static Company MapCompanyPojoToEntity(CompanyDto companyDto){
        Company company = new Company();
        company.setCompanyId(companyDto.getCompanyId());
        company.setCompanyName(companyDto.getCompanyName());
        company.setCompanyNo(companyDto.getCompanyNo());
        company.setPanNumber(companyDto.getPanNumber());
        company.setIncdate(companyDto.getIncdate());
        company.setAddress(companyDto.getAddress());
        company.setPhone(companyDto.getPhone());
        company.setPincode(companyDto.getPincode());
        company.setFax(companyDto.getFax());
        company.setLang(companyDto.getLang());
        company.setEmail(companyDto.getEmail());
        company.setWeb(companyDto.getWeb());
        company.setCountryId(companyDto.getCountryId());
        company.setStateId(companyDto.getStateId());
        company.setCurrencyId(companyDto.getCurrencyId());
        company.setGstRegister(companyDto.getGstRegister());
        company.setGstNo(companyDto.getGstNo());
        company.setStatus("Active");
        company.setGstRegisteredDate(companyDto.getGstRegisteredDate());
        company.setLocationName(companyDto.getLocationName());
        company.setConnectNo(companyDto.getConnectNo());
        company.setLogo(companyDto.getLogo());
        return company;
    }

}
