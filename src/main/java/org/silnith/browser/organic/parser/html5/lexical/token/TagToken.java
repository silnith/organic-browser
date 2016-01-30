package org.silnith.browser.organic.parser.html5.lexical.token;

import java.util.ArrayList;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;


public abstract class TagToken extends Token {
    
    public static class Attribute {
        
        private final StringBuilder name;
        
        private final StringBuilder value;
        
        public Attribute() {
            super();
            this.name = new StringBuilder();
            this.value = new StringBuilder();
        }
        
        public void appendToName(final char ch) {
            name.append(ch);
        }
        
        public String getName() {
            return name.toString();
        }
        
        public void appendToValue(final char ch) {
            value.append(ch);
        }
        
        public void appendToValue(final char[] ch) {
            value.append(ch);
        }
        
        public String getValue() {
            return value.toString();
        }
        
        @Override
        public String toString() {
            return name.toString() + '=' + '"' + value + '"';
        }
        
    }
    
    private StringBuilder tagName;
    
    private boolean selfClosing;
    
    private final List<Attribute> attributes;
    
    private Attribute currentAttribute;
    
    public TagToken() {
        super();
        this.tagName = new StringBuilder();
        this.selfClosing = false;
        this.attributes = new ArrayList<>();
        this.currentAttribute = null;
    }
    
    public void setSelfClosing() {
        selfClosing = true;
    }
    
    public boolean isSelfClosing() {
        return selfClosing;
    }
    
    public void setTagName(final String tagName) {
        this.tagName = new StringBuilder(tagName);
    }
    
    public String getTagName() {
        return tagName.toString();
    }
    
    public void appendToTagName(final char character) {
        tagName.append(character);
    }
    
    public Attribute createNewAttribute() {
        final Attribute attribute = new Attribute();
        attributes.add(attribute);
        currentAttribute = attribute;
        return attribute;
    }
    
    public Attribute getCurrentAttribute() {
        return currentAttribute;
    }
    
    public List<Attribute> getAttributes() {
        return attributes;
    }
    
    public void validateCurrentAttributeNameUnique(final boolean allowParseError) {
        final String name = currentAttribute.getName();
        for (final Attribute attr : attributes) {
            if (attr == currentAttribute) {
                continue;
            }
            if (name.equals(attr.getName())) {
                if (allowParseError) {
                    attributes.remove(currentAttribute);
                    break;
                    /*
                     * Note that this is still the "current attribute", so any
                     * further processing (such as the attribute value) continue
                     * to work on it before it is garbage-collected. This is
                     * intentional.
                     */
                } else {
                    throw new ParseErrorException("Duplicate attribute name: " + name);
                }
            }
        }
    }
    
}
