package com.pccw.assessment;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class AssessmentApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AssessmentApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
    }
}
