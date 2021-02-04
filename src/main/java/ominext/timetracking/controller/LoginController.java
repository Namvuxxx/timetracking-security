package ominext.timetracking.controller;

import ominext.timetracking.model.dto.LoginDTO;
import ominext.timetracking.service.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginServiceImpl service;

    @PostMapping("")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        if (service.isCorrect(username, password)) {
            String token = service.token(username, password);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setBearerAuth(token);
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body("Login successful");
        }
        return ResponseEntity.status(401).body("Login unsuccessful");
    }
}
