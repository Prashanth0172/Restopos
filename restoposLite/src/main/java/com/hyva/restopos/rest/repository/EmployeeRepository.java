package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{
    Employee findByEmployeeName(String name);
    Employee findAllByEmployeeNameAndEmployeeIdNotIn(String name, Long id);
    Employee findAllByEmployeeName(String name);
    List<Employee> findAllByStatus(String status);
    List<Employee>findAllByEmployeeNameContainingAndStatus(String countryName,String status);
    List<Employee>findAllByEmployeeNameContainingAndStatus(String countryName,String status,Pageable pageable);
    Employee findFirstByStatus(String status,Sort sort);
    List<Employee>findAllByStatus(String status,Pageable pageable);
    Employee findFirstByEmployeeNameContainingAndStatus(String countryName,String status,Sort sort);
}
