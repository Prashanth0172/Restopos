package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.KitchenMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KitchenRepository extends JpaRepository<KitchenMaster,Long> {
    List<KitchenMaster> findAllByStatus(String status);
    KitchenMaster findAllByKitchenNameAndKitchenIdNotIn(String name,Long id);
    KitchenMaster findAllByKitchenName(String name);
    KitchenMaster findAllByKitchenId(Long id);
    KitchenMaster findFirstByStatus(String name,Sort sort);
    List<KitchenMaster> findAllByKitchenNameContainingAndStatus(String name,String status);
    List<KitchenMaster> findAllByKitchenNameContainingAndStatus(String name,String status,Pageable pageable);
    List<KitchenMaster> findAllByStatus(String status, Pageable pageable);
    KitchenMaster findFirstByKitchenNameContainingAndStatus(String countryName,String status,Sort sort);




}
