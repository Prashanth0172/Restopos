package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.OtherContacts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface ContactRepository extends JpaRepository<OtherContacts,Long>, JpaSpecificationExecutor {
    List<OtherContacts> findAllByFullName(String name);
    OtherContacts findByFullNameAndOtherContactIdNotIn(String Name, Long id);
    OtherContacts findByFullName(String Name);
    List<OtherContacts> findAllByFullNameContainingAndStatus(String name, String status);
    List<OtherContacts> findAllByStatus(String status);
    OtherContacts findFirstByFullNameContainingAndStatus(String name, String status, Sort sort);
    List<OtherContacts> findAllByFullNameContainingAndStatus(String name, String status, Pageable pageable);
    OtherContacts findFirstByStatus(String status, Sort sort);
    List<OtherContacts> findAllByStatus(String status, Pageable pageable);
}
