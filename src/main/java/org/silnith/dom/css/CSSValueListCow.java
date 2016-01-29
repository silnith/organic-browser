package org.silnith.dom.css;

import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

public class CSSValueListCow extends CSSValueCow implements CSSValueList {

	public CSSValueListCow() {
		super(CSSValue.CSS_VALUE_LIST);
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSValue item(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
