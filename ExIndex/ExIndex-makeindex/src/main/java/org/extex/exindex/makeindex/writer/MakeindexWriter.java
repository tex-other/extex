/*
 * Copyright (C) 2007-2009 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.makeindex.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;

import org.extex.exindex.core.type.page.PageReference;
import org.extex.exindex.makeindex.Entry;
import org.extex.exindex.makeindex.Parameters;

/**
 * This class is an index writer for the makeindex emulator.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:6666 $
 */
public class MakeindexWriter implements IndexWriter {

    /**
     * The field <tt>params</tt> contains the parameters.
     */
    private Parameters params;

    /**
     * The field <tt>writer</tt> contains the writer.
     */
    private Writer writer;

    /**
     * Creates a new object.
     * 
     * @param writer the writer
     * @param params the parameters
     * 
     * @throws IOException in case of an I/O error
     */
    public MakeindexWriter(Writer writer, Parameters params) throws IOException {

        this.writer = writer;
        this.params = params;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exindex.makeindex.writer.IndexWriter#write(java.util.List,
     *      java.util.logging.Logger, java.lang.String)
     */
    public int[] write(List<Entry> entries, Logger logger, String startPage)
            throws IOException {

        final String item0 = params.getString("index:item_0");
        final String item1 = params.getString("index:item_1");
        final String delim0 = params.getString("index:delim_0");
        final String delim1 = params.getString("index:delim_1");
        final String encapPrefix = params.getString("index:encap-prefix");
        final String encapInfix = params.getString("index:encap-infix");
        final String encalSuffix = params.getString("index:encap-suffix");
        final long headingFlag = params.getNumber("index:headings-flag");
        int[] count = new int[2];
        char currentHeading = '\0';

        writer.write(params.getString("markup:index-open"));

        if (startPage != null) {
            writer.write(params.getString("markup:setpage-prefix"));
            writer.write(startPage);
            writer.write(params.getString("markup:setpage-suffix"));
        }

        for (Entry e : entries) {
            if (headingFlag != 0 && e.getHeading() != currentHeading) {
                currentHeading = writeHeading(headingFlag, e);
            }
            writer.write(item0);
            writer.write(e.getValue());
            List<PageReference> pages = e.getPages();
            boolean first = true;

            for (PageReference page : pages) {
                if (first) {
                    first = false;
                    writer.write(delim0);
                } else {
                    writer.write(delim1);
                }
                String encap = page.getEncap();
                if (encap != null) {
                    writer.write(encapPrefix);
                    writer.write(encap);
                    writer.write(encapInfix);
                    writer.write(page.getPage());
                    writer.write(encalSuffix);
                } else {
                    writer.write(page.getPage());
                }
            }
        }

        writer.write(params.getString("markup:index-close"));
        return count;
    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param headingFlag the heading flag
     * @param e the entry
     * 
     * @return
     * 
     * @throws IOException in case of an I/O error
     */
    private char writeHeading(final long headingFlag, Entry e)
            throws IOException {

        char heading = e.getHeading();
        writer.write(params.getString("markup:heading-prefix"));
        if (headingFlag > 0) {
            if (heading == ' ') {
                writer.write(params.getString("markup:symhead-positive"));
            } else if (heading == '1') {
                writer.write(params.getString("markup:numhead-positive"));
            } else {
                writer.write(Character.toUpperCase(heading));
            }
        } else if (heading == ' ') {
            writer.write(params.getString("markup:symhead-negative"));
        } else if (heading == '1') {
            writer.write(params.getString("markup:numhead-negative"));
        } else {
            writer.write(Character.toLowerCase(heading));
        }
        writer.write(params.getString("markup:heading-suffix"));
        return heading;
    }

}
