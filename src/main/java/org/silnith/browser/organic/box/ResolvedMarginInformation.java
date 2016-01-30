package org.silnith.browser.organic.box;

import org.silnith.css.model.data.AbsoluteLength;


public class ResolvedMarginInformation {
    
    private final AbsoluteLength marginTop;
    
    private final AbsoluteLength marginRight;
    
    private final AbsoluteLength marginBottom;
    
    private final AbsoluteLength marginLeft;
    
    public ResolvedMarginInformation(final AbsoluteLength marginTop, final AbsoluteLength marginRight,
            final AbsoluteLength marginBottom, final AbsoluteLength marginLeft) {
        super();
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
    }
    
    public AbsoluteLength getMarginTop() {
        return marginTop;
    }
    
    public AbsoluteLength getMarginRight() {
        return marginRight;
    }
    
    public AbsoluteLength getMarginBottom() {
        return marginBottom;
    }
    
    public AbsoluteLength getMarginLeft() {
        return marginLeft;
    }
    
}
