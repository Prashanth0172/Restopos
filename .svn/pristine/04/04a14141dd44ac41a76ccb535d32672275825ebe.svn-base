package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.TableConfig;
import com.hyva.restopos.rest.pojo.TableConfigPojo;

import java.util.ArrayList;
import java.util.List;

public class TableConfigMapper {

    public static TableConfig MapTableConfigPojoToEntity(TableConfigPojo tableConfigPojo){
        TableConfig tableConfig = new TableConfig();
        tableConfig.setTableconfigid(tableConfigPojo.getTableconfigid());
        tableConfig.setConfigurationname(tableConfigPojo.getConfigurationname());
        tableConfig.setColumntableconfig(tableConfigPojo.getColumntableconfig());
        tableConfig.setStatus(tableConfigPojo.getStatus());
        tableConfig.setRowtableconfig(tableConfigPojo.getRowtableconfig());

        return tableConfig;
    }

    public static List<TableConfigPojo> mapEntityToPojo(List<TableConfig> List) {
        List<TableConfigPojo> list = new ArrayList<>();
        for (TableConfig config : List) {
            TableConfigPojo tableConfigPojo = new TableConfigPojo();
            tableConfigPojo.setTableconfigid(config.getTableconfigid());
            tableConfigPojo.setConfigurationname(config.getConfigurationname());
            tableConfigPojo.setColumntableconfig(config.getColumntableconfig());
            tableConfigPojo.setRowtableconfig(config.getRowtableconfig());
            tableConfigPojo.setStatus(config.getStatus());
            list.add(tableConfigPojo);
        }
        return list;
    }
}
