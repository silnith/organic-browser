package org.silnith.browser.organic.box;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;


public class PaddingInformationTest {
    
    private PaddingInformation paddingInformation;
    
    @Test
    public void testGetPaddingInformation() {
        final StyleData styleData = new StyleData(null);
        final AbsoluteLength twoPixels = new AbsoluteLength(2, AbsoluteUnit.PX);
        final AbsoluteLength threePixels = new AbsoluteLength(3, AbsoluteUnit.PX);
        final AbsoluteLength fourPixels = new AbsoluteLength(4, AbsoluteUnit.PX);
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        styleData.setComputedValue(PropertyName.PADDING_TOP, twoPixels);
        styleData.setComputedValue(PropertyName.PADDING_RIGHT, threePixels);
        styleData.setComputedValue(PropertyName.PADDING_BOTTOM, fourPixels);
        styleData.setComputedValue(PropertyName.PADDING_LEFT, fivePixels);
        
        paddingInformation = PaddingInformation.getPaddingInformation(styleData);
        
        assertEquals(twoPixels, paddingInformation.getPaddingTop());
        assertEquals(threePixels, paddingInformation.getPaddingRight());
        assertEquals(fourPixels, paddingInformation.getPaddingBottom());
        assertEquals(fivePixels, paddingInformation.getPaddingLeft());
    }
    
    @Test
    public void testPaddingInformationAbsolute() {
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        paddingInformation = new PaddingInformation(fivePixels, fivePixels, fivePixels, fivePixels);
    }
    
//	@Test
//	public void testPaddingInformationRelative() {
//		final RelativeLength fiveEm = new RelativeLength(CSSNumber.valueOf(5), RelativeUnit.EM);
//
//		paddingInformation = new PaddingInformation(fiveEm, fiveEm, fiveEm, fiveEm);
//	}
    
    @Test
    public void testPaddingInformationPercentage() {
        final PercentageLength fivePercent = new PercentageLength(5);
        
        paddingInformation = new PaddingInformation(fivePercent, fivePercent, fivePercent, fivePercent);
    }
    
    @Test
    public void testGetPaddingTop() {
        final AbsoluteLength twoPixels = new AbsoluteLength(2, AbsoluteUnit.PX);
        final AbsoluteLength threePixels = new AbsoluteLength(3, AbsoluteUnit.PX);
        final AbsoluteLength fourPixels = new AbsoluteLength(4, AbsoluteUnit.PX);
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        paddingInformation = new PaddingInformation(twoPixels, threePixels, fourPixels, fivePixels);
        
        assertEquals(twoPixels, paddingInformation.getPaddingTop());
    }
    
    @Test
    public void testGetPaddingRight() {
        final AbsoluteLength twoPixels = new AbsoluteLength(2, AbsoluteUnit.PX);
        final AbsoluteLength threePixels = new AbsoluteLength(3, AbsoluteUnit.PX);
        final AbsoluteLength fourPixels = new AbsoluteLength(4, AbsoluteUnit.PX);
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        paddingInformation = new PaddingInformation(twoPixels, threePixels, fourPixels, fivePixels);
        
        assertEquals(threePixels, paddingInformation.getPaddingRight());
    }
    
    @Test
    public void testGetPaddingBottom() {
        final AbsoluteLength twoPixels = new AbsoluteLength(2, AbsoluteUnit.PX);
        final AbsoluteLength threePixels = new AbsoluteLength(3, AbsoluteUnit.PX);
        final AbsoluteLength fourPixels = new AbsoluteLength(4, AbsoluteUnit.PX);
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        paddingInformation = new PaddingInformation(twoPixels, threePixels, fourPixels, fivePixels);
        
        assertEquals(fourPixels, paddingInformation.getPaddingBottom());
    }
    
    @Test
    public void testGetPaddingLeft() {
        final AbsoluteLength twoPixels = new AbsoluteLength(2, AbsoluteUnit.PX);
        final AbsoluteLength threePixels = new AbsoluteLength(3, AbsoluteUnit.PX);
        final AbsoluteLength fourPixels = new AbsoluteLength(4, AbsoluteUnit.PX);
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        
        paddingInformation = new PaddingInformation(twoPixels, threePixels, fourPixels, fivePixels);
        
        assertEquals(fivePixels, paddingInformation.getPaddingLeft());
    }
    
    @Test
    public void testResolve() {
        final PercentageLength fivePercent = new PercentageLength(5);
        final AbsoluteLength oneHundredPixels = new AbsoluteLength(100, AbsoluteUnit.PX);
        
        paddingInformation = new PaddingInformation(fivePercent, fivePercent, fivePercent, fivePercent);
        
        final ResolvedPaddingInformation resolved = paddingInformation.resolve(oneHundredPixels);
        
        final AbsoluteLength fivePixels = new AbsoluteLength(5, AbsoluteUnit.PX);
        assertEquals(fivePixels, resolved.getPaddingTop());
        assertEquals(fivePixels, resolved.getPaddingRight());
        assertEquals(fivePixels, resolved.getPaddingBottom());
        assertEquals(fivePixels, resolved.getPaddingLeft());
    }
    
}
