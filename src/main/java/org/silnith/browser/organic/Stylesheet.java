package org.silnith.browser.organic;

import java.util.Collection;


public class Stylesheet {
    
    private final Collection<CSSRule> rules;
    
    private final Collection<CSSPseudoElementRuleSet> pseudoRules;
    
    public Stylesheet(final Collection<CSSRule> rules, final Collection<CSSPseudoElementRuleSet> pseudoRules) {
        this.rules = rules;
        this.pseudoRules = pseudoRules;
    }
    
    public Collection<CSSRule> getRules() {
        return rules;
    }
    
    public Collection<CSSPseudoElementRuleSet> getPseudoRules() {
        return pseudoRules;
    }
}
