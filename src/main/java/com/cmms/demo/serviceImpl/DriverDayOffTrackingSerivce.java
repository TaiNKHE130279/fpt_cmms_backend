package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.DriverDTO;
import com.cmms.demo.dto.DriverDayOffTrackingDTO;
import com.cmms.demo.dto.DriverDayOffTrackingDetailDTO;
import com.cmms.demo.reponsitory.DayOffRequestRepository;
import com.cmms.demo.reponsitory.DriverDayOffTrackingDetailRepository;
import com.cmms.demo.reponsitory.DriverDayOffTrackingRepository;
import com.cmms.demo.reponsitory.RequestDayOffStatusRepository;
import com.cmms.demo.specification.DayOffSpecs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DriverDayOffTrackingSerivce {
    @Autowired
    DayOffRequestRepository dayOffRequestRepository;
    @Autowired
    ModelMapper modelMapper;


    @Autowired
    DriverServiceImpl driverServiceImpl;

    @Autowired
    DriverDayOffTrackingRepository trackingRepository;

    @Autowired
    RequestDayOffStatusRepository statusRepository;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    DriverDayOffTrackingDetailRepository dayOffTrackingDetailRepository;

    public List<DriverDayOffTracking> getAllRequestOfUser() {
        List<DriverDayOffTracking> offTrackings = new ArrayList<>();
        String username = userServiceImpl.currentAccount();
        offTrackings = trackingRepository.getRequestListOfUser(username);
        return offTrackings;
    }

    public Page<DriverDayOffTracking> getAllRequest(int pageIndex, int pageSize, String driver_name, Long status, String start, String end) {
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by("id").descending());
        Page<DriverDayOffTracking> offTrackings = trackingRepository.findAll(Specification.where(DayOffSpecs.filter(driver_name, status, start, end)), pageable);
        return offTrackings;
    }

    public DriverDayOffTracking createRequest(DriverDayOffTracking tracking) {
        setStatus(tracking, "PENDING");
        tracking.setDate_create(new Date(System.currentTimeMillis()));
        return trackingRepository.save(tracking);
    }

    public DriverDayOffTracking updateRequest(DriverDayOffTrackingDTO tracking) {
        DriverDayOffTracking offTracking = findOneById(tracking.getId());
        if (offTracking.getId() != null) {
            if (!offTracking.isProcessed()) {
                offTracking.setDescription(tracking.getDescription());
                offTracking.setEnd_date(tracking.getEnd_date());
                offTracking.setStart_date(tracking.getStart_date());
                return trackingRepository.save(offTracking);
            }
        }

        return null;
    }

    public DriverDayOffTracking findOneById(Long id) {
        return trackingRepository.findById(id).orElse(null);
    }


    public DriverDayOffTracking confirmRequest(DriverDayOffTracking tracking) {
        setStatus(tracking, "APPROVED");
        tracking.setProcessed(true);
        tracking.setApproved(true);
        createDetailByTracking(tracking);
        return trackingRepository.save(tracking);
    }

    public void createDetailByTracking(DriverDayOffTracking tracking) {
        List<Date> dateList = getDatesBetweenUsingJava8(tracking.getStart_date().toLocalDate(), tracking.getEnd_date().toLocalDate()).stream().map(localDate -> Date.valueOf(localDate)).collect(Collectors.toList());
        for (Date date : dateList) {
            DriverDayOffTrackingDetailPOJO detailPOJO = new DriverDayOffTrackingDetailPOJO();
            detailPOJO.setDriver_code(tracking.getDriver_code().getDrive_code());
            detailPOJO.setDate(date);
            detailPOJO.setNote(tracking.getDescription());
            dayOffTrackingDetailRepository.save(detailPOJO);
        }
    }


    public List<LocalDate> getDatesBetweenUsingJava8(
            LocalDate startDate, LocalDate endDate) {

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        List<LocalDate> list = IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
        if (startDate.isBefore(endDate)) {
            list.add(endDate);
        }
        return list;
    }

    public DriverDayOffTracking rejectRequest(DriverDayOffTracking tracking) {
        setStatus(tracking, "REJECTED");
        tracking.setProcessed(true);
        tracking.setStarted(false);
        return trackingRepository.save(tracking);
    }


    public void cancelRequest(DriverDayOffTracking tracking) {
        String current_user = userServiceImpl.currentAccount();
        if (tracking.getDriver_code().getUser().getAccount().equals(current_user)) {
            if (!tracking.isProcessed()) {
                trackingRepository.delete(tracking);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Yêu cầu của bạn đã được chấp thuận, không thể sửa.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Bạn không có quyền sửa yêu cầu của người khác.");
        }
    }


    private DriverDayOffTracking setStatus(DriverDayOffTracking tracking, String status_name) {
        tracking.setStatus(statusRepository.findByStatus(status_name));
        return tracking;
    }

    private long getDuration(Date start_date, Date end_date) {
        Calendar calendar_startDate = Calendar.getInstance();
        Calendar calendar_endDate = Calendar.getInstance();
        calendar_endDate.setTime(end_date);
        calendar_startDate.setTime(start_date);
        long noDay = ((calendar_endDate.getTime().getTime() - calendar_startDate.getTime().getTime()) / (24 * 3600 * 1000)) + 1;
        return noDay;
    }

    public void processApprovedRequest() {
        List<DriverDayOffTracking> trackingList = trackingRepository.getApprovedList(true);
        trackingList.stream().forEach(tracking -> processRemainingRequestTime(tracking));
    }

    private void processRemainingRequestTime(DriverDayOffTracking tracking) {
        boolean flag = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date end_date = tracking.getEnd_date();
        Date current_date = new Date(System.currentTimeMillis());
        String _start = dateFormat.format(tracking.getStart_date());
        String _current = dateFormat.format(current_date);
        String _end = dateFormat.format(tracking.getEnd_date());

        if (tracking.isApproved() && tracking.isProcessed()) {
            if (_current.equals(_start)) {
                tracking.setStarted(true);
                flag = true;
            }

            if (tracking.isStarted() == true) {
                long timeRemain = (end_date.getTime() - current_date.getTime()) / (24 * 3600 * 1000);
                tracking.setTimeRemain(timeRemain);
            }

            if (_end.equals(_current) || end_date.before(current_date)) {
                tracking.setApproved(false);
                tracking.setStarted(false);
                Notification notification = new Notification();
                notification.setTitle("");
            }
            if (flag == true) {
                trackingRepository.save(tracking);
            }
        }
    }


    public DriverDayOffTrackingDTO convertToDTO(DriverDayOffTracking tracking) {
        DriverDayOffTrackingDTO dto = modelMapper.map(tracking, DriverDayOffTrackingDTO.class);
        dto.setDriver_id(DriverDTO.from(tracking.getDriver_code()));
        return dto;
    }

    public DriverDayOffTracking convertToEntity(DriverDayOffTrackingDTO dto) {
        DriverDayOffTracking newTracking = modelMapper.map(dto, DriverDayOffTracking.class);

        //modify
        String user = userServiceImpl.currentAccount();
        UserPOJO pojo = userServiceImpl.getUserByName(user);
        //end.
        newTracking.setDriver_code(pojo.getDriver());
        newTracking.setName_request(dto.getName_request());
        if (dto.getId() != null) {
            DriverDayOffTracking oldTracking = findOneById(dto.getId());
            newTracking.setId(oldTracking.getId());
        }
        newTracking.setDuration(getDuration(newTracking.getStart_date(), newTracking.getEnd_date()));
        return newTracking;
    }


    public List<DayOffRequest> getAllKindOfRequest() {
        return dayOffRequestRepository.findAll();
    }

    public List<DayOffRequestStatus> allStatus() {
        List<DayOffRequestStatus> statuses = statusRepository.findAll();
        return statuses;
    }

    public String getCurrentUser() {
        return userServiceImpl.currentAccount();
    }

    public List<DriverDayOffTrackingDetailPOJO> allDetailByDriverId(String code, Integer month, Integer year) {
        List<DriverDayOffTrackingDetailPOJO> allDayOfDayOff = dayOffTrackingDetailRepository.getAllByDriverId(code, month, year);
        return allDayOfDayOff;
    }

    public DriverDayOffTrackingDetailPOJO createDetail(String code, DriverDayOffTrackingDetailDTO dto) {
        DriverDayOffTrackingDetailPOJO detailPOJO = new DriverDayOffTrackingDetailPOJO();
        detailPOJO.setDriver_code(dto.getDriver_code());
        detailPOJO.setDate(dto.getDate());
        detailPOJO.setNote(dto.getNote());
        detailPOJO.setAwol(true);
        return dayOffTrackingDetailRepository.save(detailPOJO);
    }
}
