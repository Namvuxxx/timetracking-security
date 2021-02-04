package ominext.timetracking.controller;


import ominext.timetracking.model.dto.DataDTO;
import ominext.timetracking.service.DataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataServiceImpl service;

    @GetMapping("/month")
    public ResponseEntity<DataDTO> getDataMonth(@RequestParam long id, @RequestParam int month, @RequestParam int year,
                                                @RequestHeader(name = AUTHORIZATION) String token) {
        DataDTO dto = service.getDataMonth(id, month, year, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/year")
    public ResponseEntity<List<DataDTO>> getDataYear(@RequestParam long id, @RequestParam int year,
                                                     @RequestHeader(name = AUTHORIZATION) String token) {
        List<DataDTO> listDTO = service.getDataYear(id, year, token);
        if (listDTO != null) {
            return ResponseEntity.ok(listDTO);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/doData")
    public ResponseEntity<DataDTO> doData(@RequestParam long id, @RequestParam int month, @RequestParam int year,
                                          @RequestHeader(name = AUTHORIZATION) String token) {
        DataDTO dto = service.doData(id, month, year, token);
        if (dto != null) {
            return ResponseEntity.status(201).body(dto);
        }
        return ResponseEntity.status(403).build();
    }
}
