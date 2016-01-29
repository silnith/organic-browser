package org.silnith.browser.organic.property.accessor;

import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;

public class PaddingLeftAccessor extends PaddingAccessor {

	public PaddingLeftAccessor(final LengthParser lengthParser,
			final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
		super(PropertyName.PADDING_LEFT, lengthParser, fontSizeAccessor);
	}

}
