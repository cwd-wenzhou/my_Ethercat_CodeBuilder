package com.imc.prase;


import com.imc.model.DataType;
import com.imc.model.Direction;
import com.imc.model.Entry;
import com.imc.model.Pdo;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MyPraser {
    public String prase(String name, String productCode, String revisionNo, String fliePath, List<Pdo> rxPdos, List<Pdo> txPdos, List<Direction> directions) {
        Element targetDevice = getTargetElement(name, productCode, revisionNo, fliePath);
        assert targetDevice != null;
        targetDevice.elements("Sm").forEach(sm -> directions.add(Direction.getInstance(sm.getStringValue())));
        targetDevice.elements("TxPdo").forEach(pdo -> txPdos.add(prasePdo(pdo, "TxPdo")));
        targetDevice.elements("RxPdo").forEach(pdo -> rxPdos.add(prasePdo(pdo, "RxPdo")));
        excludePdo(rxPdos);
        excludePdo(txPdos);
        return targetDevice.element("Type").getStringValue().replace("-", "_");
    }

    public Element getTargetElement(String name, String productCode, String revisionNo, String xmlFliesPath) {
        SAXReader saxReader = new SAXReader();
        Document doc;
        File file = new File(xmlFliesPath);
        File[] xmlFiles = file.listFiles();
        assert xmlFiles != null;
        for (File xmlFile : xmlFiles) {
            try {
                doc = saxReader.read(xmlFile);
            } catch (DocumentException e) {
                System.out.println("NO FILE!");
                throw new RuntimeException(e);
            }
            Element root = doc.getRootElement();
            Element descriptions = root.element("Descriptions");
            Element devices = descriptions.element("Devices");
            for (Element device : devices.elements()) {
                Element type = device.element("Type");
                List<Element> names = device.elements("Name");
                boolean name_check = false;
                for (Element element : names) {
                    if (element.getStringValue().equals(name)) {
                        name_check = true;
                    }
                }
                if (productCode.equals(type.attribute("ProductCode").getValue()) &&
                        revisionNo.equals(type.attribute("RevisionNo").getValue()) &&
                        name_check) {
                    return device;
                }
            }
        }

        return null;
    }

    /**
     * @param pdoElement pdoElement
     * @param pdoType    "TxPdo" OR " RxPdo"
     * @return ???????????????pdo
     */
    public Pdo prasePdo(Element pdoElement, String pdoType) {
        if (!pdoElement.getName().equals(pdoType)) {
            return null;
        }
        Pdo pdo = new Pdo();
        pdo.setIndex(pdoElement.element("Index").getStringValue().replace("#", "0"));
        pdo.setName(pdoType + "_" + pdoElement.element("Name").getStringValue().replace(" ", "_"));
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

