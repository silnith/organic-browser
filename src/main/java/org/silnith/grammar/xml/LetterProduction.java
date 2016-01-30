package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [84] Letter ::= {@linkplain BaseCharProduction BaseChar} |
 * {@linkplain IdeographicProduction Ideographic}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Letter">Letter
 *      </a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class LetterProduction extends XMLProduction {
    
    private final NonTerminalSymbol Letter;
    
    public LetterProduction(final Grammar<UnicodeTerminalSymbols> grammar, final BaseCharProduction baseCharProduction,
            final IdeographicProduction ideographicProduction) {
        super(grammar);
        Letter = this.grammar.getNonTerminalSymbol("Letter");
        
        final NonTerminalSymbol BaseChar = baseCharProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Ideographic = ideographicProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(Letter, nullHandler, BaseChar);
        this.grammar.addProduction(Letter, nullHandler, Ideographic);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Letter;
    }
    
}
