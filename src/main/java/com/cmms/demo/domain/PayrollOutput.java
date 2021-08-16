package com.cmms.demo.domain;

import com.cmms.demo.dto.DriverDayOffTrackingDTO;
import com.cmms.demo.dto.PayrollDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollOutput {
    private int totalPages;
    private List<PayrollDTO> payrolls = new ArrayList<>();

}
