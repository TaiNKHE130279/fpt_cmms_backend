package com.cmms.demo.endpoint;

import com.cmms.demo.domain.BookingSchedule;
import com.cmms.demo.dto.*;
import com.cmms.demo.service.BookingScheduleService;
import com.cmms.demo.serviceImpl.BookingScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/bookingSchedule",name = "Quản lý lịch đặt")
public class BookingController {
    @Autowired
    private BookingScheduleService service;

    @PostMapping(value = "/{code}", name = "Tạo lịch đặt")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'POST', this)}")
    public ResponseEntity<List<BookingScheduleDTO>> addBooking(@PathVariable String code) {
        List<BookingSchedule> ls = service.addBookingSchedule(code);
        List<BookingScheduleDTO> lsDto = ls.stream().map(BookingScheduleDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lsDto, HttpStatus.OK);
    }

    @PutMapping("/{code}")
    public ResponseEntity<List<BookingScheduleDTO>> update(@PathVariable String code) {
        List<BookingSchedule> ls = service.update(code);
        List<BookingScheduleDTO> lsDto = ls.stream().map(BookingScheduleDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lsDto, HttpStatus.OK);
    }

    @GetMapping("/getOne")
    public ResponseEntity<BookingScheduleDTO> getOne(@RequestParam("projectCode") String projectCode,
            @RequestParam("date") String date) {
        BookingSchedule b = service.getOne(projectCode, date);
        return new ResponseEntity<>(BookingScheduleDTO.from(b), HttpStatus.OK);
    }

    @GetMapping("/getByProject")
    public ResponseEntity<BookingScheduleOutput> getListByProjectCode(@RequestParam("page") int pageIndex,
            @RequestParam("limit") int pageSize, @RequestParam("projectCode") String code) {
        Page<BookingSchedule> page = service.getPageByProjectCode(pageIndex, pageSize, code);
        List<BookingScheduleDTO> lsDto = page.stream().map(BookingScheduleDTO::from).collect(Collectors.toList());
        BookingScheduleOutput output = new BookingScheduleOutput(page.getTotalPages(), lsDto);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping(value = "/fillCalendar")
    public ResponseEntity<List<DataToFillCalendarDTO>> getDataToFillCalendar(@RequestParam("begin") String begin,
            @RequestParam("end") String end) {
        return new ResponseEntity<>(service.getDataToFillCalendar(begin, end), HttpStatus.OK);
    }

    @GetMapping(value = "/projectInfo",name = "Xem chi tiết công việc trong một ngày")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'VIEW', this)}")
    public ResponseEntity<ProjectInfoDTO> getProjectInfo(@RequestParam("projectCode") String code,
            @RequestParam("date") String date) {
        return new ResponseEntity<>(service.getProjectInfo(code, date), HttpStatus.OK);
    }

    @GetMapping(value = "/fullProjectInfo",name = "Xem chi tiết toàn bộ công việc")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'VIEW', this)}")
    public ResponseEntity<FullProjectInfoDTO> getFullProjectInfo(@RequestParam("projectCode") String code,
                                                                 @RequestParam("page") int pageIndex,
                                                                 @RequestParam("limit") int pageSize,
                                                                 @RequestParam(value = "driverName", required = false) String driverName,
                                                                 @RequestParam(value = "from", required = false) String from,
                                                                 @RequestParam(value = "to", required = false) String to) {
        return new ResponseEntity<>(service.getFullProjectInfo(code, pageIndex, pageSize, driverName, from, to), HttpStatus.OK);
    }

    @DeleteMapping(name = "Hủy lịch")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'DELETE', this)}")
    public ResponseEntity<Integer> cancelBooking(@RequestParam("projectCode") String code,
            @RequestParam("begin") String begin, @RequestParam("end") String end) {
        int count = service.cancelBooking(code, begin, end);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}
