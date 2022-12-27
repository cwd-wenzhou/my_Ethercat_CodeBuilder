package com.imc.my_ethercat_codebuilder;

import com.alibaba.fastjson.JSON;
import com.imc.assemble.MyAssembler;
import com.imc.assemble.MyconfigAssembler;
import com.imc.model.Direction;
import com.imc.model.Pdo;
import com.imc.model.Slave_info;
import com.imc.prase.MyPraser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MyEthercatCodeBuilderApplicationTests {

    @Autowired
    MyPraser myPraser;

    @Autowired
    MyAssembler myAssembler;

    @Autowired
    MyconfigAssembler myconfigAssembler;

    @Test
    void contextLoads() throws IOException {
        String slave_info_str = "[{\"name\":\"EK1100 EtherCAT-Koppler (2A E-Bus)\",\"product_code\":\"0x44c2c52\",\"revision_number\":\"0x120000\",\"product_code\":\"0x44c2c52\",\"position\":\"0x0\"},{\"name\":\"EL1008 8K. Dig. Eingang 24V, 3ms\",\"product_code\":\"0x3f03052\",\"revision_number\":\"0x110000\",\"product_code\":\"0x3f03052\",\"position\":\"0x1\"},{\"name\":\"EL2008 8K. Dig. Ausgang 24V, 0.5A\",\"product_code\":\"0x7d83052\",\"revision_number\":\"0x110000\",\"product_code\":\"0x7d83052\",\"position\":\"0x2\"},{\"name\":\"EL3064 4K.Ana. Eingang 0-10V\",\"product_code\":\"0xbf83052\",\"revision_number\":\"0x140000\",\"product_code\":\"0xbf83052\",\"position\":\"0x3\"},]";
        String xmlfilePath = "/home/imc/Documents/ethercat/my_Ethercat_CodeBuilder/src/main/resources/example/xmls";
        String slaveSrcPath = "/home/imc/Documents/ethercat/Ethercat-server-control/src/slaves/";
        List<Slave_info> slaveInfos = JSON.parseArray(slave_info_str, Slave_info.class);
        for (Slave_info slaveInfo : slaveInfos) {
            MyCodeBuilder(slaveInfo,xmlfilePath,slaveSrcPath);
        }
    }

    private void MyCodeBuilder(Slave_info slaveInfo, String xmlfilePath, String slaveSrcPath) throws IOException {

        List<Pdo> rxPdos = new ArrayList<>();
        List<Pdo> txPdos = new ArrayList<>();
        List<Direction> directions = new ArrayList<>();
        String className = myPraser.prase(slaveInfo.getName(), slaveInfo.getProduct_code(), slaveInfo.getRevision_number(), xmlfilePath, rxPdos, txPdos, directions);

        String source = myAssembler.assembleSource(className, rxPdos, txPdos, directions);
        String header = myAssembler.assembleHeader(className, rxPdos, txPdos, directions);

        String sourceFilePath = slaveSrcPath + className + ".cpp";
        String headerFilePath = slaveSrcPath + "include/" + className + ".h";
        String configCppPath = slaveSrcPath + "config.cpp";
        Files.write(Paths.get(sourceFilePath), source.getBytes());
        Files.write(Paths.get(headerFilePath), header.getBytes());
        myconfigAssembler.editConfigFile(configCppPath, slaveInfo.getProduct_code(), className);
    }
}
