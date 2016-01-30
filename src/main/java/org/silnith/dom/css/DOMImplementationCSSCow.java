package org.silnith.dom.css;

import org.silnith.dom.DOMImplementationCow;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.DOMImplementationCSS;


public class DOMImplementationCSSCow extends DOMImplementationCow implements DOMImplementationCSS {
    
    public DOMImplementationCSSCow() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public CSSStyleSheet createCSSStyleSheet(final String title, final String media) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
