/*
 * Copyright (C) 2011 The ExTeX Group and individual authors listed below
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

package org.extex.sitebuilder.main;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.extex.sitebuilder.core.SiteBuilder;

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class SiteBuilderMain {

    /**
     * The field <tt>handler</tt> contains the log handler.
     */
    private static final Handler handler = new Handler() {

        /**
         * {@inheritDoc}
         * 
         * @see java.util.logging.Handler#close()
         */
        @Override
        public void close() throws SecurityException {

            //
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.logging.Handler#flush()
         */
        @Override
        public void flush() {

            //
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
         */
        @Override
        public void publish(LogRecord record) {

            System.err.print(record.getLevel().toString());
            System.err.print(" ");
            System.err.println(record.getMessage());
        }

    };

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Logger.getLogger("org.apache.velocity").setLevel(Level.WARNING);
        SiteBuilder siteBuilder = new SiteBuilder();
        Logger logger = siteBuilder.getLogger();
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        for (Handler h : logger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
        logger.addHandler(handler);
        // siteBuilder.setBaseDir(new File("../www/src/site/html"));
        // siteBuilder.setResourceDir(new File("../www/src/site/resources"));
        siteBuilder.setSiteMap(new File("target/site/sitemap.html"));
        siteBuilder.omit("CVS", ".svn");
        siteBuilder.lib("src/site/velocity/macros.vm");
        try {
            siteBuilder.run();
        } catch (Exception e) {
            logger.throwing(SiteBuilder.class.getName(), "run", e);
            System.exit(-1);
        }
    }

}
