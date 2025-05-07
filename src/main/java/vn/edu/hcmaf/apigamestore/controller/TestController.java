package vn.edu.hcmaf.apigamestore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class TestController {
    @GetMapping("/ping")
    public String ping() {
        System.out.println("Ping called");
        return "pong";
    }
}