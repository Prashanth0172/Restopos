package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.RestaurantTokenRecord;
import com.hyva.restopos.rest.entities.SalesInvoice;
import com.hyva.restopos.rest.pojo.RestaurantTokenRecordDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

import java.util.List;
import java.util.Map;

public interface RestaurantTokenRecordRepository extends JpaRepository<RestaurantTokenRecord,Long> {
    List<RestaurantTokenRecord> findBySiId(SalesInvoice salesInvoice);
    List<RestaurantTokenRecord> findBySiNo(String no);
    @Query("select r.token from RestaurantTokenRecord as r where r.siNo=:no")
    List<String> findBy(@Param(value = "no") String no);
    @Query("select r.siNo from RestaurantTokenRecord as r where r.dayEndStatus='false'")
    List<String> findByDayEndStatus();
    @Query("select r.siNo from RestaurantTokenRecord as r where r.date between :fromDate and :toDate")
    List<String> findByDate(@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate);
    @Query("select r from RestaurantTokenRecord as r where r.dayEndStatus='false' and r.siId is not null")
    List<RestaurantTokenRecord> findBy();
    List<RestaurantTokenRecord> findByTableIdAndTableNameOrderByRestaurantTokenIdDesc(String id,String name);
    List<RestaurantTokenRecord> findByOrderByRestaurantTokenIdDesc();
    @Query("select rt.siId.totalReceived as totalAmt from RestaurantTokenRecord as rt where rt.siId.sIStatus='Prepared' and rt.siId is not null and rt.time in :stringList and rt.date between :fromDate and :toDate group by rt.siId")
    List<Map> findByOrderBy(@Param(value = "stringList")List<String> stringList, @Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate);
    @Query("select rt.restaurantTokenId as restaurantTokenId,rt.tableName as tableName,rt.tableId as tableId,rt.waiterName as waiter,rt.token as tokenNo,rt.removedItemsList as tokenItemDetailsList,rt.productStatus as status,rt.kitchenTokenStart as kitchenTokenStart from RestaurantTokenRecord as rt where rt.waiterName=:waiterName and rt.productStatus in :productStatusWaiter  and rt.kitchenTokenStart between :fromDate and :toDate  ORDER BY rt.restaurantTokenId DESC")
    List<Map> findByWaiterName(@Param(value = "waiterName")String waiterName,@Param(value = "productStatusWaiter")String[] productStatusWaiter,@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate);
    @Query("select rt.restaurantTokenId as restaurantTokenId,rt.tableName as tableName,rt.tableId as tableId,rt.waiterName as waiter,rt.token as tokenNo,rt.removedItemsList as tokenItemDetailsList,rt.productStatus as status,rt.kitchenTokenStart as kitchenTokenStart from RestaurantTokenRecord as rt where  rt.productStatus in :productStatusWaiter  and rt.kitchenTokenStart between :fromDate and :toDate  ORDER BY rt.restaurantTokenId DESC")
    List<Map> findBy(@Param(value = "productStatusWaiter")String[] productStatusWaiter,@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate);
    @Query("select rt.restaurantTokenId as restaurantTokenId,rt.tableName as tableName,rt.tableId as tableId,rt.waiterName as waiter,rt.token as tokenNo,rt.removedItemsList as tokenItemDetailsList,rt.productStatus as status,rt.kitchenTokenStart as kitchenTokenStart from RestaurantTokenRecord as rt where rt.productStatus in :productStatusKitchen  and   rt.kitchenTokenStart between :fromDate and :toDate  ORDER BY rt.restaurantTokenId DESC")
    List<Map> findByProductStatus( @Param(value = "productStatusKitchen")String[] productStatusKitchen,@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate);

    List<RestaurantTokenRecord> findByOrderNo(String orderNo);

    @Query("select rt.date as date,rt.tableName as tableName,rt.itemDeteails as itemDeteails,rt.waiterName as waiterName,rt.token as tokenNo from RestaurantTokenRecord as rt where rt.date between :fromDate and :toDate and rt.token=:token ")
    List<Map> findAllByToken(@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate,@Param(value = "token")String token);
    @Query("select rt.date as date,rt.tableName as tableName,rt.itemDeteails as itemDeteails,rt.waiterName as waiterName,rt.token as tokenNo from RestaurantTokenRecord as rt where rt.date between :fromDate and :toDate")
    List<Map> findAll(@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate);

    @Query("select rt.token as token,rt.itemDeteails as itemDeteails from RestaurantTokenRecord as rt where rt.removedItemsList <> '[]'")
    List<Map> findAllByTokenList();
    @Query("select rt.siNo as siNo from RestaurantTokenRecord as rt where rt.time in :time and rt.date between :fromDate and :toDate ")
    List<String> findAllByshift(@Param(value = "time")List<String> time,@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate);

    @Query("select distinct(date(rt.date)) from RestaurantTokenRecord as rt where rt.time in :time and rt.date between :fromDate and :toDate and rt.siId is not null and rt.siId.invoiceType is not null and rt.siId.sIStatus='Prepared' group by rt.date ")
    List<Date> findAllBy(@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate,@Param(value = "time")List<String> times);
    @Query("select rt.siId.totalReceived as totalAmt,rt.siId.sIId as SIId from RestaurantTokenRecord as rt where rt.time in :time and rt.date between :fromDate and :toDate and rt.siId is not null and rt.siId.invoiceType is not null and rt.siId.sIStatus='Prepared' group by rt.siId ")
    List<Map> findAllBySiId(@Param(value = "fromDate")Date fromDate,@Param(value = "toDate")Date toDate,@Param("time")List<String> timesList);
    RestaurantTokenRecord findFirstBySiId(SalesInvoice salesInvoice);
    List<RestaurantTokenRecord> findByTableIdAndSiIdNotNull(String tables);
    @Query("select r.dayEndDate as dayEndDate from RestaurantTokenRecord as r where r.dayEndDate between :fromDate and :toDate  and r.dayEndStatus='true' group by r.dayEndDate")
    List<Map> findByDateStatus(@Param(value = "fromDate") Date fromDate,@Param(value = "toDate") Date toDate);
    @Query("select r.dayEndDate as date,r.siId.sIId as SIId,r.siId.totalReceivable as totalReceivable from RestaurantTokenRecord as r where r.dayEndDate=:date and r.siId.sIStatus='Prepared'  and r.dayEndStatus='true' group by r.dayEndDate,r.siId")
    List<Map> findByDateStatusSIID(@Param(value = "date") Date date);
    @Query("select r.siNo as siNo from RestaurantTokenRecord as r where r.dayEndDate=:date and r.dayEndStatus='true'")
    List<String> findByDateStatusSdfIID(@Param(value = "date") Date date);

    @Query("select r.siNo as SQNo,r.dayEndDate as date from RestaurantTokenRecord  as r where r.dayEndDate between :fromDate and :toDate group by r.siNo,r.dayEndDate ")
    List<Map>findAllBy(@Param(value = "fromDate") Date fromDate,@Param(value = "toDate") Date toDate);

}
