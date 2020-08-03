package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Customer findAllByFirstNameAndCustomerIdNotIn(String name,Long id);
    List<Customer> findAllByFirstNameContainingAndStatus(String name,String status);
    List<Customer> findAllByFirstNameContainingAndStatus(String name, String status, Pageable pageable);
    Customer findFirstByFirstNameContainingAndStatus(String name, String status, Sort sort);
   List<Customer> findAllByFirstName(String name);
   Customer findByFirstName(String name);
  List<Customer>  findAllByStatus(String status);
    Customer findAllByCustomerId(Long id);
   Customer findFirstByStatus(String status,Sort sort);
  List< Customer> findAllByStatus(String status,Pageable pageable);
}
