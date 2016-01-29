package org.silnith.browser.organic.parser;

import java.io.IOException;

import org.silnith.browser.organic.network.Download;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

public class XMLLoadStoreParser implements FileParser<Document> {

	private final DOMImplementationLS loadStoreFeature;

	public XMLLoadStoreParser(final DOMImplementationRegistry domImplementationRegistry) {
		super();
		final DOMImplementation domImplementation = domImplementationRegistry.getDOMImplementation("Core 3.0 +LS 3.0 +CSS 2.0");
		this.loadStoreFeature = (DOMImplementationLS) domImplementation.getFeature("+LS", "3.0");
		if (this.loadStoreFeature == null) {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public Document parse(final Download download) throws IOException {
		final LSInput lsInput = loadStoreFeature.createLSInput();
		lsInput.setByteStream(download.getInputStream());
		final LSParser lsParser = loadStoreFeature.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
		final Document document = lsParser.parse(lsInput);
		return document;
	}

	@Override
	public void dispose() {
	}

}
