package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [88] Digit ::= [#x0030-#x0039] | [#x0660-#x0669] | [#x06F0-#x06F9] |
 * [#x0966-#x096F] | [#x09E6-#x09EF] | [#x0A66-#x0A6F] | [#x0AE6-#x0AEF] |
 * [#x0B66-#x0B6F] | [#x0BE7-#x0BEF] | [#x0C66-#x0C6F] | [#x0CE6-#x0CEF] |
 * [#x0D66-#x0D6F] | [#x0E50-#x0E59] | [#x0ED0-#x0ED9] | [#x0F20-#x0F29]
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Digit">Digit</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DigitProduction extends XMLProduction {
    
    public DigitProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
