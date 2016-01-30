package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.Arrays;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.Comment;


/**
 * [15] Comment ::= '&lt;!--' (({@linkplain CharProduction Char} - '-') | ('-' (
 * {@linkplain CharProduction Char} - '-')))* '--&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Comment">Comment
 *      </a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CommentProduction extends XMLProduction {
    
    private static class CommentHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final Comment comment = new Comment();
            final String content = (String) rightHandSide.get(4);
            comment.content = content.substring(0, content.length() - 1);
            return comment;
        }
        
    }
    
    private static class EmptyCommentHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final Comment comment = new Comment();
            comment.content = "";
            return comment;
        }
        
    }
    
    private static class CommentHyphenHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(rightHandSide.get(0));
            stringBuilder.append('-');
            if (rightHandSide.size() > 2) {
                stringBuilder.append(rightHandSide.get(2));
            }
            return stringBuilder.toString();
        }
        
    }
    
    private final NonTerminalSymbol Comment;
    
    public CommentProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        Comment = this.grammar.getNonTerminalSymbol("Comment");
        
        final NonTerminalSymbol Comment_span_Plus = this.grammar.getNonTerminalSymbol("Comment-span-Plus");
        final NonTerminalSymbol Comment_Char_Plus = this.grammar.getNonTerminalSymbol("Comment-Char-Plus");
        final NonTerminalSymbol Comment_Char = this.grammar.getNonTerminalSymbol("Comment-Char");
        
        this.grammar.addProduction(Comment, new CommentHandler(), lessThanSign, exclamationMark, hyphenMinus,
                hyphenMinus, Comment_span_Plus, hyphenMinus, greaterThanSign);
        this.grammar.addProduction(Comment, new EmptyCommentHandler(), lessThanSign, exclamationMark, hyphenMinus,
                hyphenMinus, hyphenMinus, hyphenMinus, greaterThanSign);
                
        final CommentHyphenHandler commentHyphenHandler = new CommentHyphenHandler();
        this.grammar.addProduction(Comment_span_Plus, commentHyphenHandler, Comment_Char_Plus, hyphenMinus,
                Comment_span_Plus);
        this.grammar.addProduction(Comment_span_Plus, commentHyphenHandler, Comment_Char_Plus, hyphenMinus);
        
        this.grammar.addProduction(Comment_Char_Plus, stringHandler, Comment_Char_Plus, Comment_Char);
        this.grammar.addProduction(Comment_Char_Plus, stringHandler, Comment_Char);
        
        this.grammar.addProduction(Comment_Char, characterHandler, tab);
        this.grammar.addProduction(Comment_Char, characterHandler, lineFeed);
        this.grammar.addProduction(Comment_Char, characterHandler, carriageReturn);
        this.grammar.addProduction(Comment_Char, characterHandler, space);
        this.grammar.addProduction(Comment_Char, characterHandler, exclamationMark);
        this.grammar.addProduction(Comment_Char, characterHandler, quotationMark);
        this.grammar.addProduction(Comment_Char, characterHandler, numberSign);
        this.grammar.addProduction(Comment_Char, characterHandler, dollarSign);
        this.grammar.addProduction(Comment_Char, characterHandler, percentSign);
        this.grammar.addProduction(Comment_Char, characterHandler, ampersand);
        this.grammar.addProduction(Comment_Char, characterHandler, apostrophe);
        this.grammar.addProduction(Comment_Char, characterHandler, leftParenthesis);
        this.grammar.addProduction(Comment_Char, characterHandler, rightParenthesis);
        this.grammar.addProduction(Comment_Char, characterHandler, asterisk);
        this.grammar.addProduction(Comment_Char, characterHandler, plusSign);
        this.grammar.addProduction(Comment_Char, characterHandler, comma);
        this.grammar.addProduction(Comment_Char, characterHandler, fullStop);
        this.grammar.addProduction(Comment_Char, characterHandler, solidus);
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.grammar.addProduction(Comment_Char, characterHandler, s);
        }
        this.grammar.addProduction(Comment_Char, characterHandler, colon);
        this.grammar.addProduction(Comment_Char, characterHandler, semicolon);
        this.grammar.addProduction(Comment_Char, characterHandler, lessThanSign);
        this.grammar.addProduction(Comment_Char, characterHandler, equalsSign);
        this.grammar.addProduction(Comment_Char, characterHandler, greaterThanSign);
        this.grammar.addProduction(Comment_Char, characterHandler, questionMark);
        this.grammar.addProduction(Comment_Char, characterHandler, atSymbol);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(Comment_Char, characterHandler, s);
        }
        this.grammar.addProduction(Comment_Char, characterHandler, leftBracket);
        this.grammar.addProduction(Comment_Char, characterHandler, reverseSolidus);
        this.grammar.addProduction(Comment_Char, characterHandler, rightBracket);
        this.grammar.addProduction(Comment_Char, characterHandler, circumflexAccent);
        this.grammar.addProduction(Comment_Char, characterHandler, lowLine);
        this.grammar.addProduction(Comment_Char, characterHandler, graveAccent);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(Comment_Char, characterHandler, s);
        }
        this.grammar.addProduction(Comment_Char, characterHandler, leftCurlyBrace);
        this.grammar.addProduction(Comment_Char, characterHandler, verticalBar);
        this.grammar.addProduction(Comment_Char, characterHandler, rightCurlyBrace);
        this.grammar.addProduction(Comment_Char, characterHandler, tilde);
        this.grammar.addProduction(Comment_Char, characterHandler, delete);
        this.grammar.addProduction(Comment_Char, characterHandler, belowSurrogates);
        this.grammar.addProduction(Comment_Char, characterHandler, aboveSurrogates);
        this.grammar.addProduction(Comment_Char, characterHandler, supplementaryMultilingualPlane);
        this.grammar.addProduction(Comment_Char, characterHandler, supplementaryIdeographicPlane);
        this.grammar.addProduction(Comment_Char, characterHandler, supplementaryUnassigned);
        this.grammar.addProduction(Comment_Char, characterHandler, supplementarySpecialPurposePlane);
        this.grammar.addProduction(Comment_Char, characterHandler, supplementaryPrivateUsePlanes);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Comment;
    }
    
}
