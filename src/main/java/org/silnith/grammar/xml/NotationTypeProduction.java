package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalO;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.verticalBar;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [58] NotationType ::= 'NOTATION' {@linkplain SProduction S} '('
 * {@linkplain SProduction S}? {@linkplain NameProduction Name} (
 * {@linkplain SProduction S}? '|' {@linkplain SProduction S}?
 * {@linkplain NameProduction Name})* {@linkplain SProduction S}? ')'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NotationType">
 *      NotationType</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NotationTypeProduction extends XMLProduction {
    
    private final NonTerminalSymbol NotationType;
    
    public NotationTypeProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final NameProduction nameProduction) {
        super(grammar);
        NotationType = this.grammar.getNonTerminalSymbol("NotationType");
        
        final NonTerminalSymbol NotationType_inner = this.grammar.getNonTerminalSymbol("NotationType-inner");
        final NonTerminalSymbol NotationType_inner_Plus = this.grammar.getNonTerminalSymbol("NotationType-inner-Plus");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(NotationType, nullHandler, capitalN, capitalO, capitalT, capitalA, capitalT,
                capitalI, capitalO, capitalN, S, leftParenthesis, S, Name, S, NotationType_inner_Plus,
                rightParenthesis);
        this.grammar.addProduction(NotationType, nullHandler, capitalN, capitalO, capitalT, capitalA, capitalT,
                capitalI, capitalO, capitalN, S, leftParenthesis, Name, S, NotationType_inner_Plus, rightParenthesis);
        this.grammar.addProduction(NotationType, nullHandler, capitalN, capitalO, capitalT, capitalA, capitalT,
                capitalI, capitalO, capitalN, S, leftParenthesis, S, Name, NotationType_inner_Plus, rightParenthesis);
        this.grammar.addProduction(NotationType, nullHandler, capitalN, capitalO, capitalT, capitalA, capitalT,
                capitalI, capitalO, capitalN, S, leftParenthesis, Name, NotationType_inner_Plus, rightParenthesis);
        this.grammar.addProduction(NotationType, nullHandler, capitalN, capitalO, capitalT, capitalA, capitalT,
                capitalI, capitalO, capitalN, S, leftParenthesis, S, Name, S, rightParenthesis);
        this.grammar.addProduction(NotationType, nullHandler, capitalN, capitalO, capitalT, capitalA, capitalT,
                capitalI, capitalO, capitalN, S, leftParenthesis, Name, S, rightParenthesis);
        this.grammar.addProduction(NotationType, nullHandler, capitalN, capitalO, capitalT, capitalA, capitalT,
                capitalI, capitalO, capitalN, S, leftParenthesis, S, Name, rightParenthesis);
        this.grammar.addProduction(NotationType, nullHandler, capitalN, capitalO, capitalT, capitalA, capitalT,
                capitalI, capitalO, capitalN, S, leftParenthesis, Name, rightParenthesis);
                
        this.grammar.addProduction(NotationType_inner_Plus, nullHandler, NotationType_inner, NotationType_inner_Plus);
        this.grammar.addProduction(NotationType_inner_Plus, nullHandler, NotationType_inner);
        
        this.grammar.addProduction(NotationType_inner, nullHandler, verticalBar, S, Name, S);
        this.grammar.addProduction(NotationType_inner, nullHandler, verticalBar, Name, S);
        this.grammar.addProduction(NotationType_inner, nullHandler, verticalBar, S, Name);
        this.grammar.addProduction(NotationType_inner, nullHandler, verticalBar, Name);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return NotationType;
    }
    
}
