package org.silnith.css.model.data;

public enum PercentageUnit implements Unit {
	PERCENTAGE("%");

	private final String suffix;

	private PercentageUnit(final String suffix) {
		this.suffix = suffix;
	}

	@Override
	public String getSuffix() {
		return suffix;
	}

}
