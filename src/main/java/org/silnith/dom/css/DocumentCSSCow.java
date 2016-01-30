package org.silnith.dom.css;

import org.silnith.dom.stylesheets.DocumentStyleCow;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.DocumentCSS;


public class DocumentCSSCow extends DocumentStyleCow implements DocumentCSS {
    
    public DocumentCSSCow() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public CSSStyleDeclaration getOverrideStyle(final Element elt, final String pseudoElt) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
