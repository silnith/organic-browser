package org.silnith.css.model.data;


public class AlignAndOffset {
    
    private final VerticalAlignment verticalAlignment;
    
    private final Length<?> offset;
    
    public AlignAndOffset(VerticalAlignment verticalAlignment) {
        super();
        this.verticalAlignment = verticalAlignment;
        this.offset = AbsoluteLength.ZERO;
    }
    
    public AlignAndOffset(Length<?> offset) {
        super();
        this.verticalAlignment = VerticalAlignment.BASELINE;
        this.offset = offset;
    }
    
    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }
    
    public Length<?> getOffset() {
        return offset;
    }
    
}
