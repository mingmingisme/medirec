package com.medirec;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.medirec.mapper")
public class MedirecApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedirecApplication.class, args);
    }

}
