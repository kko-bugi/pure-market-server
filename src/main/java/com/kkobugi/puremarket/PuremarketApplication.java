package com.kkobugi.puremarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PuremarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(PuremarketApplication.class, args);
    }

}
