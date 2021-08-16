package com.cmms.demo.service;

import com.cmms.demo.domain.DailyFinanceReport;
import com.cmms.demo.dto.DailyFinanceReportDto;
import com.cmms.demo.dto.DailyFinanceReportOutput;

public interface DailyFinanceReportService {
    DailyFinanceReportDto addDailyReport(DailyFinanceReportDto dto);

    DailyFinanceReportOutput filter(int pageIndex, int pageSize, Integer paymentType, String fromDate, String toDate, String title);

    DailyFinanceReportDto getOne(Long id);

    DailyFinanceReportDto updateDailyFinanceRp(DailyFinanceReportDto item);

    String deleteDailyFinanceRp(Long id);
}
