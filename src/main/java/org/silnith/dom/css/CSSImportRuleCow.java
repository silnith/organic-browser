package org.silnith.dom.css;

import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;

public class CSSImportRuleCow extends CSSRuleCow implements CSSImportRule {

	public CSSImportRuleCow() {
		super(CSSRule.IMPORT_RULE);
	}

	@Override
	public String getHref() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MediaList getMedia() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSStyleSheet getStyleSheet() {
		// TODO Auto-generated method stub
		return null;
	}

}
