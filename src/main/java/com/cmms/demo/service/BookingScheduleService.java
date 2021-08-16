package com.cmms.demo.service;

import com.cmms.demo.domain.BookingSchedule;
import com.cmms.demo.dto.DataToFillCalendarDTO;
import com.cmms.demo.dto.FullProjectInfoDTO;
import com.cmms.demo.dto.ProjectInfoDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingScheduleService {
    List<BookingSchedule> addBookingSchedule(String projectCode);

    BookingSchedule getOne(String projectCode, String strDate);

    List<BookingSchedule> update(String projectCode);

    Page<BookingSchedule> getPageByProjectCode(int pageIndex, int pageSize, String projectCode);

    List<DataToFillCalendarDTO> getDataToFillCalendar(String begin, String end);

    ProjectInfoDTO getProjectInfo(String projectCode, String strDate);

    FullProjectInfoDTO getFullProjectInfo(String projectCode, int pageIndex, int pageSize, String driverName, String from, String to);

    int cancelBooking(String projectCode, String begin, String end);
}
