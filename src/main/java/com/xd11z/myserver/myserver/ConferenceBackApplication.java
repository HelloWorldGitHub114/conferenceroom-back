package com.xd11z.myserver.myserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ConferenceBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceBackApplication.class, args);
    }

}
