package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.InvoiceOnline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceOnlineRepository extends JpaRepository<InvoiceOnline,Long> {
    InvoiceOnline findByInvoiceno(String inv);
}
