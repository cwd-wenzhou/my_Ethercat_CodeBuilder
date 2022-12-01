package com.imc.my_ethercat_codebuilder;

import com.imc.assemble.MyAssembler;
import com.imc.model.Pdo;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.imc.prase.MyPraser;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MyEthercatCodeBuilderApplicationTests {

    @Autowired
    MyPraser myPraser;

    @Autowired
    MyAssembler myAssembler;

    @Test
    void contextLoads() throws DocumentException {
//        Element targetDevice = myPraser.getTargetElement("#x03f03052",
//                "#x00100000",
//                "/home/cwd/Documents/my_Ethercat_CodeBuilder/src/main/resources/example/xmls/" +
//                        "Beckhoff EL1xxx.xml");
        Element targetDevice = myPraser.getTargetElement("#xbf83052",
                "#x140000",
                "/home/cwd/Documents/my_Ethercat_CodeBuilder/src/main/resources/example/xmls/" +
                        "Beckhoff EL30xx.xml");
        assert targetDevice != null;
        String className = targetDevice.element("Type").getStringValue();
        List<Pdo> rxPdos = new ArrayList<>();
        List<Pdo> txPdos = new ArrayList<>();

        targetDevice.elements("TxPdo").forEach(pdo -> txPdos.add(myPraser.prasePdo(pdo, "TxPdo")));
        targetDevice.elements("RxPdo").forEach(pdo -> rxPdos.add(myPraser.prasePdo(pdo, "RxPdo")));
        myPraser.excludePdo(rxPdos);
        myPraser.excludePdo(txPdos);

        String entiresAssemble = myAssembler.entiresAssemble(className, rxPdos, txPdos);
        String pdosAssemble = myAssembler.pdosAssemble(className, rxPdos, txPdos);
        String syncAssemble = myAssembler.syncAssemble(className, rxPdos, txPdos);
        String syncInfoAssemble = myAssembler.syncInfoAssemble(className, rxPdos, txPdos);
        System.out.println(entiresAssemble);
        System.out.println(pdosAssemble);
        System.out.println(syncAssemble);
        System.out.println(syncInfoAssemble);
    }
}
