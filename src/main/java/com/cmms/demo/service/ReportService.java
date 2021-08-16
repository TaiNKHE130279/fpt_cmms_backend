package com.cmms.demo.service;

import com.cmms.demo.dto.DriverReportDTO;

import java.util.List;

public interface ReportService {
    List<DriverReportDTO> getDriverReport(String fromDate, String endDate);
}
