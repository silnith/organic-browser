package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CARRIAGE_RETURN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.grammar.dom.AfterLastChildInsertionPosition;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.DOCTYPEToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Document;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-initial-insertion-mode">
 *      12.2.5.4.1 The "initial" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InitialInsertionMode extends InsertionMode {
    
    public InitialInsertionMode(final Parser parser) {
        super(parser);
    }
    
    @Override
    public boolean insert(final Token token) {
        final Document document = getDocument();
        
        switch (token.getType()) {
        case CHARACTER: {
            final CharacterToken characterToken = (CharacterToken) token;
            final char content = characterToken.getCharacter();
            switch (content) {
            case CHARACTER_TABULATION: // fall through
            case LINE_FEED: // fall through
            case FORM_FEED: // fall through
            case CARRIAGE_RETURN: // fall through
            case SPACE: {
                return IGNORE_TOKEN;
            } // break;
            default: {
                return anythingElse(characterToken);
            } // break;
            }
        } // break;
        case COMMENT: {
            final CommentToken commentToken = (CommentToken) token;
            insertComment(commentToken, new AfterLastChildInsertionPosition(document));
            return TOKEN_HANDLED;
        } // break;
        case DOCTYPE: {
            final DOCTYPEToken doctypeToken = (DOCTYPEToken) token;
            final String name = doctypeToken.getName();
            final String publicIdentifier = doctypeToken.getPublicIdentifier();
            final String systemIdentifier = doctypeToken.getSystemIdentifier();
            
            if (name.equals("html")) {
                // valid
            } else if (publicIdentifier != null) {
                // valid
            } else if (systemIdentifier != null && !systemIdentifier.equals("about:legacy-compat")) {
                // valid
            } else if (name.equals("html")
                    && (publicIdentifier != null && publicIdentifier.equals("-//W3C//DTD HTML 4.0//EN"))
                    && (systemIdentifier == null
                            || systemIdentifier.equals("http://www.w3.org/TR/REC-html40/strict.dtd"))) {
                // valid
            } else if (name.equals("html")
                    && (publicIdentifier != null && publicIdentifier.equals("-//W3C//DTD HTML 4.01//EN"))
                    && (systemIdentifier == null || systemIdentifier.equals("http://www.w3.org/TR/html4/strict.dtd"))) {
                // valid
            } else if (name.equals("html")
                    && (publicIdentifier != null && publicIdentifier.equals("-//W3C//DTD XHTML 1.0 Strict//EN"))
                    && (systemIdentifier != null
                            && systemIdentifier.equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"))) {
                // valid
            } else if (name.equals("html")
                    && (publicIdentifier != null && publicIdentifier.equals("-//W3C//DTD XHTML 1.1//EN"))
                    && (systemIdentifier != null
                            && systemIdentifier.equals("http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"))) {
                // valid
            } else {
                // parse error
            }
            
//			final DOMImplementation implementation = document.getImplementation();
//			final DocumentType documentType = implementation.createDocumentType(
//					name == null ? "" : name,
//					publicIdentifier == null ? "" : publicIdentifier,
//					systemIdentifier == null ? "" : systemIdentifier);
//			implementation.createDocument(HTML_NAMESPACE, "html", documentType);
//			document.appendChild(documentType);
            
            setInsertionMode(Parser.Mode.BEFORE_HTML);
            return TOKEN_HANDLED;
        } // break;
        default: {
            return anythingElse(token);
        } // break;
        }
    }
    
    private boolean anythingElse(final Token token) {
        setInsertionMode(Parser.Mode.BEFORE_HTML);
        return REPROCESS_TOKEN;
    }
    
}
