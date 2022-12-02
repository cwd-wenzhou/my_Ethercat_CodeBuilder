package com.imc.assemble;

import com.imc.model.BitCount;
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
                "   {0, EC_DIR_OUTPUT, 0, nullptr, EC_WD_DISABLE},\n" +
                "   {1, EC_DIR_INPUT,  0, nullptr, EC_WD_DISABLE},\n" +
                "   {2, EC_DIR_OUTPUT, ";
        if (rxPdos.size() == 0) {
            res += "0, nullptr";
        } else {
            res += rxPdos.size() + ", " + pdoName + " + 0";
        }
        res += ",EC_WD_DISABLE},\n" +
                "   {3, EC_DIR_INPUT,  ";

        if (txPdos.size() == 0) {
            res += "0, nullptr";
        } else {
            res += txPdos.size() + ", " + pdoName + " + " + rxPdos.size();
        }

        res += ", EC_WD_DISABLE},\n   {0xff}};\n";
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
        BitCount bitCount = new BitCount();
        for (Pdo pdo : pdos) {
            for (Entry entry : pdo.getEntries()) {
                if (Objects.equals(entry.getIndex(), "0x0")) {
                    bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                    continue;
                }
                res += "        {0, position, vendor_id, product_code, " + entry.getIndex() + ", " + entry.getSubindex() + ", &pdo_offset.offset_" +
                        entry.getIndex().substring(2, 6) + "[" + bitCount.getBit() + "]";
                if (Integer.parseInt(entry.getBitLen()) % 8 == 0) {
                    res += "},\n";
                } else {
                    res += ", bit_position" + bitCount.getOffset() + "},\n";
                }

                if (entry.getIndex().equals(lastIndex)) {
                    bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                } else {
                    bitCount = new BitCount();
                    bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                    lastIndex = entry.getIndex();
                }
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
}
