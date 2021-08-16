package com.cmms.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class DriverOutput {
    private int totalPages;
    private List<DriverDTO> driverList = new ArrayList<>();

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<DriverDTO> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<DriverDTO> driverList) {
        this.driverList = driverList;
    }
}
