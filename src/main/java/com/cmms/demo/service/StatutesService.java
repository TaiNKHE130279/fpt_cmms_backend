package com.cmms.demo.service;

import com.cmms.demo.domain.BonusPOJO;
import com.cmms.demo.dto.BonusDTO;

import java.util.List;

public interface StatutesService {

    BonusPOJO updateBonus(BonusDTO dto);

    List<BonusPOJO> getAllBonus();
}
