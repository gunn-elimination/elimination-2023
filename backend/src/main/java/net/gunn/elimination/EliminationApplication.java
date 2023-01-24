package net.gunn.elimination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class EliminationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EliminationApplication.class, args);
    }

}
