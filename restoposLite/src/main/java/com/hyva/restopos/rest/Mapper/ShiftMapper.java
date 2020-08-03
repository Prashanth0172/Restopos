package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.pojo.*;

import java.util.ArrayList;
import java.util.List;

public class ShiftMapper {

    public static Shift MapPojoToEntity(ShiftPojo shiftPojo){
        Shift shift = new Shift();
        shift.setShiftId(shiftPojo.getMealtime_id());
        shift.setShiftName(shiftPojo.getMealtime_name());
        shift.setFromTime(shiftPojo.getStart_time());
        shift.setToTime(shiftPojo.getEnd_time());
        shift.setStatus(shiftPojo.getMealtime_status());
        return shift;
    }
    public static List<ShiftPojo> mapShiftEntityToPojo(List<Shift> List) {
        List<ShiftPojo> list = new ArrayList<>();
        for (Shift shift : List) {

            ShiftPojo shiftPojo = new ShiftPojo();
            shiftPojo.setMealtime_id(shift.getShiftId());
            shiftPojo.setMealtime_name(shift.getShiftName());
            shiftPojo.setStart_time(shift.getFromTime());
            shiftPojo.setEnd_time(shift.getToTime());
            shiftPojo.setMealtime_status(shift.getStatus());
            list.add(shiftPojo);
        }
        return list;
    }

}
