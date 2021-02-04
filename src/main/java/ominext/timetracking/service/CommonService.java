package ominext.timetracking.service;

import ominext.timetracking.model.baseenum.RoleEmployeeEnum;
import ominext.timetracking.model.entity.Employee;
import ominext.timetracking.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    @Autowired
    private IEmployeeRepository employeeRepository;


    public boolean isAdmin(String token) {
        return getRole(token) == RoleEmployeeEnum.ADMIN;
    }


    public long getId(String token) {
        String username = token.split(":")[0];
        if (employeeRepository.findByUsername(username).isPresent()) {
            Employee employee = employeeRepository.findByUsername(username).get();
            return employee.getId();
        }
        return 0;
    }


    public RoleEmployeeEnum getRole(String token) {
        String username = token.split(":")[0];

        if (employeeRepository.findByUsername(username).isPresent()) {
            Employee employee = employeeRepository.findByUsername(username).get();
            return employee.getRole();
        }
        return null;
    }

}
