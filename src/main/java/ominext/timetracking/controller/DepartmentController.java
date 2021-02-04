package ominext.timetracking.controller;

import ominext.timetracking.model.dto.DepartmentDTO;
import ominext.timetracking.service.DepartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentServiceImpl service;

    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                      @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                                                      @RequestHeader(name = AUTHORIZATION) String token) {
        List<DepartmentDTO> list = service.getAll(page, size, token);
        if (list != null) {
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable long id, @RequestHeader(name = AUTHORIZATION) String token) {
        DepartmentDTO dto = service.getById(id, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(403).build();
    }


    @PostMapping("/create")
    public ResponseEntity<DepartmentDTO> create(@RequestBody DepartmentDTO departmentDTO, @RequestHeader(name = AUTHORIZATION) String token) {
        DepartmentDTO dto = service.create(departmentDTO, token);
        if (dto != null) {
            return ResponseEntity.status(201).body(dto);
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/update")
    public ResponseEntity<DepartmentDTO> update(@RequestBody DepartmentDTO departmentDTO, @RequestHeader(name = AUTHORIZATION) String token) {
        DepartmentDTO dto = service.update(departmentDTO, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(403).build();
    }

}
