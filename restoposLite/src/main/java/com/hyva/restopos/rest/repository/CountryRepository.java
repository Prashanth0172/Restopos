package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country,Long> {
    Country findAllByCountryNameAndCountryIdNotIn(String name,Long id);
    Country findAllByCountryName(String name);
    Country findAllByCountryId(Long id);
    List<Country> findAllByStatus(String status);
    List<Country>findAllByCountryNameContainingAndStatus(String countryName,String status);
    List<Country>findAllByCountryNameContainingAndStatus(String countryName,String status,Pageable pageable);
    Country findFirstByStatus(String status,Sort sort);
    List<Country>findAllByStatus(String status,Pageable pageable);
    Country findFirstByCountryNameContainingAndStatus(String countryName,String status,Sort sort);

}
