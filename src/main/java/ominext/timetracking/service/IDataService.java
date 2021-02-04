package ominext.timetracking.service;

import ominext.timetracking.model.dto.DataDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IDataService {

    DataDTO getDataMonth(long id, int month, int year, String token);

    DataDTO doData(long id, int month, int year, String token);

    List<DataDTO> getDataYear(long idEmp, int year, String token);

    //Tính thời gian overtime đã duyệt trong tháng
    long doOverTime(long id, int month, int year);

    //Tính thời gian nghỉ phép trong tháng và thời gian đi làm muộn có trong đơn nghỉ phép
    //VD: đơn xin nghỉ phép là 11h hôm nay đến 11h hôm sau thì thời gian đi muộn trong đơn nghỉ phép là (11-8)
    long doTimeOff(long id, int month, int year);

    //Thời gian đi làm muộn mà có trong Đơn xin nghỉ
    long doTimeLate(long id, int month, int year);

    //Tính thời gian làm dựa vào checkin/out và thời gian đi làm muộn trong tháng
    long[] doWorking(long id, int month, int year);

    //Thời gian đi muộn trong 1 ngày
    long getTimelate(long time, long startTime, long endTime, long startBreakTime, long endBreakTime);

    long getBetweenTwoDayToMinutes(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    long getBetweenTwoDayToHours(LocalDate localDate1, LocalDate localDate2);
}
