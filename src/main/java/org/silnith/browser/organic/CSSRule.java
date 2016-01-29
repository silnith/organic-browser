package org.silnith.browser.organic;

import org.silnith.css.model.data.PropertyName;

public class CSSRule {

	private final String elementName;

	private final PropertyName propertyName;

	private final String styleValue;

	public CSSRule(final String elementName, final PropertyName propertyName, final String styleValue) {
		this.elementName = elementName;
		this.propertyName = propertyName;
		this.styleValue = styleValue;
	}

	public PropertyName getPropertyName() {
		return propertyName;
	}

	public boolean shouldApply(final StyledElement styledElement) {
		final String tagName = styledElement.getTagName();
		
		if (tagName.equalsIgnoreCase(elementName)) {
			return true;
		}
		
		return false;
	}

	public void apply(final StyledContent styledElement) {
		final StyleData styleData = styledElement.getStyleData();
		
		styleData.setSpecifiedValue(propertyName, styleValue);
	}

	@Override
	public String toString() {
		return elementName + " { " + propertyName.getKey() + " : " + styleValue + " }";
	}

}
