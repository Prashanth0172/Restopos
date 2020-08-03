package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.SalesInvoice;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice,Long> {
    List<SalesInvoice> findAllBySINoStartingWith(String sino);
    SalesInvoice findAllBySIId(Long siid);
    SalesInvoice findAllBySINo(String sino);
    List<SalesInvoice> findFirst20BySIStatus(String status,Sort sort);
    @Query("select si.sIDate as salesDate,si.totalAmount as totalAmount,si.salesTotalTaxAmt as salesTotalTaxAmt,si.totalDiscountAmount as totalDiscountAmount from SalesInvoice si where si.sIDate between :fromDate and :toDate and si.sIStatus='Prepared'")
    List<Map> findBy(@Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);

    @Query("select si.invoiceType as invoiceType,sum(si.totalReceived) as totalAmt from SalesInvoice as si where si.sIDate between :fromDate and :toDate and si.sIStatus='Prepared' group by si.invoiceType")
    List<Map> findByinvoice(@Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
    List<SalesInvoice> findAllBySINoContaining(String no,Sort sort);
    List<SalesInvoice> findAllBySIStatus(String status);
    @Query("select si.invoiceType as invoiceType from SalesInvoice as si group by si.invoiceType")
    List<SalesInvoice> findAllByInvoiceType();
    @Query("select si.agentId.agentName as agentName,si.sIDate as invdate, si.sINo as invoiceNO, si.totalAmount as totalAmount, si.agentId.accountNo as accountNo,si.agentId.email as email,si.agentId.commission as commission from SalesInvoice as si where si.sIDate between :fromDate and :toDate ")
    List<Map> findAllBy( @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
    @Query("select si.agentId.agentName as agentName,si.sIDate as invdate, si.sINo as invoiceNO, si.totalAmount as totalAmount, si.agentId.accountNo as accountNo,si.agentId.email as email,si.agentId.commission as commission from SalesInvoice as si where si.sIDate between :fromDate and :toDate and si.agentId=:agent")
    List<Map> findAllByAgent( @Param(value = "agent")String name);
    @Query("select si.agentId.agentName as agentName,si.sIDate as invdate, si.sINo as invoiceNO, si.totalAmount as totalAmount, si.agentId.accountNo as accountNo,si.agentId.email as email,si.agentId.commission as commission from SalesInvoice as si where si.sIDate between :fromDate and :toDate")
    List<Map> findAllByAgentId( @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
    @Query("select si.agentId.agentName as agentName,si.sIDate as invdate, si.sINo as invoiceNO, si.totalAmount as totalAmount, si.agentId.accountNo as accountNo,si.agentId.email as email,si.agentId.commission as commission from SalesInvoice as si where si.sIDate between :fromDate and :toDate and si.agentId.agentId=:agent")
    List<Map> findAllByAgentIdAndSIDate( @Param(value = "agent") Long id,@Param(value="fromDate")Date fromDate,@Param(value = "toDate")Date toDate);
//    @Query("select si.sIDate as date, si.sINo as sINo, si.customerId.customerName as customerName,si.sIStatus as status, si.totalAmount as totalAmount, si.totalReceivable as totalReceivable from SalesInvoice as si where si.customerId.customerId=:customerId and si.sIStatus='Cancelled Invoice' and si.sIId.sIDate between :fromDate and :toDate")
//    List<Map> findByCancelledSalesbyCustomer(@Param(value = "customerId")Long customerId, @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
//    @Query("select si.sIDate as date, si.sINo as sINo, si.customerId.customerName as customerName,si.sIStatus as status, si.totalAmount as totalAmount, si.totalReceivable as totalReceivable from SalesInvoice as si where si.sINo between :fromsiNo and :tosiNo and si.sIStatus='Cancelled Invoice' and si.sIId.sIDate between :fromDate and :toDate")
//    List<Map> findByCancelledSalesInvoiceFilter(@Param(value = "fromDate")Date fromDate, @Param(value = "fromsiNo")String fromsiNo,@Param(value = "toDate")Date toDate,@Param(value = "tosiNo")String tosiNo);
//    @Query("select si.sIDate as date, si.sINo as sINo, si.customerId.customerName as customerName,si.sIStatus as status, si.totalAmount as totalAmount, si.totalReceivable as totalReceivable from SalesInvoice as si where si.customerId.customerId=:customerId and si.sINo between :fromsiNo and :tosiNo and si.sIStatus='Cancelled Invoice' and si.sIId.sIDate between :fromDate and :toDate")
//    List<Map> findByCancelledSalesInvoiceFilterAndCustomer(@Param(value = "customerId")Long customerId,@Param(value = "fromDate")Date fromDate, @Param(value = "fromsiNo")String fromsiNo,@Param(value = "toDate")Date toDate,@Param(value = "tosiNo")String tosiNo);
//    @Query("select si.sIDate as date, si.sINo as sINo, si.customerId.customerName as customerName,si.sIStatus as status, si.totalAmount as totalAmount, si.totalReceivable as totalReceivable from SalesInvoice as si where si.sIStatus='Cancelled Invoice' and si.sIId.sIDate between :fromDate and :toDate")
//    List<Map> findByCancelledSales(@Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);

}
