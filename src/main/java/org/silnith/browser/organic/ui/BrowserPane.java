package org.silnith.browser.organic.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.silnith.browser.organic.BoxRenderer;
import org.silnith.browser.organic.CSSPseudoElementRuleSet;
import org.silnith.browser.organic.CSSRule;
import org.silnith.browser.organic.CascadeApplier;
import org.silnith.browser.organic.StyleTreeBuilder;
import org.silnith.browser.organic.StyledContent;
import org.silnith.browser.organic.StyledDOMElement;
import org.silnith.browser.organic.StyledElement;
import org.silnith.browser.organic.Stylesheet;
import org.silnith.browser.organic.StylesheetBuilder;
import org.silnith.browser.organic.box.BlockLevelBox;
import org.silnith.browser.organic.box.Formatter;
import org.silnith.browser.organic.network.Download;
import org.silnith.browser.organic.network.DownloadManager;
import org.silnith.browser.organic.parser.DocumentParser;
import org.silnith.browser.organic.parser.StyleParser;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.Display;
import org.silnith.css.model.data.ListStylePosition;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;


public class BrowserPane extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
//	private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    
    private final DownloadManager downloadManager;
    
    private final DocumentParser documentParser;
    
    private final StyleParser styleParser;
    
    private final JTextField urlBar;
    
    private final JScrollPane scrollPane;
    
    private BoxRenderer boxRenderer;
    
    private Thread monitorThread;
    
    public BrowserPane(final DownloadManager downloadManager, final DocumentParser documentParser,
            final StyleParser styleParser) {
        super(new BorderLayout());
        this.downloadManager = downloadManager;
        this.documentParser = documentParser;
        this.styleParser = styleParser;
        this.boxRenderer = null;
        this.monitorThread = null;
        
        this.scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        BrowserPane.this.add(scrollPane, BorderLayout.CENTER);
        
        this.urlBar = new JTextField();
        this.urlBar.setText("http://www.w3.org/");
//		this.urlBar.setText("http://motherfuckingwebsite.com/");
        BrowserPane.this.add(urlBar, BorderLayout.NORTH);
        urlBar.addActionListener(new UrlBarListener());
        
        scrollPane.setPreferredSize(new Dimension(800, 600));
    }
    
    private class Monitor implements Runnable {
        
        private final String urlText;
        
        public Monitor(final String urlText) {
            super();
            this.urlText = urlText;
        }
        
        private <T> T getResult(final String operation, final Callable<T> callable)
                throws InterruptedException, ExecutionException {
            final TimedCallable<T> timedCallable = new TimedCallable<>(callable);
//            final Future<T> future = EXECUTOR_SERVICE.submit(timedCallable);
//            final T result = future.get();
            final T result;
            try {
                result = timedCallable.call();
            } catch (final Exception e) {
                e.printStackTrace();
                return null;
            }
            System.out.println(operation + " duration: " + timedCallable.getDuration() + "ms");
            return result;
        }
        
        private void printNode(final Node node) {
            try {
                final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
                final DOMImplementation domImpl = registry.getDOMImplementation("Core 3.0 +LS 3.0");
                final DOMImplementationLS domImplLS = (DOMImplementationLS) domImpl.getFeature("+LS", "3.0");
                final LSSerializer serializer = domImplLS.createLSSerializer();
                serializer.getDomConfig().setParameter("format-pretty-print", true);
                final LSOutput output = domImplLS.createLSOutput();
                output.setByteStream(System.out);
                serializer.write(node, output);
            } catch (final DOMException e) {
                e.printStackTrace();
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            } catch (final InstantiationException e) {
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            } catch (final ClassCastException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            final URL url;
            try {
                url = new URL(urlText);
            } catch (final MalformedURLException ex) {
                ex.printStackTrace();
                return;
            }
            
            try {
                final Downloader downloader = new Downloader(url);
                final Download download = getResult("Download", downloader);
                
                final Parser parser = new Parser(download);
                final Document document = getResult("Parse", parser);
                
                printNode(document);
                
                final Styler styler = new Styler(new StyleTreeBuilder(), document);
                final StyledElement styledElement = getResult("Style", styler);
                
                final StylesheetBuilder stylesheetBuilder = new StylesheetBuilder();
                final URI uri = url.toURI();
                final Collection<Stylesheet> stylesheets = stylesheetBuilder.buildStylesheets(document, uri);
                
                final PropertyAccessorFactory propertyAccessorFactory = new PropertyAccessorFactory();
                final CascadeApplier cascadeApplier = new CascadeApplier(propertyAccessorFactory);
                final Cascader cascader = new Cascader(cascadeApplier, styledElement, stylesheets);
                getResult("Cascade", cascader);
                
//                printNode(styledElement.createDOM(document));
                
                @SuppressWarnings("unchecked")
                final PropertyAccessor<Display> displayAccessor =
                        (PropertyAccessor<Display>) propertyAccessorFactory.getPropertyAccessor(PropertyName.DISPLAY);
                @SuppressWarnings("unchecked")
                final PropertyAccessor<AbsoluteLength> fontSizeAccessor =
                        (PropertyAccessor<AbsoluteLength>) propertyAccessorFactory.getPropertyAccessor(
                                PropertyName.FONT_SIZE);
                @SuppressWarnings("unchecked")
                final PropertyAccessor<ListStylePosition> listStylePositionAccessor =
                        (PropertyAccessor<ListStylePosition>) propertyAccessorFactory.getPropertyAccessor(
                                PropertyName.LIST_STYLE_POSITION);
                final Formatter formatter = new Formatter(displayAccessor, fontSizeAccessor, listStylePositionAccessor);
                final Formatter2 formatter2 = new Formatter2(formatter, styledElement);
                final BlockLevelBox blockBox = getResult("Format", formatter2);
                
//                printNode(blockBox.createDOM(document));
                
                final BoxRenderer renderer = new BoxRenderer(blockBox);
                scrollPane.getViewport().removeChangeListener(boxRenderer);
                boxRenderer = renderer;
                scrollPane.setViewportView(boxRenderer);
                scrollPane.getViewport().addChangeListener(boxRenderer);
            } catch (final ExecutionException e) {
                e.printStackTrace();
                return;
            } catch (final InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private class Downloader implements Callable<Download> {
        
        private final URL url;
        
        public Downloader(final URL url) {
            super();
            if (url == null) {
                throw new NullPointerException();
            }
            this.url = url;
        }
        
        @Override
        public Download call() throws Exception {
            final Download download = downloadManager.download(url);
            download.connect();
            download.download();
            return download;
        }
        
    }
    
    private class Parser implements Callable<Document> {
        
        private final Download download;
        
        public Parser(final Download download) {
            super();
            this.download = download;
        }
        
        @Override
        public Document call() throws Exception {
            return documentParser.parseDocument(download);
//			return documentParser.createDocument(download);
        }
        
    }
    
    private class Styler implements Callable<StyledElement> {
        
        private final StyleTreeBuilder styleTreeBuilder;
        
        private final Document document;
        
        public Styler(final StyleTreeBuilder styleTreeBuilder, final Document document) {
            super();
            this.styleTreeBuilder = styleTreeBuilder;
            this.document = document;
        }
        
        @Override
        public StyledElement call() throws Exception {
            return styleTreeBuilder.addStyleInformation(document);
        }
        
    }
    
    private class Cascader implements Runnable, Callable<Object> {
        
        private final CascadeApplier cascadeApplier;
        
        private final StyledElement styledElement;
        
        private final Collection<Stylesheet> stylesheets;
        
        public Cascader(final CascadeApplier cascadeApplier, final StyledElement styledElement,
                final Collection<Stylesheet> stylesheets) {
            super();
            this.cascadeApplier = cascadeApplier;
            this.styledElement = styledElement;
            this.stylesheets = stylesheets;
        }
        
        @Override
        public void run() {
            final Collection<StyledDOMElement> allNodes = new HashSet<>();
            final StyledDOMElement styledDOMElement = (StyledDOMElement) styledElement;
            allNodes.add(styledDOMElement);
            allNodes.addAll(getChildren(styledDOMElement));
            for (final Stylesheet stylesheet : stylesheets) {
                for (final CSSRule cssRule : stylesheet.getRules()) {
//                    cssRule.apply(styledElement);
                    cssRule.apply(allNodes);
                }
                cascadeApplier.cascade(styledElement, stylesheet);
            }
        }
        
        private Collection<StyledDOMElement> getChildren(final StyledDOMElement element) {
            final Collection<StyledDOMElement> ret = new HashSet<>();
            for (final StyledContent child : element.getChildren()) {
                if (child instanceof StyledDOMElement) {
                    final StyledDOMElement styledChild = (StyledDOMElement) child;
                    ret.add(styledChild);
                    ret.addAll(getChildren(styledChild));
                }
            }
            return ret;
        }
        
        @Override
        public Object call() throws Exception {
            run();
            return null;
        }
        
    }
    
    private class Formatter2 implements Callable<BlockLevelBox> {
        
        private final Formatter formatter;
        
        private final StyledElement styledElement;
        
        public Formatter2(final Formatter formatter, final StyledElement styledElement) {
            super();
            this.styledElement = styledElement;
            this.formatter = formatter;
        }
        
        @Override
        public BlockLevelBox call() throws Exception {
            return formatter.createBlockBox(styledElement);
        }
        
    }
    
    private class UrlBarListener implements ActionListener {
        
        private UrlBarListener() {
            super();
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            System.out.println(e);
            final String urlText = urlBar.getText();
            
            final Monitor monitor = new Monitor(urlText);
            
            monitorThread = new Thread(monitor);
            monitorThread.start();
        }
        
    }
    
}
