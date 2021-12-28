package com.example.logparser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class LogParserApplication {



    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(LogParserApplication.class, args);
        String mysqlUrl = context.getEnvironment().getProperty("spring.datasource.url");
        log.info("connected to mysql at {}", mysqlUrl);
    }



}
