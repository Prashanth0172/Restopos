package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Category;
import com.hyva.restopos.rest.entities.SalesInvoice;
import com.hyva.restopos.rest.entities.SalesInvoiceDetails;
import com.hyva.restopos.rest.pojo.AnalysisResponsePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SalesInvoiceDetailsRepository extends JpaRepository<SalesInvoiceDetails,Long>,JpaSpecificationExecutor {
    @Query("select si.itemId.itemId as itemId,si.itemId.itemName as itemName,sum(si.itemAmountIncTax) as totalAmtReceived,sum(si.qtyOrdered) as QtySold from SalesInvoiceDetails as si where si.sIId.sINo in :list and si.itemId.idItemCategory=:category group by si.itemId")
    List<Map> findBy(@Param(value = "list")List<String> list, @Param(value = "category")Category category);
    @Query("select si.itemId.itemName as name,sum(si.qtyOrdered) as value,sum(si.itemAmountExcTax) as itemAmountExcTax from SalesInvoiceDetails as si where si.sIId.sIDate between :fromDate and :toDate and si.sIId.sIStatus='Prepared' group by si.itemId")
    List<Map> findForItems(@Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
    List<SalesInvoiceDetails> findBySIId(SalesInvoice salesInvoice);

//    @Query("select si.itemId.itemName as itemName,sum(si.qtyOrdered) as qty,sum(si.itemAmountIncTax) as invoiceAmount from SalesInvoiceDetails as si where si.itemId.itemId=:itemId and si.sIId.sIStatus='prepared' and si.sIId.sIDate between :fromDate and :toDate group by si.itemId")
//    List<Map> findByMonthendReportbyItem(@Param(value = "itemId")Long itemId, @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
//    @Query("select si.itemId.itemName as itemName,sum(si.qtyOrdered) as qty,sum(si.itemAmountIncTax) as invoiceAmount from SalesInvoiceDetails as si where si.itemId.itemId=:itemId and si.itemId.idItemCategory.categoryId=:categoryId and si.sIId.sIStatus='prepared' and si.sIId.sIDate between :fromDate and :toDate group by si.itemId")
//    List<Map> findByMonthendReportbyCategoryAndItem(@Param(value = "itemId")Long itemId,@Param(value = "categoryId")Long categoryId, @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
    @Query("select si.itemId.itemName as itemName,sum(si.qtyOrdered) as qty,sum(si.itemAmountIncTax) as invoiceAmount from SalesInvoiceDetails as si where  si.sIId.sIStatus='prepared' and si.sIId.sIDate between :fromDate and :toDate group by si.itemId")
    List<Map> findByMonthendReport(@Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
//    @Query("select si.itemId.itemName as itemName,sum(si.qtyOrdered) as qty,sum(si.itemAmountIncTax) as invoiceAmount from SalesInvoiceDetails as si where si.itemId.idItemCategory.categoryId=:categoryId and si.sIId.sIStatus='prepared' and si.sIId.sIDate between :fromDate and :toDate group by si.itemId")
//    List<Map> findByMonthendReportbyCategory(@Param(value = "categoryId")Long categoryId, @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
//    @Query("select si.itemId.itemId as itemId, si.itemId.itemName as itemName ,sum(si.qtyOrdered) as QtySold,sum(si.itemAmountIncTax) as totalAmtReceived from SalesInvoiceDetails as si where si.sIId.sIId in :invoiceList group by si.itemId")
//    List<Map> findByMonthendReportbyCategoryitemID(@Param(value = "invoiceList") List<Long> invoicesList);

//    @Query("select si.itemId.itemName as itemName,si.itemId.idItemCategory.name as name , si.sIId.sIDate as date,sum(si.qtyOrdered) as qty,sum(si.itemAmountIncTax) as invoiceAmount from SalesInvoiceDetails as si where si.itemId.itemId=:itemId and si.sIId.sIStatus='prepared' and si.sIId.sIDate between :fromDate and :toDate group by si.itemId,si.sIId.sIDate")
//    List<Map> findByItemSalesbyItem(@Param(value = "itemId")Long itemId, @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
//    @Query("select si.itemId.itemName as itemName,si.itemId.idItemCategory.name as name , si.sIId.sIDate as date,sum(si.qtyOrdered) as qty,sum(si.itemAmountIncTax) as invoiceAmount from SalesInvoiceDetails as si where si.itemId.idItemCategory.categoryId=:categoryId and si.sIId.sIStatus='prepared' and si.sIId.sIDate between :fromDate and :toDate group by si.itemId,si.sIId.sIDate")
//    List<Map> findByItemSalesbyCategory(@Param(value = "categoryId")Long categoryId, @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
//    @Query("select si.itemId.itemName as itemName,si.itemId.idItemCategory.name as name , si.sIId.sIDate as date,sum(si.qtyOrdered) as qty,sum(si.itemAmountIncTax) as invoiceAmount from SalesInvoiceDetails as si where si.itemId.itemId=:itemId and si.itemId.idItemCategory.categoryId=:categoryId and si.sIId.sIStatus='prepared' and si.sIId.sIDate between :fromDate and :toDate group by si.itemId,si.sIId.sIDate")
//    List<Map> findByItemSalesbyCategoryAndItem(@Param(value = "itemId")Long itemId,@Param(value = "categoryId")Long categoryId, @Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);
//    @Query("select si.itemId.itemName as itemName,si.itemId.idItemCategory.name as name , si.sIId.sIDate as date,sum(si.qtyOrdered) as qty,sum(si.itemAmountIncTax) as invoiceAmount from SalesInvoiceDetails as si where  si.sIId.sIStatus='prepared' and si.sIId.sIDate between :fromDate and :toDate group by si.itemId,si.sIId.sIDate")
//    List<Map> findByItemSales(@Param(value = "fromDate")Date fromDate, @Param(value = "toDate")Date toDate);

    @Query("select si.itemId.itemId as itemId, si.itemId.itemName as itemName,sum(si.itemAmountIncTax) as totalAmtReceived,sum(si.qtyOrdered) as QtySold from SalesInvoiceDetails as si where si.itemId.idItemCategory=:itemCategory and si.sIId.sINo in :invoice group by si.itemId")
    List<Map> findAllBy(@Param(value = "itemCategory")Category itemCategory,@Param(value = "invoice")List<String> invoice);
    @Query("select si from SalesInvoiceDetails as si where si.sIId.sIStatus='prepared'")
    List<SalesInvoiceDetails> findAllByStatus();
    @Query("select si.sIId.sINo as sQNo,si.sIId.sIId as salesinvoiceId,si.sIId.sIDate as date,si.sIId.sIStatus as sQStatus,si.sIId.totalAmount as totalAmount,si.sIId.totalReceived  as totalReceivable,si.sIId.totalDiscountAmount as discountAmount,si.sIId.salesTotalTaxAmt as salesTotalTaxAmt,sum(si.itemAmountExcTax) as  itemAmountExcTax from SalesInvoiceDetails as si where si.sIId.sIStatus='Prepared' and si.sIId.sINo in :invoicenos and si.sIId.userId.employeeName in :name and si.sIId.sIId between :fromId and :toId and si.sIId.customerId.customerId =:customerId group by si.sIId")
    List<Map> findAllByConditionscustomerAndEmployee(@Param(value = "invoicenos")List<String> invoice,@Param(value = "name")List<String> name,@Param(value = "fromId")Long fromId, @Param(value = "toId")Long toId,@Param(value = "customerId") Long id);
    @Query("select si.sIId.sINo as sQNo,si.sIId.sIId as salesinvoiceId,si.sIId.sIDate as date,si.sIId.sIStatus as sQStatus,si.sIId.totalAmount as totalAmount,si.sIId.totalReceived  as totalReceivable,si.sIId.totalDiscountAmount as discountAmount,si.sIId.salesTotalTaxAmt as salesTotalTaxAmt,sum(si.itemAmountExcTax) as  itemAmountExcTax from SalesInvoiceDetails as si where si.sIId.sIStatus='Prepared' and si.sIId.sINo in :invoicenos and si.sIId.userId.employeeName in :name and si.sIId.sIId between :fromId and :toId group by si.sIId")
    List<Map> findAllByConditionsEmployee(@Param(value = "invoicenos")List<String> invoice,@Param(value = "name")List<String> name,@Param(value = "fromId")Long fromId, @Param(value = "toId")Long toId);
    @Query("select si.sIId.sINo as sQNo,si.sIId.sIId as salesinvoiceId,si.sIId.sIDate as date,si.sIId.sIStatus as sQStatus,si.sIId.totalAmount as totalAmount,si.sIId.totalReceived  as totalReceivable,si.sIId.totalDiscountAmount as discountAmount,si.sIId.salesTotalTaxAmt as salesTotalTaxAmt,sum(si.itemAmountExcTax) as  itemAmountExcTax from SalesInvoiceDetails as si where si.sIId.sIStatus='Prepared' and si.sIId.sINo in :invoicenos and si.sIId.sIId between :fromId and :toId and si.sIId.customerId.customerId =:customerId group by si.sIId")
    List<Map> findAllByConditionsCustomer(@Param(value = "invoicenos")List<String> invoice,@Param(value = "fromId")Long fromId, @Param(value = "toId")Long toId,@Param(value = "customerId") Long id);
    @Query("select si.sIId.sINo as sQNo,si.sIId.sIId as salesinvoiceId,si.sIId.sIDate as date,si.sIId.sIStatus as sQStatus,si.sIId.totalAmount as totalAmount,si.sIId.totalReceived  as totalReceivable,si.sIId.totalDiscountAmount as discountAmount,si.sIId.salesTotalTaxAmt as salesTotalTaxAmt,sum(si.itemAmountExcTax) as  itemAmountExcTax from SalesInvoiceDetails as si where si.sIId.sIStatus='Prepared' and si.sIId.sINo in :invoicenos  and si.sIId.sIId between :fromId and :toId group by si.sIId")
    List<Map> findAllByConditions(@Param(value = "invoicenos")List<String> invoice,@Param(value = "fromId")Long fromId, @Param(value = "toId")Long toId);

}
