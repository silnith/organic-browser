package org.silnith.css.model.data;

public class LengthParser {
    
    private final AbsoluteLengthParser absoluteLengthParser;
    
    private final RelativeLengthParser relativeLengthParser;
    
    private final PercentageLengthParser percentageLengthParser;
    
    public LengthParser(final AbsoluteLengthParser absoluteLengthParser,
            final RelativeLengthParser relativeLengthParser, final PercentageLengthParser percentageLengthParser) {
        super();
        this.absoluteLengthParser = absoluteLengthParser;
        this.relativeLengthParser = relativeLengthParser;
        this.percentageLengthParser = percentageLengthParser;
    }
    
    public Length<?> parse(final String specifiedValue) {
        final AbsoluteLength absoluteLength = absoluteLengthParser.parse(specifiedValue);
        
        if (absoluteLength != null) {
            return absoluteLength;
        } else {
            final RelativeLength relativeLength = relativeLengthParser.parse(specifiedValue);
            
            if (relativeLength != null) {
                return relativeLength;
            } else {
                final PercentageLength percentageLength = percentageLengthParser.parse(specifiedValue);
                
                if (percentageLength != null) {
                    return percentageLength;
                } else {
                    return null;
                }
            }
        }
    }
    
}
