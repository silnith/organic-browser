package org.silnith.browser.organic.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.silnith.browser.organic.network.Download;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JAXPParser implements FileParser<Document> {

	private final DocumentBuilder documentBuilder;

	public JAXPParser(final DocumentBuilderFactory documentBuilderFactory) throws ParserConfigurationException, SAXException {
		super();
		// validating means DTD only
		documentBuilderFactory.setValidating(false);
		// can only ignore whitespace if it is validating
		documentBuilderFactory.setIgnoringElementContentWhitespace(false);
		documentBuilderFactory.setIgnoringComments(false);
		documentBuilderFactory.setNamespaceAware(false);
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "all");
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "all");
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "all");
		documentBuilderFactory.setCoalescing(false);
		documentBuilderFactory.setExpandEntityReferences(true);
		documentBuilderFactory.setNamespaceAware(false);
		documentBuilderFactory.setXIncludeAware(false);
		final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schemaFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		final Source schemaSource = new StreamSource();
		final Schema schema = schemaFactory.newSchema(schemaSource);
		documentBuilderFactory.setSchema(schema);
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
	}

	@Override
	public Document parse(final Download download) throws IOException {
		final InputSource inputSource = new InputSource(new ByteArrayInputStream(download.getContentBytes()));
		try {
			return documentBuilder.parse(inputSource);
		} catch (final SAXException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void dispose() {
	}

}
