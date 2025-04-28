package dev.arack.twitterfake;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.modulith.ApplicationModule;

@Log4j2
@ApplicationModule
@EnableJpaAuditing
@SpringBootApplication
public class TwitterFakeApplication {

    public static void main(String[] args) {

        SpringApplication.run(TwitterFakeApplication.class, args);
    }
}
