package com.imc.my_ethercat_codebuilder;

import com.alibaba.fastjson.JSON;
import com.imc.model.Slave_info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"com.imc"})
public class MyEthercatCodeBuilderApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MyEthercatCodeBuilderApplication.class, args);
    }

}
