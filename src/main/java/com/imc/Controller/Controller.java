package com.imc.Controller;

import com.alibaba.fastjson.JSON;
import com.imc.model.Slave_info;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/ethercat/codeBuild")
@CrossOrigin
public class Controller {

    /**
     * gcl 的版本号
     * 代表这最后修改时间
     *
     * @return 最后修改时间
     */
    @GetMapping()
    @ResponseBody
    public void getVersion() {
        String xmlfilePath = "/home/imc/Documents/ethercat/my_Ethercat_CodeBuilder/src/main/resources/example/xmls";
        if (args.length < 1) {
            System.out.println("put in EtherCAT slave_info_str!");
        } else {
            String slave_info_str = "[{\"name\":\"EK1100 EtherCAT-Koppler (2A E-Bus)\",\"product_code\":\"0x44c2c52\",\"revision_number\":\"0x120000\",\"product_code\":\"0x44c2c52\",\"position\":\"0x0\"},{\"name\":\"EL1008 8K. Dig. Eingang 24V, 3ms\",\"product_code\":\"0x3f03052\",\"revision_number\":\"0x110000\",\"product_code\":\"0x3f03052\",\"position\":\"0x1\"},{\"name\":\"EL2008 8K. Dig. Ausgang 24V, 0.5A\",\"product_code\":\"0x7d83052\",\"revision_number\":\"0x110000\",\"product_code\":\"0x7d83052\",\"position\":\"0x2\"},{\"name\":\"EL3064 4K.Ana. Eingang 0-10V\",\"product_code\":\"0xbf83052\",\"revision_number\":\"0x140000\",\"product_code\":\"0xbf83052\",\"position\":\"0x3\"},]";
            List<Slave_info> slaveInfos = JSON.parseArray(slave_info_str, Slave_info.class);
            for (Slave_info slaveInfo : slaveInfos) {

            }
        }
    }


}
