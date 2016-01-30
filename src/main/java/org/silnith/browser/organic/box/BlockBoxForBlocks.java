package org.silnith.browser.organic.box;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.silnith.browser.organic.RenderableContainer;
import org.silnith.browser.organic.RenderedBackground;
import org.silnith.browser.organic.RenderedBox;
import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.StyledElement;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class BlockBoxForBlocks implements BlockLevelBox, BlockFormattingContext {
    
    private final StyledElement styledElement;
    
    private final BackgroundInformation backgroundInformation;
    
    private final MarginInformation marginInformation;
    
    private final BorderInformation borderInformation;
    
    private final PaddingInformation paddingInformation;
    
    private final List<BlockLevelBox> children;
    
    public BlockBoxForBlocks(final StyledElement styledElement) {
        super();
        this.styledElement = styledElement;
        final StyleData styleData = this.styledElement.getStyleData();
        this.backgroundInformation = BackgroundInformation.getBackgroundInformation(styleData);
        this.marginInformation = MarginInformation.getMarginInformation(styleData);
        this.borderInformation = BorderInformation.getBorderInformation(styleData);
        this.paddingInformation = PaddingInformation.getPaddingInformation(styleData);
        this.children = new ArrayList<>();
    }
    
    @Override
    public synchronized void addChild(final BlockLevelBox child) {
        children.add(child);
    }
    
    @Override
    public synchronized List<BlockLevelBox> getChildren() {
        return Collections.unmodifiableList(children);
    }
    
    private float getPixels(final AbsoluteLength absoluteLength) {
        return absoluteLength.convertTo(AbsoluteUnit.PX).getLength().floatValue();
    }
    
    @Override
    public ResolvedMarginInformation getMarginInformation(final AbsoluteLength containingBlockWidth) {
        return marginInformation.resolve(containingBlockWidth);
    }
    
    @Override
    public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
            final Graphics2D graphics) {
        if (containingBlockWidth == null) {
            throw new NullPointerException();
        }
        if (targetWidth == null) {
            throw new NullPointerException();
        }
        
        final ResolvedPaddingInformation resolvedPaddingInformation =
                paddingInformation.resolve(AbsoluteLength.max(containingBlockWidth, AbsoluteLength.ZERO));
                
        AbsoluteLength childContainingBlockWidth = targetWidth;
        childContainingBlockWidth = childContainingBlockWidth.minus(borderInformation.getBorderLeftWidth());
        childContainingBlockWidth = childContainingBlockWidth.minus(resolvedPaddingInformation.getPaddingLeft());
        childContainingBlockWidth = childContainingBlockWidth.minus(resolvedPaddingInformation.getPaddingRight());
        childContainingBlockWidth = childContainingBlockWidth.minus(borderInformation.getBorderRightWidth());
        childContainingBlockWidth = AbsoluteLength.max(childContainingBlockWidth, AbsoluteLength.ZERO);
        
        AbsoluteLength childMinWidth = AbsoluteLength.ZERO;
        for (final BlockLevelBox child : children) {
            // TODO: Margins may be negative.
            final ResolvedMarginInformation childMargins = child.getMarginInformation(childContainingBlockWidth);
            
            AbsoluteLength childMinusMargins = childContainingBlockWidth;
            childMinusMargins = childMinusMargins.minus(childMargins.getMarginLeft());
            childMinusMargins = childMinusMargins.minus(childMargins.getMarginRight());
            childMinusMargins = AbsoluteLength.max(childMinusMargins, AbsoluteLength.ZERO);
            
            final AbsoluteLength childNegotiatedWidth =
                    child.negotiateWidth(childContainingBlockWidth, childMinusMargins, graphics);
                    
            AbsoluteLength negotiatedPlusMargins = childNegotiatedWidth;
            negotiatedPlusMargins = negotiatedPlusMargins.plus(childMargins.getMarginLeft());
            negotiatedPlusMargins = negotiatedPlusMargins.plus(childMargins.getMarginRight());
            
            childMinWidth = AbsoluteLength.max(childMinWidth, negotiatedPlusMargins);
        }
        
        AbsoluteLength minWidth = childMinWidth;
        minWidth = minWidth.plus(borderInformation.getBorderLeftWidth());
        minWidth = minWidth.plus(resolvedPaddingInformation.getPaddingLeft());
        minWidth = minWidth.plus(resolvedPaddingInformation.getPaddingRight());
        minWidth = minWidth.plus(borderInformation.getBorderRightWidth());
        return minWidth;
    }
    
    @Override
    public RenderableContent layoutContents(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
            final Graphics2D graphics) {
        if (containingBlockWidth == null) {
            throw new NullPointerException();
        }
        if (targetWidth == null) {
            throw new NullPointerException();
        }
        final AbsoluteLength thisTargetWidth = AbsoluteLength.max(targetWidth, AbsoluteLength.ZERO);
        
        final ResolvedPaddingInformation resolvedPaddingInformation =
                paddingInformation.resolve(AbsoluteLength.max(containingBlockWidth, AbsoluteLength.ZERO));
                
        AbsoluteLength childTargetWidth = thisTargetWidth;
        childTargetWidth = childTargetWidth.minus(borderInformation.getBorderLeftWidth());
        childTargetWidth = childTargetWidth.minus(resolvedPaddingInformation.getPaddingLeft());
        childTargetWidth = childTargetWidth.minus(resolvedPaddingInformation.getPaddingRight());
        childTargetWidth = childTargetWidth.minus(borderInformation.getBorderRightWidth());
        final AbsoluteLength childContainingBlock = AbsoluteLength.max(childTargetWidth, AbsoluteLength.ZERO);
        
        final float borderTop = getPixels(borderInformation.getBorderTopWidth());
        final float borderRight = getPixels(borderInformation.getBorderRightWidth());
        final float borderBottom = getPixels(borderInformation.getBorderBottomWidth());
        final float borderLeft = getPixels(borderInformation.getBorderLeftWidth());
        final float paddingTop = getPixels(resolvedPaddingInformation.getPaddingTop());
//		final float paddingRight = getPixels(resolvedPaddingInformation.getPaddingRight());
        final float paddingBottom = getPixels(resolvedPaddingInformation.getPaddingBottom());
        final float paddingLeft = getPixels(resolvedPaddingInformation.getPaddingLeft());
        
        final List<RenderableContent> childRenderables = new LinkedList<>();
        final List<Point2D> childLocations = new LinkedList<>();
        
        final Dimension2D contentSize = new Dimension();
        contentSize.setSize(0, 0);
        final Point2D currentChildLocation = new Point2D.Float();
        currentChildLocation.setLocation(borderLeft + paddingLeft, borderTop + paddingTop);
        AbsoluteLength previousBottomMargin = AbsoluteLength.ZERO;
        for (final BlockLevelBox child : children) {
            // TODO: Margins may be negative.
            final ResolvedMarginInformation childMargins = child.getMarginInformation(childContainingBlock);
            final AbsoluteLength marginTop = childMargins.getMarginTop();
            final AbsoluteLength marginRight = childMargins.getMarginRight();
            final AbsoluteLength marginBottom = childMargins.getMarginBottom();
            final AbsoluteLength marginLeft = childMargins.getMarginLeft();
            
            AbsoluteLength marginBetweenBlocks = AbsoluteLength.max(previousBottomMargin, marginTop);
            marginBetweenBlocks = marginBetweenBlocks.minus(previousBottomMargin);
            
            AbsoluteLength childMinusMargins = childContainingBlock;
            childMinusMargins = childMinusMargins.minus(marginLeft);
            childMinusMargins = childMinusMargins.minus(marginRight);
            
            // top margin of child - margin collapse
            final Point2D childLocationCopy = new Point2D.Float();
            childLocationCopy.setLocation(currentChildLocation.getX() + getPixels(marginLeft),
                    currentChildLocation.getY() + getPixels(marginBetweenBlocks));
            childLocations.add(childLocationCopy);
            final RenderableContent renderableContent =
                    child.layoutContents(childContainingBlock, childMinusMargins, graphics);
            childRenderables.add(renderableContent);
            final Dimension2D childSize = renderableContent.getSize();
            currentChildLocation.setLocation(currentChildLocation.getX(), currentChildLocation.getY()
                    + getPixels(marginBetweenBlocks) + childSize.getHeight() + getPixels(marginBottom));
            // bottom margin of child
            contentSize.setSize(
                    Math.max(contentSize.getWidth(),
                            getPixels(marginLeft) + childSize.getWidth() + getPixels(marginRight)),
                    getPixels(marginBetweenBlocks) + contentSize.getHeight() + childSize.getHeight()
                            + getPixels(marginBottom));
                            
            previousBottomMargin = marginBottom;
        }
        
        /*
         * TODO: Should the block expand horizontally to fill all available
         * space?
         */
//		final float contentWidth = (float) contentSize.getWidth();
        final float contentHeight = (float) contentSize.getHeight();
//		final float paddedContentWidth = paddingLeft + contentWidth + paddingRight;
        final float paddedContentHeight = paddingTop + contentHeight + paddingBottom;
//		final float totalWidth = borderLeft + paddedContentWidth + borderRight;
        final float totalHeight = borderTop + paddedContentHeight + borderBottom;
        final float totalWidth = getPixels(thisTargetWidth);
        final float paddedContentWidth = totalWidth - (borderLeft + borderRight);
        
        final RenderedBox box = new RenderedBox(paddedContentWidth, paddedContentHeight, borderInformation);
        // Put the border under the child contents.
        childLocations.add(0, new Point2D.Float());
        childRenderables.add(0, box);
        
        // Add background to the beginning of the list of children.
        contentSize.setSize(totalWidth, totalHeight);
        final RenderedBackground background = new RenderedBackground(contentSize, backgroundInformation.getColor());
        // Note this is at the start of the lists, not the end.
        childLocations.add(0, new Point2D.Float());
        childRenderables.add(0, background);
        
        final RenderableContainer renderable = new RenderableContainer(childLocations, childRenderables, contentSize);
        
        return renderable;
    }
    
    @Override
    public Node createDOM(final Document document) {
        final Element node = document.createElement("BlockBoxForBlocks");
        for (final PropertyName propertyName : PropertyName.values()) {
            if (styledElement.getStyleData().isPropertyComputed(propertyName)) {
                node.setAttribute(propertyName.getKey(),
                        String.valueOf(styledElement.getStyleData().getComputedValue(propertyName)));
            }
        }
        for (final BlockLevelBox child : children) {
            node.appendChild(child.createDOM(document));
        }
        return node;
    }
    
    @Override
    public String toString() {
        return "BlockBoxForBlocks {children: " + children + "}";
    }
    
}
