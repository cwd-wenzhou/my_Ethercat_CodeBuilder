package com.imc.prase;


import com.imc.model.DataType;
import com.imc.model.Entry;
import com.imc.model.Pdo;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MyPraser {

    public Element getTargetElement(String productCode, String revisionNo, String fliePath) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(fliePath);
        Element root = doc.getRootElement();
        Element descriptions = root.element("Descriptions");
        Element devices = descriptions.element("Devices");
        for (Element device : devices.elements()) {
            Element type = device.element("Type");
            if (productCode.equals(type.attribute("ProductCode").getValue()) &&
                    revisionNo.equals(type.attribute("RevisionNo").getValue())) {
                return device;
            }
        }
        return null;
    }

    /**
     * @param pdoElement pdoElement
     * @param pdoType    "TxPdo" OR " RxPdo"
     * @return 解析完成的pdo
     */
    public Pdo prasePdo(Element pdoElement, String pdoType) {
        if (!pdoElement.getName().equals(pdoType)) {
            return null;
        }
        Pdo pdo = new Pdo();
        pdo.setIndex(pdoElement.element("Index").getStringValue().replace("#", "0"));
        pdo.setName(pdoType+"_"+pdoElement.element("Name").getStringValue().replace(" ", "_"));
        pdo.setExclude(new ArrayList<>());
        pdo.setEntries(new ArrayList<>());
        for (Element exclude : pdoElement.elements("Exclude")) {
            pdo.getExclude().add(exclude.getStringValue().replace("#", "0"));
        }

        for (Element entry : pdoElement.elements("Entry")) {
            pdo.getEntries().add(praseEntry(entry));
        }
        return pdo;
    }


    private Entry praseEntry(Element entryElement) {
        if (!entryElement.getName().equals("Entry")) {
            return null;
        }
        Entry entry = new Entry();
        entry.setIndex(entryElement.element("Index").getStringValue().replace("#", "0"));
        entry.setBitLen(entryElement.element("BitLen").getStringValue());

        Element subIndex = entryElement.element("SubIndex");
        if (subIndex == null) {
            entry.setSubindex("0");
        } else {
            entry.setSubindex(subIndex.getStringValue());
        }


        Element name = entryElement.element("Name");
        if (name == null) {
            entry.setName(null);
        } else {
            entry.setName(name.getStringValue().replace(" ", "_"));
        }

        Element dataType = entryElement.element("DataType");
        if (dataType == null) {
            entry.setDataType(DataType.getDataTypeBytypeString("0"));
        } else {
            entry.setDataType(DataType.getDataTypeBytypeString(dataType.getStringValue()));
        }
        return entry;
    }

    public void excludePdo(List<Pdo> pdos) {
        Iterator<Pdo> iterator = pdos.iterator();
        List<String> toBeExclude = new ArrayList<>();
        while (iterator.hasNext()) {
            Pdo next = iterator.next();
            if (toBeExclude.contains(next.getIndex())) {
                iterator.remove();
            } else {
                toBeExclude.addAll(next.getExclude());
            }
        }
    }
}

