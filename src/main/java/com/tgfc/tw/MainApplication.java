package com.tgfc.tw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
@SpringBootApplication(scanBasePackages={"com.tgfc.tw", "tw.tgfc.common.spring.ldap.conf"})
@EntityScan(basePackages = "com.tgfc.tw.entity")
@EnableJpaRepositories(basePackages = "com.tgfc.tw.entity.repository")
@EnableJpaAuditing
@EnableScheduling
public class MainApplication extends SpringBootServletInitializer {

    private static Logger logger = LoggerFactory.getLogger(MainApplication.class);

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08:00"));
    }

    //註解測試1
    public static void main(String[] args) {
        logger.info("Hungry Start");

        SpringApplication.run(MainApplication.class);
    }
}
