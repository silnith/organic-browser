package org.silnith.browser.organic.property.accessor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.silnith.css.model.data.PropertyName;

public class PropertyAccessorFactoryTest {

	private final PropertyAccessorFactory factory = new PropertyAccessorFactory();

	@Test
	public void testGetDisplayPropertyAccessor() {
		assertTrue(factory.getPropertyAccessor(PropertyName.DISPLAY) instanceof DisplayAccessor);
	}

}
