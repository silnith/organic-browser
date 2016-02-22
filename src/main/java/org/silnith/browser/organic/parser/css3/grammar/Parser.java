package org.silnith.browser.organic.parser.css3.grammar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.silnith.browser.organic.network.Download;
import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.token.AtRuleNode;
import org.silnith.browser.organic.parser.css3.grammar.token.ComponentValue;
import org.silnith.browser.organic.parser.css3.grammar.token.Declaration;
import org.silnith.browser.organic.parser.css3.grammar.token.DeclarationNode;
import org.silnith.browser.organic.parser.css3.grammar.token.Function;
import org.silnith.browser.organic.parser.css3.grammar.token.QualifiedRuleNode;
import org.silnith.browser.organic.parser.css3.grammar.token.Rule;
import org.silnith.browser.organic.parser.css3.grammar.token.SimpleBlock;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.browser.organic.parser.css3.lexical.TokenStream;
import org.silnith.browser.organic.parser.css3.lexical.Tokenizer;
import org.silnith.browser.organic.parser.css3.lexical.token.AtKeywordToken;
import org.silnith.browser.organic.parser.css3.lexical.token.FunctionToken;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;


/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#parser-entry-points">5.3
 *      Parser Entry Points</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class Parser {
    
    private final TokenStream tokenizer;
    
    /**
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#current-input-token">
     *      current input token</a>
     */
    private Token currentInputToken;
    
    /**
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#next-input-token">next
     *      input token</a>
     */
    private Token nextInputToken;
    
    private boolean reconsumeCurrentInputToken;
    
    public Parser(final TokenStream tokenizer) {
        super();
        this.tokenizer = tokenizer;
        this.currentInputToken = null;
        this.nextInputToken = null;
        this.reconsumeCurrentInputToken = false;
    }
    
    @PostConstruct
    public void prime() throws IOException {
        nextInputToken = tokenizer.getNextToken();
    }
    
    protected boolean isLexicalToken(final Token token, final LexicalToken.LexicalType type) {
        if (token.getType() == Token.Type.LEXICAL_TOKEN) {
            final LexicalToken lexicalToken = (LexicalToken) token;
            return lexicalToken.getLexicalType() == type;
        } else {
            return false;
        }
    }
    
    protected boolean isEOFToken(final Token token) {
        return token.getType() == Token.Type.EOF_TOKEN
                || isLexicalToken(token, LexicalToken.LexicalType.EOF);
    }

    /**
     * @throws IOException
     * @see <a href=
     *      "http://dev.w3.org/csswg/css-syntax/#consume-the-next-input-token">
     *      consume the next input token</a>
     */
    protected void consumeNextInputToken() throws IOException {
        if (reconsumeCurrentInputToken) {
            reconsumeCurrentInputToken = false;
        } else {
            currentInputToken = nextInputToken;
            nextInputToken = tokenizer.getNextToken();
        }
    }
    
    protected void reconsumeCurrentInputToken() throws IOException {
        reconsumeCurrentInputToken = true;
    }
    
    /**
     * "Parse a stylesheet" is intended to be the normal parser entry point, for
     * parsing stylesheets.
     * 
     * @return
     * @throws IOException
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#parse-a-stylesheet">5.3
     *      .1 Parse a stylesheet</a>
     */
    public List<Rule> parseStylesheet() throws IOException {
        final CSSStyleSheet stylesheet = null;
        // set top-level flag
        final List<Rule> rules = consumeListOfRules();
        return rules;
    }
    
    /**
     * "Parse a list of rules" is intended for the content of at-rules such
     * as @media. It differs from "Parse a stylesheet" in the handling of
     * <CDO-token> and <CDC-token>.
     * 
     * @throws IOException
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#parse-a-list-of-rules">
     *      5.3.2 Parse a list of rules</a>
     */
    public List<Rule> parseListOfRules() throws IOException {
        // unset top-level flag
        return consumeListOfRules();
    }
    
    /**
     * "Parse a rule" is intended for use by the {@link CSSStyleSheet#insertRule
     * method}, and similar functions which might exist, which parse text into a
     * single rule.
     * 
     * @return
     * @throws IOException
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#parse-a-rule">5.3.3
     *      Parse a rule</a>
     */
    public Rule parseRule() throws IOException {
        while (isLexicalToken(nextInputToken, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
            consumeNextInputToken();
        }
        final Rule rule;
        if (isEOFToken(nextInputToken)) {
            // syntax error
            throw new ParseErrorException();
        } else if (isLexicalToken(nextInputToken, LexicalToken.LexicalType.AT_KEYWORD_TOKEN)) {
            rule = consumeAtRule();
        } else {
            rule = consumeQualifiedRule();
            if (rule == null) {
                // syntax error
                throw new ParseErrorException();
            }
        }
        while (isLexicalToken(nextInputToken, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
            consumeNextInputToken();
        }
        if (isEOFToken(nextInputToken)) {
            return rule;
        } else {
            // syntax error
            throw new ParseErrorException();
        }
    }

    /**
     * "Parse a declaration" is used in @supports conditions.
     * 
     * @return
     * @throws IOException
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#parse-a-declaration">5.
     *      3.4 Parse a declaration</a>
     */
    public Declaration parseDeclaration() throws IOException {
        while (isLexicalToken(nextInputToken, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
            consumeNextInputToken();
        }
        if ( !isLexicalToken(nextInputToken, LexicalToken.LexicalType.IDENT_TOKEN)) {
            // syntax error
            throw new ParseErrorException();
        }
        final Declaration declaration = consumeDeclaration();
        if (declaration == null) {
            // syntax error
            throw new ParseErrorException();
        } else {
            return declaration;
        }
    }
    
    /**
     * "Parse a list of declarations" is for the contents of a style attribute,
     * which parses text into the contents of a single style rule.
     * 
     * @return
     * @throws IOException
     * @see <a href=""></a>
     * @see org.w3c.dom.css.ElementCSSInlineStyle#getStyle()
     */
    public List<Declaration> parseListOfDeclarations() throws IOException {
        final CSSStyleDeclaration declarationBlock = null;
        return consumeListOfDeclarations();
    }
    
    /**
     * "Parse a component value" is for things that need to consume a single
     * value, like the parsing rules for attr().
     * 
     * @return
     * @throws IOException
     * @see <a href=""></a>
     */
    public Token parseComponentValue() throws IOException {
        while (isLexicalToken(nextInputToken, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
            consumeNextInputToken();
        }
        if (isEOFToken(nextInputToken)) {
            // syntax error
            throw new ParseErrorException();
        }
        final Token value = consumeComponentValue();
        while (isLexicalToken(nextInputToken, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
            consumeNextInputToken();
        }
        if (isLexicalToken(nextInputToken, LexicalToken.LexicalType.EOF)) {
            return value;
        } else {
            // syntax error
            throw new ParseErrorException("Unexpected token: " + nextInputToken);
        }
    }
    
    /**
     * "Parse a list of component values" is for the contents of presentational
     * attributes, which parse text into a single declaration’s value, or for
     * parsing a stand-alone selector [SELECT] or list of Media Queries
     * [MEDIAQ], as in Selectors API or the media HTML attribute.
     * 
     * @return
     * @throws IOException
     * @see <a href=""></a>
     */
    public List<Token> parseListOfComponentValues() throws IOException {
        final List<Token> listOfComponentValues = new ArrayList<>();
        Token value;
        do {
            value = consumeComponentValue();
            listOfComponentValues.add(value);
        } while (value.getType() != Token.Type.EOF_TOKEN && !isLexicalToken(value, LexicalToken.LexicalType.EOF));
        listOfComponentValues.remove(listOfComponentValues.size() - 1);
        return listOfComponentValues;
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href=""></a>
     */
    public List<List<Token>> parseCommaSeparatedListOfComponentValues() throws IOException {
        final List<List<Token>> listOfCVLs = new ArrayList<>();
        Token value;
        do {
            final List<Token> cvl = new ArrayList<>();
            value = consumeComponentValue();
            while (value.getType() != Token.Type.EOF_TOKEN && !isLexicalToken(value, LexicalToken.LexicalType.EOF)
                    && !isLexicalToken(value, LexicalToken.LexicalType.COMMA_TOKEN)) {
                cvl.add(value);
                value = consumeComponentValue();
            }
            listOfCVLs.add(cvl);
        } while (value.getType() != Token.Type.EOF_TOKEN && !isLexicalToken(value, LexicalToken.LexicalType.EOF));
        return listOfCVLs;
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href=
     *      "http://dev.w3.org/csswg/css-syntax/#consume-a-list-of-rules">5.4.1
     *      Consume a list of rules</a>
     */
    protected List<Rule> consumeListOfRules() throws IOException {
        final List<Rule> listOfRules = new ArrayList<>();
        do {
            consumeNextInputToken();
            switch (currentInputToken.getType()) {
            case EOF_TOKEN: {
                return listOfRules;
            } // break;
            case LEXICAL_TOKEN: {
                final LexicalToken lexicalToken = (LexicalToken) currentInputToken;
                switch (lexicalToken.getLexicalType()) {
                case WHITESPACE_TOKEN: {
                    // do nothing
                    continue;
                } // break;
                case EOF: {
                    return listOfRules;
                } // break;
                case CDO_TOKEN: // fall through
                case CDC_TOKEN: {
                    // if top level flag, do nothing
                    final boolean topLevelFlag = false;
                    if (topLevelFlag) {
                        // do nothing
                    } else {
                        reconsumeCurrentInputToken();
                        final QualifiedRuleNode qualifiedRule = consumeQualifiedRule();
                        if (qualifiedRule != null) {
                            listOfRules.add(qualifiedRule);
                        }
                    }
                    continue;
                } // break;
                case AT_KEYWORD_TOKEN: {
                    reconsumeCurrentInputToken();
                    final AtRuleNode atRule = consumeAtRule();
                    if (atRule != null) {
                        listOfRules.add(atRule);
                    }
                    continue;
                } // break;
                default:
                    break;
                }
            }
                break;
            default:
                break;
            }
            // default case:
            reconsumeCurrentInputToken();
            final QualifiedRuleNode qualifiedRule = consumeQualifiedRule();
            if (qualifiedRule != null) {
                listOfRules.add(qualifiedRule);
            }
        } while (true);
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-an-at-rule">5.4
     *      .2 Consume an at-rule</a>
     */
    protected AtRuleNode consumeAtRule() throws IOException {
        consumeNextInputToken();
        assert currentInputToken.getType() == Token.Type.LEXICAL_TOKEN;
        assert ((LexicalToken) currentInputToken).getLexicalType() == LexicalToken.LexicalType.AT_KEYWORD_TOKEN;
        final AtKeywordToken atKeywordToken = (AtKeywordToken) currentInputToken;
        final List<Token> prelude = new ArrayList<>();
        final AtRuleNode atRuleNode = new AtRuleNode(atKeywordToken.getStringValue(), prelude);
        do {
            consumeNextInputToken();
            switch (currentInputToken.getType()) {
            case LEXICAL_TOKEN: {
                final LexicalToken lexicalToken = (LexicalToken) currentInputToken;
                switch (lexicalToken.getLexicalType()) {
                case SEMICOLON_TOKEN: // fall through
                case EOF: {
                    return atRuleNode;
                } // break;
                case LEFT_BRACE_TOKEN: {
                    final SimpleBlock simpleBlock = consumeSimpleBlock();
                    atRuleNode.setBlock(simpleBlock);
                    return atRuleNode;
                } // break;
                default:
                    break;
                }
            }
                break;
            case COMPONENT_VALUE: {
                final ComponentValue componentValueToken = (ComponentValue) currentInputToken;
                switch (componentValueToken.getComponentValueType()) {
                case SIMPLE_BLOCK: {
                    final SimpleBlock blockToken = (SimpleBlock) componentValueToken;
                    atRuleNode.setBlock(blockToken);
                    return atRuleNode;
                } // break;
                default:
                    break;
                }
            }
                break;
            case EOF_TOKEN: {
                return atRuleNode;
            } // break;
            default:
                break;
            }
            // default case:
            reconsumeCurrentInputToken();
            final Token componentValue = consumeComponentValue();
            prelude.add(componentValue);
        } while (true);
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href=
     *      "http://dev.w3.org/csswg/css-syntax/#consume-a-qualified-rule">5.4.3
     *      Consume a qualified rule</a>
     */
    protected QualifiedRuleNode consumeQualifiedRule() throws IOException {
        final List<Token> prelude = new ArrayList<>();
        final QualifiedRuleNode qualifiedRuleNode = new QualifiedRuleNode(prelude);
        do {
            consumeNextInputToken();
            switch (currentInputToken.getType()) {
            case EOF_TOKEN: {
                // parse error
                return null;
            } // break;
            case LEXICAL_TOKEN: {
                final LexicalToken lexicalToken = (LexicalToken) currentInputToken;
                switch (lexicalToken.getLexicalType()) {
                case EOF: {
                    // parse error
                    return null;
                } // break;
                case LEFT_BRACE_TOKEN: {
                    final SimpleBlock simpleBlock = consumeSimpleBlock();
                    qualifiedRuleNode.setBlock(simpleBlock);
                    return qualifiedRuleNode;
                } // break;
                default:
                    break;
                }
            }
                break;
            case COMPONENT_VALUE: {
                final ComponentValue componentValueToken = (ComponentValue) currentInputToken;
                switch (componentValueToken.getComponentValueType()) {
                case SIMPLE_BLOCK: {
                    final SimpleBlock blockToken = (SimpleBlock) componentValueToken;
                    if (blockToken.getToken().getLexicalType() == LexicalToken.LexicalType.LEFT_BRACE_TOKEN) {
                        qualifiedRuleNode.setBlock(blockToken);
                        return qualifiedRuleNode;
                    }
                }
                    break;
                default:
                    break;
                }
            }
                break;
            default:
                break;
            }
            // default case:
            reconsumeCurrentInputToken();
            final Token componentValue = consumeComponentValue();
            prelude.add(componentValue);
        } while (true);
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href=
     *      "http://dev.w3.org/csswg/css-syntax/#consume-a-list-of-declarations">
     *      5.4.4 Consume a list of declarations</a>
     */
    protected List<Declaration> consumeListOfDeclarations() throws IOException {
        final List<Declaration> listOfDeclarations = new ArrayList<>();
        do {
            consumeNextInputToken();
            switch (currentInputToken.getType()) {
            case EOF_TOKEN: {
                return listOfDeclarations;
            } // break;
            case LEXICAL_TOKEN: {
                final LexicalToken lexicalToken = (LexicalToken) currentInputToken;
                switch (lexicalToken.getLexicalType()) {
                case WHITESPACE_TOKEN: // fall through
                case SEMICOLON_TOKEN: {
                    // do nothing
                    continue;
                } // break;
                case EOF: {
                    return listOfDeclarations;
                } // break;
                case AT_KEYWORD_TOKEN: {
                    reconsumeCurrentInputToken();
                    final AtRuleNode atRuleNode = consumeAtRule();
                    listOfDeclarations.add(atRuleNode);
                    continue;
                } // break;
                case IDENT_TOKEN: {
                    final List<Token> tempTokens = new ArrayList<>();
                    tempTokens.add(currentInputToken);
                    while (nextInputToken.getType() != Token.Type.EOF_TOKEN
                            && !isLexicalToken(nextInputToken, LexicalToken.LexicalType.EOF)
                            && !isLexicalToken(nextInputToken, LexicalToken.LexicalType.SEMICOLON_TOKEN)) {
                        final Token componentValue = consumeComponentValue();
                        tempTokens.add(componentValue);
                    }
                    final Parser tempParser = new Parser(new TokenListStream(tempTokens));
                    tempParser.prime();
                    final DeclarationNode declaration = tempParser.consumeDeclaration();
                    if (declaration != null) {
                        listOfDeclarations.add(declaration);
                    }
                    continue;
                } // break;
                default:
                    break;
                }
            }
                break;
            default:
                break;
            }
            // parse error
            reconsumeCurrentInputToken();
            while (nextInputToken.getType() != Token.Type.EOF_TOKEN
                    && !isLexicalToken(nextInputToken, LexicalToken.LexicalType.EOF)
                    && !isLexicalToken(nextInputToken, LexicalToken.LexicalType.SEMICOLON_TOKEN)) {
                consumeComponentValue();
                // throw away the result
            }
        } while (true);
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-declaration">
     *      5.4.5 Consume a declaration</a>
     */
    protected DeclarationNode consumeDeclaration() throws IOException {
        assert nextInputToken.getType() == Token.Type.LEXICAL_TOKEN;
        assert ((LexicalToken) nextInputToken).getLexicalType() == LexicalToken.LexicalType.IDENT_TOKEN;
        
        consumeNextInputToken();
        final IdentToken identToken = (IdentToken) currentInputToken;
        final List<Token> value = new ArrayList<>();
        final DeclarationNode declarationNode = new DeclarationNode(identToken.getStringValue(), value);
        while (isLexicalToken(nextInputToken, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
            consumeNextInputToken();
        }
        if ( !isLexicalToken(nextInputToken, LexicalToken.LexicalType.COLON_TOKEN)) {
            // parse error
            return null;
        } else {
            consumeNextInputToken();
        }
        while (nextInputToken.getType() != Token.Type.EOF_TOKEN
                && !isLexicalToken(nextInputToken, LexicalToken.LexicalType.EOF)) {
            final Token componentValue = consumeComponentValue();
            value.add(componentValue);
        }
        if (value.size() >= 2) {
            // also need to filter out whitespace from "last two"
            final Token secondToLast = value.get(value.size() - 2);
            final Token last = value.get(value.size() - 1);
            // if secondToLast is <delim-token> "!"
            // and last is <ident-token> "important"
            // cut last 2 off the list and set important flag
        }
        return declarationNode;
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href=
     *      "http://dev.w3.org/csswg/css-syntax/#consume-a-component-value">5.4.
     *      6 Consume a component value</a>
     */
    protected Token consumeComponentValue() throws IOException {
        consumeNextInputToken();
        if (isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_BRACE_TOKEN)
                || isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_BRACKET_TOKEN)
                || isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_PARENTHESIS_TOKEN)) {
            return consumeSimpleBlock();
        } else if (isLexicalToken(currentInputToken, LexicalToken.LexicalType.FUNCTION_TOKEN)) {
            return consumeFunction();
        } else {
            return currentInputToken;
        }
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href=
     *      "http://dev.w3.org/csswg/css-syntax/#consume-a-simple-block">5.4.7
     *      Consume a simple block</a>
     */
    protected SimpleBlock consumeSimpleBlock() throws IOException {
        assert isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_BRACE_TOKEN)
                || isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_BRACKET_TOKEN)
                || isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_PARENTHESIS_TOKEN);
                
        final LexicalToken.LexicalType endTokenType;
        if (isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_BRACE_TOKEN)) {
            endTokenType = LexicalToken.LexicalType.RIGHT_BRACE_TOKEN;
        } else if (isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_BRACKET_TOKEN)) {
            endTokenType = LexicalToken.LexicalType.RIGHT_BRACKET_TOKEN;
        } else if (isLexicalToken(currentInputToken, LexicalToken.LexicalType.LEFT_PARENTHESIS_TOKEN)) {
            endTokenType = LexicalToken.LexicalType.RIGHT_PARENTHESIS_TOKEN;
        } else {
            throw new ParseErrorException();
        }
        final List<Token> value = new ArrayList<>();
        final SimpleBlock simpleBlockNode = new SimpleBlock((LexicalToken) currentInputToken, value);
        do {
            consumeNextInputToken();
            if (currentInputToken.getType() == Token.Type.EOF_TOKEN
                    || isLexicalToken(currentInputToken, LexicalToken.LexicalType.EOF)
                    || isLexicalToken(currentInputToken, endTokenType)) {
                return simpleBlockNode;
            } else {
                reconsumeCurrentInputToken();
                final Token componentValue = consumeComponentValue();
                value.add(componentValue);
            }
        } while (true);
    }
    
    /**
     * @return
     * @throws IOException
     * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-function">5.4
     *      .8 Consume a function</a>
     */
    protected Function consumeFunction() throws IOException {
        assert isLexicalToken(currentInputToken, LexicalToken.LexicalType.FUNCTION_TOKEN);
        
        final FunctionToken functionToken = (FunctionToken) currentInputToken;
        final List<Token> value = new ArrayList<>();
        final Function functionNode = new Function(functionToken.getStringValue(), value);
        do {
            consumeNextInputToken();
            if (currentInputToken.getType() == Token.Type.EOF_TOKEN
                    || isLexicalToken(currentInputToken, LexicalToken.LexicalType.EOF)
                    || isLexicalToken(currentInputToken, LexicalToken.LexicalType.RIGHT_PARENTHESIS_TOKEN)) {
                return functionNode;
            } else {
                reconsumeCurrentInputToken();
                final Token componentValue = consumeComponentValue();
                value.add(componentValue);
            }
        } while (true);
    }
    
    public static void main(final String[] args) throws IOException {
        final URL url;
        url = new URL("http://rgsb.org/rgsb.css");
        final Download download = new Download(url);
        download.connect();
        download.download();
        System.out.println(download.getContent());
        System.out.println();
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
        System.out.println();
        final String contentEncoding = download.getContentEncoding();
        final Reader reader;
        if (contentEncoding == null) {
            reader = new InputStreamReader(download.getInputStream(), Charset.forName("UTF-8"));
        } else {
            reader = new InputStreamReader(download.getInputStream(), contentEncoding);
        }
        final Tokenizer tokenizer = new Tokenizer(reader);
        tokenizer.setAllowParseErrors(false);
        final Parser parser = new Parser(tokenizer);
        parser.prime();
        
        final List<Rule> stylesheet = parser.parseStylesheet();
        System.out.println(stylesheet);
    }
    
}
