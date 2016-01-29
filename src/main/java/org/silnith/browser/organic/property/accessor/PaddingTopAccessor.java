package org.silnith.browser.organic.property.accessor;

import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;

public class PaddingTopAccessor extends PaddingAccessor {

	public PaddingTopAccessor(final LengthParser lengthParser,
			final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
		super(PropertyName.PADDING_TOP, lengthParser, fontSizeAccessor);
	}

}
