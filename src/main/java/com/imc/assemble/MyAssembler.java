package com.imc.assemble;

import com.imc.model.Pdo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MyAssembler {
    final
    MyHeaderAssembler myHeaderAssembler;

    final
    MySourceAssembler mySourceAssembler;
    Date date = new Date();

    public MyAssembler(MyHeaderAssembler myHeaderAssembler, MySourceAssembler mySourceAssembler) {
        this.myHeaderAssembler = myHeaderAssembler;
        this.mySourceAssembler = mySourceAssembler;
    }

    public String assembleHeader(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {

        String res =  "/*\n" +
                " * @Author: cwd-wenzhou 619715109@qq.com\n" +
                " * @Date: " + date + "\n" +
                " * @Description: Created by my_EtherCat_CodeBuilder\n"+
                " *\n" +
                " * Copyright (c) 2022 by cwd-wenzhou 619715109@qq.com, All Rights Reserved.\n" +
                " */\n"+
                "#ifndef ETHERCAT_SERVER_CONTROL_"+className+"_H\n"+
                "#define ETHERCAT_SERVER_CONTROL_"+className+"_H\n\n"+
                "#include \"../../include/slave.h\"\n\n";

        res+=myHeaderAssembler.offsetAssemble(className,rxPdos,txPdos);
        res+=myHeaderAssembler.pdoClassAssemble(className,rxPdos,txPdos);
        res+=myHeaderAssembler.classAssemble(className);
        res+=myHeaderAssembler.externAssemble(className,rxPdos,txPdos);
        res+="\n#endif // ETHERCAT_SERVER_CONTROL_"+className+"_H";
        return res;
    }

    public String assembleSource(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {

        String res =
                "/*\n" +
                " * @Author: cwd-wenzhou 619715109@qq.com\n" +
                " * @Date: " + date + "\n" +
                " * @Description: Created by my_EtherCat_CodeBuilder\n"+
                " *\n" +
                " * Copyright (c) 2022 by cwd-wenzhou 619715109@qq.com, All Rights Reserved.\n" +
                " */\n"+
                "#include \"include/"+className+".h\"\n\n";
        res += mySourceAssembler.entiresAssemble(className, rxPdos, txPdos)
                + mySourceAssembler.pdosAssemble(className, rxPdos, txPdos)
                + mySourceAssembler.syncAssemble(className, rxPdos, txPdos)
                + mySourceAssembler.syncInfoAssemble(className)
                + mySourceAssembler.domainRegsAssemble(className, rxPdos, txPdos)
                + mySourceAssembler.configAssemble(className)
                + mySourceAssembler.readDataAssemble(className, txPdos)
                + mySourceAssembler.processAssemble(className)
                + mySourceAssembler.writeDataAssemble(className, rxPdos)
                + mySourceAssembler.printAssemble(className, rxPdos, txPdos)
                + mySourceAssembler.destructorAssemble(className);
        return res;
    }

}
