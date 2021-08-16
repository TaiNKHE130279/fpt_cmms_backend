package com.cmms.demo.domain;

import com.cmms.demo.dto.PayrollDTO;
import com.cmms.demo.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserOutput {
    private int totalPages;
    private List<UserDTO> accounts = new ArrayList<>();
}
