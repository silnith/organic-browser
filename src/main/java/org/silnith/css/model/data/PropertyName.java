package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;


public enum PropertyName {
    BACKGROUND_ATTACHMENT("background-attachment"),
    BACKGROUND_COLOR("background-color"),
    BACKGROUND_IMAGE("background-image"),
    BACKGROUND_POSITION("background-position"),
    BACKGROUND_REPEAT("background-repeat"),
    BACKGROUND("background"),  // pseudo
    BORDER_COLLAPSE("border-collapse"),
    BORDER_COLOR("border-color"),
    BORDER_SPACING("border-spacing"),
    BORDER_STYLE("border-style"),
    BORDER_TOP("border-top"),
    BORDER_RIGHT("border-right"),
    BORDER_BOTTOM("border-bottom"),
    BORDER_LEFT("border-left"),
    BORDER_TOP_COLOR("border-top-color"),
    BORDER_RIGHT_COLOR("border-right-color"),
    BORDER_BOTTOM_COLOR("border-bottom-color"),
    BORDER_LEFT_COLOR("border-left-color"),
    BORDER_TOP_STYLE("border-top-style"),
    BORDER_RIGHT_STYLE("border-right-style"),
    BORDER_BOTTOM_STYLE("border-bottom-style"),
    BORDER_LEFT_STYLE("border-left-style"),
    BORDER_TOP_WIDTH("border-top-width"),
    BORDER_RIGHT_WIDTH("border-right-width"),
    BORDER_BOTTOM_WIDTH("border-bottom-width"),
    BORDER_LEFT_WIDTH("border-left-width"),
    BORDER_WIDTH("border-width"),
    BORDER("border"),
    BOTTOM("bottom"),
    CAPTION_SIDE("caption-side"),
    CLEAR("clear"),
    CLIP("clip"),
    COLOR("color"),
    CONTENT("content"),
    COUNTER_INCREMENT("counter-increment"),
    COUNTER_RESET("counter-reset"),
    CURSOR("cursor"),
    DIRECTION("direction"),
    DISPLAY("display"),
    EMPTY_CELLS("empty-cells"),
    FLOAT("float"),
    FONT_FAMILY("font-family"),
    FONT_SIZE("font-size"),
    FONT_STYLE("font-style"),
    FONT_VARIANT("font-variant"),
    FONT_WEIGHT("font-weight"),
    FONT("font"),
    HEIGHT("height"),
    LEFT("left"),
    LETTER_SPACING("letter-spacing"),
    LINE_HEIGHT("line-height"),
    LIST_STYLE_IMAGE("list-style-image"),
    LIST_STYLE_POSITION("list-style-position"),
    LIST_STYLE_TYPE("list-style-type"),
    LIST_STYLE("list-style"),
    MARGIN_TOP("margin-top"),
    MARGIN_RIGHT("margin-right"),
    MARGIN_BOTTOM("margin-bottom"),
    MARGIN_LEFT("margin-left"),
    MARGIN("margin"),
    MAX_HEIGHT("max-height"),
    MAX_WIDTH("max-width"),
    MIN_HEIGHT("min-height"),
    MIN_WIDTH("min-width"),
    ORPHANS("orphans"),
    OUTLINE_COLOR("outline-color"),
    OUTLINE_STYLE("outline-style"),
    OUTLINE_WIDTH("outline-width"),
    OUTLINE("outline"),
    OVERFLOW("overflow"),
    PADDING_TOP("padding-top"),
    PADDING_RIGHT("padding-right"),
    PADDING_BOTTOM("padding-bottom"),
    PADDING_LEFT("padding-left"),
    PADDING("padding"),
    PAGE_BREAK_AFTER("page-break-after"),
    PAGE_BREAK_BEFORE("page-break-before"),
    PAGE_BREAK_INSIDE("page-break-inside"),
    POSITION("position"),
    QUOTES("quotes"),
    RIGHT("right"),
    TABLE_LAYOUT("table-layout"),
    TEXT_ALIGN("text-align"),
    TEXT_DECORATION("text-decoration"),
    TEXT_INDENT("text-indent"),
    TEXT_TRANSFORM("text-transform"),
    TOP("top"),
    UNICODE_BIDI("unicode-bidi"),
    VERTICAL_ALIGN("vertical-align"),
    VISIBILITY("visibility"),
    WHITE_SPACE("white-space"),
    WIDOWS("widows"),
    WIDTH("width"),
    WORD_SPACING("word-spacing"),
    Z_INDEX("z-index");
    
    private static final Map<String, PropertyName> propertyMap;
    
    static {
        propertyMap = new HashMap<>();
        for (final PropertyName propertyName : values()) {
            propertyMap.put(propertyName.getKey(), propertyName);
        }
    }
    
    public static PropertyName getPropertyName(final String name) {
        return propertyMap.get(name);
    }
    
    private final String key;
    
    private PropertyName(final String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
    
}
