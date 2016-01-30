package org.silnith.browser.organic.box;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

import org.silnith.browser.organic.StyledElement;
import org.silnith.css.model.data.AbsoluteLength;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * A formatting box that represents an inline replaced element, such as "img".
 * This inline box participates in a {@link BlockFormattingContext}. (??)
 * 
 * @author kent
 */
public class AtomicLevelInlineBox implements InlineLevelBox, BlockFormattingContext {
    
    private final BlockBoxForBlocks delegate;
    
    public AtomicLevelInlineBox(final StyledElement styledElement) {
        super();
        this.delegate = new BlockBoxForBlocks(styledElement);
    }
    
    @Override
    public void addChild(final BlockLevelBox child) {
        delegate.addChild(child);
    }
    
    @Override
    public List<BlockLevelBox> getChildren() {
        return delegate.getChildren();
    }
    
    @Override
    public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
            final Graphics2D graphics) {
        return delegate.negotiateWidth(targetWidth, targetWidth, graphics);
    }
    
    @Override
    public LayoutResults layoutContents(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
            final Graphics2D graphics, final boolean canOverflow) {
        return new Results(delegate.layoutContents(targetWidth, targetWidth, graphics));
    }
    
    @Override
    public Node createDOM(final Document document) {
        final Element node = document.createElement("AtomicLevelInlineBox");
        node.appendChild(delegate.createDOM(document));
        return node;
    }
    
    @Override
    public String toString() {
        return "AtomicLevelInlineBox {" + delegate + "}";
    }
    
    private class Results implements LayoutResults {
        
        private final RenderableLineContent renderableLineContent;
        
        public Results(final RenderableContent renderableContent) {
            super();
            this.renderableLineContent = new Render(renderableContent);
        }
        
        @Override
        public RenderableLineContent getLayedOutContent() {
            return renderableLineContent;
        }
        
        @Override
        public boolean isSplit() {
            return false;
        }
        
        @Override
        public InlineLevelBox getRemainingContent() {
            return null;
        }
        
    }
    
    private class Render implements RenderableLineContent {
        
        private final RenderableContent renderableContent;
        
        public Render(final RenderableContent renderableContent) {
            super();
            this.renderableContent = renderableContent;
        }
        
        @Override
        public Dimension2D getSize() {
            return renderableContent.getSize();
        }
        
        @Override
        public void paintComponent(final Point2D startPoint, final Graphics2D graphics) {
            renderableContent.paintComponent(startPoint, graphics);
        }
        
        @Override
        public float getBaseline() {
            return (float) (renderableContent.getSize().getHeight() / 2f);
        }
        
    }
    
}
