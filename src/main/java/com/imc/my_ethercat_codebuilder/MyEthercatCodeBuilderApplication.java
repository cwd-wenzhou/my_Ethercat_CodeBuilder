package com.imc.my_ethercat_codebuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {"com.imc"})
public class MyEthercatCodeBuilderApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MyEthercatCodeBuilderApplication.class, args);
    }

}
