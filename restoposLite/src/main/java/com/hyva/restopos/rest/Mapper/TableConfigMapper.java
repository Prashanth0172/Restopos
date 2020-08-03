package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.TableConfig;
import com.hyva.restopos.rest.pojo.TableConfigPojo;

import java.util.ArrayList;
import java.util.List;

public class TableConfigMapper {

    public static TableConfig MapTableConfigPojoToEntity(TableConfigPojo tableConfigPojo){
        TableConfig tableConfig = new TableConfig();
        tableConfig.setTableconfigid(tableConfigPojo.getId());
        tableConfig.setConfigurationname(tableConfigPojo.getZone_name());
        tableConfig.setColumntableconfig(tableConfigPojo.getNo_of_col());
        tableConfig.setStatus(tableConfigPojo.getStatus());
        tableConfig.setRowtableconfig(tableConfigPojo.getNo_of_rows());

        return tableConfig;
    }

    public static List<TableConfigPojo> mapEntityToPojo(List<TableConfig> List) {
        List<TableConfigPojo> list = new ArrayList<>();
        for (TableConfig config : List) {
            TableConfigPojo tableConfigPojo = new TableConfigPojo();
            tableConfigPojo.setId(config.getTableconfigid());
            tableConfigPojo.setZone_name(config.getConfigurationname());
            tableConfigPojo.setNo_of_col(config.getColumntableconfig());
            tableConfigPojo.setNo_of_rows(config.getRowtableconfig());
            tableConfigPojo.setStatus(config.getStatus());
            list.add(tableConfigPojo);
        }
        return list;
    }
}
