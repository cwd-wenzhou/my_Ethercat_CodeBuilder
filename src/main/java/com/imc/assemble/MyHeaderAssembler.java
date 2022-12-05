package com.imc.assemble;

import com.imc.model.BitCount;
import com.imc.model.DataType;
import com.imc.model.Entry;
import com.imc.model.Pdo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MyHeaderAssembler {
    public String offsetAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        String res = "struct " + className + "_PDO_OFFSET {\n";
        res += offsetAssemble_help(rxPdos);
        res += offsetAssemble_help(txPdos);
        res += "};\n";
        return res;
    }

    public String offsetAssemble_help(List<Pdo> pdos) {
        StringBuilder res = new StringBuilder();
        BitCount bitCount = new BitCount();
        String lastIndex = null;
        int size = 0;
        for (Pdo pdo : pdos) {
            for (Entry entry : pdo.getEntries()) {
                if (Objects.equals(entry.getIndex(), "0x0")) {
                    bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                    continue;
                }
                if (!entry.getIndex().equals(lastIndex)) {
                    if (lastIndex != null) {
                         size = bitCount.getBit();
                        if (bitCount.getOffset() > 0) {
                            size++;
                        }
                        res.append("    unsigned int offset_").append(lastIndex, 2, 6).append("[").append(size).append("];\n");
                    }
                    bitCount = new BitCount();
                }
                bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                lastIndex = entry.getIndex();
            }
        }
        if (lastIndex!=null){
            res.append("    unsigned int offset_").append(lastIndex, 2, 6).append("[").append(size).append("];\n");
        }
        return res.toString();
    }


    public String pdoClassAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        String res = pdoClassAssemble_help(className + "_TxPdo", txPdos);
        res += pdoClassAssemble_help(className + "_RxPdo", rxPdos);
        return res;
    }

    private String pdoClassAssemble_help(String pdoName, List<Pdo> pdos) {
        StringBuilder res = new StringBuilder("class " + pdoName + " {\n" + "public:\n");
        for (Pdo pdo : pdos) {
            res.append("    //").append(pdo.getIndex()).append("\n");
            for (Entry entry : pdo.getEntries()) {
                if (entry.getDataType().isBits()) {
                    res.append("    bool ").append(pdo.getName()).append("_").append(entry.getName()).append("[").append(entry.getDataType().getXmlString().charAt(3)).append("]\n");
                } else if (!entry.getDataType().equals(DataType.UNkonw)) {
                    res.append("    ").append(entry.getDataType().getTypeString()).append(" ").append(pdo.getName()).append("_").append(entry.getName()).append(";\n");
                }
            }

        }
        res.append("};\n");
        return res.toString();
    }

    public String classAssemble(String className) {
        return "class " + className + " : public SLAVE {\n" +
                "public:\n" +
                "    " + className + "_TxPdo *" + className.toLowerCase() + "TxPdo;\n" +
                "    " + className + "_RxPdo *" + className.toLowerCase() + "RxPdo;\n" +
                "    struct " + className + "_PDO_OFFSET pdo_offset;\n    void config(int position) override;\n" +
                "\n" +
                "    void read_data() override;\n" +
                "\n" +
                "    void write_data() override;\n" +
                "\n" +
                "    void process_data() override;\n" +
                "\n" +
                "    void print() override;\n" +
                "\n" +
                "    ec_pdo_entry_reg_t *Domain_regs(uint16_t position, uint32_t vendor_id, uint32_t product_code) override;\n" +
                "\n" +
                "    ec_sync_info_t *get_ec_sync_info_t_() override;\n" +
                "\n" +
                "    ~" + className + "() override;\n" +
                "};\n";
    }

    public String externAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        int entriesCount = 0;
        int pdosCount = rxPdos.size() + txPdos.size();
        for (Pdo pdo : rxPdos) {
            for (Entry entry : pdo.getEntries()) {
                if (!Objects.equals(entry.getIndex(), "0x0")) {
                    entriesCount++;
                }
            }
        }
        for (Pdo pdo : txPdos) {
            for (Entry entry : pdo.getEntries()) {
                if (!Objects.equals(entry.getIndex(), "0x0")) {
                    entriesCount++;
                }
            }
        }
        return "extern ec_pdo_entry_info_t "+className+"_pdo_entries[" + entriesCount + "];\n" +
                "extern ec_pdo_info_t "+className+"_pdos[" + pdosCount + "];\n" +
                "extern ec_sync_info_t "+className+"_syncs[5];\n";
    }
}
