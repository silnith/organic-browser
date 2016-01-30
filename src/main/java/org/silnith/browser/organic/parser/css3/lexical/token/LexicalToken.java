package org.silnith.browser.organic.parser.css3.lexical.token;

import org.silnith.browser.organic.parser.css3.Token;


public abstract class LexicalToken extends Token {
    
    public enum LexicalType {
        IDENT_TOKEN,
        FUNCTION_TOKEN,
        AT_KEYWORD_TOKEN,
        HASH_TOKEN,
        STRING_TOKEN,
        BAD_STRING_TOKEN,
        URL_TOKEN,
        BAD_URL_TOKEN,
        DELIM_TOKEN,
        NUMBER_TOKEN,
        PERCENTAGE_TOKEN,
        DIMENSION_TOKEN,
        UNICODE_RANGE_TOKEN,
        INCLUDE_MATCH_TOKEN,
        DASH_MATCH_TOKEN,
        PREFIX_MATCH_TOKEN,
        SUFFIX_MATCH_TOKEN,
        SUBSTRING_MATCH_TOKEN,
        COLUMN_TOKEN,
        WHITESPACE_TOKEN,
        CDO_TOKEN,
        CDC_TOKEN,
        COLON_TOKEN,
        SEMICOLON_TOKEN,
        COMMA_TOKEN,
        LEFT_BRACKET_TOKEN,
        RIGHT_BRACKET_TOKEN,
        LEFT_PARENTHESIS_TOKEN,
        RIGHT_PARENTHESIS_TOKEN,
        LEFT_BRACE_TOKEN,
        RIGHT_BRACE_TOKEN,
        EOF
    }
    
    public LexicalToken() {
        super(Token.Type.LEXICAL_TOKEN);
    }
    
    public abstract LexicalType getLexicalType();
    
}
