package com.cmms.demo.endpoint;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.*;
import com.cmms.demo.service.DriverTemporaryService;
import com.cmms.demo.service.ScheduleDetailService;
import com.cmms.demo.service.WorkingConfirmStatusService;
import com.cmms.demo.serviceImpl.MachineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/scheduleDetail")
public class AssignDriverController {
    @Autowired
    private ScheduleDetailService service;
    @Autowired
    private WorkingConfirmStatusService workingStatusService;

    @PostMapping("/assignDrive")
    public ResponseEntity<List<ScheduleDetailDTO>> assignDriver(@RequestBody List<RequestBodyToAssignDriverDTO> lsDto) {
        List<BookingScheduleDetail> lstDetail = service.assignDriver(lsDto);
        List<ScheduleDetailDTO> lstOutput = lstDetail.stream().map(item -> ScheduleDetailDTO.from(item))
                .collect(Collectors.toList());
        return new ResponseEntity<>(lstOutput, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<List<ScheduleDetailDTO>> update(@RequestBody List<ParamToUpdateScheduleDetailDTO> lsDto) {
        List<BookingScheduleDetail> d = service.update(lsDto);
        List<ScheduleDetailDTO> lsOutput = d.stream().map(item -> ScheduleDetailDTO.from(item)).collect(Collectors.toList());
        return new ResponseEntity<>(lsOutput, HttpStatus.OK);
    }

    @GetMapping("/getListDriverNotAssigned")
    public ResponseEntity<List<DriverDTO>> getListDriverNotAssigned(@RequestParam("begin_date") String beginDate,
                                                                    @RequestParam("end_date") String endDate,
                                                                    @RequestParam("begin_time") String beginTime,
                                                                    @RequestParam("finish_time") String finishTime) {
        return new ResponseEntity<>(service.getListDriverNotAssigned(beginDate, endDate, beginTime, finishTime), HttpStatus.OK);
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<ScheduleDetailDTO> getOne(@PathVariable Long id) {
        BookingScheduleDetail b = service.getOne(id);
        return new ResponseEntity<>(ScheduleDetailDTO.from(b), HttpStatus.OK);
    }

    @GetMapping("/getAllWorkingStatus")
    public ResponseEntity<List<WorkingStatusDTO>> getAllWorkingStatus() {
        List<WorkingConfirmStatus> ls = workingStatusService.getAll();
        List<WorkingStatusDTO> lsDto = ls.stream().map(WorkingStatusDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lsDto, HttpStatus.OK);
    }

    @GetMapping("/getOneWorkingStatus/{statusId}")
    public ResponseEntity<WorkingStatusDTO> getOneWorkingStatus(@PathVariable Long statusId) {
        return new ResponseEntity<>(WorkingStatusDTO.from(workingStatusService.getOne(statusId)), HttpStatus.OK);
    }

    @GetMapping("/getListDriverAssigned/{projectCode}")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'VIEW', this)}")
    public ResponseEntity<List<DriverDTO>> getListDriverAssigned(@PathVariable String projectCode) {
        return new ResponseEntity<>(service.getListDriverAssigned(projectCode), HttpStatus.OK);
    }

    @GetMapping("/getScheduleDetailByDriver/{driverCode}")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'VIEW', this)}")
    public ResponseEntity<List<ScheduleDetailByDriverDTO>> getScheduleDetailByDriver(@PathVariable String driverCode) {
        return new ResponseEntity<>(service.getScheduleDetailByDriver(driverCode), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id), HttpStatus.OK);
    }

    @GetMapping("/getListTaskOfDriverOnWeek")
    public ResponseEntity<List<ListTaskOfDriverDTO>> getListTaskByDriverAndDate(@RequestParam("driverCode") String driverCode) {
        return new ResponseEntity<>(service.getListTaskByDriverAndDate(driverCode), HttpStatus.OK);
    }

    @GetMapping("/historyTask")
    public ResponseEntity<HistoryTaskOfDriverOutput> getListHistoryTaskOfDriver(@RequestParam("page") int pageIndex,
                                                                                @RequestParam("limit") int pageSize,
                                                                                @RequestParam("driverCode") String driverCode,
                                                                                @RequestParam(value = "customerName", required = false) String customerName,
                                                                                @RequestParam(value = "date", required = false) String date) {
        return new ResponseEntity<>(service.getListHistoryTaskOfDriver(pageIndex, pageSize, driverCode, date, customerName), HttpStatus.OK);
    }

    @PutMapping("/updateTimeByDriver")
    public ResponseEntity<String> updateTimeByDriver(@RequestParam("id") Long id,
                                                     @RequestParam("time") String time,
                                                     @RequestBody MultipartFile image) {
        return new ResponseEntity<>(service.updateTimeByDriver(id, time, image), HttpStatus.OK);
    }

    @PostMapping("/checkDate")
    public ResponseEntity<Integer> checkDate(@RequestParam("projectCode") String projectCode,
                                             @RequestParam("driverCode") String code,
                                             @RequestParam("date") String d1) {
        return new ResponseEntity<Integer>(service.checkDate(projectCode, code, d1), HttpStatus.OK);
    }

    @PutMapping("/executeCompleteBtn")
    public ResponseEntity<Integer> executeCompleteBtn(@RequestParam("projectCode") String projectCode,
                                                      @RequestParam("date") String date) {
        return new ResponseEntity<>(service.executeCompleteBtn(projectCode, date), HttpStatus.OK);
    }

    @GetMapping("/getListNeedConfirm")
    public ResponseEntity<HistoryTaskOfDriverOutput> getListNeedConfirm(@RequestParam("page") int pageIndex,
                                                                        @RequestParam("limit") int pageSize,
                                                                        @RequestParam(value = "typeId", required = false) Long typeId,
                                                                        @RequestParam(value = "driverName", required = false) String driverName,
                                                                        @RequestParam(value = "from", required = false) String from,
                                                                        @RequestParam(value = "to", required = false) String to) {
        return new ResponseEntity<>(service.getListNeedConfirm(pageIndex, pageSize, typeId, driverName, from, to), HttpStatus.OK);
    }

    @PutMapping("/updateWorkingStatus/{id}")
    public ResponseEntity<Integer> updateWorkingStatus(@PathVariable Long id,
                                                       @RequestParam("typeId") Long typeId) {
        return new ResponseEntity<>(service.updateWorkingStatus(id, typeId), HttpStatus.OK);
    }
}
