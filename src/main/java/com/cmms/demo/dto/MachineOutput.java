package com.cmms.demo.dto;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class MachineOutput {
    private int totalPages;
    private List<MachineDTO> machineList = new ArrayList<>();

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<MachineDTO> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<MachineDTO> page) {
        this.machineList = page;
    }
}
