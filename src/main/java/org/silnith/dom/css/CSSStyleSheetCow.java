package org.silnith.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;
import org.w3c.dom.stylesheets.StyleSheet;

public class CSSStyleSheetCow implements CSSStyleSheet {

	public CSSStyleSheetCow() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDisabled(boolean disabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getOwnerNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StyleSheet getParentStyleSheet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHref() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MediaList getMedia() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSRule getOwnerRule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSRuleList getCssRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertRule(String rule, int index) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteRule(int index) throws DOMException {
		// TODO Auto-generated method stub

	}

}
