package org.silnith.browser.organic.parser.css3;

import java.io.IOException;

public interface TokenStream {

	Token getNextToken() throws IOException;

}
