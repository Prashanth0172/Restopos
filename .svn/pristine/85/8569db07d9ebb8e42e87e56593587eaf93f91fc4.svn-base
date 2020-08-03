package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Currency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    Currency findAllByCurrencyId(Long id);
    Currency findAllByCurrencyNameAndCurrencyIdNotIn(String name,Long id);
    Currency findAllByCurrencyName(String name);
    List<Currency> findAllByStatus(String status);
    List<Currency>findAllByCurrencyNameContainingAndStatus(String countryName,String status);
    List<Currency>findAllByCurrencyNameContainingAndStatus(String countryName,String status,Pageable pageable);
    Currency findFirstByStatus(String status,Sort sort);
    List<Currency>findAllByStatus(String status,Pageable pageable);
    Currency findFirstByCurrencyNameContainingAndStatus(String countryName,String status,Sort sort);
}
