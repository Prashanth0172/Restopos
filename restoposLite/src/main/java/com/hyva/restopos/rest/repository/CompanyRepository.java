package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {

    Company findByCompanyId(Long id);
    Company findAllByOrderIdAndStatus(String orderId,String status);
    Company findAllByStatus(String status);
    Company findAllByCompanyName(String name);
}
