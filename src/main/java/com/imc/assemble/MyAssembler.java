package com.imc.assemble;

import com.imc.model.BitCount;
import com.imc.model.DataType;
import com.imc.model.Entry;
import com.imc.model.Pdo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MyAssembler {
    int count;

    public String entiresAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        count = 0;
        return "ec_pdo_entry_info_t " + className + "_pdo_entries[] = {\n" + entiresAssemble_help(rxPdos) +
                entiresAssemble_help(txPdos) +
                "};\n";
    }

    private String entiresAssemble_help(List<Pdo> pdos) {
        StringBuilder res = new StringBuilder();
        for (Pdo pdo : pdos) {
            res.append("    //").append(pdo.getIndex()).append("\n");
            for (Entry entry : pdo.getEntries()) {
                if (!Objects.equals(entry.getIndex(), "0x0")) {
                    count++;
                }
                res.append("    {").append(entry.getIndex()).append(", ").
                        append(entry.getSubindex()).append(", ").
                        append(entry.getBitLen()).append("},//").
                        append(entry.getName()).append("\n");
            }
        }
        return res.toString();
    }

    public String pdosAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        int pdosCount = 0;
        String entriesName = className + "_pdo_entries";
        String res = "ec_pdo_info_t " + className + "_pdos[] = {\n";
        for (Pdo pdo : rxPdos) {
            res += "    {" + pdo.getIndex() + ", " + pdo.getEntries().size() + ", " + entriesName + " + " + pdosCount + "},\n";
            pdosCount += pdo.getEntries().size();
        }
        for (Pdo pdo : txPdos) {
            res += "    {" + pdo.getIndex() + ", " + pdo.getEntries().size() + ", " + entriesName + " + " + pdosCount + "},\n";
            pdosCount += pdo.getEntries().size();
        }
        res += "};\n";
        return res;
    }

    public String syncAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        String pdoName = className + "_pdos";
        String res = "ec_sync_info_t " + className + "_syncs[] = {\n" +
                "    {0, EC_DIR_OUTPUT, 0, nullptr, EC_WD_DISABLE},\n" +
                "    {1, EC_DIR_INPUT,  0, nullptr, EC_WD_DISABLE},\n" +
                "    {2, EC_DIR_OUTPUT, ";
        if (rxPdos.size() == 0) {
            res += "0, nullptr";
        } else {
            res += rxPdos.size() + ", " + pdoName + " + 0";
        }
        res += ",EC_WD_DISABLE},\n" +
                "    {3, EC_DIR_INPUT,  ";

        if (txPdos.size() == 0) {
            res += "0, nullptr";
        } else {
            res += txPdos.size() + ", " + pdoName + " + " + rxPdos.size();
        }

        res += ", EC_WD_DISABLE},\n    {0xff}};\n";
        return res;
    }

    public String syncInfoAssemble(String className) {
        return "ec_sync_info_t *" + className + "::get_ec_sync_info_t_() {\n" +
                "    return " + className + "_syncs;\n}\n";
    }

    public String domainRegsAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        String res = "ec_pdo_entry_reg_t *" + className + "::Domain_regs(uint16_t position, uint32_t vendor_id, uint32_t product_code) {\n"
                + "    auto *ans = new ec_pdo_entry_reg_t[" + count + "]{\n";
        res += domainRegsAssemble_help(rxPdos);
        res += domainRegsAssemble_help(txPdos);
        res += "    };\n    return ans;\n}\n";
        return res;
    }

    private String domainRegsAssemble_help(List<Pdo> pdos) {
        String res = "";
        String lastIndex = null;
        BitCount bitCount;
        for (Pdo pdo : pdos) {
            bitCount = new BitCount();
            for (Entry entry : pdo.getEntries()) {
                if (Objects.equals(entry.getIndex(), "0x0")) {
                    bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                    continue;
                }
                if (!entry.getIndex().equals(lastIndex)) {
                    bitCount = new BitCount();
                }
                res += "        {0, position, vendor_id, product_code, " + entry.getIndex() + ", " + entry.getSubindex() + ", &pdo_offset.offset_" +
                        entry.getIndex().substring(2, 6) + "[" + bitCount.getBit() + "]";
                if (Integer.parseInt(entry.getBitLen()) % 8 == 0) {
                    res += "},\n";
                } else {
                    res += ", bit_position" + bitCount.getOffset() + "},\n";
                }
                bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                lastIndex = entry.getIndex();
            }
        }
        return res;
    }

    public String configAssemble(String className) {
        String res = "";
        res += "void " + className + "::config(int position) {\n" +
                "    std::string txpdo_name = \"" + className + "_TxPDO_\";\n" +
                "    txpdo_name.append(std::to_string(position));\n" +
                "    int fd = shm_open(txpdo_name.c_str(), O_CREAT | O_RDWR, 0666);\n" +
                "    ftruncate(fd, sizeof(" + className + "_TxPDO));\n" +
                "    " + className.toLowerCase() + "TxPdo = (" + className + "_TxPDO *) mmap(nullptr, sizeof(" + className + "_TxPDO), PROT_WRITE, MAP_SHARED, fd, 0);\n" +
                "    printf(\"  mmap to %s\\n\", txpdo_name.c_str());\n" +
                "\n" +
                "    std::string rxpdo_name = \"" + className + "_RxPDO_\";\n" +
                "    rxpdo_name.append(std::to_string(position));\n" +
                "    fd = shm_open(rxpdo_name.c_str(), O_CREAT | O_RDWR, 0666);\n" +
                "    ftruncate(fd, sizeof(" + className + "_RxPDO));\n" +
                "    " + className.toLowerCase() + "RxPdo = (" + className + "_RxPDO *) mmap(nullptr, sizeof(" + className + "_RxPDO), PROT_WRITE|PROT_WRITE, MAP_SHARED, fd, 0);\n" +
                "    printf(\"  mmap to %s\\n\", rxpdo_name.c_str());\n" +
                "}\n";
        return res;
    }

    public String readDataAssemble(String classname, List<Pdo> txPdos) {
        String exPdoVarName = classname.toLowerCase() + "TxPdo";
        String res = "void " + classname + "::read_data(){\n";
        String lastIndex = null;
        for (Pdo pdo : txPdos) {
            BitCount bitCount = new BitCount();
            res += "    //" + pdo.getIndex() + "\n";
            for (Entry entry : pdo.getEntries()) {
                if (Objects.equals(entry.getIndex(), "0x0")) {
                    bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                    continue;
                }
                if (!entry.getIndex().equals(lastIndex)) {
                    bitCount = new BitCount();
                }
                switch (entry.getDataType()) {
                    case UNkonw:
                        break;
                    case BOOL, BIT1:
                        res += "    " + exPdoVarName + "->" + pdo.getName() + "_" + entry.getName() + " = " + entry.getDataType().getReadString() +
                                "(domain_pd + pdo_offset.offset_" + entry.getIndex().substring(2, 6) + "[" + bitCount.getBit() + "]" +
                                ", " + bitCount.getOffset() + ");\n";
                        break;
                    case BIT2, BIT3, BIT4, BIT5, BIT6, BIT7, BIT8:
                        int num = Integer.parseInt(entry.getDataType().getXmlString().substring(3, 4));
                        for (int i = 0; i < num; i++) {
                            int bitPoint = bitCount.getOffset() + i;
                            res += "    " + exPdoVarName + "->" + pdo.getName() + "_" + entry.getName() + "[" + i + "]" + " = " + entry.getDataType().getReadString() +
                                    "(domain_pd + pdo_offset.offset_" + entry.getIndex().substring(2, 6) + "[" + bitCount.getBit() + "]" +
                                    ", " + bitPoint + ");\n";
                        }
                        break;
                    default:
                        res += "    " + exPdoVarName + "->" + pdo.getName() + "_" + entry.getName() + " = " + entry.getDataType().getReadString() +
                                "(domain_pd + pdo_offset.offset_" + entry.getIndex().substring(2, 6) + "[" + bitCount.getBit() + "]" + ");\n";
                        break;
                }
                bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                lastIndex = entry.getIndex();
            }
        }
        res += "}\n";
        return res;
    }


    public String writeDataAssemble(String classname, List<Pdo> rxPdos) {
        String exPdoVarName = classname.toLowerCase() + "RxPdo";
        String res = "void " + classname + "::write_data(){\n";
        String lastIndex = null;
        for (Pdo pdo : rxPdos) {
            BitCount bitCount = new BitCount();
            res += "    //" + pdo.getIndex() + "\n";
            for (Entry entry : pdo.getEntries()) {
                if (Objects.equals(entry.getIndex(), "0x0")) {
                    bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                    continue;
                }
                if (!entry.getIndex().equals(lastIndex)) {
                    bitCount = new BitCount();
                }
                switch (entry.getDataType()) {
                    case UNkonw:
                        break;
                    case BOOL, BIT1:
                        res += "    " + entry.getDataType().getWriteString() + "(domain_pd + pdo_offset.offset_" + entry.getIndex().substring(2, 6) +
                                "[" + bitCount.getBit() + "]" + ", " + exPdoVarName + "->" + pdo.getName() + "_" + entry.getName() + "[" + bitCount.getBit() + "]" +
                                ", " + bitCount.getOffset() + ");\n";
                        break;
                    case BIT2, BIT3, BIT4, BIT5, BIT6, BIT7, BIT8:
                        int num = Integer.parseInt(entry.getDataType().getXmlString().substring(3, 4));
                        for (int i = 0; i < num; i++) {
                            int bitPoint = bitCount.getOffset() + i;
                            res += "    " + entry.getDataType().getWriteString() + "(domain_pd + pdo_offset.offset_" + entry.getIndex().substring(2, 6) +
                                    "[" + bitCount.getBit() + "]" + ", " + exPdoVarName + "->" + pdo.getName() + "_" + entry.getName() + "[" + bitCount.getBit() + "]" +
                                    ", " + bitPoint + ");\n";
                        }
                        break;
                    default:
                        res += "    " + entry.getDataType().getWriteString() + "(domain_pd + pdo_offset.offset_" + entry.getIndex().substring(2, 6) +
                                "[" + bitCount.getBit() + "]" + ", " + exPdoVarName + "->" + pdo.getName() + "_" + entry.getName() + ");\n";
                        break;
                }
                bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                lastIndex = entry.getIndex();
            }
        }
        res += "}\n";
        return res;
    }

    public String destructorAssemble(String className){
        return className+"::~"+className+"() {\n" +
                "    munmap("+className.toLowerCase()+"RxPdo, sizeof("+className+"_RxPDO));\n"+
                "    munmap("+className.toLowerCase()+"TxPdo, sizeof("+className+"_TxPDO));\n"+
                "}";
    }

    public String printAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos){
        String res="void "+className+"::print() {\n" +
                "    std::cout << \"=====================================================\" << std::endl";
        res+=printAssemble_help(className,rxPdos);
        res+=printAssemble_help(className,txPdos);
        res+=";\n}\n";
        return res;
    }

    private String printAssemble_help(String className, List<Pdo> pdos){
        String res="";
        //todo
        for (Pdo pdo : pdos) {
            for (Entry entry : pdo.getEntries()) {
                if (entry.getDataType().isBits()) {
                    res += "    bool " + pdo.getName() + "_" + entry.getName() + "[" + entry.getDataType().getXmlString().charAt(3) + "]\n";
                } else if (entry.getDataType().equals(DataType.UNkonw)) {
                    continue;
                } else {
                    res += "    " + entry.getDataType().getTypeString() + " " + pdo.getName() + "_" + entry.getName() + ";\n";
                }
            }

        }
        return res;
    }
    public String processAssemble(String className){
        return "void "+className+"::process_data() {\n" +
                "#ifdef PRINT_DATA\n" +
                "    static int count = 1;\n" +
                "    count++;\n" +
                "    if (count == 1000) {\n" +
                "        count = 0;\n" +
                "        print();\n" +
                "    }\n" +
                "#endif //PRINT_DATA\n" +
                "}\n";
    }

    public String pdoClassAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        String res = pdoClassAssemble_help(className + "_TxPdo", txPdos);
        res += pdoClassAssemble_help(className + "_RxPdo", rxPdos);
        return res;
    }

    private String pdoClassAssemble_help(String pdoName, List<Pdo> pdos) {
        String res = "class " + pdoName + " {\n" + "public:\n";
        for (Pdo pdo : pdos) {
            res += "    //" + pdo.getIndex() + "\n";
            for (Entry entry : pdo.getEntries()) {
                if (entry.getDataType().isBits()) {
                    res += "    bool " + pdo.getName() + "_" + entry.getName() + "[" + entry.getDataType().getXmlString().charAt(3) + "]\n";
                } else if (entry.getDataType().equals(DataType.UNkonw)) {
                    continue;
                } else {
                    res += "    " + entry.getDataType().getTypeString() + " " + pdo.getName() + "_" + entry.getName() + ";\n";
                }
            }

        }
        res += "}\n";
        return res;
    }

}
