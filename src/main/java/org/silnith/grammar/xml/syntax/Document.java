package org.silnith.grammar.xml.syntax;

import java.util.List;


public class Document {
    
    public Prolog prolog;
    
    public Element element;
    
    public List<Misc> miscList;
    
    public Document() {
    }
    
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (prolog != null) {
            stringBuilder.append(prolog);
            stringBuilder.append('\n');
        }
        stringBuilder.append(element);
        if (miscList != null) {
            for (final Misc misc : miscList) {
                stringBuilder.append(misc);
            }
        }
        return stringBuilder.toString();
    }
    
}
