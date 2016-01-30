package org.silnith.css.model.data;

public class CSSNumber extends Number implements Comparable<CSSNumber> {
    
    private static final long serialVersionUID = 1L;
    
    public static final CSSNumber ZERO = CSSNumber.valueOf(0);
    
    public static CSSNumber valueOf(final float value) {
        return new CSSNumber(value);
    }
    
    private final float value;
    
    public CSSNumber(final float value) {
        if (value == -0.0f) {
            this.value = 0;
        } else {
            this.value = value;
        }
    }
    
    @Override
    public int intValue() {
        return (int) value;
    }
    
    @Override
    public long longValue() {
        return (long) value;
    }
    
    @Override
    public float floatValue() {
        return value;
    }
    
    @Override
    public double doubleValue() {
        return value;
    }
    
    public CSSNumber plus(final CSSNumber number) {
        return CSSNumber.valueOf(value + number.value);
    }
    
    public CSSNumber plus(final float number) {
        return CSSNumber.valueOf(value + number);
    }
    
    public CSSNumber minus(final CSSNumber number) {
        return CSSNumber.valueOf(value - number.value);
    }
    
    public CSSNumber minus(final float number) {
        return CSSNumber.valueOf(value - number);
    }
    
    public CSSNumber times(final CSSNumber number) {
        return CSSNumber.valueOf(value * number.value);
    }
    
    public CSSNumber times(final float number) {
        return CSSNumber.valueOf(value * number);
    }
    
    public CSSNumber dividedBy(final CSSNumber number) {
        return CSSNumber.valueOf(value / number.value);
    }
    
    public CSSNumber dividedBy(final float number) {
        return CSSNumber.valueOf(value / number);
    }
    
    @Override
    public int compareTo(final CSSNumber o) {
        return Float.compare(value, o.value);
    }
    
    @Override
    public int hashCode() {
        return Float.valueOf(value).hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CSSNumber) {
            final CSSNumber other = (CSSNumber) obj;
            
            return Float.valueOf(value).equals(other.value);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
}
