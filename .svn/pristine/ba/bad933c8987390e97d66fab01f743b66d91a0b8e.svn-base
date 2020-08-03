package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.UserAccountSetup;
import com.hyva.restopos.rest.pojo.UserAccountSetUpDTO;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static List<UserAccountSetUpDTO> MapEntityToPojo(List<UserAccountSetup> userList){
        List<UserAccountSetUpDTO> userAccountSetUpDTOS=new ArrayList<>();
        for(UserAccountSetup userAccountSetup:userList) {
            UserAccountSetUpDTO userAccountSetUpDTO=new UserAccountSetUpDTO();
            userAccountSetUpDTO.setUser_loginId(userAccountSetup.getUserloginId());
            userAccountSetUpDTO.setFull_name(userAccountSetup.getFullName());
            userAccountSetUpDTO.setAccessLocations(userAccountSetup.getAllLocations());
            userAccountSetUpDTO.setUseraccount_id(userAccountSetup.getUseraccountId());
            userAccountSetUpDTO.setPasswordUser(userAccountSetup.getPasswordUser());
            userAccountSetUpDTO.setSecurityAnswer(userAccountSetup.getSecurityAnswer());
            userAccountSetUpDTO.setSecurityQuestion(userAccountSetup.getSecurityQuestion());
            userAccountSetUpDTO.setEmail(userAccountSetup.getEmail());
            userAccountSetUpDTO.setPhone(userAccountSetup.getPhone());
            userAccountSetUpDTO.setEmployeeflag(userAccountSetup.isEmployeeflag());
            userAccountSetUpDTO.setEmail(userAccountSetup.getEmail());
            userAccountSetUpDTO.setStatus(userAccountSetup.getStatus());
            userAccountSetUpDTO.setKitchenId(userAccountSetup.getKitchenId());
            userAccountSetUpDTO.setKitchenflag(userAccountSetup.isKitchenflag());
            userAccountSetUpDTOS.add(userAccountSetUpDTO);
        }
        return userAccountSetUpDTOS;
    }
}
