package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.State;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State,Long>{

    State findAllByStateId(Long id);
    State findAllByStateNameAndStateIdNotIn(String name,Long id);
    State findAllByStateName(String name);
    List<State> findAllByStatus(String status);
    List<State>findAllByStateNameContainingAndStatus(String name,String status);
    List<State> findAllByStateNameContainingAndStatus(String name,String status,Pageable pageable);
    State findFirstByStatus(String status,Sort sort);
    List<State>findAllByStatus(String status,Pageable pageable);
    State findFirstByStateNameContainingAndStatus(String name,String status,Sort sort);
   List<State> findAllByCountryIdAndStatus(Long cid,String status);
}
