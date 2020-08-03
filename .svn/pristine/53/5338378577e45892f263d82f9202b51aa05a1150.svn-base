package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.City;
import com.hyva.restopos.rest.entities.State;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City,Long>{

    City findAllById(Long id);
    City findAllByNameAndIdNotIn(String name, Long id);
    City findAllByName(String name);
    List<City> findAllByStatus(String status);
    List<City>findAllByNameContainingAndStatus(String name, String status);
    List<City>findAllByNameContainingAndStatus(String name, String status, Pageable pageable);
    City findFirstByStatus(String status, Sort sort);
    List<City>findAllByStatus(String status, Pageable pageable);
    City findFirstByNameContainingAndStatus(String name, String status, Sort sort);
   List<City> findAllByIdAndStatus(Long cid, String status);
}
