package com.cmms.demo.dto;

import com.cmms.demo.domain.BookingSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingScheduleDTO {
    private Long id;
    private String project_code;
    private String date;

    public static BookingScheduleDTO from(BookingSchedule item){
        BookingScheduleDTO dto = new BookingScheduleDTO();
        dto.setId(item.getId());
        dto.setProject_code(item.getProject().getProject_code());
        dto.setDate(item.getDate().toString());
        return dto;
    }
}
