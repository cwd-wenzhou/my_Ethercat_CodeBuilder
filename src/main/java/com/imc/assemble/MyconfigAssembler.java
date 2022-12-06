package com.imc.assemble;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class MyconfigAssembler {


    public String switchAssemble(String productCode, String className) {
        return "        case " + productCode.replace("#", "0") + ":\n" +
                "            printf(\" config as " + className + " Device!\\n\");\n" +
                "            pslave = new " + className + ";\n" +
                "            break;\n";
    }

    public String includeAssemble(String className) {
        return "#include \"include/" + className + ".h\"\n";
    }


    public void editConfigFile(String configCppPath, String productCode, String className) {
        BufferedReader br;
        String line;
        StringBuilder buf = new StringBuilder();
        String includeString = includeAssemble(className);
        try {
            // 根据文件路径创建缓冲输入流
            br = new BufferedReader(new FileReader(configCppPath));
            // 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
            while ((line = br.readLine()) != null) {
                // 此处修改某两处内容
                if (line.equals(includeString)) {
                    return;
                }
                if (line.startsWith("//Code-Builder include add here")) {
                    buf.append(includeString).append(line);
                } else if (line.startsWith("//Code-Builder switch case add here")) {
                    buf.append(switchAssemble(productCode, className)).append(line);
                }
                // 如果不用修改, 则按原来的内容回写
                else {
                    buf.append(line);
                }
                buf.append(System.getProperty("line.separator"));
            }
            br.close();
            Files.write(Paths.get(configCppPath), buf.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
