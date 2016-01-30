package org.silnith.css.model.data;

public class AbsoluteLength extends Length<AbsoluteUnit> implements Comparable<AbsoluteLength> {
    
    public static final AbsoluteLength ZERO = getZero(AbsoluteUnit.IN);
    
    public static AbsoluteLength max(final AbsoluteLength a, final AbsoluteLength b) {
        if (a.compareTo(b) < 0) {
            return b;
        } else {
            return a;
        }
    }
    
    public static AbsoluteLength getZero(final AbsoluteUnit units) {
        return new AbsoluteLength(CSSNumber.ZERO, units);
    }
    
    private static float pixelsPerInch = 96f;
    
    public AbsoluteLength(final CSSNumber length, final AbsoluteUnit unit) {
        super(length, unit);
    }
    
    public AbsoluteLength(final float length, final AbsoluteUnit unit) {
        this(CSSNumber.valueOf(length), unit);
    }
    
    /**
     * Returns {@link Length.Type#ABSOLUTE}.
     */
    @Override
    public Type getType() {
        return Type.ABSOLUTE;
    }
    
    @Override
    public int compareTo(final AbsoluteLength o) {
        final AbsoluteLength other = o.convertTo(getUnit());
        return getLength().compareTo(other.getLength());
    }
    
    public AbsoluteLength plus(final AbsoluteLength length) {
        final AbsoluteUnit targetUnit = getUnit();
        final AbsoluteLength convertedLength = length.convertTo(targetUnit);
        return new AbsoluteLength(getLength().plus(convertedLength.getLength()), targetUnit);
    }
    
    public AbsoluteLength minus(final AbsoluteLength length) {
        final AbsoluteUnit targetUnit = getUnit();
        final AbsoluteLength convertedLength = length.convertTo(targetUnit);
        return new AbsoluteLength(getLength().minus(convertedLength.getLength()), targetUnit);
    }
    
    public AbsoluteLength times(final CSSNumber number) {
        return new AbsoluteLength(getLength().times(number), getUnit());
    }
    
    public AbsoluteLength times(final float number) {
        return new AbsoluteLength(getLength().times(number), getUnit());
    }
    
    public AbsoluteLength dividedBy(final CSSNumber number) {
        return new AbsoluteLength(getLength().dividedBy(number), getUnit());
    }
    
    public AbsoluteLength dividedBy(final float number) {
        return new AbsoluteLength(getLength().dividedBy(number), getUnit());
    }
    
    public AbsoluteLength convertTo(final AbsoluteUnit newUnit) {
        /*
         * PC <--> PT PT <--> IN PX <--> IN IN <--> CM CM <--> MM IN --> [PT,
         * PX, CM] PT --> [IN, PC] CM --> [MM, IN]
         */
        switch (getUnit()) {
        case CM: {
            switch (newUnit) {
            case CM: {
                return this;
            } // break;
            case IN: {
                return new AbsoluteLength(getLength().dividedBy(2.54f), AbsoluteUnit.IN);
            } // break;
            case MM: {
                return new AbsoluteLength(getLength().times(10f), AbsoluteUnit.MM);
            } // break;
            case PC: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PC);
            } // break;
            case PT: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PT);
            } // break;
            case PX: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PX);
            } // break;
            default: {
                throw new UnsupportedOperationException();
            } // break;
            }
        } // break;
        case IN: {
            switch (newUnit) {
            case CM: {
                return new AbsoluteLength(getLength().times(2.54f), AbsoluteUnit.CM);
            } // break;
            case IN: {
                return this;
            } // break;
            case MM: {
                return convertTo(AbsoluteUnit.CM).convertTo(AbsoluteUnit.MM);
            } // break;
            case PC: {
                return convertTo(AbsoluteUnit.PT).convertTo(AbsoluteUnit.PC);
            } // break;
            case PT: {
                return new AbsoluteLength(getLength().times(72f), AbsoluteUnit.PT);
            } // break;
            case PX: {
                // TODO: get system DPI
                return new AbsoluteLength(getLength().times(pixelsPerInch), AbsoluteUnit.PX);
            } // break;
            default: {
                throw new UnsupportedOperationException();
            } // break;
            }
        } // break;
        case MM: {
            switch (newUnit) {
            case CM: {
                return new AbsoluteLength(getLength().dividedBy(10f), AbsoluteUnit.CM);
            } // break;
            case IN: {
                return convertTo(AbsoluteUnit.CM).convertTo(AbsoluteUnit.IN);
            } // break;
            case MM: {
                return this;
            } // break;
            case PC: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PC);
            } // break;
            case PT: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PT);
            } // break;
            case PX: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PX);
            } // break;
            default: {
                throw new UnsupportedOperationException();
            } // break;
            }
        } // break;
        case PC: {
            switch (newUnit) {
            case CM: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.CM);
            } // break;
            case IN: {
                return convertTo(AbsoluteUnit.PT).convertTo(AbsoluteUnit.IN);
            } // break;
            case MM: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.MM);
            } // break;
            case PC: {
                return this;
            } // break;
            case PT: {
                return new AbsoluteLength(getLength().times(12f), AbsoluteUnit.PT);
            } // break;
            case PX: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PX);
            } // break;
            default: {
                throw new UnsupportedOperationException();
            } // break;
            }
        } // break;
        case PT: {
            switch (newUnit) {
            case CM: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.CM);
            } // break;
            case IN: {
                return new AbsoluteLength(getLength().dividedBy(72f), AbsoluteUnit.IN);
            } // break;
            case MM: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.MM);
            } // break;
            case PC: {
                return new AbsoluteLength(getLength().dividedBy(12f), AbsoluteUnit.PC);
            } // break;
            case PT: {
                return this;
            } // break;
            case PX: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PX);
            } // break;
            default: {
                throw new UnsupportedOperationException();
            } // break;
            }
        } // break;
        case PX: {
            switch (newUnit) {
            case CM: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.CM);
            } // break;
            case IN: {
                // TODO: get system DPI
                return new AbsoluteLength(getLength().dividedBy(pixelsPerInch), AbsoluteUnit.IN);
            } // break;
            case MM: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.MM);
            } // break;
            case PC: {
                return convertTo(AbsoluteUnit.PT).convertTo(AbsoluteUnit.PC);
            } // break;
            case PT: {
                return convertTo(AbsoluteUnit.IN).convertTo(AbsoluteUnit.PT);
            } // break;
            case PX: {
                return this;
            } // break;
            default: {
                throw new UnsupportedOperationException();
            } // break;
            }
        } // break;
        default: {
            throw new UnsupportedOperationException();
        } // break;
        }
    }
    
}
