package org.silnith.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;


public class CSSStyleRuleCow extends CSSRuleCow implements CSSStyleRule {
    
    public CSSStyleRuleCow() {
        super(CSSRule.STYLE_RULE);
    }
    
    @Override
    public String getSelectorText() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setSelectorText(final String selectorText) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public CSSStyleDeclaration getStyle() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
