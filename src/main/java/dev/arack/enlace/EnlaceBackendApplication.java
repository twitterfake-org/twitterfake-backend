package dev.arack.enlace;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModuleInitializer;

@Log4j2
@ApplicationModule
@EnableJpaAuditing
@SpringBootApplication
public class EnlaceBackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(EnlaceBackendApplication.class, args);

        log.info("Swagger UI is available at » http://localhost:8080/swagger-ui.html");
        log.info("H2 Console is available at » http://localhost:8080/h2-console");
    }
}
