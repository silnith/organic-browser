package org.silnith.browser.organic.parser.html5.grammar.mode;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intemplate">
 *      12.2.5.4.18 The "in template" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InTemplateInsertionMode extends InsertionMode {
    
    public InTemplateInsertionMode(final Parser parser) {
        super(parser);
    }
    
    @Override
    public boolean insert(final Token token) {
        switch (token.getType()) {
        case CHARACTER: // fall through
        case COMMENT: // fall through
        case DOCTYPE: {
            return processUsingRulesFor(Parser.Mode.IN_BODY, token);
        } // break;
        case START_TAG: {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            switch (tagName) {
            case "base": // fall through
            case "basefont": // fall through
            case "bgsound": // fall through
            case "link": // fall through
            case "meta": // fall through
            case "noframes": // fall through
            case "script": // fall through
            case "style": // fall through
            case "template": // fall through
            case "title": {
                return processUsingRulesFor(Parser.Mode.IN_HEAD, startTagToken);
            } // break;
            case "caption": // fall through
            case "colgroup": // fall through
            case "tbody": // fall through
            case "tfoot": // fall through
            case "thead": {
                popCurrentTemplateInsertionMode();
                pushOntoStackOfTemplateInsertionModes(Parser.Mode.IN_TABLE);
                setInsertionMode(Parser.Mode.IN_TABLE);
                return REPROCESS_TOKEN;
            } // break;
            case "col": {
                popCurrentTemplateInsertionMode();
                pushOntoStackOfTemplateInsertionModes(Parser.Mode.IN_COLUMN_GROUP);
                setInsertionMode(Parser.Mode.IN_COLUMN_GROUP);
                return REPROCESS_TOKEN;
            } // break;
            case "tr": {
                popCurrentTemplateInsertionMode();
                pushOntoStackOfTemplateInsertionModes(Parser.Mode.IN_TABLE_BODY);
                setInsertionMode(Parser.Mode.IN_TABLE_BODY);
                return REPROCESS_TOKEN;
            } // break;
            case "th": // fall through
            case "td": {
                popCurrentTemplateInsertionMode();
                pushOntoStackOfTemplateInsertionModes(Parser.Mode.IN_ROW);
                setInsertionMode(Parser.Mode.IN_ROW);
                return REPROCESS_TOKEN;
            } // break;
            default: {
                popCurrentTemplateInsertionMode();
                pushOntoStackOfTemplateInsertionModes(Parser.Mode.IN_BODY);
                setInsertionMode(Parser.Mode.IN_BODY);
                return REPROCESS_TOKEN;
            } // break;
            }
        } // break;
        case END_TAG: {
            final EndTagToken endTagToken = (EndTagToken) token;
            final String tagName = endTagToken.getTagName();
            switch (tagName) {
            case "template": {
                return processUsingRulesFor(Parser.Mode.IN_HEAD, endTagToken);
            } // break;
            default: {
                if (isAllowParseErrors()) {
                    return IGNORE_TOKEN;
                } else {
                    throw new ParseErrorException("Unexpected end tag token in template: " + endTagToken);
                }
            } // break;
            }
        } // break;
        case EOF: {
            // if no template on stack of open elements, stop parsing
            if ( !isStackOfOpenElementsContains("template")) {
                stopParsing();
                return TOKEN_HANDLED;
            }
            if (isAllowParseErrors()) {
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, "template"));
                clearListOfActiveFormattingElementsUpToLastMarker();
                popCurrentTemplateInsertionMode();
                resetInsertionModeAppropriately();
                return REPROCESS_TOKEN;
            } else {
                throw new ParseErrorException("Unexpected end-of-file in template.");
            }
        } // break;
        default: {
            return anythingElse(token);
        } // break;
        }
    }
    
    private boolean anythingElse(final Token token) {
        throw new ParseErrorException();
    }
    
}
