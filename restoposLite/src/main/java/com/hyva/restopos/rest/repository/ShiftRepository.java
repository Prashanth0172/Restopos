package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Shift;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift,Long> {

    Shift findAllByShiftId(Long id);
    Shift findAllByShiftNameAndShiftIdNotIn(String name,Long id);
    Shift findAllByShiftName(String name);
    List<Shift> findAllByStatus(String status);
    List<Shift> findAllByStatus(String status,Pageable pageable);
    Shift findFirstByStatus(String status,Sort sort);
    List<Shift>findAllByShiftNameContainingAndStatus(String name,String status);
    Shift findFirstByShiftNameContainingAndStatus(String name, String status, Sort sort);
   List<Shift>findAllByShiftNameContainingAndStatus(String name, String status, Pageable pageable);
}
