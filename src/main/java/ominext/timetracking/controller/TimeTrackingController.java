package ominext.timetracking.controller;

import ominext.timetracking.model.dto.TimeTrackingDTO;
import ominext.timetracking.service.TimeTrackingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/tracking")
public class TimeTrackingController {

    @Autowired
    private TimeTrackingServiceImpl service;


    @GetMapping("/day")
    public ResponseEntity<List<TimeTrackingDTO>> getAllDay(@RequestParam long id,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                           @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                           @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                                                           @RequestHeader(name = AUTHORIZATION) String token) {
        List<TimeTrackingDTO> list = service.getAllDay(id, date, page, size, token);
        if (list != null) {
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/month")
    public ResponseEntity<List<TimeTrackingDTO>> getAllMonth(@RequestParam long id, @RequestParam int month, @RequestParam int year,
                                                             @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                             @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                                                             @RequestHeader(name = AUTHORIZATION) String token) {
        List<TimeTrackingDTO> list = service.getAllMonth(id, month, year, page, size, token);
        if (list != null) {
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<TimeTrackingDTO> create(@PathVariable long id, @RequestBody TimeTrackingDTO dto,
                                                  @RequestHeader(name = AUTHORIZATION) String token) {
        TimeTrackingDTO timeTrackingDTO = service.create(id, dto, token);
        if (timeTrackingDTO != null) {
            return ResponseEntity.status(201).body(timeTrackingDTO);
        }
        return ResponseEntity.status(403).build();
    }
}
