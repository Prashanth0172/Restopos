package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Agent;
import com.hyva.restopos.rest.entities.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent,Long> {

    Agent findAllByAgentNameAndAgentIdNotIn(String name,Long id);
    Agent findByAgentName(String name);
    List<Agent> findAllByAgentName(String name);
    Agent findAllByAgentId(Long id);
    List<Agent> findAllByStatus(String status);
    List<Agent>findAllByAgentNameContainingAndStatus(String countryName,String status);
    List<Agent>findAllByAgentNameContainingAndStatus(String countryName,String status,Pageable pageable);
    Agent findFirstByStatus(String status,Sort sort);
    List<Agent>findAllByStatus(String status,Pageable pageable);
    Agent findFirstByAgentNameContainingAndStatus(String countryName,String status,Sort sort);
}
