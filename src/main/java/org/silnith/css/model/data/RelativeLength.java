package org.silnith.css.model.data;

public class RelativeLength extends Length<RelativeUnit> {
    
    public RelativeLength(final CSSNumber length, final RelativeUnit unit) {
        super(length, unit);
    }
    
    public RelativeLength(final float length, final RelativeUnit unit) {
        this(CSSNumber.valueOf(length), unit);
    }
    
    /**
     * Returns {@link Type#RELATIVE}.
     */
    @Override
    public Type getType() {
        return Type.RELATIVE;
    }
    
    public AbsoluteLength resolve(final AbsoluteLength relativeTo) {
        switch (getUnit()) {
        case EM: {
            return relativeTo.times(getLength());
//			return new AbsoluteLength(relativeTo.getLength().times(getLength()), relativeTo.getUnit());
        } // break;
        case EX: {
            /*
             * The correct way to calculate this is to get the FontMetrics
             * object and ask it for the x-height.
             */
            return relativeTo.times(getLength()).dividedBy(2);
//			return new AbsoluteLength(relativeTo.getLength().times(getLength()).dividedBy(2f), relativeTo.getUnit());
        } // break;
        default:
            throw new UnsupportedOperationException();
        }
    }
    
}
