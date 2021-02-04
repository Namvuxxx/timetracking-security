package ominext.timetracking.controller;

import ominext.timetracking.service.LogOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
public class LogOutController {
    @Autowired
    private LogOutService service;

    @GetMapping("/api/logout")
    public ResponseEntity<String> logOut(@RequestHeader(AUTHORIZATION) String token) {
        service.logOut(token);
        return ResponseEntity.ok("Logout Success");
    }
}
