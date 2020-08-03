package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.pojo.*;

import java.util.ArrayList;
import java.util.List;

public class StaffGroupMapper {

    public static StaffGroup MapStaffPojoToEntity(StaffGroupPojo staffGroupPojo){
        StaffGroup staffGroup = new StaffGroup();
        staffGroup.setStaffgroupid(staffGroupPojo.getStaff_group_id());
        staffGroup.setStaffgroupname(staffGroupPojo.getStaff_group_name());
        staffGroup.setStatus(staffGroupPojo.getStatus());
        staffGroup.setPermission(staffGroupPojo.getPermission());
        return staffGroup;
    }
    public static List<StaffGroupPojo> mapStaffEntityToPojo(List<StaffGroup> List) {
        List<StaffGroupPojo> list = new ArrayList<>();
        for (StaffGroup staffGroup : List) {
            StaffGroupPojo staffGroupPojo = new StaffGroupPojo();
            staffGroupPojo.setStaff_group_id(staffGroup.getStaffgroupid());
            staffGroupPojo.setStaff_group_name(staffGroup.getStaffgroupname());
            staffGroupPojo.setStatus(staffGroup.getStatus());
            staffGroupPojo.setPermission(staffGroup.getPermission());
            list.add(staffGroupPojo);
        }
        return list;
    }

}
