package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.TableReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TableReservationRepository extends JpaRepository<TableReservation,Long> {
    TableReservation  findAllById(Long id);
    List<TableReservation> findAllByTableNameAndDateAndTimeIn(String name, Date data, List<String> time);
}
