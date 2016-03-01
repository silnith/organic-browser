package org.silnith.browser.organic.property.accessor;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.silnith.css.model.data.Display;
import org.silnith.css.model.data.PropertyName;


public class PropertyAccessorFactoryTest {
    
    private final PropertyAccessorFactory factory = new PropertyAccessorFactory();
    
    @Test
    public void testGetDisplayPropertyAccessor() {
        final PropertyAccessor<Display> displayAccessor = factory.getPropertyAccessor(PropertyName.DISPLAY);
        
        assertTrue(displayAccessor instanceof DisplayAccessor);
    }
    
}
