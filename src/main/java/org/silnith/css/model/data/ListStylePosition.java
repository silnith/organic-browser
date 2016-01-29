package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;

public enum ListStylePosition {
	INSIDE("inside"),
	OUTSIDE("outside");

	private static final Map<String, ListStylePosition> lookupMap;

	static {
		lookupMap = new HashMap<>();
		for (final ListStylePosition listStylePosition : values()) {
			lookupMap.put(listStylePosition.getKey(), listStylePosition);
		}
	}

	public static ListStylePosition getFromValue(final String value) {
		return lookupMap.get(value);
	}

	private final String key;

	private ListStylePosition(final String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
