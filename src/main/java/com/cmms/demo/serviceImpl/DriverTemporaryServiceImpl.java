package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.BookingScheduleDetail;
import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.domain.DriverTemporary;
import com.cmms.demo.dto.RequestBodyToAssignTemporaryDTO;
import com.cmms.demo.reponsitory.DriverTemporaryRepository;
import com.cmms.demo.reponsitory.ScheduleDetailRepository;
import com.cmms.demo.service.DriverTemporaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriverTemporaryServiceImpl implements DriverTemporaryService {
    @Autowired
    private DriverTemporaryRepository repository;
    @Autowired
    private ScheduleDetailRepository detailRepository;
    @Autowired
    private DriverServiceImpl driverServiceImpl;

    public DriverTemporary convertTo(String projectCode, String oldDriverCode, String driverCodeTemporary, Date date
            , String beginTime, String finishTime){
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            DriverTemporary dt = new DriverTemporary();
            dt.setDriver_code(driverCodeTemporary);
            DriverPOJO d = driverServiceImpl.getOne(driverCodeTemporary);
            dt.setMachine_code(d.getDrive_code());
            dt.setMachine_code(d.getMachine().getMachine_code());
            dt.setBegin_time(new Time(format.parse(beginTime).getTime()));
            dt.setFinish_time(new Time(format.parse(finishTime).getTime()));
            BookingScheduleDetail bs = detailRepository.getDetailByDriverAndDate(oldDriverCode, date, projectCode);
            bs.setHas_temporary(true);
            detailRepository.save(bs);
            dt.setDetail(bs);
            return dt;
        }catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<DriverTemporary> assignTemporary(RequestBodyToAssignTemporaryDTO dto){
        List<DriverTemporary> lsOutput = new ArrayList<>();
        try {
            java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getBegin_date());
            java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getEnd_date());
            LocalDate startDate = new Date(fromDate.getTime()).toLocalDate();
            LocalDate endDate = new Date(toDate.getTime()).toLocalDate();
            endDate = endDate.plusDays(1);
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                String beginTime = "";
                String finishTime = "";
                if (date.isEqual(startDate)) {
                    beginTime = dto.getBegin_time();
                    if(startDate.isEqual(endDate.minusDays(1))){
                        finishTime = dto.getFinish_time();
                    }else {
                        finishTime = "17:00:00";
                    }
                } else if (date.isEqual(endDate.minusDays(1))) {
                    beginTime = "8:00:00";
                    finishTime = dto.getFinish_time();
                } else {
                    beginTime = "8:00:00";
                    finishTime = "17:00:00";
                }
                lsOutput.add(convertTo(dto.getProject_code(),dto.getDriver_code(),dto.getTemporary_driver_code()
                        ,Date.valueOf(date), beginTime, finishTime));
            }
            if(lsOutput != null && lsOutput.size() >0){
                return repository.saveAll(lsOutput);
            }else{
                return null;
            }
        }catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DriverTemporary getOneByScheduleDetailId(Long scheduleDetailId){
        DriverTemporary d = repository.getOneByScheduleDetailId(scheduleDetailId);
        return d;
    }
}
