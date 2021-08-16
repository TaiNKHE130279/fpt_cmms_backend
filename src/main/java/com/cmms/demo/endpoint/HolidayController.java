package com.cmms.demo.endpoint;

import com.cmms.demo.domain.Holiday;
import com.cmms.demo.dto.HolidayDTO;
import com.cmms.demo.dto.RequestToAddHolidayDTO;
import com.cmms.demo.serviceImpl.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/holiday")
public class HolidayController {
    @Autowired
    private HolidayService service;

    @PostMapping
    public ResponseEntity<List<HolidayDTO>> addHoliday(@RequestBody final RequestToAddHolidayDTO dto){
        List<Holiday> lsHolidays = service.add(dto);
        List<HolidayDTO> lsDto = lsHolidays.stream().map(HolidayDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lsDto, HttpStatus.OK);
    }
}
