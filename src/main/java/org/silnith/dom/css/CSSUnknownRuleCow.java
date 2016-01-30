package org.silnith.dom.css;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSUnknownRule;


public class CSSUnknownRuleCow extends CSSRuleCow implements CSSUnknownRule {
    
    public CSSUnknownRuleCow() {
        super(CSSRule.UNKNOWN_RULE);
    }
    
}
