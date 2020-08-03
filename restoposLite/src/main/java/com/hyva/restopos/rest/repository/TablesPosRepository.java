package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.TablesPos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TablesPosRepository extends JpaRepository<TablesPos,Long> {
    TablesPos findAllByTableNameAndTableidNotIn(String name,Long id);
    TablesPos findAllByTableName(String name);
    List<TablesPos> findAllByStatus(String status);
    List<TablesPos> findAllByTableStatusAndStatus(String tablestatus,String status);
    List<TablesPos> findAllByTableStatusAndTableNameContainingAndStatus(String tablestatus,String name,String status);
    List<TablesPos> findAllByStatusAndTableNameContaining(String status,String tableName);
    TablesPos findAllByTableid(Long id);
    List<TablesPos>findAllByTableNameContainingAndStatus(String countryName,String status);
    List<TablesPos>findAllByTableNameContainingAndStatus(String countryName,String status,Pageable pageable);
    TablesPos findFirstByStatus(String status,Sort sort);
    List<TablesPos>findAllByStatus(String status,Pageable pageable);
    List<TablesPos>findAllByConfigurationnameAndStatusAndTableNameContaining(String configName,String status,String tableName);
    List<TablesPos>findAllByConfigurationnameAndStatus(String configName,String status);
    TablesPos findFirstByTableNameContainingAndStatus(String countryName,String status,Sort sort);
    List<TablesPos> findByStatusAndTableNameNotIn(String status,List<String> tablesList);
    List<TablesPos> findByTableNameIn(List<String> tablesList);
    List<TablesPos> findAllByMessageNotNull();
}
