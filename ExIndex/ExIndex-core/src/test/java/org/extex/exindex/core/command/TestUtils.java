/*
 * Copyright (C) 2008 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.core.command;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.extex.exindex.core.Indexer;
import org.extex.exindex.core.type.IndexContainer;
import org.extex.logging.LogFormatter;

/**
 * This is a utility class for the tests.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public final class TestUtils {

    /**
     * Create a proper logger.
     * 
     * @return the logger
     */
    public static Logger makeLogger() {

        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.SEVERE);
        logger.setUseParentHandlers(false);
        return logger;
    }

    /**
     * Create a proper logger.
     * 
     * @return the logger
     */
    public static Logger makeLogger(ByteArrayOutputStream log) {

        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.SEVERE);
        logger.setUseParentHandlers(false);
        Handler handler = new StreamHandler(log, new LogFormatter());
        handler.setLevel(Level.WARNING);
        logger.addHandler(handler);
        return logger;
    }

    /**
     * Run a test. This means load some configuration instruction into an
     * Indexer.
     * 
     * @param in the option string
     * 
     * @return the function binding for define-attributes
     * 
     * @throws Exception in case of an error
     */
    public static IndexContainer runTest(String in) throws Exception {

        Indexer indexer = new Indexer();
        assertNotNull(indexer);
        indexer.load(new StringReader(in), "<reader>");
        IndexContainer container = indexer.getContainer();
        assertNotNull(container);
        return container;
    }

    /**
     * Creates a new object.
     */
    private TestUtils() {

        // unused
    }

}