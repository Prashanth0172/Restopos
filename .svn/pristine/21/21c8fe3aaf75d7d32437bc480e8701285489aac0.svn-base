package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Agent;
import com.hyva.restopos.rest.pojo.AgentPojo;

import java.util.ArrayList;
import java.util.List;

public class AgentMapper {


    public static Agent MapAgentPojoToEntity(AgentPojo agentPojo){
        Agent agent = new Agent();
        agent.setAgentId(agentPojo.getId());
        agent.setAgentName(agentPojo.getAgentName());
        agent.setAccountNo(agentPojo.getAccountNo());
        agent.setAgentAccountCode(agentPojo.getAgentAccountCode());
        agent.setEmail(agentPojo.getEmail());
        agent.setMobile(agentPojo.getMobile());
        agent.setAddress(agentPojo.getAddress());
        agent.setCommission(agentPojo.getCommission());
        agent.setEffectiveDate(agentPojo.getEffectiveDate());
        agent.setGstinNo(agentPojo.getGstinNo());
        agent.setEcommerce(agentPojo.getEcommerce());
        agent.setLocationId(agentPojo.getLocationId());
        agent.setUseraccount_id(agentPojo.getUseraccount_id());
        agent.setStatus(agentPojo.getStatus());
        return agent;
    }

    public static List<AgentPojo> mapAgentEntityToPojo(List<Agent> List) {
        List<AgentPojo> list = new ArrayList<>();
        for (Agent agent : List) {
            AgentPojo agentPojo = new AgentPojo();
            agentPojo.setId(agent.getAgentId());
            agentPojo.setAgentName(agent.getAgentName());
            agentPojo.setAccountNo(agent.getAccountNo());
            agentPojo.setAgentAccountCode(agent.getAgentAccountCode());
            agentPojo.setEmail(agent.getEmail());
            agentPojo.setMobile(agent.getMobile());
            agentPojo.setAddress(agent.getAddress());
            agentPojo.setCommission(agent.getCommission());
            agentPojo.setEffectiveDate(agent.getEffectiveDate());
            agentPojo.setGstinNo(agent.getGstinNo());
            agentPojo.setEcommerce(agent.getEcommerce());
            agentPojo.setLocationId(agent.getLocationId());
            agentPojo.setUseraccount_id(agent.getUseraccount_id());
            agentPojo.setStatus(agent.getStatus());
            list.add(agentPojo);
        }
        return list;
    }
}

