package org.example;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XmlTest {
    public static void main(String[] args) throws Exception {

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(
                new MimeHeaders(),
                new FileInputStream("/absolute/path/to/your/xml/data.xml"));

        SOAPBody body = message.getSOAPBody();
        NodeList returnList = body.getElementsByTagName("te:trade");
        Stream<Node> nodeStream = IntStream.range(0, returnList.getLength()).mapToObj(returnList::item);
        nodeStream.forEach(node -> {
            NodeList childList = node.getChildNodes();
            Stream<Node> childStream = IntStream.range(0, childList.getLength()).mapToObj(childList::item);
            List<NodeList> legs = childStream.filter(tempNode -> tempNode.getNodeName().contains("legs")).map(filteredNode -> filteredNode.getChildNodes()).collect(Collectors.toList());
            legs.forEach(nodeList -> {
                Stream<Node> legStream = IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item);
                legStream.filter(nodex -> nodex.getNodeType()==1).parallel().forEach(leg -> {
                    try {
                        String individualLeg = writeXML(leg);
                        // Now you can process this leg anyway you want //
                        System.out.println(individualLeg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
           });

        });
    }

    private static String writeXML(Node node) throws Exception {
        StringWriter sw = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(node), new StreamResult(sw));


        return sw.toString();
    }
}
