package org.silnith.browser.organic;

import org.w3c.dom.Element;

public class StyledDOMElement extends StyledElement {

	private final Element element;

	public StyledDOMElement(final StyledElement parent, final Element element,
			final StyleData styleData) {
		super(parent, styleData);
		if (element == null) {
			throw new NullPointerException();
		}
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

	@Override
	public String getTagName() {
		return element.getTagName();
	}

}
