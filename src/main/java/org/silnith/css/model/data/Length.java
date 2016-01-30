package org.silnith.css.model.data;

public abstract class Length<T extends Unit> {
    
    public enum Type {
        ABSOLUTE,
        RELATIVE,
        PERCENTAGE
    }
    
    private final CSSNumber length;
    
    private final T unit;
    
    protected Length(final CSSNumber length, final T unit) {
        if (length == null) {
            throw new NullPointerException("Length cannot be null.");
        }
        if (unit == null) {
            throw new NullPointerException("Unit cannot be null.");
        }
        this.length = length;
        this.unit = unit;
    }
    
    /**
     * The type of length this is. Based on the returned value the user can
     * safely cast to the concrete subtype.
     * <table>
     * <thead>
     * <tr>
     * <th>Returned value</th>
     * <th>Concrete type</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td>{@link Type#ABSOLUTE}</td>
     * <td>{@link AbsoluteLength}</td>
     * </tr>
     * <tr>
     * <td>{@link Type#RELATIVE}</td>
     * <td>{@link RelativeLength}</td>
     * </tr>
     * <tr>
     * <td>{@link Type#PERCENTAGE}</td>
     * <td>{@link PercentageLength}</td>
     * </tr>
     * </tbody>
     * </table>
     * 
     * @return the type of length this is
     * @see {@link AbsoluteLength}
     * @see {@link RelativeLength}
     * @see {@link PercentageLength}
     */
    public abstract Type getType();
    
    /**
     * Returns the numeric value of this length as a {@link CSSNumber}.
     * 
     * @return the numeric value of this length
     */
    public CSSNumber getLength() {
        return length;
    }
    
    /**
     * Returns the units of this length.
     * 
     * @return the length units
     * @see AbsoluteUnit
     * @see RelativeUnit
     * @see PercentageUnit
     */
    public T getUnit() {
        return unit;
    }
    
    @Override
    public int hashCode() {
        return getLength().hashCode() ^ getUnit().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Length) {
            final Length<?> object = (Length<?>) obj;
            
            return getLength().equals(object.getLength()) && getUnit().equals(object.getUnit());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return length + unit.getSuffix();
    }
    
}
