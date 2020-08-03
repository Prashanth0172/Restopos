package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.PosPaymentTypes;
import com.hyva.restopos.rest.entities.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PosPaymentTypesRepository extends JpaRepository<PosPaymentTypes,Long>,JpaSpecificationExecutor {
    @Query("select p from PosPaymentTypes as p where p.salesInvoice.sINo in :siNos")
    List<PosPaymentTypes> findBySalesInvoice(@Param(value = "siNos") List<String> siNos);
    @Query("select pos from PosPaymentTypes as pos where pos.salesInvoice.sINo in :invoiceList and pos.salesInvoice.sIStatus='Prepared'")
    List<PosPaymentTypes> findByInvoice(@Param(value = "invoiceList")List<String> invoiceList);
    List<PosPaymentTypes> findBySalesInvoice(SalesInvoice salesInvoice);
    PosPaymentTypes findAllBySalesInvoice(SalesInvoice salesInvoice);
//    @Query("select pos from PosPaymentTypes as pos where pos.salesInvoice.sIDate between :fromDate and toDate and pos.salesInvoice.sIStatus='Prepared' and pos.salesInvoice in :salesList")
//    List<PosPaymentTypes> findBy(@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate,@Param(value = "salesList")List<SalesInvoice> salesList);

    @Query("select pos from PosPaymentTypes as pos where pos.salesInvoice.sINo in :sino ")
    List<PosPaymentTypes> findAllBy(@Param(value = "sino")List<String> sino);

}
