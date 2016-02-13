package org.silnith.css.model.data;

/**
 * A percentage length value as defined in the CSS specification.
 * 
 * @author kent
 */
public class PercentageLength extends Length<PercentageUnit> implements Comparable<PercentageLength> {
    
    /**
     * Creates a new percentage length.
     * 
     * @param length the percentage scaled to the range 0 &ndash; 100
     */
    public PercentageLength(final CSSNumber length) {
        super(length, PercentageUnit.PERCENTAGE);
    }
    
    /**
     * Creates a new percentage length.
     * 
     * @param length the percentage scaled to the range 0 &ndash; 100
     */
    public PercentageLength(final float length) {
        this(CSSNumber.valueOf(length));
    }
    
    /**
     * Returns {@link Length.Type#PERCENTAGE}.
     */
    @Override
    public Type getType() {
        return Type.PERCENTAGE;
    }
    
    /**
     * Resolve the percentage length to an absolute length relative to the
     * provided absolute length. (e.g. if this length is 15% and the absolute
     * length to resolve it against is 100px, the returned length will be 15px)
     * 
     * @param relativeTo the length to take the percentage of
     * @return the resolved absolute length
     */
    public AbsoluteLength resolve(final AbsoluteLength relativeTo) {
        return relativeTo.times(getLength()).dividedBy(100);
    }
    
    @Override
    public int compareTo(final PercentageLength o) {
        assert getUnit() == o.getUnit();
        
        return getLength().compareTo(o.getLength());
    }
    
}
