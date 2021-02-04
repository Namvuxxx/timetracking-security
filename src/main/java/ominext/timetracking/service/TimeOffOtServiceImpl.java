package ominext.timetracking.service;

import ominext.timetracking.model.baseenum.StatusTimeOffOtEnum;
import ominext.timetracking.model.baseenum.TypeTimeOffOtEnum;
import ominext.timetracking.model.dto.TimeOffOtDTO;
import ominext.timetracking.model.entity.Employee;
import ominext.timetracking.model.entity.TimeOffOt;
import ominext.timetracking.repository.IEmployeeRepository;
import ominext.timetracking.repository.ITimeOffOtRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeOffOtServiceImpl implements ITimeOffOtService {

    @Autowired
    private ITimeOffOtRepository repository;
    @Autowired
    private IEmployeeRepository employeeRepository;
    @Autowired
    private CommonService commonService;

    @Override
    public TimeOffOtDTO getById(long id, String token) {
        ModelMapper mapper = new ModelMapper();
        if (repository.findById(id).isPresent()) {
            TimeOffOt timeOffOt = repository.findById(id).get();
            long idEmpl = timeOffOt.getEmployee().getId();
            long idManager = timeOffOt.getEmployee().getManager();
            if (commonService.isAdmin(token) || commonService.getId(token) == idEmpl || commonService.getId(token) == idManager) {
                return mapper.map(timeOffOt, TimeOffOtDTO.class);
            }
        }

        return null;
    }

    @Override
    public List<TimeOffOtDTO> getAllMonth(long id, TypeTimeOffOtEnum type, int month, int year, int page, int size, String token) {
        ModelMapper mapper = new ModelMapper();
        if ((commonService.isAdmin(token)) || commonService.getId(token) == id) {
            Pageable pageable = PageRequest.of(page, size);
            Type listType = new TypeToken<List<TimeOffOtDTO>>() {
            }.getType();
            List<TimeOffOt> list = repository.findAllMonth(id, type, month, year, pageable);
            return mapper.map(list, listType);
        }
        return null;
    }

    @Override
    public List<TimeOffOtDTO> getAllYear(long id, TypeTimeOffOtEnum type, int year, int page, int size, String token) {
        ModelMapper mapper = new ModelMapper();
        if ((commonService.isAdmin(token)) || (commonService.getId(token) == id)) {
            Pageable pageable = PageRequest.of(page, size);
            Type typeList = new TypeToken<List<TimeOffOtDTO>>() {
            }.getType();
            List<TimeOffOt> list = repository.findAllYear(id, type, year, pageable);
            return mapper.map(list, typeList);

        }
        return null;

    }

    @Override
    public TimeOffOtDTO create(TimeOffOtDTO dto, String token) {
        ModelMapper mapper = new ModelMapper();
        long id = commonService.getId(token);
        if (!repository.existsById(dto.getId())) {
            TimeOffOt timeOffot = mapper.map(dto, TimeOffOt.class);
            Employee employee = new Employee();
            if (employeeRepository.findById(id).isPresent()) {
                employee = employeeRepository.findById(id).get();
            }

            timeOffot.setEmployee(employee);
            timeOffot.setStatus(StatusTimeOffOtEnum.NEW);                              /*Don moi tao co Status la NEW*/
            timeOffot.setCreatedAt(LocalDateTime.now());
            timeOffot.setCreatedBy(id);
            repository.saveAndFlush(timeOffot);
            return mapper.map(timeOffot, TimeOffOtDTO.class);
        }
        return null;
    }

    @Override
    public TimeOffOtDTO update(TimeOffOtDTO dto, String token) {
        ModelMapper mapper = new ModelMapper();
        long id = commonService.getId(token);
        if (repository.findById(dto.getId()).isPresent()) {
            TimeOffOt offOt = repository.findById(dto.getId()).get();

//           Chỉ user mới chỉnh sửa được đơn của mình và đơn đấy phải có status là NEW thì mới chỉnh sửa được
            if (id == repository.findById(dto.getId()).get().getEmployee().getId() && offOt.getStatus() == StatusTimeOffOtEnum.NEW) {
                TimeOffOt timeOffOt = mapper.map(dto, TimeOffOt.class);
                Employee employee = new Employee();
                if (employeeRepository.findById(id).isPresent()) {
                    employee = employeeRepository.findById(id).get();
                }
                timeOffOt.setStatus(StatusTimeOffOtEnum.NEW);
                timeOffOt.setUpdatedAt(LocalDateTime.now());
                timeOffOt.setUpdatedBy(id);
                timeOffOt.setCreatedBy(offOt.getCreatedBy());
                timeOffOt.setCreatedAt(offOt.getCreatedAt());
                timeOffOt.setEmployee(employee);
                repository.saveAndFlush(timeOffOt);
                return mapper.map(timeOffOt, TimeOffOtDTO.class);
            }

        }

        return null;

    }

    @Override
    public TimeOffOtDTO approve(long idOffOT, StatusTimeOffOtEnum statusTimeOffOtEnum, String token) {
        ModelMapper mapper = new ModelMapper();
        if (repository.findById(idOffOT).isPresent()) {
            TimeOffOt timeOff_ot = repository.findById(idOffOT).get();
            Employee employee = timeOff_ot.getEmployee();
            if (commonService.getId(token) == employee.getManager()) {    /*Chi co manager moi co quyen duyet don*/

                if (statusTimeOffOtEnum == StatusTimeOffOtEnum.APPROVED) {
                    timeOff_ot.setStatus(StatusTimeOffOtEnum.APPROVED);

                    timeOff_ot.setUpdatedAt(LocalDateTime.now());
                    timeOff_ot.setUpdatedBy(commonService.getId(token));
                    repository.saveAndFlush(timeOff_ot);

                    return mapper.map(timeOff_ot, TimeOffOtDTO.class);
                }
                if (statusTimeOffOtEnum == StatusTimeOffOtEnum.REJECTED) {
                    timeOff_ot.setStatus(StatusTimeOffOtEnum.REJECTED);

                    timeOff_ot.setUpdatedAt(LocalDateTime.now());
                    timeOff_ot.setUpdatedBy(commonService.getId(token));
                    repository.saveAndFlush(timeOff_ot);
                    return mapper.map(timeOff_ot, TimeOffOtDTO.class);
                }
            }
        }
        return null;
    }


}
