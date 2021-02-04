package ominext.timetracking.controller;

import ominext.timetracking.model.baseenum.StatusTimeOffOtEnum;
import ominext.timetracking.model.baseenum.TypeTimeOffOtEnum;
import ominext.timetracking.model.dto.TimeOffOtDTO;
import ominext.timetracking.service.TimeOffOtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/offOt")
public class TimeOffOtController {

    @Autowired
    private TimeOffOtServiceImpl service;


    @GetMapping("/{id}")
    public ResponseEntity<TimeOffOtDTO> getById(@PathVariable long id, @RequestHeader(name = AUTHORIZATION) String token) {
        TimeOffOtDTO dto = service.getById(id, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/month")
    public ResponseEntity<List<TimeOffOtDTO>> getAllMonth(@RequestParam long id, @RequestParam TypeTimeOffOtEnum type,
                                                          @RequestParam int month, @RequestParam int year,
                                                          @RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "20") int size,
                                                          @RequestHeader(name = AUTHORIZATION) String token) {

        List<TimeOffOtDTO> list = service.getAllMonth(id, type, month, year, page, size, token);
        if (list != null) {
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/year")
    public ResponseEntity<List<TimeOffOtDTO>> getAllYear(@RequestParam long id, @RequestParam TypeTimeOffOtEnum type, @RequestParam int year,
                                                         @RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "20") int size,
                                                         @RequestHeader(name = AUTHORIZATION) String token) {
        List<TimeOffOtDTO> list = service.getAllYear(id, type, year, page, size, token);
        if (list != null) {
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/create")
    public ResponseEntity<TimeOffOtDTO> create(@RequestBody TimeOffOtDTO offOtDTO, @RequestHeader(name = AUTHORIZATION) String token) {
        TimeOffOtDTO dto = service.create(offOtDTO, token);
        if (dto != null) {
            return ResponseEntity.status(201).body(dto);
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/update")
    public ResponseEntity<TimeOffOtDTO> update(@RequestBody TimeOffOtDTO offOtDTO, @RequestHeader(name = AUTHORIZATION) String token) {
        TimeOffOtDTO dto = service.update(offOtDTO, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/approve")
    public ResponseEntity<TimeOffOtDTO> approve(@RequestParam long id, @RequestParam StatusTimeOffOtEnum status,        /*status la approved hoac rejected*/
                                                @RequestHeader(name = AUTHORIZATION) String token) {
        TimeOffOtDTO dto = service.approve(id, status, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(403).build();
    }
}
