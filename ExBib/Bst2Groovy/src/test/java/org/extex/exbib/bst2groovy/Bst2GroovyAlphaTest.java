/*
 * Copyright (C) 2008 The ExTeX Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.extex.exbib.bst2groovy;

import java.io.OutputStreamWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.extex.framework.configuration.ConfigurationFactory;
import org.extex.resource.ResourceFinder;
import org.extex.resource.ResourceFinderFactory;
import org.junit.Test;

/**
 * This is a test suite for Bst2Groovy.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Bst2GroovyAlphaTest {

    /**
     * <testcase> ... </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void test1() throws Exception {

        Logger logger = Logger.getLogger(getClass().getName());
        logger.setLevel(Level.SEVERE);
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
        try {
            ResourceFinder finder =
                    new ResourceFinderFactory().createResourceFinder(
                        ConfigurationFactory
                            .newInstance("config/path/testFinder"), logger,
                        System.getProperties(), null);
            // finder.enableTracing(true);

            Bst2Groovy bst2Groovy = new Bst2Groovy();
            bst2Groovy.setResourceFinder(finder);
            OutputStreamWriter w = new OutputStreamWriter(System.out);
            bst2Groovy.run(w, "alpha.bst");
            w.flush();
        } finally {
            logger.removeHandler(handler);
        }
    }
}
