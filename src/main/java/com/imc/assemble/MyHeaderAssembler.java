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
        BitCount bitCount = new BitCount();
        String lastIndex = null;
        for (Pdo pdo : rxPdos) {
            for (Entry entry : pdo.getEntries()) {
                if (Objects.equals(entry.getIndex(), "0x0")) {
                    bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                    continue;
                }
                if (!entry.getIndex().equals(lastIndex)) {
                    if (lastIndex != null) {
                        int size = bitCount.getBit();
                        if (bitCount.getOffset() > 0) {
                            size++;
                        }
                        res += "    unsigned int offset_" + lastIndex.substring(2, 6) + "[" + size + "];\n";
                    }
                    bitCount = new BitCount();
                }
                bitCount.addBit(Integer.parseInt(entry.getBitLen()));
                lastIndex = entry.getIndex();
            }
        }
        res += "};\n";
        return res;
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

    public String classAssemble(String className) {
        return "class " + className + " : public SLAVE {\n" +
                "public:\n" +
                "    " + className + "TxPdo *" + className.toLowerCase() + "Txpdo;\n" +
                "    " + className + "RxPdo *" + className.toLowerCase() + "Rxpdo;\n" +
                "    struct " + className + "_PDO_OFFSET pdo_offset;\nvoid config(int position) override;\n" +
                "    void read_data() override;\n" +
                "    void write_data() override;\n" +
                "    void process_data() override;\n" +
                "    void print() override;\n" +
                "    ec_pdo_entry_reg_t *Domain_regs(uint16_t position, uint32_t vendor_id, uint32_t product_code) override;\n" +
                "    ec_sync_info_t *get_ec_sync_info_t_() override;\n" +
                "    ~" + className + "() override;\n" +
                "};\n";
    }

    public String externAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        int entriesCount = 0;
        int pdosCount = rxPdos.size()+txPdos.size();
        for (Pdo pdo : rxPdos) {
            for (Entry entry : pdo.getEntries()) {
                if (!Objects.equals(entry.getIndex(), "0x0")) {
                    entriesCount++;
                }
            }
        }
        return "extern ec_pdo_entry_info_t EL3064_pdo_entries[" + entriesCount + "];\n" +
                "extern ec_pdo_info_t EL3064_pdos[" + pdosCount + "];\n" +
                "extern ec_sync_info_t EL3064_syncs[5];\n";
    }
}
