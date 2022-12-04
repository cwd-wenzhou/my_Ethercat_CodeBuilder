package com.imc.my_ethercat_codebuilder;

import com.imc.assemble.MyAssembler;
import com.imc.model.Pdo;
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

    @Test
    void contextLoads() throws IOException {
        String productCode = "#x01";
        String revisionNo = "#x00001";
        String xmlfilePath = "/home/imc/Documents/ethercat/my_Ethercat_CodeBuilder/src/main/resources/example/xmls/" +
                "Maxsine_EP3E_EC_V01_11.xml";


//        String productCode = "#x03f03052";
//        String revisionNo = "#xx00100000";
//        String filePath = "/home/imc/Documents/ethercat/my_Ethercat_CodeBuilder/src/main/resources/example/xmls/" +
//                "Maxsine_EP3E_EC_V01_11.xml";

//        String productCode = "#xbf83052";
//        String revisionNo = "#x140000";
//        String filePath = "/home/imc/Documents/ethercat/my_Ethercat_CodeBuilder/src/main/resources/example/xmls/" +
//                        "Beckhoff EL30xx.xml";


        List<Pdo> rxPdos = new ArrayList<>();
        List<Pdo> txPdos = new ArrayList<>();
        String className = myPraser.prase(productCode, revisionNo, xmlfilePath, rxPdos, txPdos);

        String source = myAssembler.assembleSource(className, rxPdos, txPdos);
        String header = myAssembler.assembleHeader(className, rxPdos, txPdos);


        String sourceFilePath = "/home/imc/Documents/ethercat/Ethercat-server-control/src/slaves/" + className + ".cpp";
        String headerFilePath = "/home/imc/Documents/ethercat/Ethercat-server-control/src/slaves/include/" + className + ".h";

        Files.write(Paths.get(sourceFilePath), source.getBytes());
        Files.write(Paths.get(headerFilePath), header.getBytes());

        System.out.println(source);
        System.out.println(header);

    }
}
