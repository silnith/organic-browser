package org.silnith.css.model.data;

public class Color {

	private static final Color TRANSPARENT = new Color();

	public static Color getTransparent() {
		return TRANSPARENT;
	}

	private final int red;
	private final int green;
	private final int blue;

	private final boolean transparent;

	public Color(final int red, final int green, final int blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.transparent = false;
	}

	public Color(final float red, final float green, final float blue) {
		super();
		this.red = Float.valueOf(red * 255f).intValue();
		this.green = Float.valueOf(green * 255f).intValue();
		this.blue = Float.valueOf(blue * 255f).intValue();
		this.transparent = false;
	}

	private Color() {
		super();
		this.red = 0;
		this.green = 0;
		this.blue = 0;
		this.transparent = true;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public java.awt.Color getAWTColor() {
		return new java.awt.Color(red, green, blue);
	}

	@Override
	public String toString() {
		if (transparent) {
			return "transparent";
		} else {
			return "rgb(" + red + ", " + green + ", " + blue + ")";
		}
	}

}
