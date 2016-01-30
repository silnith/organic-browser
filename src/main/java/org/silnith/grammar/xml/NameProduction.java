package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [5] Name ::= {@linkplain NameStartCharProduction NameStartChar} (
 * {@linkplain NameCharProduction NameChar})*
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Name">Name</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NameProduction extends XMLProduction {
    
    private final NonTerminalSymbol Name;
    
    public NameProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final NameStartCharProduction nameStartCharProduction, final NameCharProduction nameCharProduction) {
        super(grammar);
        Name = this.grammar.getNonTerminalSymbol("Name");
        
        final NonTerminalSymbol NameStartChar = nameStartCharProduction.getNonTerminalSymbol();
        final NonTerminalSymbol NameChar_Plus = nameCharProduction.getPlus();
        
        this.grammar.addProduction(Name, stringHandler, NameStartChar, NameChar_Plus);
        this.grammar.addProduction(Name, stringHandler, NameStartChar);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Name;
    }
    
}
