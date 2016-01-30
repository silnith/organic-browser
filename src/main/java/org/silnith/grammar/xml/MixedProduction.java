package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.asterisk;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalC;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalP;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.numberSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.verticalBar;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [51] Mixed ::= '(' {@linkplain SProduction S}? '#PCDATA' (
 * {@linkplain SProduction S}? '|' {@linkplain SProduction S}?
 * {@linkplain NameProduction Name})* {@linkplain SProduction S}? ')*' | '('
 * {@linkplain SProduction S}? '#PCDATA' {@linkplain SProduction S}? ')'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Mixed">Mixed</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class MixedProduction extends XMLProduction {
    
    private final NonTerminalSymbol Mixed;
    
    public MixedProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final NameProduction nameProduction) {
        super(grammar);
        Mixed = this.grammar.getNonTerminalSymbol("Mixed");
        
        final NonTerminalSymbol Mixed_inner = this.grammar.getNonTerminalSymbol("Mixed-inner");
        final NonTerminalSymbol Mixed_inner_Plus = this.grammar.getNonTerminalSymbol("Mixed-inner-Plus");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, S, Mixed_inner_Plus, rightParenthesis, asterisk);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, S, rightParenthesis, asterisk);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, Mixed_inner_Plus, rightParenthesis, asterisk);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, rightParenthesis, asterisk);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, S, Mixed_inner_Plus, rightParenthesis, asterisk);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, S, rightParenthesis, asterisk);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, Mixed_inner_Plus, rightParenthesis, asterisk);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, rightParenthesis, asterisk);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, S, rightParenthesis);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, rightParenthesis);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, S, rightParenthesis);
        this.grammar.addProduction(Mixed, nullHandler, leftParenthesis, numberSign, capitalP, capitalC, capitalD,
                capitalA, capitalT, capitalA, rightParenthesis);
                
        this.grammar.addProduction(Mixed_inner_Plus, nullHandler, Mixed_inner, Mixed_inner_Plus);
        this.grammar.addProduction(Mixed_inner_Plus, nullHandler, Mixed_inner);
        
        this.grammar.addProduction(Mixed_inner, nullHandler, verticalBar, S, Name, S);
        this.grammar.addProduction(Mixed_inner, nullHandler, verticalBar, Name, S);
        this.grammar.addProduction(Mixed_inner, nullHandler, verticalBar, S, Name);
        this.grammar.addProduction(Mixed_inner, nullHandler, verticalBar, Name);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Mixed;
    }
    
}
