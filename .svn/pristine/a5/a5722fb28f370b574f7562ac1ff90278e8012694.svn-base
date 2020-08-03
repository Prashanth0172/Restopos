package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.Hiposservice.HiposService;
import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.pojo.*;
import com.hyva.restopos.rest.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {
    @Autowired
    CountryRepository countryRepository;

    public static Employee MapEmployeePojoToEntity(EmployeePojo employeePojo){
        Employee employee = new Employee();
        employee.setEmployeeId(employeePojo.getStaff_id());
        employee.setEmployeeName(employeePojo.getStaff_name());
        employee.setIncentives(employeePojo.getIncentives());
        employee.setStaff_group_id(employeePojo.getStaff_group_id());
        employee.setStatus(employeePojo.getStaff_status());
        employee.setUsername(employeePojo.getUsername());
        employee.setPassword(employeePojo.getPassword());
        employee.setStaff_email(employeePojo.getStaff_email());
        employee.setPermission(employeePojo.getPermission());
        return employee;
    }

    public static List<EmployeePojo> mapEmpEntityToPojo(List<Employee> List) {
        List<EmployeePojo> list = new ArrayList<>();
        for (Employee config : List) {
            EmployeePojo employeePojo = new EmployeePojo();
            employeePojo.setStaff_id(config.getEmployeeId());
            employeePojo.setStaff_name(config.getEmployeeName());
            employeePojo.setStaff_email(config.getEmployeeName());
            employeePojo.setIncentives(config.getIncentives());
            employeePojo.setStaff_email(config.getStaff_email());
            employeePojo.setStaff_status(config.getStatus());
            employeePojo.setStaff_group_id(config.getStaff_group_id());
            employeePojo.setUsername(config.getUsername());
            employeePojo.setPassword(config.getPassword());
            employeePojo.setPermission(config.getPermission());
            list.add(employeePojo);
        }
        return list;
    }


    public static State MapStateEntityToPojo(StatePojo statePojo){
        State state = new State();
        state.setStateId(statePojo.getId());
        state.setCountryId(statePojo.getCountry_id());
        state.setStateName(statePojo.getName());
        state.setStatus(statePojo.getStatus());
        return state;
    }

    public static List<AddNewItemDTO> mapItemEntityToPojo(List<Item> List) {
        List<AddNewItemDTO> list = new ArrayList<>();
        for (Item config : List) {
            AddNewItemDTO itemPojo = new AddNewItemDTO();
            itemPojo.setItemId(config.getItemId());
            itemPojo.setItemCode(config.getItemCode());
            itemPojo.setItemName(config.getItemName());
            itemPojo.setItemDesc(config.getItemDesc());
            itemPojo.setItemStatus(config.getItemStatus());
            itemPojo.setItemType(config.getItemtype());
            itemPojo.setHsnCode(config.getHsnCode());
            itemPojo.setFoodtype(config.getFoodtype());
            itemPojo.setReOrderLevel(config.getReorderlevel());
            itemPojo.setItemImage(config.getImageFile());
            itemPojo.setInclusiveJSON(config.getInclusiveJSON());
            itemPojo.setProductionItem(config.getProductionName());
            itemPojo.setSalesPrice(config.getMenu_price());
            itemPojo.setPurchasePrice(config.getMenu_price());
            itemPojo.setItemCategory(config.getIdItemCategory());
            list.add(itemPojo);
        }
        return list;
    }


}
