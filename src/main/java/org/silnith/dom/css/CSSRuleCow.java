package org.silnith.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;


public abstract class CSSRuleCow implements CSSRule {
    
    private final short type;
    
    public CSSRuleCow(final short type) {
        super();
        this.type = type;
    }
    
    @Override
    public short getType() {
        return type;
    }
    
    @Override
    public String getCssText() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setCssText(final String cssText) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public CSSStyleSheet getParentStyleSheet() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public CSSRule getParentRule() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
