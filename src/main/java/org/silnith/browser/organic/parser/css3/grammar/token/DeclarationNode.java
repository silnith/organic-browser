package org.silnith.browser.organic.parser.css3.grammar.token;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;


/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#declaration">declaration
 *      </a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DeclarationNode implements Declaration {
    
    private final String name;
    
    private final List<Token> value;
    
    private boolean importantFlag;
    
    public DeclarationNode(final String name, final List<Token> value) {
        super();
        this.name = name;
        this.value = value;
    }
    
    public boolean isImportantFlag() {
        return importantFlag;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Token> getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "DeclarationNode [name=" + name + ", value=" + value + ", importantFlag=" + importantFlag + "]";
    }
    
}
