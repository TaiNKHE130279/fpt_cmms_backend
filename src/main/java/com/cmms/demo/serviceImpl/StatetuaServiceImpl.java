package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.BonusPOJO;
import com.cmms.demo.dto.BonusDTO;
import com.cmms.demo.reponsitory.BonusRepository;
import com.cmms.demo.service.StatutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StatetuaServiceImpl implements StatutesService {
    @Autowired
    BonusRepository bonusRepository;

    @Override
    public BonusPOJO updateBonus(BonusDTO dto) {
        BonusPOJO bonusPOJO = bonusRepository.getById(dto.getId());
        if (bonusPOJO.isDefault()) {
            bonusPOJO.setType(dto.getUnit());
            bonusPOJO.setValue(dto.getValue());
            //bonusPOJO.setName();
            return bonusRepository.save(bonusPOJO);
        }
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Không được phép cập nhật.");
    }

    @Override
    public List<BonusPOJO> getAllBonus() {
        return null;
    }
}
