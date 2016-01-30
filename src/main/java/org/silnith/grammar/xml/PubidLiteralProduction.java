package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.apostrophe;
import static org.silnith.grammar.UnicodeTerminalSymbols.quotationMark;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [12] PubidLiteral ::= '"' {@linkplain PubidCharProduction PubidChar}* '"' |
 * "'" ({@linkplain PubidCharProduction PubidChar} - "'")* "'"
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PubidLiteral">
 *      PubidLiteral</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PubidLiteralProduction extends XMLProduction {
    
    private final NonTerminalSymbol PubidLiteral;
    
    public PubidLiteralProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final PubidCharProduction pubidCharProduction) {
        super(grammar);
        PubidLiteral = this.grammar.getNonTerminalSymbol("PubidLiteral");
        
        final NonTerminalSymbol QuotedPubidChar_Plus = this.grammar.getNonTerminalSymbol("QuotedPubidChar-Plus");
        
        final NonTerminalSymbol PubidChar = pubidCharProduction.getNonTerminalSymbol();
        final NonTerminalSymbol PubidChar_Plus = pubidCharProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(PubidLiteral, nullHandler, quotationMark, QuotedPubidChar_Plus, quotationMark);
        this.grammar.addProduction(PubidLiteral, nullHandler, quotationMark, quotationMark);
        this.grammar.addProduction(PubidLiteral, nullHandler, apostrophe, PubidChar_Plus, apostrophe);
        this.grammar.addProduction(PubidLiteral, nullHandler, apostrophe, apostrophe);
        this.grammar.addProduction(QuotedPubidChar_Plus, stringHandler, PubidChar);
        this.grammar.addProduction(QuotedPubidChar_Plus, stringHandler, apostrophe);
        this.grammar.addProduction(QuotedPubidChar_Plus, stringHandler, QuotedPubidChar_Plus, PubidChar);
        this.grammar.addProduction(QuotedPubidChar_Plus, stringHandler, QuotedPubidChar_Plus, apostrophe);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return PubidLiteral;
    }
    
}
