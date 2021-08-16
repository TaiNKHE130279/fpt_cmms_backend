package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.WorkByHourDTO;
import com.cmms.demo.reponsitory.BookingScheduleRepository;
import com.cmms.demo.reponsitory.WorkByHourRepository;
import com.cmms.demo.reponsitory.ScheduleDetailRepository;
import com.cmms.demo.service.WorkByHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class WorkByHourServiceImpl implements WorkByHourService {
    @Autowired
    WorkByHourRepository repository;
    @Autowired
    ProjectServiceImpl projectServiceImpl;
    @Autowired
    GoogleDriveService googleDriveService;
    @Autowired
    private PaymentStatusService statusService;
    @Autowired
    private ScheduleDetailRepository detailRepository;
    @Autowired
    private BookingScheduleRepository bookingRepository;
    @Autowired
    private DriverServiceImpl driverServiceImpl;

    @Override
    public WorkByHourPOJO getOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public WorkByHourPOJO create(WorkByHourDTO dto) {
        ProjectPOJO project = projectServiceImpl.getOne(dto.getProject_code());
        WorkByHourPOJO workByHourPOJO = new WorkByHourPOJO();
        workByHourPOJO.setPrice_per_hour(dto.getPrice_per_hour());
        workByHourPOJO.setProject(project);
        workByHourPOJO.setCreate_date(new Date(System.currentTimeMillis()));
        return repository.save(workByHourPOJO);
    }

    @Override
    public WorkByHourPOJO update(WorkByHourDTO dto){
        WorkByHourPOJO item = getOne(dto.getId());
        if(item != null){
            if(dto.getPrice_per_hour() != null){
                item.setPrice_per_hour(dto.getPrice_per_hour());
            }
            return repository.save(item);
        }
        return null;
    }

    @Override
    public WorkByHourPOJO getByProjectCode(String project_code) {
        return repository.getByProjectCode(project_code);
    }


}
