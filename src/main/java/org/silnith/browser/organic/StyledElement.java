package org.silnith.browser.organic;

import java.util.ArrayList;
import java.util.List;

import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public abstract class StyledElement extends StyledContent {
    
    private final StyleData styleData;
    
    private final List<StyledContent> children;
    
    private PseudoElement before;
    
    private PseudoElement after;
    
    public StyledElement(final StyledElement parent, final StyleData styleData) {
        super(parent);
        // parent may be null
        if (styleData == null) {
            throw new NullPointerException();
        }
        this.styleData = styleData;
        this.children = new ArrayList<>();
        this.before = null;
        this.after = null;
    }
    
    public abstract String getTagName();
    
    @Override
    public StyleData getStyleData() {
        return styleData;
    }
    
    public void setBeforeContent(final PseudoElement beforeContent) {
        this.before = beforeContent;
    }
    
    public void setAfterContent(final PseudoElement afterContent) {
        this.after = afterContent;
    }
    
    public synchronized void addChild(final StyledContent styledNode) {
        children.add(styledNode);
    }
    
    public synchronized List<StyledContent> getChildren() {
        final ArrayList<StyledContent> appendedChildren = new ArrayList<>();
        if (before != null) {
            appendedChildren.add(before);
        }
        appendedChildren.addAll(children);
        if (after != null) {
            appendedChildren.add(after);
        }
        return appendedChildren;
    }
    
    @Override
    public Node createDOM(final Document document) {
        final Element node = document.createElement("StyledElement");
        node.setAttribute("tagName", getTagName());
        for (final PropertyName propertyName : PropertyName.values()) {
            if (styleData.isPropertyComputed(propertyName)) {
                node.setAttribute(propertyName.getKey(), String.valueOf(styleData.getComputedValue(propertyName)));
            }
        }
        for (final StyledContent child : getChildren()) {
            node.appendChild(child.createDOM(document));
        }
        return node;
    }
    
    @Override
    public synchronized String toString() {
        return getTagName() + " {styleData: " + styleData + ", children: " + children + "}";
    }
    
}
