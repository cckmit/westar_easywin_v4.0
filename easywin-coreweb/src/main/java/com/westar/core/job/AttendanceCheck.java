package com.westar.core.job;

import com.westar.core.service.AttenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 考勤检查
 */
@Component
public class AttendanceCheck {

    @Autowired
    private AttenceService attenceService;

    //每天凌晨4点执行
    @Scheduled(cron="0 0 4 ? * *")
    public void checkAttendance(){
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}