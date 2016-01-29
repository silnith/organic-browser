package org.silnith.browser.organic;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class StyledContent {

	private final StyledElement parent;

	public StyledContent(final StyledElement parent) {
		// may be null
		this.parent = parent;
	}

	public abstract StyleData getStyleData();

	public StyledElement getParent() {
		return parent;
	}

	public abstract Node createDOM(final Document document);

}