package br.com.fiap.skillup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SkillupApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillupApiApplication.class, args);
    }
}
