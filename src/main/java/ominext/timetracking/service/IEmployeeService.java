package ominext.timetracking.service;

import ominext.timetracking.model.dto.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {


    List<EmployeeDTO> getAll(int page, int size, String token);

    EmployeeDTO getByUsername(String username, String token);

    EmployeeDTO getById(long idEmpl, String token);

    EmployeeDTO create(EmployeeDTO employeeDTO, String token);

    EmployeeDTO update(EmployeeDTO employeeDTO, String token);

    boolean isDeleted(long id, String token);


}
