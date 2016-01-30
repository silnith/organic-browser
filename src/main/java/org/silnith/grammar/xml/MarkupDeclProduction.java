package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [29] markupdecl ::= {@linkplain ElementDeclProduction elementdecl} |
 * {@linkplain AttlistDeclProduction AttlistDecl} |
 * {@linkplain EntityDeclProduction EntityDecl} |
 * {@linkplain NotationDeclProduction NotationDecl} | {@linkplain PIProduction
 * PI} | {@linkplain CommentProduction Comment}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-markupdecl">
 *      markupdecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class MarkupDeclProduction extends XMLProduction {
    
    private final NonTerminalSymbol markupdecl;
    
    public MarkupDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final ElementDeclProduction elementDeclProduction, final AttlistDeclProduction attlistDeclProduction,
            final EntityDeclProduction entityDeclProduction, final NotationDeclProduction notationDeclProduction,
            final PIProduction piProduction, final CommentProduction commentProduction) {
        super(grammar);
        markupdecl = this.grammar.getNonTerminalSymbol("markupdecl");
        
        final NonTerminalSymbol elementdecl = elementDeclProduction.elementdecl;
        final NonTerminalSymbol AttlistDecl = attlistDeclProduction.getNonTerminalSymbol();
        final NonTerminalSymbol EntityDecl = entityDeclProduction.getNonTerminalSymbol();
        final NonTerminalSymbol NotationDecl = notationDeclProduction.getNonTerminalSymbol();
        final NonTerminalSymbol PI = piProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Comment = commentProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(markupdecl, nullHandler, elementdecl);
        this.grammar.addProduction(markupdecl, nullHandler, AttlistDecl);
        this.grammar.addProduction(markupdecl, nullHandler, EntityDecl);
        this.grammar.addProduction(markupdecl, nullHandler, NotationDecl);
        this.grammar.addProduction(markupdecl, nullHandler, PI);
        this.grammar.addProduction(markupdecl, nullHandler, Comment);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return markupdecl;
    }
    
}
