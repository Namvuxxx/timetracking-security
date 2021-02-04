package ominext.timetracking.service;

import ominext.timetracking.model.dto.EmployeeDTO;
import ominext.timetracking.model.entity.Department;
import ominext.timetracking.model.entity.Employee;
import ominext.timetracking.model.entity.Position;
import ominext.timetracking.repository.IEmployeeRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private CommonService commonService;

    @Override
    public List<EmployeeDTO> getAll(int page, int size, String token) {
        ModelMapper mapper = new ModelMapper();
        Pageable pageable = PageRequest.of(page, size);
        if (commonService.isAdmin(token)) {
            Type type = new TypeToken<List<EmployeeDTO>>() {
            }.getType();
            mapper.addMappings(employeeDTOPropertyMap());
            return mapper.map(employeeRepository.findAll(pageable).toList(), type);
        }
        return null;
    }

    @Override
    public EmployeeDTO getByUsername(String username, String token) {
        ModelMapper mapper = new ModelMapper();
        Employee employee = new Employee();
        if (employeeRepository.findByUsername(username).isPresent()) {
            employee = employeeRepository.findByUsername(username).get();
        }

        if (commonService.isAdmin(token) || employee.getId() == commonService.getId(token)) {
            mapper.addMappings(employeeDTOPropertyMap());
            return mapper.map(employee, EmployeeDTO.class);
        }
        return null;

    }

    @Override
    public EmployeeDTO getById(long id, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token) && id == commonService.getId(token)) {
            if (employeeRepository.findById(id).isPresent()) {
                Employee employee = employeeRepository.findById(id).get();
                mapper.addMappings(employeeDTOPropertyMap());
                return mapper.map(employee, EmployeeDTO.class);
            }
        }
        return null;
    }

    @Override
    public EmployeeDTO create(EmployeeDTO employeeDTO, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token) && !employeeRepository.existsById(employeeDTO.getId())) {                /*Chi admin moi co quyen, id phai chua ton tai*/
            mapper.addMappings(employeePropertyMap());
            Employee employee = mapper.map(employeeDTO, Employee.class);
            employee.setPosition(new Position(employeeDTO.getPosition()));
            employee.setDepartment(new Department(employeeDTO.getDepartment()));
            employee.setPassword(DigestUtils.md5Hex("123")); // Mat khau mac dinh la 123
            employee.setCreatedAt(LocalDateTime.now());
            employee.setCreatedBy(commonService.getId(token));
            employeeRepository.saveAndFlush(employee);
            return employeeDTO;
        }
        return null;
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employeeDTO, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token) || employeeDTO.getId() == commonService.getId(token)) {   /*Neu khong la admin va khong phai la user day thi return null*/
            if (employeeRepository.findById(employeeDTO.getId()).isPresent()) {
                Employee employee2 = employeeRepository.findById(employeeDTO.getId()).get(); // tao employee2 de truyen du lieu khi chinh sua
                if (employeeDTO.getId() == commonService.getId(token)) {                        /*Nếu user tự chỉnh sửa thì không có quyền đổi manager, masterdata*/
                    employeeDTO.setManager(employee2.getManager());
                    employeeDTO.setTimeOff(employee2.getTimeOff());
                    employeeDTO.setDepartment(employee2.getDepartment().getId());
                    employeeDTO.setPosition(employee2.getPosition().getId());
                    employeeDTO.setStartTime(employee2.getStartTime());
                    employeeDTO.setEndTime(employee2.getEndTime());
                    employeeDTO.setStartBreaktime(employee2.getStartBreaktime());
                    employeeDTO.setEndBreaktime(employee2.getEndBreaktime());
                }

                mapper.addMappings(employeePropertyMap());

                Employee employee1 = mapper.map(employeeDTO, Employee.class);
                employee1.setPassword(employee2.getPassword());
                employee1.setCreatedAt(employee2.getCreatedAt());
                employee1.setCreatedBy(employee2.getCreatedBy());

                employee1.setUpdatedAt(LocalDateTime.now());
                employee1.setUpdatedBy(commonService.getId(token));
                employeeRepository.saveAndFlush(employee1);
                return employeeDTO;
            }
        }
        return null;
    }


    @Override
    public boolean isDeleted(long id, String token) {
        if (commonService.isAdmin(token)) {                                /*Chi admin moi co quyen*/
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

// Setup mapping: dữ liệu id từ DTO của position và department chuyển thành Object

    public PropertyMap<EmployeeDTO, Employee> employeePropertyMap() {
        return new PropertyMap<EmployeeDTO, Employee>() {
            @Override
            protected void configure() {
                map(source.getDepartment(), destination.getDepartment().getId());
                map(source.getPosition(), destination.getPosition().getId());
            }
        };
    }

//  Setup mapping: dữ liệu chỉ lấy id của object position và department đưa vào DTO

    public PropertyMap<Employee, EmployeeDTO> employeeDTOPropertyMap() {
        return new PropertyMap<Employee, EmployeeDTO>() {
            @Override
            protected void configure() {
                map().setPosition(source.getPosition().getId());
                map().setDepartment(source.getDepartment().getId());
            }
        };
    }

}
