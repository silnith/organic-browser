package org.silnith.browser.organic.parser.css3.grammar.token;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;


/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#qualified-rule">qualified
 *      rule</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class QualifiedRuleNode implements Rule {
    
    private final List<Token> prelude;
    
    private SimpleBlock block;
    
    public QualifiedRuleNode(final List<Token> prelude) {
        super();
        this.prelude = prelude;
        this.block = null;
    }
    
    public List<Token> getPrelude() {
        return prelude;
    }

    public SimpleBlock getBlock() {
        return block;
    }

    public void setBlock(final SimpleBlock blockNode) {
        this.block = blockNode;
    }

    @Override
    public String toString() {
        return "QualifiedRuleNode {"
                + "prelude=" + prelude
                + ", "
                + "block=" + block
                + "}";
    }
    
}
