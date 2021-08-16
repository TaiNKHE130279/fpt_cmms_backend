package com.cmms.demo.domain;

import com.cmms.demo.dto.DriverDTO;
import com.cmms.demo.dto.DriverDayOffTrackingDTO;

import java.util.ArrayList;
import java.util.List;

public class DayOffOutput {
    private int totalPages;
    private List<DriverDayOffTrackingDTO> driverList = new ArrayList<>();

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<DriverDayOffTrackingDTO> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<DriverDayOffTrackingDTO> driverList) {
        this.driverList = driverList;
    }
}
