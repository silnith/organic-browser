package org.silnith.dom.css;

import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;


public class CSSFontFaceRuleCow extends CSSRuleCow implements CSSFontFaceRule {
    
    public CSSFontFaceRuleCow(final short type) {
        super(CSSRule.FONT_FACE_RULE);
    }
    
    @Override
    public CSSStyleDeclaration getStyle() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
