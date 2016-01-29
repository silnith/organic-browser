package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;

public enum FontAbsoluteSize {
	XX_SMALL("xx-small"),
	X_SMALL("x-small"),
	SMALL("small"),
	MEDIUM("medium"),
	LARGE("large"),
	X_LARGE("x-large"),
	XX_LARGE("xx-large");

	private static final Map<String, FontAbsoluteSize> lookupMap;

	static {
		lookupMap = new HashMap<>();
		for (final FontAbsoluteSize fontAbsoluteSize : FontAbsoluteSize.values()) {
			lookupMap.put(fontAbsoluteSize.getValue(), fontAbsoluteSize);
		}
	}

	public static FontAbsoluteSize getFontAbsoluteSize(final String key) {
		return lookupMap.get(key);
	}

	private final String value;

	private FontAbsoluteSize(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
