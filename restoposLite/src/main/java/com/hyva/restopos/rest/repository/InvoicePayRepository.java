package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.InvoicePayDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoicePayRepository extends JpaRepository<InvoicePayDetails,Long>{
}
