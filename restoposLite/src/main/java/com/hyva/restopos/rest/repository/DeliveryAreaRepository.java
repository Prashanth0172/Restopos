package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.DeliveryArea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea,Long> {
    DeliveryArea findAllByName(String name);
    DeliveryArea findAllByNameAndIdNotIn(String name,Long id);
    List<DeliveryArea> findAllByNameContainingAndStatus(String name, String status, Pageable pageable);
    List<DeliveryArea> findByNameContainingAndStatus(String name, String status);
    List<DeliveryArea> findAllByStatus(String status,Pageable pageable);
}
