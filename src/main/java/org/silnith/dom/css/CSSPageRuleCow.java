package org.silnith.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;

public class CSSPageRuleCow extends CSSRuleCow implements CSSPageRule {

	public CSSPageRuleCow() {
		super(CSSRule.PAGE_RULE);
	}

	@Override
	public String getSelectorText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelectorText(String selectorText) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public CSSStyleDeclaration getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

}
