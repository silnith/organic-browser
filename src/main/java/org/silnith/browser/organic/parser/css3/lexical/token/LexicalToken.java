package org.silnith.browser.organic.parser.css3.lexical.token;

import org.silnith.browser.organic.parser.css3.Token;


public abstract class LexicalToken extends Token {
    
    public enum LexicalType {
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.IdentToken
         */
        IDENT_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.FunctionToken
         */
        FUNCTION_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.AtKeywordToken
         */
        AT_KEYWORD_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.HashToken
         */
        HASH_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.StringToken
         */
        STRING_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.BadStringToken
         */
        BAD_STRING_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.URLToken
         */
        URL_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.BadURLToken
         */
        BAD_URL_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.DelimToken
         */
        DELIM_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.NumberToken
         */
        NUMBER_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.PercentageToken
         */
        PERCENTAGE_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.DimensionToken
         */
        DIMENSION_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.UnicodeRangeToken
         */
        UNICODE_RANGE_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.IncludeMatchToken
         */
        INCLUDE_MATCH_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.DashMatchToken
         */
        DASH_MATCH_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.PrefixMatchToken
         */
        PREFIX_MATCH_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.SuffixMatchToken
         */
        SUFFIX_MATCH_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.SubstringMatchToken
         */
        SUBSTRING_MATCH_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.ColumnToken
         */
        COLUMN_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.WhitespaceToken
         */
        WHITESPACE_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.CDOToken
         */
        CDO_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.CDCToken
         */
        CDC_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.ColonToken
         */
        COLON_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.SemicolonToken
         */
        SEMICOLON_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.CommaToken
         */
        COMMA_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.LeftSquareBracketToken
         */
        LEFT_BRACKET_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.RightSquareBracketToken
         */
        RIGHT_BRACKET_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.LeftParenthesisToken
         */
        LEFT_PARENTHESIS_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.RightParenthesisToken
         */
        RIGHT_PARENTHESIS_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.LeftCurlyBracketToken
         */
        LEFT_BRACE_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.RightCurlyBracketToken
         */
        RIGHT_BRACE_TOKEN,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.EOFToken
         */
        EOF
    }
    
    private final LexicalType lexicalType;
    
    public LexicalToken(final LexicalType lexicalType) {
        super(Type.LEXICAL_TOKEN);
        this.lexicalType = lexicalType;
    }
    
    public final LexicalType getLexicalType() {
        return lexicalType;
    }
    
}
