package com.cmms.demo.schedule;

import com.cmms.demo.serviceImpl.DriverDayOffTrackingSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Configuration
@EnableScheduling
public class ScheduledTasks implements SchedulingConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    @Autowired
    DriverDayOffTrackingSerivce serivce;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //taskRegistrar.setScheduler(poolScheduler());
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                // edit method here
              //  serivce.processApprovedRequest();

            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                Calendar nextExecutionTime = new GregorianCalendar();
                Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
               // nextExecutionTime.add(Calendar.HOUR_OF_DAY, getNewExecutionTime());
                nextExecutionTime.add(Calendar.HOUR, getNewExecutionTime());
                return nextExecutionTime.getTime();
            }
        });
    }

    private int getNewExecutionTime() {
        //Load Your execution time from database or property file
        return 4;
    }
}
