package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.Position;
import org.silnith.css.model.data.PropertyName;


/**
 * A property accessor for the "position" property.
 * 
 * @author kent
 * @see PropertyName#POSITION
 * @see Position
 */
public class PositionAccessor extends PropertyAccessor<Position> {
    
    public PositionAccessor() {
        super(PropertyName.POSITION, false);
    }
    
    @Override
    public Position getInitialValue(final StyleData styleData) {
        return Position.STATIC;
    }
    
    @Override
    protected Position parse(final StyleData styleData, final String specifiedValue) {
        final Position position = Position.getFromValue(specifiedValue);
        if (position == null) {
            throw new IllegalArgumentException(
                    "Illegal value for property: " + getPropertyName() + ": " + specifiedValue);
        }
        return position;
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
