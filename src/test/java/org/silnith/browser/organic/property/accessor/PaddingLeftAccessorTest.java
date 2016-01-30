package org.silnith.browser.organic.property.accessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.CSSNumber;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.RelativeLength;
import org.silnith.css.model.data.RelativeUnit;


@RunWith(MockitoJUnitRunner.class)
public class PaddingLeftAccessorTest {
    
    @Mock
    private LengthParser lengthParser;
    
    @Mock
    private PropertyAccessor<AbsoluteLength> fontSizeAccessor;
    
    private PaddingLeftAccessor paddingLeftAccessor;
    
    @Before
    public void setUp() {
        paddingLeftAccessor = new PaddingLeftAccessor(lengthParser, fontSizeAccessor);
    }
    
    @Test
    public void testGetDependencies() {
        final Set<PropertyName> dependencies = paddingLeftAccessor.getDependencies();
        
        assertNotNull(dependencies);
        assertEquals(1, dependencies.size());
        assertTrue(dependencies.contains(PropertyName.FONT_SIZE));
    }
    
    @Test
    public void testGetInitialValueStyleData() {
        final Length<?> initialValue = paddingLeftAccessor.getInitialValue(null);
        
        assertEquals(CSSNumber.ZERO, initialValue.getLength());
        assertEquals(Length.Type.ABSOLUTE, initialValue.getType());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals(PropertyName.PADDING_LEFT, paddingLeftAccessor.getPropertyName());
    }
    
    @Test
    public void testIsInheritedByDefault() {
        assertFalse(paddingLeftAccessor.isInheritedByDefault());
    }
    
    @Test
    public void testComputeValueSpecifiedAbsolute() {
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        when((AbsoluteLength) lengthParser.parse(anyString())).thenReturn(fivePixels);
        
        final StyleData styleData = new StyleData(null);
        styleData.setSpecifiedValue(PropertyName.PADDING_LEFT, "5px");
        
        paddingLeftAccessor.computeValue(styleData);
        
        assertTrue(styleData.isPropertyComputed(PropertyName.PADDING_LEFT));
        assertEquals(fivePixels, styleData.getComputedValue(PropertyName.PADDING_LEFT));
        
        verify(lengthParser).parse(eq("5px"));
        verify(fontSizeAccessor, never()).getComputedValue(any(StyleData.class));
    }
    
    @Test
    public void testComputeValueSpecifiedAbsoluteInvalid() {
        final AbsoluteLength negativeFivePixels = new AbsoluteLength( -5, AbsoluteUnit.PX);
        
        when((AbsoluteLength) lengthParser.parse(anyString())).thenReturn(negativeFivePixels);
        
        final StyleData styleData = new StyleData(null);
        styleData.setSpecifiedValue(PropertyName.PADDING_LEFT, "-5px");
        
        paddingLeftAccessor.computeValue(styleData);
        
        assertTrue(styleData.isPropertyComputed(PropertyName.PADDING_LEFT));
        final Length<?> computedValue = (Length<?>) styleData.getComputedValue(PropertyName.PADDING_LEFT);
        assertEquals(CSSNumber.ZERO, computedValue.getLength());
        assertEquals(Length.Type.ABSOLUTE, computedValue.getType());
        
        verify(lengthParser).parse("-5px");
        verify(fontSizeAccessor, never()).getComputedValue(any(StyleData.class));
    }
    
    @Test
    public void testComputeValueSpecifiedRelative() {
        final AbsoluteLength onePoint = new AbsoluteLength(1, AbsoluteUnit.PT);
        final RelativeLength twoEM = new RelativeLength(2, RelativeUnit.EM);
        
        when((RelativeLength) lengthParser.parse(anyString())).thenReturn(twoEM);
        when(fontSizeAccessor.getComputedValue(any(StyleData.class))).thenReturn(onePoint);
        
        final StyleData styleData = new StyleData(null);
        styleData.setSpecifiedValue(PropertyName.PADDING_LEFT, "2em");
        
        paddingLeftAccessor.computeValue(styleData);
        
        assertTrue(styleData.isPropertyComputed(PropertyName.PADDING_LEFT));
        assertEquals(new AbsoluteLength(2, AbsoluteUnit.PT), styleData.getComputedValue(PropertyName.PADDING_LEFT));
        
        verify(lengthParser).parse(eq("2em"));
        verify(fontSizeAccessor).getComputedValue(same(styleData));
    }
    
    @Test
    public void testComputeValueSpecifiedRelativeInvalid() {
        final AbsoluteLength onePoint = new AbsoluteLength(1, AbsoluteUnit.PT);
        final RelativeLength negativeTwoEM = new RelativeLength( -2, RelativeUnit.EM);
        
        when((RelativeLength) lengthParser.parse(anyString())).thenReturn(negativeTwoEM);
        when(fontSizeAccessor.getComputedValue(any(StyleData.class))).thenReturn(onePoint);
        
        final StyleData styleData = new StyleData(null);
        styleData.setSpecifiedValue(PropertyName.PADDING_LEFT, "-2em");
        
        paddingLeftAccessor.computeValue(styleData);
        
        assertTrue(styleData.isPropertyComputed(PropertyName.PADDING_LEFT));
        final Length<?> computedValue = (Length<?>) styleData.getComputedValue(PropertyName.PADDING_LEFT);
        assertEquals(CSSNumber.ZERO, computedValue.getLength());
        assertEquals(Length.Type.ABSOLUTE, computedValue.getType());
        
        verify(lengthParser).parse(eq("-2em"));
        verify(fontSizeAccessor).getComputedValue(same(styleData));
    }
    
    @Test
    public void testComputeValueSpecifiedPercent() {
        final PercentageLength fiftyPercent = new PercentageLength(50);
        
        when((PercentageLength) lengthParser.parse(anyString())).thenReturn(fiftyPercent);
        
        final StyleData styleData = new StyleData(null);
        styleData.setSpecifiedValue(PropertyName.PADDING_LEFT, "50%");
        
        paddingLeftAccessor.computeValue(styleData);
        
        assertTrue(styleData.isPropertyComputed(PropertyName.PADDING_LEFT));
        assertEquals(fiftyPercent, styleData.getComputedValue(PropertyName.PADDING_LEFT));
        
        verify(lengthParser).parse(eq("50%"));
        verify(fontSizeAccessor, never()).getComputedValue(any(StyleData.class));
    }
    
    @Test
    public void testComputeValueNotSpecified() {
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        when((AbsoluteLength) lengthParser.parse(anyString())).thenReturn(fivePixels);
        
        final StyleData parentStyle = new StyleData(null);
        final StyleData styleData = new StyleData(parentStyle);
        parentStyle.setComputedValue(PropertyName.PADDING_LEFT, fivePixels);
        
        paddingLeftAccessor.computeValue(styleData);
        
        assertTrue(styleData.isPropertyComputed(PropertyName.PADDING_LEFT));
        final Length<?> computedValue = (Length<?>) styleData.getComputedValue(PropertyName.PADDING_LEFT);
        assertEquals(CSSNumber.ZERO, computedValue.getLength());
        assertEquals(Length.Type.ABSOLUTE, computedValue.getType());
        
        verify(lengthParser, never()).parse(anyString());
        verify(fontSizeAccessor, never()).getComputedValue(any(StyleData.class));
    }
    
    @Test
    public void testComputeValueInherit() {
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        when((AbsoluteLength) lengthParser.parse(anyString())).thenReturn(fivePixels);
        
        final StyleData parentStyle = new StyleData(null);
        final StyleData styleData = new StyleData(parentStyle);
        parentStyle.setComputedValue(PropertyName.PADDING_LEFT, fivePixels);
        styleData.setSpecifiedValue(PropertyName.PADDING_LEFT, "inherit");
        
        paddingLeftAccessor.computeValue(styleData);
        
        assertTrue(styleData.isPropertyComputed(PropertyName.PADDING_LEFT));
        assertEquals(fivePixels, styleData.getComputedValue(PropertyName.PADDING_LEFT));
        
        verify(lengthParser, never()).parse(anyString());
        verify(fontSizeAccessor, never()).getComputedValue(any(StyleData.class));
    }
    
    @Test
    public void testGetComputedValue() {
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        final StyleData styleData = new StyleData(null);
        styleData.setComputedValue(PropertyName.PADDING_LEFT, fivePixels);
        
        final Length<?> computedValue = paddingLeftAccessor.getComputedValue(styleData);
        
        assertEquals(CSSNumber.valueOf(5), computedValue.getLength());
        assertEquals(AbsoluteUnit.PX, computedValue.getUnit());
    }
    
}
