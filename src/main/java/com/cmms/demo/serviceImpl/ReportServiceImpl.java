package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.BookingScheduleDetail;
import com.cmms.demo.domain.DriverDayOffTrackingDetailPOJO;
import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.dto.DriverReportDTO;
import com.cmms.demo.dto.DriverReportDetailDTO;
import com.cmms.demo.reponsitory.DriverDayOffTrackingDetailRepository;
import com.cmms.demo.reponsitory.ScheduleDetailRepository;
import com.cmms.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ScheduleDetailRepository detailRepository;
    @Autowired
    private DriverDayOffTrackingDetailRepository driverDayOffTrackingDetailRepository;
    @Autowired
    private DriverServiceImpl driverService;

    public DriverReportDTO getOneDriverReport(String driverCode, String from, String end) {
        DriverReportDTO dto = new DriverReportDTO();
        try {
            java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
            java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            LocalDate startDate = new Date(fromDate.getTime()).toLocalDate();
            LocalDate endDate = new Date(toDate.getTime()).toLocalDate();
            endDate = endDate.plusDays(1);
            List<BookingScheduleDetail> lsDetail = detailRepository.getListTaskByDriverAndDate(driverCode
                    , Date.valueOf(startDate), Date.valueOf(endDate));
            DriverPOJO driver = driverService.getOne(driverCode);
            dto.setDriver_code(driverCode);
            dto.setDriver_name(driver.getName());
            dto.setPhone(driver.getPhone());
            dto.setAddress(driver.getAddress());
            HashMap<String, String> map = getTotalWorkTime(lsDetail);
            dto.setTotal_work_time(map.get("workTime"));
            dto.setTotal_over_time(map.get("overTime"));
            List<DriverDayOffTrackingDetailPOJO> lsDayOff = driverDayOffTrackingDetailRepository.getListDayOff(driverCode
                    , Date.valueOf(startDate), Date.valueOf(endDate));
            dto.setNumber_day_off(lsDayOff.size());
            dto.setLsDayOff(getListDayOff(lsDayOff));
            dto.setLsDetail(getDetail(lsDetail));
            return dto;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> getTotalWorkTime(List<BookingScheduleDetail> ls) {
        HashMap<String, String> map = new HashMap<>();
        int hours = 0, minutes = 0, overTimeHour = 0;
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < ls.size(); i++) {
            try {
                if (ls.get(i).getWorked_hours() != null) {
                    java.util.Date t = format.parse(ls.get(i).getWorked_hours().toString());
                    calendar.setTime(t);
                    hours += calendar.get(Calendar.HOUR);
                    if (calendar.get(Calendar.HOUR) > 8) {
                        overTimeHour += (calendar.get(Calendar.HOUR) - 8);
                    }
                    minutes += calendar.get(Calendar.MINUTE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        hours += minutes / 60;
        overTimeHour += minutes / 60;
        minutes = minutes % 60;
        String workTime = "";
        String overTime = "";
        if (minutes == 0) {
            workTime = hours + ":" + minutes + "0";
            overTime = overTimeHour + ":" + minutes + "0";
        } else if (minutes < 10) {
            workTime = hours + ":" + "0" + minutes;
            overTime = overTimeHour + ":" + "0" + minutes;
        } else {
            workTime = hours + ":" + minutes;
            overTime = overTimeHour + ":" + minutes;
        }
        map.put("workTime", workTime);
        map.put("overTime", overTime);
        return map;
    }

    public List<DriverReportDetailDTO> getDetail(List<BookingScheduleDetail> ls) {
        List<DriverReportDetailDTO> lsOutput = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < ls.size(); i++) {
            BookingScheduleDetail sd = ls.get(i);
            DriverReportDetailDTO dto = new DriverReportDetailDTO();
            dto.setDate(sd.getBookingSchedule().getDate().toString());
            try {
                if (sd.getWorked_hours() != null) {
                    dto.setWork_time(format.format(sd.getWorked_hours()));
                    java.util.Date t = format.parse(sd.getWorked_hours().toString());
                    calendar.setTime(t);
                    if (calendar.get(Calendar.HOUR) > 8) {
                        int overTimeHour = (calendar.get(Calendar.HOUR) - 8);
                        String[] str = sd.getWorked_hours().toString().split(":");
                        dto.setOver_time(overTimeHour + ":" + str[1]);
                    } else {
                        dto.setOver_time(String.valueOf(0));
                    }
                }
                lsOutput.add(dto);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return lsOutput;
    }

    public List<String> getListDayOff(List<DriverDayOffTrackingDetailPOJO> ls){
        List<String> lsOutput = new ArrayList<>();
        for (int i =0; i<ls.size(); i++){
            String date = ls.get(i).getDate().toString();
            if(date != null){
                lsOutput.add(date);
            }
        }
        return lsOutput;
    }

    @Override
    public List<DriverReportDTO> getDriverReport(String fromDate, String endDate) {
        List<DriverReportDTO> lsOutput = new ArrayList<>();
        List<DriverPOJO> lsDriver = driverService.getAll();
        for (DriverPOJO d : lsDriver) {
            DriverReportDTO dto = getOneDriverReport(d.getDrive_code(), fromDate, endDate);
            if (dto != null) {
                lsOutput.add(dto);
            }
        }
        return lsOutput;
    }
}
