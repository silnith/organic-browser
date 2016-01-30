package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [89] Extender ::= #x00B7 | #x02D0 | #x02D1 | #x0387 | #x0640 | #x0E46 |
 * #x0EC6 | #x3005 | [#x3031-#x3035] | [#x309D-#x309E] | [#x30FC-#x30FE]
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Extender">
 *      Extender</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ExtenderProduction extends XMLProduction {
    
    public ExtenderProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
