package vn.edu.hcmaf.apigamestore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@Tag(name = "Test", description = "Test API")
public class TestController {
    /**
     * Endpoint to check if the server is running.
     *
     * @return a simple "pong" response.
     */
    @Operation(summary = "Ping", description = "Ping API to check if server is running")
    @GetMapping("/ping")
    public String ping() {
        System.out.println("Ping called");
        return "pong";
    }
    @GetMapping("/callback")
    public String callback(@Param("code") String code) {
      // dung de lay code gg tra ve de test
        System.out.println(code);
        return code;
    }

}