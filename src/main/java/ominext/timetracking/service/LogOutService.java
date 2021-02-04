package ominext.timetracking.service;

import ominext.timetracking.model.entity.Employee;
import ominext.timetracking.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogOutService {

    @Autowired
    private IEmployeeRepository repository;
    @Autowired
    private CommonService commonService;


    public void logOut(String token) {
        long id = commonService.getId(token);
        if (repository.findById(id).isPresent()) {
            Employee employee = repository.findById(id).get();
            employee.setLogin(false);
            repository.saveAndFlush(employee);
        }

    }
}
