package dev.arack.enlace;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
@Log4j2
public class EnlaceBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnlaceBackendApplication.class, args);

        String url = "http://localhost:8080/swagger-ui/index.html";
        log.info("\n\n• Swagger UI is available at » " + url + "\n");
    }
}
