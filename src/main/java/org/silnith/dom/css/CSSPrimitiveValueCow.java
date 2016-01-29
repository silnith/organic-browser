package org.silnith.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;

public class CSSPrimitiveValueCow extends CSSValueCow implements
		CSSPrimitiveValue {

	private short primitiveType;

	public CSSPrimitiveValueCow(final short primitiveType) {
		super(CSSValue.CSS_PRIMITIVE_VALUE);
		this.primitiveType = primitiveType;
	}

	@Override
	public short getPrimitiveType() {
		return primitiveType;
	}

	protected void setPrimitiveType(final short primitiveType) {
		this.primitiveType = primitiveType;
	}

	@Override
	public void setFloatValue(final short unitType, final float floatValue)
			throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "This value is not of type float.");
	}

	@Override
	public float getFloatValue(final short unitType) throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "This value is not of type float.");
	}

	@Override
	public void setStringValue(final short stringType, final String stringValue)
			throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "This value is not of type string.");
	}

	@Override
	public String getStringValue() throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "This value is not of type string.");
	}

	@Override
	public Counter getCounterValue() throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "This value is not of type counter.");
	}

	@Override
	public Rect getRectValue() throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "This value is not of type rect.");
	}

	@Override
	public RGBColor getRGBColorValue() throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "This value is not of type rgb color.");
	}

}
