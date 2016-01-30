package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalF;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalK;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalM;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalO;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalR;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalS;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalY;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' |
 * 'NMTOKEN' | 'NMTOKENS'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-TokenizedType">
 *      TokenizedType</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class TokenizedTypeProduction extends XMLProduction {
    
    private final NonTerminalSymbol TokenizedType;
    
    public TokenizedTypeProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        TokenizedType = this.grammar.getNonTerminalSymbol("TokenizedType");
        
        this.grammar.addProduction(TokenizedType, nullHandler, capitalI, capitalD);
        this.grammar.addProduction(TokenizedType, nullHandler, capitalI, capitalD, capitalR, capitalE, capitalF);
        this.grammar.addProduction(TokenizedType, nullHandler, capitalI, capitalD, capitalR, capitalE, capitalF,
                capitalS);
        this.grammar.addProduction(TokenizedType, nullHandler, capitalE, capitalN, capitalT, capitalI, capitalT,
                capitalY);
        this.grammar.addProduction(TokenizedType, nullHandler, capitalE, capitalN, capitalT, capitalI, capitalT,
                capitalI, capitalE, capitalS);
        this.grammar.addProduction(TokenizedType, nullHandler, capitalN, capitalM, capitalT, capitalO, capitalK,
                capitalE, capitalN);
        this.grammar.addProduction(TokenizedType, nullHandler, capitalN, capitalM, capitalT, capitalO, capitalK,
                capitalE, capitalN, capitalS);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return TokenizedType;
    }
    
}
