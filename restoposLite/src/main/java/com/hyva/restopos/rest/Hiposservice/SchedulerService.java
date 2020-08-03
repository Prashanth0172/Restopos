package com.hyva.restopos.rest.Hiposservice;

import com.hyva.restopos.config.DynamicJob;
import com.hyva.restopos.config.QuartzConfiguration;
import com.hyva.restopos.rest.pojo.MailSchedulerData;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class SchedulerService {
    private final Logger log = Logger.getLogger(getClass());

    public static final String DAILY = "DAILY";
    public static final String YEARLY = "YEARLY";
    public static final String WEEKLY = "WEEKLY";
    public static final String MONTHLY = "MONTHLY";


    @Autowired
    private SchedulerFactoryBean schedFactory;

    public String schedule(MailSchedulerData mailSchedulerData) {
        String scheduled = "Job is Scheduled!!";
        try {
            JobDetailFactoryBean jdfb = QuartzConfiguration.createJobDetail(DynamicJob.class);
            jdfb.setBeanName(getJobDetailName(mailSchedulerData));
            jdfb.afterPropertiesSet();
            JobDataMap jobDataMap = jdfb.getJobDataMap();
            jobDataMap.put("jobData", mailSchedulerData);


            String cronTriggerString = createCronTrigger(mailSchedulerData);
            CronTriggerFactoryBean cronTrigger = QuartzConfiguration.createCronTrigger(jdfb.getObject(), cronTriggerString);
            cronTrigger.setBeanName(getTriggerName(mailSchedulerData));
            cronTrigger.afterPropertiesSet();

            unschedule(mailSchedulerData);
            //Register trigger with scheduler
            schedFactory.getScheduler().scheduleJob(jdfb.getObject(), cronTrigger.getObject());
            log.info("job is scheduled = " + cronTrigger);
        } catch (Exception e) {
            log.error("e = ", e);
            scheduled = "Could not schedule a job. " + e.getMessage();
        }
        return scheduled;
    }

    public String unschedule(MailSchedulerData mailSchedulerData) {
        String scheduled = "Job is Unscheduled!!";
        TriggerKey tkey = new TriggerKey(getTriggerName(mailSchedulerData));
        JobKey jkey = new JobKey(getJobDetailName(mailSchedulerData));
        try {
            schedFactory.getScheduler().unscheduleJob(tkey);
            schedFactory.getScheduler().deleteJob(jkey);
        } catch (SchedulerException e) {
            scheduled = "Error while unscheduling " + e.getMessage();
        }
        return scheduled;
    }

    private String getJobDetailName(MailSchedulerData mailSchedulerData) {
        return mailSchedulerData.getReportName() + mailSchedulerData.getDbKeyword() + "jbdtls";

    }

    private String getTriggerName(MailSchedulerData mailSchedulerData) {
        return mailSchedulerData.getReportName() + mailSchedulerData.getDbKeyword() + "trigger";
    }

    private String createCronTrigger(MailSchedulerData mailSchedulerData) {
        StringBuilder cronString = new StringBuilder("0 0 12 1/1 * ? *");//everyDay night 12'O clock
        String scheduleType = mailSchedulerData.getScheduleType();
        String time = mailSchedulerData.getScheduleTime();
        StringBuilder space = new StringBuilder(" ");
        StringBuilder tempString = new StringBuilder("0");//seconds
        tempString.append(space);
        String[] split = time.split(":");
        tempString.append(split[1]);//appending minutes
        tempString.append(space);
        tempString.append(split[0]);//appending hours;
        tempString.append(space);
        if (scheduleType.equalsIgnoreCase(DAILY)) {
            tempString.append("1/1");//everyDay
            tempString.append(space);
//        } else if (mailSchedulerData.getScheduleType().equalsIgnoreCase(WEEKLY)) {
//            tempString.append("?");//dont want to specify day
//            tempString.append(space);
//            tempString.append("*");//match any
//            tempString.append(space);
//            tempString.append("TUE");
//        }
            tempString.append("*");
            tempString.append(space);
            tempString.append("?");
            tempString.append(space);
            tempString.append("*");
        } else if(scheduleType.equalsIgnoreCase(YEARLY)) {
            String date = mailSchedulerData.getScheduleDate();
            String[] split1 = date.split("/");
            tempString.append(split1[0]);//date
            tempString.append(space);
            tempString.append(split1[1]);//month
            tempString.append(space);
            tempString.append("?");
            tempString.append(space);
            tempString.append(split1[2]);//year
        }
        log.info("cronExpression = " + tempString);
        return tempString.toString();
    }

}
