package org.silnith.browser.organic.box;

import org.silnith.css.model.data.AbsoluteLength;


public class ResolvedPaddingInformation {
    
    private final AbsoluteLength paddingTop;
    
    private final AbsoluteLength paddingRight;
    
    private final AbsoluteLength paddingBottom;
    
    private final AbsoluteLength paddingLeft;
    
    public ResolvedPaddingInformation(final AbsoluteLength paddingTop, final AbsoluteLength paddingRight,
            final AbsoluteLength paddingBottom, final AbsoluteLength paddingLeft) {
        super();
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
        this.paddingLeft = paddingLeft;
    }
    
    public AbsoluteLength getPaddingTop() {
        return paddingTop;
    }
    
    public AbsoluteLength getPaddingRight() {
        return paddingRight;
    }
    
    public AbsoluteLength getPaddingBottom() {
        return paddingBottom;
    }
    
    public AbsoluteLength getPaddingLeft() {
        return paddingLeft;
    }
    
}
