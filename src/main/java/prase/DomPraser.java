package prase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DomPraser {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//创建一个DocumentBuilderFactory的对象
        DocumentBuilder builder = factory.newDocumentBuilder();//创建一个DocumentBuilder对象
        Document doc = builder.parse("/home/cwd/Documents/my_Ethercat_CodeBuilder/src/main/resources/example/xmls/Maxsine_EP3E_EC_V01_11.xml");
        Element root = doc.getDocumentElement();//获取根节点
        Node student = root.getFirstChild().getNextSibling();//
        Node name = student.getFirstChild().getNextSibling();//
        Node age = name.getNextSibling().getNextSibling();//
        System.out.println("节点的名称："+name.getNodeName()+"节点的文本:"+name.getTextContent());
//getFirstChild()是获取第一个子节点
//getNextSibling()是获取兄弟节点

    }
}
