package org.silnith.browser.organic.property.accessor;

import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;

public class MarginBottomAccessor extends MarginAccessor {

	public MarginBottomAccessor(final LengthParser lengthParser,
			final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
		super(PropertyName.MARGIN_BOTTOM, lengthParser, fontSizeAccessor);
	}

}
