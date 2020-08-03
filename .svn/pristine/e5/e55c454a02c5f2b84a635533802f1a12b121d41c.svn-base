package com.hyva.restopos.config;

import com.hyva.restopos.rest.entities.TablesPos;
import com.hyva.restopos.rest.pojo.MailSchedulerData;
import com.hyva.restopos.rest.repository.TablesPosRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@Transactional
public class DynamicJob implements Job {
    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    TablesPosRepository tablesPosRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Map<String, Object> mergedJobDataMap = jobExecutionContext.getMergedJobDataMap().getWrappedMap();
        MailSchedulerData schedulerProps = (MailSchedulerData) mergedJobDataMap.get("jobData");
        log.info("Executing Job = " + jobExecutionContext.getJobDetail().getKey());
        if(StringUtils.equalsIgnoreCase(schedulerProps.getReportName(),"Table Reservation")){
            TablesPos tablesPos = tablesPosRepository.findAllByTableid(Long.parseLong(schedulerProps.getTableName()));
            tablesPos.setTableStatus("Reserved");
//            TableReservation tableReservation=hiposDAO.gettableReservation(Long.parseLong(schedulerProps.getReservationId()));
//            if(tableReservation!=null)
//            tablesPos.setUseraccount_id(tableReservation.getGuestName());
            tablesPosRepository.save(tablesPos);
        }
    }
}
