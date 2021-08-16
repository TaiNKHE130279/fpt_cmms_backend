package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.Holiday;
import com.cmms.demo.dto.RequestToAddHolidayDTO;
import com.cmms.demo.reponsitory.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HolidayService {
    @Autowired
    private HolidayRepository repository;
    @Autowired
    private HolidayTypeService typeService;

    public List<Holiday> add(RequestToAddHolidayDTO dto){
        LocalDate startDate = dto.getBegin_date().toLocalDate();
        LocalDate endDate = dto.getFinish_date().toLocalDate();
        endDate = endDate.plusDays(1);
        List<Holiday> list = new ArrayList<>();
        for(LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)){
            Holiday h = new Holiday();
            h.setDate(Date.valueOf(date));
            h.setType(typeService.getOne(dto.getType_id()));
            if(repository.getByDate(Date.valueOf(date)) == null){
                list.add(h);
            }
        }
        return repository.saveAll(list);
    }

    public Holiday getByDate(Date date){
        return repository.getByDate(date);
    }

}