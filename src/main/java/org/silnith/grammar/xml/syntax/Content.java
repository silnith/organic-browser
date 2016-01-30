package org.silnith.grammar.xml.syntax;

import java.util.List;


public class Content {
    
    public List<Object> contents;
    
    public Content() {
    }
    
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (contents != null) {
            for (final Object obj : contents) {
//				stringBuilder.append('\n');
                stringBuilder.append(obj);
            }
        }
        return stringBuilder.toString();
    }
    
}
