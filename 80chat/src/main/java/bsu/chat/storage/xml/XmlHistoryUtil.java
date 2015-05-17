package bsu.chat.storage.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import bsu.chat.model.Msg;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static bsu.chat.util.MsgUtil.ID;
import static bsu.chat.util.MsgUtil.USER;
import static bsu.chat.util.MsgUtil.TEXT;
import static bsu.chat.util.MsgUtil.DATE;

public class XMLHistoryUtil {
    private static final String STORAGE_LOCATION = System.getProperty("user.home") +  File.separator + "history.xml";
    private static final String MESSAGES = "messages";
    private static final String MESSAGE = "message";

    private XMLHistoryUtil() {
    }

    public static synchronized void createStorage() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(MESSAGES);
        doc.appendChild(rootElement);

        Transformer transformer = getTransformer();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(STORAGE_LOCATION));
        transformer.transform(source, result);
    }

    public static synchronized void addData(Msg msg) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(STORAGE_LOCATION);
        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();

        Element messageElement = document.createElement(MESSAGE);
        root.appendChild(messageElement);

        messageElement.setAttribute(ID, msg.getID());

        Element user = document.createElement(USER);
        user.appendChild(document.createTextNode(msg.getUsername()));
        messageElement.appendChild(user);

        Element text = document.createElement(TEXT);
        text.appendChild(document.createTextNode(msg.getText()));
        messageElement.appendChild(text);

        Element date = document.createElement(DATE);
        date.appendChild(document.createTextNode(msg.getDate()));
        messageElement.appendChild(date);

        DOMSource source = new DOMSource(document);

        Transformer transformer = getTransformer();

        StreamResult result = new StreamResult(STORAGE_LOCATION);
        transformer.transform(source, result);
    }

    public static synchronized void addAll(List<Msg> messages) throws ParserConfigurationException, SAXException, IOException, TransformerException {

        if (!isExist()) {
            createStorage();
        }
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(STORAGE_LOCATION);
        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();

        for (Msg msg : messages) {

            Element messageElement = document.createElement(MESSAGE);
            root.appendChild(messageElement);

            messageElement.setAttribute(ID, msg.getID());

            Element user = document.createElement(USER);
            user.appendChild(document.createTextNode(msg.getUsername()));
            messageElement.appendChild(user);

            Element text = document.createElement(TEXT);
            text.appendChild(document.createTextNode(msg.getText()));
            messageElement.appendChild(text);

            Element date = document.createElement(DATE);
            date.appendChild(document.createTextNode(msg.getDate()));
            messageElement.appendChild(date);

            DOMSource source = new DOMSource(document);

            Transformer transformer = getTransformer();

            StreamResult result = new StreamResult(STORAGE_LOCATION);
            transformer.transform(source, result);
        }


    }

    public static synchronized void updateData(Msg msg) throws ParserConfigurationException, SAXException, IOException, TransformerException, XPathExpressionException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(STORAGE_LOCATION);
        document.getDocumentElement().normalize();
        Node messageToUpdate = getNodeById(document, msg.getID());

        if (messageToUpdate != null) {
            NodeList childNodes = messageToUpdate.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (TEXT.equals(node.getNodeName())) {
                    node.setTextContent(msg.getText());
                }
            }

            Transformer transformer = getTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(STORAGE_LOCATION));
            transformer.transform(source, result);
        } else {
            throw new NullPointerException();
        }
    }

    public static synchronized void deleteData(Msg msg) throws ParserConfigurationException, SAXException, IOException, TransformerException, XPathExpressionException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(STORAGE_LOCATION);
        document.getDocumentElement().normalize();

        NodeList root = document.getElementsByTagName(MESSAGES);
        Node messageToDelete = getNodeById(document, msg.getID());
        if (messageToDelete != null) {
            root.item(0).removeChild(messageToDelete);

            Transformer transformer = getTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(STORAGE_LOCATION));
            transformer.transform(source, result);
        } else {
            throw new NullPointerException();
        }
    }

    public static synchronized boolean isExist() {
        File file = new File(STORAGE_LOCATION);
        return file.exists();
    }

    public static synchronized List<Msg> getMessages() throws SAXException, IOException, ParserConfigurationException {
        List<Msg> messages = new ArrayList<Msg>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(STORAGE_LOCATION);
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();
        NodeList messageList = root.getElementsByTagName(MESSAGE);
        for (int i = 0; i < messageList.getLength(); i++) {
            Element messageElement = (Element) messageList.item(i);
            String id = messageElement.getAttribute(ID);
            String author = messageElement.getElementsByTagName(USER).item(0).getTextContent();
            String text = messageElement.getElementsByTagName(TEXT).item(0).getTextContent();
            String date = messageElement.getElementsByTagName(DATE).item(0).getTextContent();
            messages.add(new Msg(id, author, text, date));
        }
        return messages;
    }

    public static synchronized int getStorageSize() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(STORAGE_LOCATION);
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();
        return root.getElementsByTagName(MESSAGE).getLength();
    }

    private static Node getNodeById(Document doc, String id) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile("//" + MESSAGE + "[@id='" + id + "']");
        return (Node) expr.evaluate(doc, XPathConstants.NODE);
    }

    private static Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }

    public static synchronized boolean doesStorageExist() {
        File file = new File(STORAGE_LOCATION);
        return file.exists();
    }
}
