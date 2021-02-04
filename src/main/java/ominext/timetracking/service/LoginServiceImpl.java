package ominext.timetracking.service;

import ominext.timetracking.model.entity.Employee;
import ominext.timetracking.repository.IEmployeeRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Override
    public String token(String username, String password) {
        return username + ":" + DigestUtils.md5Hex(password);
    }

    @Override
    public boolean isCorrect(String username, String password) {
        if (employeeRepository.findByUsername(username).isPresent()) {
            Employee employee = employeeRepository.findByUsername(username).get();
            if (DigestUtils.md5Hex(password).equals(employee.getPassword())) {
                employee.setLogin(true);
                employeeRepository.saveAndFlush(employee);
                return true;
            }
        }
        return false;
    }

}
