package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Country;
import com.hyva.restopos.rest.entities.Discount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount,Long> {
    Discount findAllByMenuidAndIdNotIn(String name, Long id);
    Discount findAllByMenuid(String name);
    Discount findAllById(Long id);
    List<Discount> findAllByStatus(String status);
    List<Discount>findAllByMenuidContainingAndStatus(String menu, String status);
    List<Discount>findAllByMenuidContainingAndStatus(String menu, String status, Pageable pageable);
    Discount findFirstByStatus(String status, Sort sort);
    List<Discount>findAllByStatus(String status, Pageable pageable);
    Discount findFirstByMenuidContainingAndStatus(String menu, String status, Sort sort);

}
