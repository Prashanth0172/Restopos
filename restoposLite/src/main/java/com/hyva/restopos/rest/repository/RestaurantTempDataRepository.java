package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.RestaurantTempData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface RestaurantTempDataRepository extends JpaRepository<RestaurantTempData,Long> {
    RestaurantTempData findAllByTableNameAndTableId(String tableName, String tableId);
    RestaurantTempData findByTableNameAndTableId(String tableName,String tableId);
    @Query("select rt.id as id,rt.tableName as tableName,rt.tableId as tableId,rt.customerId as customerId,rt.locationId as locationId,rt.useraccount_id as useraccount_id,rt.agentId as agentId,rt.selectedItemsList as selectedItemsList,rt.orderNo as orderNo from RestaurantTempData as rt where rt.orderNo=:orderNo")
    List<Map> findBy(@Param(value = "orderNo")String orderNo);

    @Query("select rt.tableName from RestaurantTempData as rt group by rt.tableName")
    List<String> findBy();
}
