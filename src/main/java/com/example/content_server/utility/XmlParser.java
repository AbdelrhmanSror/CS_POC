package com.example.content_server.utility;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlParser {
    private static final String FILENAME = "F:\\abdelrhman\\ocr\\output\\sample.xml";

    public static Element getParsedXML() {
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Element element = null;
        try {
            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(FILENAME));

            // Document doc = db.parse(new InputSource(new StringReader(xml)));

            doc.getDocumentElement().normalize();

            // get <staff>
            NodeList list = doc.getElementsByTagName("employee");

            // for (int temp = 0; temp < list.getLength(); temp++) {

            Node node = list.item(0);
            //Element element;

            /* if (node.getNodeType() == Node.ELEMENT_NODE) {*/

            element = (Element) node;
            /*}*/
            // }

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }

        return element;
    }


}
