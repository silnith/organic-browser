package org.silnith.browser.organic.box;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.silnith.browser.organic.StyledElement;
import org.silnith.css.model.data.AbsoluteLength;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * A formatting box that represents an inline replaced element, such as "img".
 * This inline box participates in a {@link BlockFormattingContext}. (??)
 * <p>
 * Inline-level boxes that are not inline boxes (such as replaced inline-level elements, inline-block elements, and inline-table elements) are called atomic inline-level boxes because they participate in their inline formatting context as a single opaque box.
 * 
 * @author kent
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#inline-boxes">9.2.2 Inline-level elements and inline boxes</a>
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#x13">atomic inline-level boxes</a>
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
        public boolean containsPoint(Point2D startPoint, Point2D clickPoint) {
            final boolean contains = renderableContent.containsPoint(startPoint, clickPoint);
            if (contains) {
                System.out.println(this);
            }
            return contains;
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

        @Override
        public String toString() {
            return "Render [renderableContent=" + renderableContent + "]";
        }
        
    }
    
}
