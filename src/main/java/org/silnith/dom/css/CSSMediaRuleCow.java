package org.silnith.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.stylesheets.MediaList;

public class CSSMediaRuleCow extends CSSRuleCow implements CSSMediaRule {

	public CSSMediaRuleCow() {
		super(CSSRule.MEDIA_RULE);
	}

	@Override
	public MediaList getMedia() {
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
