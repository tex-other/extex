/*
 * Copyright (C) 2007 The ExTeX Group
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.extex.backend.documentWriter.itextpdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.extex.backend.documentWriter.DocumentWriter;
import org.extex.backend.documentWriter.DocumentWriterOptions;
import org.extex.backend.documentWriter.SingleDocumentStream;
import org.extex.core.exception.GeneralException;
import org.extex.font.BackendCharacter;
import org.extex.font.BackendFont;
import org.extex.font.BackendFontManager;
import org.extex.font.CoreFontFactory;
import org.extex.font.FontAware;
import org.extex.font.format.AfmMetricFont;
import org.extex.font.manager.ManagerInfo;
import org.extex.framework.AbstractFactory;
import org.extex.framework.configuration.Configurable;
import org.extex.framework.configuration.Configuration;
import org.extex.framework.configuration.exception.ConfigurationException;
import org.extex.framework.configuration.exception.ConfigurationMissingException;
import org.extex.framework.i18n.Localizer;
import org.extex.framework.i18n.LocalizerFactory;
import org.extex.framework.logger.LogEnabled;
import org.extex.resource.ResourceAware;
import org.extex.resource.ResourceFinder;
import org.extex.typesetter.type.NodeList;
import org.extex.typesetter.type.page.Page;

/**
 * Implementation of a pdf document writer with iText. The backend collect first
 * all glyphs and after this, the backend for the pdf creation is called.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class TwoPassDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            Configurable,
            ResourceAware,
            LogEnabled,
            FontAware/* ,PdftexSupport */{

    /**
     * Loader for the target document writer.
     */
    private class Loader extends AbstractFactory {

        /**
         * Returns the document writer.
         * 
         * @param subcfg the configuration.
         * @return the document writer.
         * @throws ConfigurationException from the configuration system.
         */
        public DocumentWriter getInstance(Configuration subcfg)
                throws ConfigurationException {

            return (DocumentWriter) createInstanceForConfiguration(subcfg,
                DocumentWriter.class, DocumentWriterOptions.class, options);
        }

    }

    /**
     * The {@link CoreFontFactory}.
     */
    private CoreFontFactory corefactory;

    /**
     * The document writer options.
     */
    private DocumentWriterOptions options;

    /**
     * The resource finder.
     */
    private ResourceFinder finder;

    /**
     * The field <tt>localizer</tt> contains the localizer. It is initiated
     * with a localizer for the name of this class.
     */
    private Localizer localizer =
            LocalizerFactory.getLocalizer(TwoPassDocumentWriter.class);

    /**
     * The logger.
     */
    private Logger logger;

    /**
     * The {@link BackendFontManager}.
     */
    private BackendFontManager manager;

    /**
     * The collect node visitor.
     */
    private CollectNodeVisitor nodevisitor;

    /**
     * The list for the pages.
     */
    private LinkedList<Page> pagelist = new LinkedList<Page>();

    /**
     * Map for the parameters.
     */
    private Map<String, String> param = new HashMap<String, String>();

    /**
     * the number of page which are shipped out.
     */
    private int shippedPages = 0;

    /**
     * The target {@link DocumentWriter}.
     */
    private DocumentWriter target;

    /**
     * The configuration.
     */
    private Configuration config;

    /**
     * Creates a new object.
     * 
     * @param config The configurations.
     * @param options The options.
     */
    public TwoPassDocumentWriter(Configuration config,
            DocumentWriterOptions options) {

        super();
        this.options = options;
        this.config = config;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.backend.documentWriter.DocumentWriter#close()
     */
    public void close() throws GeneralException, IOException {

        createAfmEncodings();

        if (target instanceof FontAware) {
            FontAware fa = (FontAware) target;
            fa.setFontFactory(corefactory);
        }

        // call the target
        Page page;
        while (pagelist.size() > 0 && (page = pagelist.removeFirst()) != null) {
            target.shipout(page);
        }
        target.close();
    }

    /**
     * Create a dynamic afm encoding.
     */
    private void createAfmEncodings() {

        Iterator<ManagerInfo> it = manager.iterate();
        while (it.hasNext()) {
            ManagerInfo info = it.next();
            BackendFont backendfont = info.getBackendFont();
            if (backendfont instanceof AfmMetricFont && backendfont.isType1()) {
                Iterator<BackendCharacter> charIterator = info.iterate();
                while (charIterator.hasNext()) {
                    BackendCharacter bc = charIterator.next();
                    backendfont.usedCharacter(bc);
                }
            }
        }
    }

    /**
     * The extension.
     */
    private String extension = "pdf";

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.framework.configuration.Configurable#configure(org.extex.framework.configuration.Configuration)
     */
    public void configure(Configuration config) throws ConfigurationException {

        if (config != null) {

            Configuration subcfg =
                    config.findConfiguration("Target", "default");

            String s = config.getAttribute("extension");
            if (s != null) {
                extension = s;
            }

            if (subcfg == null) {
                throw new ConfigurationMissingException(localizer
                    .format("2P.missingTarget"));
            }

            target = new Loader().getInstance(subcfg);
            if (target instanceof ResourceAware) {
                ResourceAware consumer = (ResourceAware) target;
                consumer.setResourceFinder(finder);
            }
            if (target instanceof LogEnabled) {
                LogEnabled logen = (LogEnabled) target;
                logen.enableLogging(logger);
            }
            // if (target instanceof FontAware) {
            // FontAware fa = (FontAware) target;
            // fa.setFontFactory(corefactory);
            // }
            // TODO mgn: incomplete
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.framework.logger.LogEnabled#enableLogging(java.util.logging.Logger)
     */
    public void enableLogging(Logger logger) {

        this.logger = logger;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return extension;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.font.FontAware#setFontFactory(org.extex.font.CoreFontFactory)
     */
    public void setFontFactory(CoreFontFactory factory) {

        corefactory = factory;
        checkTarget();
        List<String> sl = new ArrayList<String>();
        sl.add("ttf");
        sl.add("afm");
        sl.add("tfm");
        manager = corefactory.createManager(sl);

    }

    /**
     * Check, if the target exists.
     */
    private void checkTarget() {

        if (target == null) {
            configure(config);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.backend.documentWriter.SingleDocumentStream#setOutputStream(java.io.OutputStream)
     */
    public void setOutputStream(OutputStream writer) {

        checkTarget();
        if (target instanceof SingleDocumentStream) {
            SingleDocumentStream ss = (SingleDocumentStream) target;
            ss.setOutputStream(writer);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.backend.documentWriter.DocumentWriter#setParameter(java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(String name, String value) {

        param.put(name, value);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.resource.ResourceAware#setResourceFinder(org.extex.resource.ResourceFinder)
     */
    public void setResourceFinder(ResourceFinder finder) {

        this.finder = finder;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.backend.documentWriter.DocumentWriter#shipout(org.extex.typesetter.type.page.Page)
     */
    public int shipout(Page page) throws GeneralException, IOException {

        checkTarget();
        if (nodevisitor == null) {
            nodevisitor = new CollectNodeVisitor(manager);
        }
        shippedPages++;

        pagelist.addLast(page);
        NodeList nodes = page.getNodes();
        nodes.visit(nodevisitor, nodes);

        return shippedPages;
    }
}