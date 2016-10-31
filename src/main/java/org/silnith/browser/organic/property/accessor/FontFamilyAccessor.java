package org.silnith.browser.organic.property.accessor;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.FontFamilyNameParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.grammar.Parser;
import org.silnith.parser.css3.lexical.TokenListStream;


public class FontFamilyAccessor extends PropertyAccessor<List<String>> {
    
    private static final Set<String> VALID_FONT_FAMILY_NAMES;
    static {
        VALID_FONT_FAMILY_NAMES = new HashSet<>();
        final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final String[] availableFontFamilyNames = localGraphicsEnvironment.getAvailableFontFamilyNames(Locale.ENGLISH);
        for (final String fontFamilyName : availableFontFamilyNames) {
            VALID_FONT_FAMILY_NAMES.add(fontFamilyName.toUpperCase(Locale.ENGLISH));
        }
//        VALID_FONT_FAMILY_NAMES.addAll(Arrays.asList(availableFontFamilyNames));
    }
    
    private final FontFamilyNameParser fontFamilyNameParser;
    
    public FontFamilyAccessor(final FontFamilyNameParser fontFamilyNameParser) {
        super(PropertyName.FONT_FAMILY, true);
        this.fontFamilyNameParser = fontFamilyNameParser;
        
    }

    @Override
    public List<String> getInitialValue(StyleData styleData) {
        return Collections.singletonList("serif");
    }
    
    @Override
    protected List<String> parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final List<List<Token>> commaSeparatedListOfComponentValues = cssParser.parseCommaSeparatedListOfComponentValues();
        final List<String> fontFamilyNames = new ArrayList<>();
        for (final List<Token> tokenList : commaSeparatedListOfComponentValues) {
            final String fontFamilyName = fontFamilyNameParser.parse(tokenList);
            if (VALID_FONT_FAMILY_NAMES.contains(fontFamilyName.toUpperCase(Locale.ENGLISH))) {
                fontFamilyNames.add(fontFamilyName);
            } else {
                System.out.println("Unknown font family: " + fontFamilyName);
            }
        }
        if (fontFamilyNames.isEmpty()) {
            fontFamilyNames.add("serif");
        }
        return fontFamilyNames;
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
