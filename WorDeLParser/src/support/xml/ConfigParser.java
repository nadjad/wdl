package support.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigParser {

	public static Properties parse(String filepath) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Properties prop = new Properties();
		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(filepath);

			// get the root element
			Element docElements = dom.getDocumentElement();

			// get a nodelist of elements
			NodeList nl = docElements.getElementsByTagName("Base_Path");
			if (nl != null && nl.getLength() > 0) {
				Element el = (Element) nl.item(0);
				prop.setBasePath(el.getTextContent());
				System.out.println("----------" + el.getTextContent());
			}

			nl = docElements.getElementsByTagName("Result_Path");
			if (nl != null && nl.getLength() > 0) {
				Element el = (Element) nl.item(0);
				prop.setResultPath(el.getTextContent());
				System.out.println("----------" + el.getTextContent());
			}
			return prop;
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			return prop;
		}
	}
}
