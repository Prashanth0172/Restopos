package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.TableConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableConfigRepository extends JpaRepository<TableConfig,Long> {
    TableConfig findAllByConfigurationnameAndTableconfigidNotIn(String name,Long id);
    TableConfig findAllByConfigurationname(String name);
    TableConfig findByConfigurationnameAndRowtableconfigAndColumntableconfig(String name,int row,int column);
    TableConfig findByConfigurationnameAndRowtableconfigAndColumntableconfigAndTableconfigidNotIn(String name,int row,int column,Long id);
    List<TableConfig> findAllByStatus(String status);
    List<TableConfig>findAllByConfigurationnameContainingAndStatus(String countryName,String status);
    List<TableConfig>findAllByConfigurationnameContainingAndStatus(String countryName,String status,Pageable pageable);
    TableConfig findFirstByStatus(String status,Sort sort);
    List<TableConfig>findAllByStatus(String status,Pageable pageable);
    TableConfig findFirstByConfigurationnameContainingAndStatus(String countryName,String status,Sort sort);
    TableConfig findAllByTableconfigid(Long id);
}
