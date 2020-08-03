package com.hyva.restopos.rest.Mapper;
import com.hyva.restopos.rest.entities.State;
import com.hyva.restopos.rest.pojo.StatePojo;

import java.util.ArrayList;
import java.util.List;

public class StateMapper {

    public static List<StatePojo> mapStateEntityToPojo(List<State> List) {
        List<StatePojo> list = new ArrayList<>();
        for (State State : List) {
            StatePojo statePojo = new StatePojo();
            statePojo.setId(State.getStateId());
            statePojo.setCountry_id(State.getCountryId());
            statePojo.setStatus(State.getStatus());
            statePojo.setName(State.getStateName());
            list.add(statePojo);
        }
        return list;
    }



}
