package com.hyva.restopos.rest.Mapper;


import com.hyva.restopos.rest.entities.AccountMaster;

import com.hyva.restopos.rest.pojo.AccountMasterDTO;
import com.hyva.restopos.rest.pojo.ListChatOfAccountDto;
import com.hyva.restopos.rest.pojo.SecondChatOfAccountDto;
import com.hyva.restopos.rest.pojo.ThirdChatOfAccountDto;

import java.util.ArrayList;
import java.util.List;
public class AccountMasterMapper {

    public static List<ListChatOfAccountDto> mapAccountMasterEntityToPojo(List<AccountMaster> typeList){
        List<ListChatOfAccountDto> list=new ArrayList<>();
        for(AccountMaster type:typeList) {
            ListChatOfAccountDto listChatOfAccountDto = new ListChatOfAccountDto();
            listChatOfAccountDto.setAccountid(type.getAccountid());
            listChatOfAccountDto.setAccountname(type.getAccountname());
            listChatOfAccountDto.setAccountCode(type.getAccountcode());
            listChatOfAccountDto.setStringAccountCode(type.getStringAccountCode());
            listChatOfAccountDto.setStatus(type.getStatus());
            listChatOfAccountDto.setAparcode(type.getAparcode());
            if(type.getAccountTypeId()!=null) {
                listChatOfAccountDto.setAccountTypeId(type.getAccountTypeId().getAccountId());
                listChatOfAccountDto.setAccountTypeName(type.getAccountTypeId().getAccountName());
            }
            listChatOfAccountDto.setAccountGroupId(type.getAgid().getAccountId());
            listChatOfAccountDto.setAccountGroupName(type.getAgid().getAccountName());
            if(type.getAmaccountid()!=null) {
                listChatOfAccountDto.setAmaccountid(type.getAmaccountid().getAccountid());
            }
            list.add(listChatOfAccountDto);
        }
        return list;
    }
    public static List<ThirdChatOfAccountDto> mapThirdAccountMasterEntityToPojo(List<AccountMaster> typeList){
        List<ThirdChatOfAccountDto> list=new ArrayList<>();
        for(AccountMaster type:typeList) {
            ThirdChatOfAccountDto listChatOfAccountDto = new ThirdChatOfAccountDto();
            listChatOfAccountDto.setAccountid(type.getAccountid());
            listChatOfAccountDto.setAccountname(type.getAccountname());
            listChatOfAccountDto.setAccountCode(type.getAccountcode());
            listChatOfAccountDto.setStringAccountCode(type.getStringAccountCode());
            listChatOfAccountDto.setStatus(type.getStatus());
            listChatOfAccountDto.setAparcode(type.getAparcode());
            if(type.getAccountTypeId()!=null) {
                listChatOfAccountDto.setAccountTypeId(type.getAccountTypeId().getAccountId());
                listChatOfAccountDto.setAccountTypeName(type.getAccountTypeId().getAccountName());
            }
            listChatOfAccountDto.setAccountGroupId(type.getAgid().getAccountId());
            listChatOfAccountDto.setAccountGroupName(type.getAgid().getAccountName());
            if(type.getAmaccountid()!=null) {
                listChatOfAccountDto.setAmaccountid(type.getAmaccountid().getAccountid());
            }
            list.add(listChatOfAccountDto);
        }
        return list;
    }
    public static List<SecondChatOfAccountDto> mapSecondAccountMasterEntityToPojo(List<AccountMaster> typeList){
        List<SecondChatOfAccountDto> list=new ArrayList<>();
        for(AccountMaster type:typeList) {
            SecondChatOfAccountDto listChatOfAccountDto = new SecondChatOfAccountDto();
            listChatOfAccountDto.setAccountid(type.getAccountid());
            listChatOfAccountDto.setAccountname(type.getAccountname());
            listChatOfAccountDto.setAccountCode(type.getAccountcode());
            listChatOfAccountDto.setStringAccountCode(type.getStringAccountCode());
            listChatOfAccountDto.setStatus(type.getStatus());
            listChatOfAccountDto.setAparcode(type.getAparcode());
            if(type.getAccountTypeId()!=null) {
                listChatOfAccountDto.setAccountTypeId(type.getAccountTypeId().getAccountId());
                listChatOfAccountDto.setAccountTypeName(type.getAccountTypeId().getAccountName());
            }
            listChatOfAccountDto.setAccountGroupId(type.getAgid().getAccountId());
            listChatOfAccountDto.setAccountGroupName(type.getAgid().getAccountName());
            if(type.getAmaccountid()!=null) {
                listChatOfAccountDto.setAmaccountid(type.getAmaccountid().getAccountid());
            }
            list.add(listChatOfAccountDto);
        }
        return list;
    }
    public static List<ListChatOfAccountDto> mapChartOfAccEntityToPojo(List<AccountMaster> typeList){
        List<ListChatOfAccountDto> list=new ArrayList<>();
        for(AccountMaster type:typeList) {
            ListChatOfAccountDto accountGroupPojo = new ListChatOfAccountDto();
            accountGroupPojo.setAccountid(type.getAccountid());
            accountGroupPojo.setAccountname(type.getAccountname());
            accountGroupPojo.setStatus(type.getStatus());
            accountGroupPojo.setAccountCode(type.getAccountcode());
            accountGroupPojo.setStringAccountCode(type.getStringAccountCode());
            accountGroupPojo.setAparcode(type.getAparcode());
            accountGroupPojo.setReportValue(type.getReportvalue());
            list.add(accountGroupPojo);
        }
        return list;
    }
    public static List<AccountMasterDTO> mapAccMasterEntityToPojo(List<AccountMaster> typeList){
        List<AccountMasterDTO> list=new ArrayList<>();
        for(AccountMaster type:typeList) {
            AccountMasterDTO listChatOfAccountDto = new AccountMasterDTO();
            listChatOfAccountDto.setAccountid(type.getAccountid());
            listChatOfAccountDto.setAccountname(type.getAccountname());
            listChatOfAccountDto.setAccountcode(type.getAccountcode());
            listChatOfAccountDto.setStringAccountCode(type.getStringAccountCode());
            listChatOfAccountDto.setStatus(type.getStatus());
            listChatOfAccountDto.setAparcode(type.getAparcode());
            if(type.getAccountTypeId()!=null) {
                listChatOfAccountDto.setAccountTypeId(type.getAccountTypeId().getAccountId());
                listChatOfAccountDto.setAccountTypeName(type.getAccountTypeId().getAccountName());
            }
            listChatOfAccountDto.setAccountGroupId(type.getAgid().getAccountId());
            listChatOfAccountDto.setAccountGroup(type.getAgid().getAccountName());
            if(type.getAmaccountid()!=null) {
                listChatOfAccountDto.setAmaccountid(type.getAmaccountid().getAccountid());
            }
            list.add(listChatOfAccountDto);
        }
        return list;
    }
}
