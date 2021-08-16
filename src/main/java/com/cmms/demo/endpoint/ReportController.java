package com.cmms.demo.endpoint;

import com.cmms.demo.dto.DriverReportDTO;
import com.cmms.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/report", name = "Báo cáo")
public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping(value = "/driverReport", name = "Xem báo cáo thời gian làm của lái xe")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '/driverReport', 'GET', this)}")
    public ResponseEntity<List<DriverReportDTO>> getDriverReport(@RequestParam("from") String from,
                                                                 @RequestParam("to") String to){
        return new ResponseEntity<>(service.getDriverReport(from, to), HttpStatus.OK);
    }
}
