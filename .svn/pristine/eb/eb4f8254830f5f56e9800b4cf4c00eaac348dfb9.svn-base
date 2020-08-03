package com.hyva.restopos.rest.Mapper;


import com.hyva.restopos.rest.entities.AccountType;
import com.hyva.restopos.rest.pojo.AccountTypePojo;

import java.util.ArrayList;
import java.util.List;

public class AccountTypeMapper {

    public static AccountType mapAccountTypePojoToEntity(AccountTypePojo accountTypePojo){
        AccountType accountType = new AccountType();
        accountType.setAccountId(accountTypePojo.getAccountId());
        accountType.setAccountDescription(accountTypePojo.getAccountDescription());
        accountType.setAccountName(accountTypePojo.getAccountName());
        accountType.setStatus(accountTypePojo.getStatus());
        return accountType;
    }
    public static List<AccountTypePojo> mapAccountTypeEntityToPojo(List<AccountType> typeList){
        List<AccountTypePojo> list=new ArrayList<>();
        for(AccountType type:typeList) {
            AccountTypePojo accountTypePojo = new AccountTypePojo();
            accountTypePojo.setAccountId(type.getAccountId());
            accountTypePojo.setAccountName(type.getAccountName());
            accountTypePojo.setAccountDescription(type.getAccountDescription());
            accountTypePojo.setStatus(type.getStatus());
            list.add(accountTypePojo);
        }
        return list;
    }
}
