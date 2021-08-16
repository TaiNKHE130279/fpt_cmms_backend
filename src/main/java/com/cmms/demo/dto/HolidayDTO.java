package com.cmms.demo.dto;

import com.cmms.demo.domain.Holiday;

import java.sql.Date;

public class HolidayDTO {
    private Long id;
    private Date date;
    private HolidayTypeDTO type;

    public static HolidayDTO from(Holiday item){
        HolidayDTO dto = new HolidayDTO();
        dto.setId(item.getId());
        dto.setDate(item.getDate());

        HolidayTypeDTO type= new HolidayTypeDTO();
        type.setId(item.getType().getId());
        type.setType_name(item.getType().getType_name());
        dto.setType(type);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HolidayTypeDTO getType() {
        return type;
    }

    public void setType(HolidayTypeDTO type) {
        this.type = type;
    }
}
