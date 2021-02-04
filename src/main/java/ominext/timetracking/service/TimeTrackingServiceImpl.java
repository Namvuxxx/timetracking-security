package ominext.timetracking.service;

import ominext.timetracking.model.dto.TimeTrackingDTO;
import ominext.timetracking.model.entity.Employee;
import ominext.timetracking.model.entity.TimeTracking;
import ominext.timetracking.repository.IEmployeeRepository;
import ominext.timetracking.repository.ITimeTrackingRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeTrackingServiceImpl implements ITimeTrackingService {
    @Autowired
    IEmployeeRepository employeeRepository;
    @Autowired
    ITimeTrackingRepository repository;
    @Autowired
    private CommonService commonService;

    @Override
    public TimeTrackingDTO create(long idEmp, TimeTrackingDTO dto, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token)) {
            Employee employee = new Employee();
            if (employeeRepository.findById(idEmp).isPresent()) {
                employee = employeeRepository.findById(idEmp).get();
            }

            TimeTracking timeTracking = mapper.map(dto, TimeTracking.class);
            timeTracking.setEmployee(employee);
            timeTracking.setCreatedBy(commonService.getId(token));
            timeTracking.setCreatedAt(LocalDateTime.now());
            repository.saveAndFlush(timeTracking);
            return dto;
        }
        return null;
    }

    @Override
    public List<TimeTrackingDTO> getAllDay(long idEmp, LocalDate date, int page, int size, String token) {
        ModelMapper mapper = new ModelMapper();
        Pageable pageable = PageRequest.of(page, size);
        if (commonService.isAdmin(token) || idEmp == commonService.getId(token)) {
            Type listType = new TypeToken<List<TimeTrackingDTO>>() {
            }.getType();

            return mapper.map(repository.getAllDate(idEmp, date, pageable), listType);
        }
        return null;
    }

    @Override
    public List<TimeTrackingDTO> getAllMonth(long idEmp, int month, int year, int page, int size, String token) {
        ModelMapper mapper = new ModelMapper();
        Pageable pageable = PageRequest.of(page, size);
        if (commonService.isAdmin(token) || idEmp == commonService.getId(token)) {
            Type listType = new TypeToken<List<TimeTrackingDTO>>() {
            }.getType();
            return mapper.map(repository.getAllMonth(idEmp, month, year, pageable), listType);
        }
        return null;
    }


}
