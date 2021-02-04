package ominext.timetracking.controller;

import ominext.timetracking.model.dto.PositionDTO;
import ominext.timetracking.service.PositionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/position")
public class PositionController {


    @Autowired
    private PositionServiceImpl service;


    @GetMapping("/all")
    public ResponseEntity<List<PositionDTO>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                    @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                                                    @RequestHeader(name = AUTHORIZATION) String token) {
        List<PositionDTO> list = service.getAll(page, size, token);
        if (list != null) {
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionDTO> getById(@PathVariable long id, @RequestHeader(name = AUTHORIZATION) String token) {
        PositionDTO dto = service.getById(id, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/create")
    public ResponseEntity<PositionDTO> create(@RequestBody PositionDTO positionDTO, @RequestHeader(name = AUTHORIZATION) String token) {
        PositionDTO dto = service.create(positionDTO, token);
        if (dto != null) {
            return ResponseEntity.status(201).body(dto);
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/update")
    public ResponseEntity<PositionDTO> update(@RequestBody PositionDTO positionDTO, @RequestHeader(name = AUTHORIZATION) String token) {

        PositionDTO dto = service.update(positionDTO, token);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
