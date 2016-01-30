package org.silnith.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;


public abstract class CSSValueCow implements CSSValue {
    
    private final short cssValueType;
    
    public CSSValueCow(final short cssValueType) {
        super();
        this.cssValueType = cssValueType;
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
    public short getCssValueType() {
        return cssValueType;
    }
    
}
