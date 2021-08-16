package com.cmms.demo.endpoint;

import com.cmms.demo.domain.BonusPOJO;
import com.cmms.demo.dto.BonusDTO;
import com.cmms.demo.service.StatutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/statutes")
public class StatutesController {

    @Autowired
    StatutesService service;

    @GetMapping(value = "all-bonus")
    public ResponseEntity<List<BonusDTO>> allBonus() {
        List<BonusPOJO> list = service.getAllBonus();
        return new ResponseEntity<>(list.stream().map(bonusPOJO -> BonusDTO.from(bonusPOJO)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(value = "update-bonus")
    public ResponseEntity<BonusDTO> updateBonus(@RequestBody BonusDTO dto) {
        BonusPOJO bonusPOJO = service.updateBonus(dto);
        return new ResponseEntity<>(BonusDTO.from(bonusPOJO), HttpStatus.OK);
    }
}
