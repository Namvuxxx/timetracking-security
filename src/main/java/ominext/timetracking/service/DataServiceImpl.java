package ominext.timetracking.service;

import ominext.timetracking.model.baseenum.StatusTimeOffOtEnum;
import ominext.timetracking.model.baseenum.TypeTimeOffOtEnum;
import ominext.timetracking.model.dto.DataDTO;
import ominext.timetracking.model.entity.Data;
import ominext.timetracking.model.entity.Employee;
import ominext.timetracking.model.entity.TimeOffOt;
import ominext.timetracking.model.entity.TimeTracking;
import ominext.timetracking.repository.IDataRepository;
import ominext.timetracking.repository.IEmployeeRepository;
import ominext.timetracking.repository.ITimeOffOtRepository;
import ominext.timetracking.repository.ITimeTrackingRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.*;
import java.util.List;

@Service
public class DataServiceImpl implements IDataService {
    @Autowired
    private IEmployeeRepository employeeRepository;
    @Autowired
    private IDataRepository repository;
    @Autowired
    private ITimeTrackingRepository timeTrackingRepository;
    @Autowired
    private ITimeOffOtRepository timeOffOtRepository;
    @Autowired
    private CommonService commonService;

    //Lấy dữ liệu đã tính toán
    @Override
    public DataDTO getDataMonth(long id, int month, int year, String token) {

        if (commonService.isAdmin(token) || id == commonService.getId(token)) {
            ModelMapper mapper = new ModelMapper();
            Data data = new Data();
            if (repository.findDataByEmployeeId(id, month, year).isPresent()) {
                data = repository.findDataByEmployeeId(id, month, year).get();
            }
            return mapper.map(data, DataDTO.class);
        }
        return null;
    }

    // Tính toán dữ liệu chỉ dành cho admin
    @Override
    public DataDTO doData(long id, int month, int year, String token) {

        if (commonService.isAdmin(token) && employeeRepository.findById(id).isPresent()) {
            ModelMapper mapper = new ModelMapper();
            long timeOffLeft = 0;
            long realTime;
            Employee employee = employeeRepository.findById(id).get();
            long[] doWorking = doWorking(id, month, year);
            DataDTO dto = new DataDTO();

            //Nếu là tháng 1 thì timeOffleft đầu tháng bằng chính thời gian phép quy định
            if (month == 1) {
                if (employee.getTimeOff() > doTimeOff(id, month, year)) {
                    timeOffLeft = employee.getTimeOff() - doTimeOff(id, month, year);
                    realTime = doWorking[0] + doOverTime(id, month, year);
                } else {
                    realTime = doWorking[0] + doOverTime(id, month, year)
                            - (doTimeOff(id, month, year) - employee.getTimeOff());
                }
            } else {
                //Nếu là tháng tiếp theo thì timeOffLeff đầu tháng bằng thời gian
                // timeOffleff của tháng trước cộng thời gian quy định
                long timeBefore = 0;
                if (repository.findDataByEmployeeId(id, month - 1, year).isPresent()) {
                    timeBefore = repository.findDataByEmployeeId(id, month - 1, year).get().getTimeOffLeft();
                }

                if (employee.getTimeOff() + timeBefore > doTimeOff(id, month, year)) {
                    timeOffLeft = employee.getTimeOff() + timeBefore - doTimeOff(id, month, year);
                    realTime = doWorking[0] + doOverTime(id, month, year);
                } else {
                    realTime = doWorking[0] + doOverTime(id, month, year)
                            - (doTimeOff(id, month, year) - employee.getTimeOff() - timeBefore);
                }
            }
            dto.setMonth(month);
            dto.setYear(year);
            dto.setTimeWorking(doWorking[0]);
            long timeLate = doWorking[1] - doTimeLate(id, month, year);
            dto.setTimeLate(timeLate);
            dto.setTimeOT(doOverTime(id, month, year));
            dto.setTimeOff(doTimeOff(id, month, year));

            dto.setTimeOffLeft(timeOffLeft);
            dto.setRealTime(realTime);

            Data data = mapper.map(dto, Data.class);
            data.setEmployee(employeeRepository.findById(id).get());

            //kiểm tra xem đã có dữ liệu của tháng này trong DB chưa, nếu chưa có là tạo mơi, có rồi là update
            Data dataTrans;
            if (repository.findDataByEmployeeId(id, month, year).isPresent()) {
                dataTrans = repository.findDataByEmployeeId(id, month, year).get();
            } else {
                dataTrans = null;
            }

            if (dataTrans != null) {
                data.setId(dataTrans.getId());
                data.setUpdatedAt(LocalDateTime.now());
                data.setUpdatedBy(commonService.getId(token));
                data.setCreatedBy(dataTrans.getCreatedBy());
                data.setCreatedBy(dataTrans.getCreatedBy());
            } else {
                data.setCreatedAt(LocalDateTime.now());
                data.setCreatedBy(commonService.getId(token));
            }

            repository.saveAndFlush(data);

            dto = mapper.map(data, DataDTO.class);    /*Map lại để hiện thị thông tin cho người dùng*/
            return dto;
        }
        return null;
    }


    @Override
    public List<DataDTO> getDataYear(long id, int year, String token) {
        if (commonService.isAdmin(token) || id == commonService.getId(token)) {
            ModelMapper mapper = new ModelMapper();
            List<Data> dataList = repository.findAllYear(id, year);
            Type type = new TypeToken<List<DataDTO>>() {
            }.getType();
            return mapper.map(dataList, type);
        }
        return null;
    }

    @Override
    public long doOverTime(long id, int month, int year) {

        List<TimeOffOt> list = timeOffOtRepository.findToDoData(id, TypeTimeOffOtEnum.OVERTIME, StatusTimeOffOtEnum.APPROVED, month, year);
        long timeMonth = 0;
        for (TimeOffOt obj : list) {
            long timeDay = getBetweenTwoDayToMinutes(obj.getStartTime(), obj.getEndTime());
            timeMonth = timeMonth + timeDay;
        }
        return timeMonth;
    }


    @Override
    public long doTimeOff(long id, int month, int year) {

        List<TimeOffOt> list = timeOffOtRepository.findToDoData(id, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.APPROVED, month, year);
        long timeMonth = 0;
        for (TimeOffOt obj : list) {
            long timeDay = getBetweenTwoDayToHours(obj.getStartTime().toLocalDate(), obj.getEndTime().toLocalDate());
            timeMonth = timeMonth + timeDay;
        }
        return timeMonth * 60;
    }

    //Thời gian đi làm muộn có trong đơn xin nghỉ
    @Override
    public long doTimeLate(long id, int month, int year) {
        if (employeeRepository.findById(id).isPresent()) {
            Employee employee = employeeRepository.findById(id).get();

            // Thời gian quy định của nhân viên đổi ra phút
            long startTime = (long) employee.getStartTime().getHour() * 60 + employee.getStartTime().getMinute();
            long endTime = (long) employee.getEndTime().getHour() * 60 + employee.getEndTime().getMinute();
            long startBreaktime = (long) employee.getStartBreaktime().getHour() * 60 + employee.getStartBreaktime().getMinute();
            long endBreaktime = (long) employee.getEndBreaktime().getHour() * 60 + employee.getEndBreaktime().getMinute();

            List<TimeOffOt> list = timeOffOtRepository.findToDoDataByEndTime(id, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.APPROVED, month, year);
            long timeMonth = 0;
            for (TimeOffOt obj : list) {
                long endTimeOff = (long) obj.getEndTime().getHour() * 60 + obj.getEndTime().getMinute();     /* Đổi thời gian end của đơn off ra phút*/
                timeMonth = timeMonth + getTimelate(endTimeOff, startTime, endTime, startBreaktime, endBreaktime);
            }
            return timeMonth;
        }
        return 0;
    }

    @Override
    public long[] doWorking(long id, int month, int year) {
        if (employeeRepository.findById(id).isPresent()) {
            Employee employee = employeeRepository.findById(id).get();

            // Thời gian quy định của nhân viên đổi ra phút
            long startTime = (long) employee.getStartTime().getHour() * 60 + employee.getStartTime().getMinute();
            long endTime = (long) employee.getEndTime().getHour() * 60 + employee.getEndTime().getMinute();
            long startBreaktime = (long) employee.getStartBreaktime().getHour() * 60 + employee.getStartBreaktime().getMinute();
            long endBreaktime = (long) employee.getEndBreaktime().getHour() * 60 + employee.getEndBreaktime().getMinute();

            //Lấy so ngày trong tháng
            YearMonth yearMonthObject = YearMonth.of(year, month);
            int daysInMonth = yearMonthObject.lengthOfMonth();

            long timeWorking = 0;
            long timeLate = 0;


            for (int iDay = 1; iDay <= daysInMonth; iDay++) {
                List<TimeTracking> list = timeTrackingRepository.getAllDay(id, iDay, month, year);
                long s = 0;

                if (list.size() >= 1) {
                    //Tính thời gian đi muộn trong tháng
                    long firstChecking = (long) list.get(0).getTimeChecking().getHour() * 60 + list.get(0).getTimeChecking().getMinute();
                    timeLate = timeLate + getTimelate(firstChecking, startTime, endTime, startBreaktime, endBreaktime);

//                    if (firstChecking > startTime) {
//                        timeLate = timeLate + firstChecking - startTime;
//                    }

                    //Tính thời gian làm việc trong tháng
                    long[] time = new long[list.size()];            /* Gán thời gian checking trong ngày vào Array */
                    int i = 0;
                    for (TimeTracking tracking : list) {
                        time[i] = (long) tracking.getTimeChecking().getHour() * 60 + tracking.getTimeChecking().getMinute();
                        if (time[i] <= startTime) {
                            time[i] = startTime;
                        } else if ((startBreaktime <= time[i]) && (time[i] <= endBreaktime)) {
                            if (i % 2 == 1) {                                                               /*checkout nam trong khoang nghi trua*/
                                if (startBreaktime <= time[i - 1] && time[i - 1] <= endBreaktime) {         /*va checkin nam trong khoang nghi trua*/
                                    time[i] = time[i - 1];                                                  /*thi khong tinh cap checkin/out*/
                                } else {
                                    time[i] = startBreaktime;                                               /*checkin nam trong khoang nghi trua*/
                                }
                            } else {
                                time[i] = endBreaktime;
                            }
                        } else if (endBreaktime < time[i]) {
                            if (i % 2 == 1 && startBreaktime > time[i - 1]) {
                                s = s - (endBreaktime - startBreaktime);
                            }
                        }
                        if (time[i] >= endTime) {
                            time[i] = endTime;
                        }
                        if (i % 2 == 0) {
                            s = s - time[i];     /*Checkin sẽ trừ*/
                        } else {
                            s = s + time[i];     /*Checkot sẽ cộng*/
                        }
                        i++;
                    }
                }

                timeWorking = timeWorking + s;
            }
            long[] working = new long[2];
            working[0] = timeWorking;
            working[1] = timeLate;
            return working;
        }
        return null;
    }

    @Override
    public long getTimelate(long time, long startTime, long endTime, long startBreakTime, long endBreakTime) {
        if (time <= startTime) {
            return 0;
        } else if (time <= startBreakTime) {
            return time - startTime;
        } else if (time <= endBreakTime) {
            return startBreakTime - startTime;
        } else if (time <= endTime) {
            return time - endBreakTime + startBreakTime - startTime;
        } else
            return endTime - endBreakTime + startBreakTime - startTime;
    }

    @Override
    public long getBetweenTwoDayToMinutes(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        LocalDate date1 = localDateTime1.toLocalDate();
        LocalDate date2 = localDateTime2.toLocalDate();
        LocalTime time1 = localDateTime1.toLocalTime();
        LocalTime time2 = localDateTime2.toLocalTime();
        Period period = Period.between(date1, date2);
        long dateBetweenToMinutes = (long) period.getDays() * 24 * 60;
        long timeBetweenToMinutes = (long) time1.getHour() * 60 + time1.getMinute() - (long) time2.getHour() * 60 - time2.getMinute();
        return dateBetweenToMinutes - timeBetweenToMinutes;
    }

    @Override
    public long getBetweenTwoDayToHours(LocalDate localDate1, LocalDate localDate2) {
        Period period = Period.between(localDate1, localDate2);
        return (long) period.getDays() * 8;   /*Mot ngay tinh la 8h nghi*/
    }


}
