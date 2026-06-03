package siget_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"siget_backend", "com.sgt"})
@EntityScan(basePackages = "com.sgt.entity")
@EnableJpaRepositories(basePackages = "com.sgt.repository")
public class SigetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SigetBackendApplication.class, args);
    }
}