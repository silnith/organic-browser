package org.silnith.css.model.data;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;

public interface PropertyValueParser<T> {
    
    T parse(String specifiedValue);
    
    T parse(List<Token> specifiedValue) throws IOException;

}
