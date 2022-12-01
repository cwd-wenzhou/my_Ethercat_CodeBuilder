package com.imc.assemble;

import com.imc.model.Entry;
import com.imc.model.Pdo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyAssembler {
    public String entiresAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        StringBuilder res = new StringBuilder("ec_pdo_entry_info_t " + className + "_pdo_entries[] = {\n");
        for (Pdo pdo : rxPdos) {
            res.append("    //").append(pdo.getIndex()).append("\n");
            for (Entry entry : pdo.getEntries()) {
                res.append("    {").append(entry.getIndex()).append(", ").
                        append(entry.getSubindex()).append(", ").
                        append(entry.getBitLen()).append("},//").
                        append(entry.getName()).append("\n");
            }
        }
        for (Pdo pdo : txPdos) {
            res.append("    //").append(pdo.getIndex()).append("\n");
            for (Entry entry : pdo.getEntries()) {
                res.append("    {").append(entry.getIndex()).append(", ").
                        append(entry.getSubindex()).append(", ").
                        append(entry.getBitLen()).append("},//").
                        append(entry.getName()).append("\n");
            }
        }
        res.append("};\n");
        return res.toString();
    }

    public String pdosAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        String entriesName = className + "_pdo_entries";
        int count = 0;
        String res = "ec_pdo_info_t " + className + "_pdos[] = {\n";
        for (Pdo pdo : rxPdos) {
            res += "    {"+pdo.getIndex()+", "+pdo.getEntries().size()+", "+entriesName+" + "+count+"},\n";
            count+=pdo.getEntries().size();
        }
        for (Pdo pdo : txPdos) {
            res += "    {"+pdo.getIndex()+", "+pdo.getEntries().size()+", "+entriesName+" + "+count+"},\n";
            count+=pdo.getEntries().size();
        }
        res+="};\n";
        return res;
    }

    public String syncAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos) {
        String pdoName = className + "_pdos";
        String res = "ec_sync_info_t " + className + "_syncs[] = {\n" +
                "   {0, EC_DIR_OUTPUT, 0, nullptr, EC_WD_DISABLE},\n" +
                "   {1, EC_DIR_INPUT,  0, nullptr, EC_WD_DISABLE},\n"+
                "   {2, EC_DIR_OUTPUT, ";
        if (rxPdos.size()==0){
            res+="0, nullptr";
        }
        else {
            res+=rxPdos.size()+", "+pdoName+" + 0";
        }
        res+=",EC_WD_DISABLE},\n"+
        "   {3, EC_DIR_INPUT,  ";

        if (txPdos.size()==0){
            res+="0, nullptr";
        }
        else {
            res+=txPdos.size()+", "+pdoName+" + "+ rxPdos.size();
        }

        res+=", EC_WD_DISABLE},\n   {0xff}};\n";
        return res;
    }

    public String syncInfoAssemble(String className, List<Pdo> rxPdos, List<Pdo> txPdos){
        return "ec_sync_info_t *"+className+"::get_ec_sync_info_t_() {\n" +
                "    return "+className+"_syncs;\n}\n";
    }

}
