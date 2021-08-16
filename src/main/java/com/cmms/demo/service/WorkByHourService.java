package com.cmms.demo.service;

import com.cmms.demo.domain.WorkByHourPOJO;
import com.cmms.demo.dto.WorkByHourDTO;

public interface WorkByHourService {
    WorkByHourPOJO getOne(Long id);

    WorkByHourPOJO create(WorkByHourDTO dto);

    WorkByHourPOJO update(WorkByHourDTO dto);

    WorkByHourPOJO getByProjectCode(String project_code);
}
