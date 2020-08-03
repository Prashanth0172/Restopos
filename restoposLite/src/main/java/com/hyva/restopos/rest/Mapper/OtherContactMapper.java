package com.hyva.restopos.rest.Mapper;


import com.hyva.restopos.rest.entities.OtherContacts;
import com.hyva.restopos.rest.pojo.OtherContactsDTO;

import java.util.ArrayList;
import java.util.List;

public class OtherContactMapper {

    public static OtherContacts mapContactPojoToEntity(OtherContactsDTO otherContactsDTO){
        OtherContacts terms = new OtherContacts();
        terms.setPrefixName(otherContactsDTO.getPrefixName());
        terms.setFirstName(otherContactsDTO.getFirstName());
        terms.setMiddleName(otherContactsDTO.getMiddleName());
        terms.setLastName(otherContactsDTO.getLastName());
        terms.setFullName(otherContactsDTO.getFullName());
        terms.setPanNO(otherContactsDTO.getPanNO());
        terms.setBankName(otherContactsDTO.getBankName());
        terms.setBranchName(otherContactsDTO.getBranchName());
        terms.setAccountNo(otherContactsDTO.getAccountNo());
        terms.setGstCode(otherContactsDTO.getGstCode());
        terms.setIFSCCode(otherContactsDTO.getIFSCCode());
        terms.setBranchName(otherContactsDTO.getBranchName());
        terms.setAddress(otherContactsDTO.getAddress());
        terms.setEmail(otherContactsDTO.getEmail());
        terms.setStatus(otherContactsDTO.getStatus());
        terms.setPersonIncharge(otherContactsDTO.getPersonIncharge());
        terms.setContactNumber(otherContactsDTO.getContactNumber());
        terms.setGstCode(otherContactsDTO.getGstCode());
        terms.setWebsite(otherContactsDTO.getWebsite());
        terms.setCountry(otherContactsDTO.getCountry());
        terms.setState(otherContactsDTO.getState());
        terms.setOtherContactId(otherContactsDTO.getOtherContactId());
        return terms;
    }


    public static List<OtherContactsDTO> mapContactEntityToPojo(List<OtherContacts> List) {
        List<OtherContactsDTO> list = new ArrayList<>();
        for (OtherContacts otherContacts : List) {
            OtherContactsDTO pojo = new OtherContactsDTO();
            pojo.setPrefixName(otherContacts.getPrefixName());
            pojo.setFirstName(otherContacts.getFirstName());
            pojo.setMiddleName(otherContacts.getMiddleName());
            pojo.setLastName(otherContacts.getLastName());
            pojo.setFullName(otherContacts.getFullName());
            pojo.setPanNO(otherContacts.getPanNO());
            pojo.setBankName(otherContacts.getBankName());
            pojo.setBranchName(otherContacts.getBranchName());
            pojo.setAccountNo(otherContacts.getAccountNo());
            pojo.setGstCode(otherContacts.getGstCode());
            pojo.setIFSCCode(otherContacts.getIFSCCode());
            pojo.setBranchName(otherContacts.getBranchName());
            pojo.setAddress(otherContacts.getAddress());
            pojo.setEmail(otherContacts.getEmail());
            pojo.setPersonIncharge(otherContacts.getPersonIncharge());
            pojo.setContactNumber(otherContacts.getContactNumber());
            pojo.setGstCode(otherContacts.getGstCode());
            pojo.setWebsite(otherContacts.getWebsite());
            pojo.setStatus(otherContacts.getStatus());
            pojo.setCountry(otherContacts.getCountry());
            pojo.setState(otherContacts.getState());
            pojo.setOtherContactId(otherContacts.getOtherContactId());

            list.add(pojo);
        }
        return list;
    }
}
