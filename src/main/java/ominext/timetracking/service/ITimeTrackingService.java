package ominext.timetracking.service;

import ominext.timetracking.model.dto.TimeTrackingDTO;

import java.time.LocalDate;
import java.util.List;

public interface ITimeTrackingService {
    // Luu thoi gian tracking vao DB
    TimeTrackingDTO create(long idEmp, TimeTrackingDTO dto, String token);

    //Lay thoi gian tracking cua mot ngay
    List<TimeTrackingDTO> getAllDay(long idEmp, LocalDate date, int page, int size, String token);

    //Lay thoi gian tracking cua mot thang
    List<TimeTrackingDTO> getAllMonth(long idEmp, int month, int year, int page, int size, String token);

}
