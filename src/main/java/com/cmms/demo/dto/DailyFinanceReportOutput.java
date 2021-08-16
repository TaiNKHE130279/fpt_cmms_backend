package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyFinanceReportOutput {
    private int totalPages;
    private List<DailyFinanceReportDto> ls = new ArrayList<>();
}
