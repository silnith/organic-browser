package org.silnith.browser.organic.parser.css3.grammar.token;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;


/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#at-rule">at-rule</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AtRuleNode implements Declaration, Rule {
    
    private final String name;
    
    private final List<Token> prelude;
    
    private SimpleBlock block;
    
    public AtRuleNode(final String name, final List<Token> prelude) {
        super();
        this.name = name;
        this.prelude = prelude;
        this.block = null;
    }
    
    public AtRuleNode(final String name, final List<Token> prelude, final SimpleBlock block) {
        super();
        this.name = name;
        this.prelude = prelude;
        this.block = block;
    }
    
    public void setBlock(final SimpleBlock blockNode) {
        this.block = blockNode;
    }
    
}
