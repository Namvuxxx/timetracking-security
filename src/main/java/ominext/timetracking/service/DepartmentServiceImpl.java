package ominext.timetracking.service;

import ominext.timetracking.model.dto.DepartmentDTO;
import ominext.timetracking.model.entity.Department;
import ominext.timetracking.repository.IDepartmentRepository;
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
public class DepartmentServiceImpl implements IDepartmentService {

    @Autowired
    private IDepartmentRepository repository;

    @Autowired
    private CommonService commonService;

    @Override
    public List<DepartmentDTO> getAll(int page, int size, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token)) {
            Pageable pageable = PageRequest.of(page, size);
            Type type = new TypeToken<List<DepartmentDTO>>() {
            }.getType();
            return mapper.map(repository.findAll(pageable).toList(), type);
        }
        return null;
    }

    @Override
    public DepartmentDTO getById(long id, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token) && repository.findById(id).isPresent()) {
            return mapper.map(repository.findById(id).get(), DepartmentDTO.class);
        }
        return null;
    }

    @Override
    public DepartmentDTO create(DepartmentDTO dto, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token)) {
            Department department = mapper.map(dto, Department.class);
            department.setCreatedAt(LocalDateTime.now());
            department.setCreatedBy(commonService.getId(token));
            repository.saveAndFlush(department);
            return dto;
        }
        return null;
    }

    @Override
    public DepartmentDTO update(DepartmentDTO dto, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token) && repository.findById(dto.getId()).isPresent()) {

            Department department = mapper.map(dto, Department.class);
            department.setCreatedAt(repository.findById(dto.getId()).get().getCreatedAt());
            department.setCreatedBy(repository.findById(dto.getId()).get().getCreatedBy());
            department.setUpdatedAt(LocalDateTime.now());
            department.setUpdatedBy(commonService.getId(token));
            repository.saveAndFlush(department);
            return dto;
        }
        return null;

    }


}
