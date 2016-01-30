package org.silnith.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSRule;


public class CSSCharsetRuleCow extends CSSRuleCow implements CSSCharsetRule {
    
    public CSSCharsetRuleCow() {
        super(CSSRule.CHARSET_RULE);
    }
    
    @Override
    public String getEncoding() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setEncoding(final String encoding) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
}
