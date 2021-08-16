package com.cmms.demo.service;

import com.cmms.demo.domain.DriverTemporary;
import com.cmms.demo.dto.RequestBodyToAssignTemporaryDTO;

import java.util.List;

public interface DriverTemporaryService {
    List<DriverTemporary> assignTemporary(RequestBodyToAssignTemporaryDTO dto);
}
