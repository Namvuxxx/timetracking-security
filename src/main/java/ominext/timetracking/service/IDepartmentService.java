package ominext.timetracking.service;

import ominext.timetracking.model.dto.DepartmentDTO;

import java.util.List;

public interface IDepartmentService {
    List<DepartmentDTO> getAll(int page, int size, String token);

    DepartmentDTO getById(long id, String token);

    DepartmentDTO create(DepartmentDTO departmentDTO, String token); // id của người tạo

    DepartmentDTO update(DepartmentDTO departmentDTO, String token); // id của người update


}
