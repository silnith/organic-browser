package org.silnith.css.model.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class PropertyNameTest {

	@Test
	public void testGetPropertyName() {
		for (final PropertyName propertyName : PropertyName.values()) {
			final PropertyName retrieved = PropertyName.getPropertyName(propertyName.getKey());
			
			assertSame(propertyName, retrieved);
		}
	}

	@Test
	public void testDisplay() {
		assertSame(PropertyName.DISPLAY, PropertyName.getPropertyName("display"));
	}

}
