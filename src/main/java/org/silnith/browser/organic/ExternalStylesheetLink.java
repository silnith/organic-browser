package org.silnith.browser.organic;

import java.net.URI;


public class ExternalStylesheetLink {
    
    private final URI href;
    
    private final String rel;
    
    private final String type;
    
    private final String media;
    
    private final String title;
    
    public ExternalStylesheetLink(final URI href, final String rel, final String type, final String media,
            final String title) {
        super();
        this.href = href;
        this.rel = rel;
        this.type = type;
        this.media = media;
        this.title = title;
    }
    
    public URI getHref() {
        return href;
    }
    
    public String getType() {
        return type;
    }
    
    public String getMedia() {
        return media;
    }
    
    public String getName() {
        return title;
    }
    
    public boolean isPersistent() {
        return rel.equals("stylesheet") && title.isEmpty();
    }
    
    public boolean isPreferred() {
        return rel.equals("stylesheet") && !title.isEmpty();
    }
    
    public boolean isAlternate() {
        return rel.equals("alternate stylesheet");
    }
    
}
