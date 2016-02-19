package org.silnith.browser.organic.box;

import java.util.ArrayList;
import java.util.List;

import org.silnith.browser.organic.StyledContent;
import org.silnith.browser.organic.StyledElement;
import org.silnith.browser.organic.StyledText;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.Display;
import org.silnith.css.model.data.ListStylePosition;


/**
 * Formats the given styled document into a box model.
 * <p>
 * This class is thread-safe and re-entrant.
 * 
 * @author kent
 */
public class Formatter {
    
    private final PropertyAccessor<Display> displayAccessor;
    
    private final PropertyAccessor<AbsoluteLength> fontSizeAccessor;
    
    private final PropertyAccessor<ListStylePosition> listStylePositionAccessor;
    
    public Formatter(final PropertyAccessor<Display> displayAccessor,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor,
            final PropertyAccessor<ListStylePosition> listStylePositionAccessor) {
        super();
        this.displayAccessor = displayAccessor;
        this.fontSizeAccessor = fontSizeAccessor;
        this.listStylePositionAccessor = listStylePositionAccessor;
    }
    
    public BlockLevelBox createBlockBox(final StyledElement styledElement) {
        if (requiresBlockFormattingContext(styledElement)) {
            // make a block box for blocks
            return createBlockBoxForBlocks(styledElement);
        } else {
            // make a block box for flow
            return createBlockBoxForFlow(styledElement);
        }
    }
    
    public BlockLevelBox createListItemBox(final StyledElement styledElement) {
        // generate marker box for (possible) counter
        // marker box does not inherit background from principal
        // generate principal box for list item content
        // if list-style-position: outside, marker box is block
        // if list-style-position: inside, marker box is inline
        // and before any :before content
        final ListStylePosition listStylePosition =
                listStylePositionAccessor.getComputedValue(styledElement.getStyleData());
        if (requiresBlockFormattingContext(styledElement)) {
            // list item has block children
            switch (listStylePosition) {
            case INSIDE: {
                final BlockBoxForBlocks box = new BlockBoxForBlocks(styledElement);
                
                // do createInlineLevelBox with an inline child appended to the
                // front for the marker
                final InlineLevelBox markerBox = new InlineListItemMarker(fontSizeAccessor, styledElement);
                final AnonymousBlockBox anonymousBlockBox = new AnonymousBlockBox(styledElement);
                anonymousBlockBox.addChild(markerBox);
                box.addChild(anonymousBlockBox);
                
                fillInBlockChildren(styledElement, box, styledElement.getChildren());
                
                return box;
            } // break;
            case OUTSIDE: {
                final BlockBoxForBlocks box = new BlockBoxForBlocks(styledElement);
                
                // make block box for marker, then block box for child contents
                final BlockLevelBox markerBox = new BlockListItemMarker(fontSizeAccessor, styledElement);
                box.addChild(markerBox);
                
                fillInBlockChildren(styledElement, box, styledElement.getChildren());
                
                return box;
            } // break;
            default: {
                throw new UnsupportedOperationException("Unknown list-style-position: " + listStylePosition);
            } // break;
            }
        } else {
            // list item has only inline children
            switch (listStylePosition) {
            case INSIDE: {
                final BlockBoxForFlow box = new BlockBoxForFlow(styledElement);
                
                final InlineLevelBox markerBox = new InlineListItemMarker(fontSizeAccessor, styledElement);
                box.addChild(markerBox);
                
                fillInInlineChildren(box, styledElement.getChildren());
                
                return box;
            } // break;
            case OUTSIDE: {
                // make block box for marker and anonymous block box for list
                // item
                final BlockLevelBox markerBox = new BlockListItemMarker(fontSizeAccessor, styledElement);
                
                final BlockBoxForBlocks blockBoxForBlocks = new BlockBoxForBlocks(styledElement);
                blockBoxForBlocks.addChild(markerBox);
                
                fillInBlockChildren(styledElement, blockBoxForBlocks, styledElement.getChildren());
                
                return blockBoxForBlocks;
            } // break;
            default: {
                throw new UnsupportedOperationException("Unknown list-style-position: " + listStylePosition);
            }// break;
            }
        }
    }
    
    public BlockBoxForBlocks createBlockBoxForBlocks(final StyledElement styledBlockElement) {
        assert requiresBlockFormattingContext(styledBlockElement);
        
        final BlockBoxForBlocks box = new BlockBoxForBlocks(styledBlockElement);
        
        fillInBlockChildren(styledBlockElement, box, styledBlockElement.getChildren());
        
        return box;
    }
    
    public BlockBoxForFlow createBlockBoxForFlow(final StyledElement styledElement) {
        assert !requiresBlockFormattingContext(styledElement);
        
        final BlockBoxForFlow box = new BlockBoxForFlow(styledElement);
        
        fillInInlineChildren(box, styledElement.getChildren());
        
        return box;
    }
    
    public InlineLevelBox createInlineLevelBox(final StyledContent styledContent) {
        if (styledContent instanceof StyledElement) {
            final StyledElement styledElement = (StyledElement) styledContent;
            
            final Display display = displayAccessor.getComputedValue(styledElement.getStyleData());
            switch (display) {
            case BLOCK: {
                /*
                 * This means the grandparent block will need to be split
                 * according to the CSS2 spec section 9.2.1.1
                 */
                throw new UnsupportedOperationException();
            } // break;
            case INLINE: {
                final InlineBox inlineBox = new InlineBox(styledElement);
                
                fillInInlineChildren(inlineBox, styledElement.getChildren());
                
                return inlineBox;
            } // break;
            case INLINE_BLOCK: {
                final AtomicLevelInlineBox atomicLevelInlineBox = new AtomicLevelInlineBox(styledElement);
                
                // add children as though this were a block element
                fillInBlockChildren(styledElement, atomicLevelInlineBox, styledElement.getChildren());
                
                return atomicLevelInlineBox;
            } // break;
            case NONE: {
                return null;
            } // break;
            default: {
                System.out.println(display);
                throw new UnsupportedOperationException();
            } // break;
            }
        } else if (styledContent instanceof StyledText) {
            final StyledText styledText = (StyledText) styledContent;
            
            final AnonymousInlineBox anonymousInlineBox = new AnonymousInlineBox(fontSizeAccessor, styledText);
            
            return anonymousInlineBox;
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    private void fillInBlockChildren(final StyledElement styledBlockElement, final BlockFormattingContext box,
            final List<StyledContent> children) {
        final List<StyledContent> runOfInlineContent = new ArrayList<>();
        for (final StyledContent child : children) {
            // dispatch
            if (child instanceof StyledElement) {
                final StyledElement childElement = (StyledElement) child;
                
                final Display childDisplay = displayAccessor.getComputedValue(childElement.getStyleData());
                switch (childDisplay) {
                case INLINE: {
                    runOfInlineContent.add(childElement);
                }
                    break;
                case BLOCK: {
                    if ( !runOfInlineContent.isEmpty()) {
                        // create anonymous block box
                        final AnonymousBlockBox anonymousBlockBox = new AnonymousBlockBox(styledBlockElement);
                        box.addChild(anonymousBlockBox);
                        // make inline box for inline content
                        for (final StyledContent run : runOfInlineContent) {
                            final InlineLevelBox inlineBox = createInlineLevelBox(run);
                            anonymousBlockBox.addChild(inlineBox);
                        }
                        runOfInlineContent.clear();
                    }
                    final BlockLevelBox childBox = createBlockBox(childElement);
                    box.addChild(childBox);
                }
                    break;
                case NONE: {
                }
                    break;
                case LIST_ITEM: {
                    if ( !runOfInlineContent.isEmpty()) {
                        // create anonymous block box
                        final AnonymousBlockBox anonymousBlockBox = new AnonymousBlockBox(styledBlockElement);
                        box.addChild(anonymousBlockBox);
                        // make inline box for inline content
                        for (final StyledContent run : runOfInlineContent) {
                            final InlineLevelBox inlineBox = createInlineLevelBox(run);
                            anonymousBlockBox.addChild(inlineBox);
                        }
                        runOfInlineContent.clear();
                    }
                    final BlockLevelBox childBox = createListItemBox(childElement);
                    box.addChild(childBox);
                }
                    break;
                default: {
                    throw new UnsupportedOperationException("Display: " + childDisplay);
                } // break;
                }
            } else {
                runOfInlineContent.add(child);
            }
        }
        
        if ( !runOfInlineContent.isEmpty()) {
            // make one more anonymous box with the remaining content
            final AnonymousBlockBox anonymousBlockBox = new AnonymousBlockBox(styledBlockElement);
            box.addChild(anonymousBlockBox);
            for (final StyledContent run : runOfInlineContent) {
                final InlineLevelBox inlineBox = createInlineLevelBox(run);
                anonymousBlockBox.addChild(inlineBox);
            }
        }
    }
    
    private void fillInInlineChildren(final InlineFormattingContext box, final List<StyledContent> children) {
        for (final StyledContent child : children) {
            final InlineLevelBox childBox = createInlineLevelBox(child);
            if (childBox != null) {
                box.addChild(childBox);
            }
        }
    }
    
    private boolean requiresBlockFormattingContext(final StyledElement styledElement) {
        for (final StyledContent child : styledElement.getChildren()) {
            if (child instanceof StyledElement) {
                final StyledElement childStyledElement = (StyledElement) child;
                final Display childDisplay = displayAccessor.getComputedValue(childStyledElement.getStyleData());
                if (isBlockLevel(childDisplay)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean isBlockLevel(final Display display) {
        switch (display) {
        case BLOCK:
            return true;
        case LIST_ITEM:
            return true;
        case TABLE:
            return true;
        default:
            return false;
        }
    }
    
    private boolean isInlineLevel(final Display display) {
        switch (display) {
        case INLINE:
            return true;
        case INLINE_TABLE:
            return true;
        case INLINE_BLOCK:
            return true;
        default:
            return false;
        }
    }
    
}
