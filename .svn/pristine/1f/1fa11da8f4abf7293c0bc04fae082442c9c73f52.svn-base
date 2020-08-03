package com.hyva.restopos.rest.Mapper;


import com.hyva.restopos.rest.entities.AccountGroup;
import com.hyva.restopos.rest.pojo.AccountGroupPojo;

import java.util.ArrayList;
import java.util.List;

public class AccountGroupMapper {

    public static AccountGroup mapAccountGroupPojoToEntity(AccountGroupPojo accountGroupPojo){
        AccountGroup accountGroup = new AccountGroup();
        accountGroup.setAccountId(accountGroupPojo.getAccountId());
        accountGroup.setAccountDescription(accountGroupPojo.getAccountDescription());
        accountGroup.setAccountName(accountGroupPojo.getAccountName());
        accountGroup.setAccountType(accountGroupPojo.getAccountType());
        accountGroup.setAccountCode(accountGroupPojo.getAccountCode());
        accountGroup.setStatus(accountGroupPojo.getStatus());
        return accountGroup;
    }


    public static List<AccountGroupPojo> mapAccountGroupEntityToPojo(List<AccountGroup> typeList){
        List<AccountGroupPojo> list=new ArrayList<>();
        for(AccountGroup type:typeList) {
            AccountGroupPojo accountGroupPojo = new AccountGroupPojo();
            accountGroupPojo.setAccountId(type.getAccountId());
            accountGroupPojo.setAccountName(type.getAccountName());
            accountGroupPojo.setAccountDescription(type.getAccountDescription());
            accountGroupPojo.setStatus(type.getStatus());
            accountGroupPojo.setAccountType(type.getAccountType());
            accountGroupPojo.setAccountCode(type.getAccountCode());
            list.add(accountGroupPojo);
        }
        return list;
    }
}
