package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Customer;
import com.hyva.restopos.rest.pojo.CustomerPojo;

import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {

    public static List<CustomerPojo> mapcustomerEntityToPojo(List<Customer> List) {
        List<CustomerPojo> list = new ArrayList<>();
        for (Customer customer : List) {
            CustomerPojo customerPojo = new CustomerPojo();
            customerPojo.setCustomer_id(customer.getCustomerId());
            customerPojo.setFirst_name(customer.getFirstName());
            customerPojo.setLast_name(customer.getLastName());
            customerPojo.setEmail(customer.getEmail());
            customerPojo.setPassword(customer.getPassword());
            customerPojo.setTelephone(customer.getTelephone());
            customerPojo.setAddress_1(customer.getAddress1());
            customerPojo.setCity(customer.getCity());
            customerPojo.setState(customer.getState());
            customerPojo.setStatus(customer.getStatus());
            customerPojo.setCountry_id(customer.getCountryId());
            customerPojo.setPostcode(customer.getPostcode());
            list.add(customerPojo);
        }
        return list;
    }


    public static Customer MapCustomerPojoToEntity(CustomerPojo customerPojo){
        Customer customer = new Customer();
        customer.setCustomerId(customerPojo.getCustomer_id());
        customer.setFirstName(customerPojo.getFirst_name());
        customer.setLastName(customerPojo.getLast_name());
        customer.setEmail(customerPojo.getEmail());
        customer.setPassword(customerPojo.getPassword());
        customer.setTelephone(customerPojo.getTelephone());
        customer.setAddress1(customerPojo.getAddress_1());
        customer.setCity(customerPojo.getCity());
        customer.setState(customerPojo.getState());
        customer.setStatus(customerPojo.getStatus());
        customer.setCountryId(customerPojo.getCountry_id());
        customer.setPostcode(customerPojo.getPostcode());
        return customer;
    }
}
