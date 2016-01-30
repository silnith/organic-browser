package org.silnith.grammar.xml.syntax;

public class SDDecl {
    
    public boolean standalone;
    
    public SDDecl() {
    }
    
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" standalone=");
        stringBuilder.append('"');
        if (standalone) {
            stringBuilder.append("yes");
        } else {
            stringBuilder.append("no");
        }
        stringBuilder.append('"');
        return stringBuilder.toString();
    }
    
}
