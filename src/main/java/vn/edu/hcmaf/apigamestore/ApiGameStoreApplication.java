package vn.edu.hcmaf.apigamestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "vn.edu.hcmaf.apigamestore")
public class ApiGameStoreApplication {

    public static void main(String[] args) {

        SpringApplication.run(ApiGameStoreApplication.class, args);
    }

}
