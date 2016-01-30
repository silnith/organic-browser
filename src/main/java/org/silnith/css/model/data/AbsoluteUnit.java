package org.silnith.css.model.data;

public enum AbsoluteUnit implements Unit {
    /**
     * 2.54cm 25.4mm 72pt 96px
     */
    IN("in"),
    /**
     * 10mm 1/2.54 in 96/2.54 px
     */
    CM("cm"),
    /**
     * 1mm 96/25.4 px
     */
    MM("mm"),
    /**
     * 1/72 of an inch 4/3px
     */
    PT("pt"),
    /**
     * 12pt 16px
     */
    PC("pc"),
    /**
     * 1px 0.75pt
     */
    PX("px");
    
    private final String suffix;
    
    private AbsoluteUnit(final String suffix) {
        this.suffix = suffix;
    }
    
    @Override
    public String getSuffix() {
        return suffix;
    }
    
}
