/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.util.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import de.dante.util.StringList;
import de.dante.util.StringListIterator;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;

/**
 * This file finder searches for the file in different directories and with
 * several extensions.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class FileFinder implements ResourceFinder, LoggerTaker, PropertiesTaker {

    /**
     * The constant <tt>EXTENSION_TAG</tt> contains the name of the tag to get
     * the possible extensions.
     */
    private static final String EXTENSION_TAG = "extension";

    /**
     * The constant <tt>PATH_TAG</tt> contains the name of the tag to get the
     * path information.
     */
    private static final String PATH_TAG = "path";

    /**
     * The field <tt>config</tt> contains the configuration object on which this
     * file finder is based.
     */
    private Configuration config;

    /**
     * The field <tt>logger</tt> contains the logger to be used for tracing.
     */
    private Logger logger = null;

    /**
     * The field <tt>properties</tt> contains the ...
     */
    private Properties properties = null;

    /**
     * The field <tt>trace</tt> contains the indicator that tracing is required.
     * This field is set to <code>true</code> according to the configuration.
     */
    private boolean trace = false;

    /**
     * Creates a new object.
     *
     * @param configuration the encapsulated configuration object
     */
    public FileFinder(final Configuration configuration) {

        super();
        this.config = configuration;
        String t = configuration.getAttribute("trace");
        if (t != null && Boolean.valueOf(t).booleanValue()) {
            trace = true;
        }
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTrace(boolean)
     */
    public void enableTrace(final boolean flag) {

        trace = flag;
    }

    /**
     * Try to find a file by adding extensions.
     *
     * @param name the name of the file to find
     * @param path the path of the file to find
     * @param cfg the configuration
     *
     * @return the input stream for the file or <code>null</code> if none was
     *  found.
     */
    private InputStream find(final String name, final String path,
            final Configuration cfg) {

        InputStream stream;
        StringListIterator extIt = cfg.getValues(EXTENSION_TAG).getIterator();

        while (extIt.hasNext()) {
            String ext = extIt.next();
            File file = new File(path, name + ext);

            if (trace && logger != null) {
                logger.fine("FileFinder: Try " + file.toString() + "\n");
            }
            if (file.canRead()) {
                try {
                    stream = new FileInputStream(file);
                    if (trace && logger != null) {
                        logger.fine("FileFinder: Found " + file.toString()
                                + "\n");
                    }
                    return stream;
                } catch (FileNotFoundException e) {
                    // Ignore unreadable files.
                    // This should not happen since it has already been
                    // tested before.
                    logger.fine("FileFinder: Not found " + file.toString()
                            + "\n");
                }
            }
        }
        return null;
    }

    /**
     * Try to find a file on some paths by adding extensions.
     *
     * @param name the name of the file to find
     * @param paths a list of paths to explore
     * @param cfg the configuration
     *
     * @return the input stream for the file or <code>null</code> if none was
     *  found.
     */
    private InputStream find(final String name, final StringList paths,
            final Configuration cfg) {

        InputStream stream = null;
        StringListIterator iterator = paths.getIterator();

        while (stream == null && iterator.hasNext()) {
            String p = iterator.next();
            stream = find(name, p, cfg);
        }

        return stream;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        if (trace && logger != null) {
            logger.fine("FileFinder: Searching " + name + " [" + type + "]\n");
        }

        InputStream stream = null;
        Configuration cfg = config.findConfiguration(type);
        if (cfg == null) {
            String t = config.getAttribute("default");
            if (t == null) {
                throw new ConfigurationMissingAttributeException("default",
                        config);
            }
            cfg = config.getConfiguration(t);

            if (trace && logger != null) {
                logger.fine("FileFinder: " + type + " not found; Using default"
                        + t + ".\n");
            }
        }

        Iterator iterator = cfg.iterator(PATH_TAG);
        while (stream == null && iterator.hasNext()) {
            Configuration c = (Configuration) iterator.next();
            String prop = c.getAttribute("property");
            if (prop != null) {
                String path = properties.getProperty(prop);
                if (path != null) {
                    stream = find(name, new StringList(path, System
                            .getProperty("path.separator", ":")), cfg);
                }
            } else {
                stream = find(name, c.getValue(), cfg);
            }
        }

        if (trace && logger != null && stream == null) {
            logger.fine("FileFinder: Failed for " + name + "\n");
        }

        return stream;
    }

    /**
     * Getter for logger.
     *
     * @return the logger.
     */
    public Logger getLogger() {

        return this.logger;
    }

    /**
     * Setter for logger.
     *
     * @param logger the logger to set.
     */
    public void setLogger(final Logger logger) {

        this.logger = logger;
    }

    /**
     * @see de.dante.util.resource.PropertiesTaker#setProperties(java.util.Properties)
     */
    public void setProperties(final Properties properties) {

        this.properties = properties;
    }
}