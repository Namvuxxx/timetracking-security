package ominext.timetracking.controller;

import ominext.timetracking.model.dto.EmployeeDTO;
import ominext.timetracking.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {


    @Autowired
    private EmployeeServiceImpl employeeService;

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDTO>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                    @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                                                    @RequestHeader(name = AUTHORIZATION) String token) {
        List<EmployeeDTO> list = employeeService.getAll(page, size, token);
        if (list != null) {
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(403).build();
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<EmployeeDTO> getByUsername(@PathVariable String username,
                                                     @RequestHeader(name = AUTHORIZATION) String token) {
        EmployeeDTO dto = employeeService.getByUsername(username, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable long id,
                                               @RequestHeader(name = AUTHORIZATION) String token) {
        EmployeeDTO dto = employeeService.getById(id, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/create")
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO employeeDTO,
                                              @RequestHeader(name = AUTHORIZATION) String token, BindingResult result) {

        EmployeeDTO dto = employeeService.create(employeeDTO, token);
        if (!result.hasErrors()) {

            if (dto != null) {
                return ResponseEntity.status(201).body(dto);
            } else return ResponseEntity.status(403).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/update")
    public ResponseEntity<EmployeeDTO> update(@Valid @RequestBody EmployeeDTO employeeDTO,
                                              @RequestHeader(name = AUTHORIZATION) String token, BindingResult result) {
        if (!result.hasErrors()) {
            EmployeeDTO dto = employeeService.update(employeeDTO, token);
            if (dto != null) {
                return ResponseEntity.ok(dto);
            } else return ResponseEntity.status(403).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable long id, @RequestHeader(name = AUTHORIZATION) String token) {
        if (employeeService.isDeleted(id, token)) {
            return ResponseEntity.status(200).body("DELETED");
        }
        return ResponseEntity.status(403).body("");
    }
}
