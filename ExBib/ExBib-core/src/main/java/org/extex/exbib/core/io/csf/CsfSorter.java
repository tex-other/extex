/*
 * Copyright (C) 2008-2009 The ExTeX Group and individual authors listed below
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

package org.extex.exbib.core.io.csf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.extex.exbib.core.ExBib;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.db.sorter.Sorter;
import org.extex.exbib.core.db.sorter.Startable;
import org.extex.exbib.core.exceptions.ExBibCsfNotFoundException;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.framework.configuration.exception.ConfigurationException;
import org.extex.resource.PropertyAware;
import org.extex.resource.ResourceAware;
import org.extex.resource.ResourceFinder;

/**
 * This is a sorter which used a csf file to read a configuration from.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class CsfSorter
        implements
            Comparator<Entry>,
            Sorter,
            Serializable,
            ResourceAware,
            PropertyAware,
            Startable {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    private static final long serialVersionUID = 2008L;

    /**
     * The field <tt>ord</tt> contains the order mapping.
     */
    private int[] ord = new int[256];

    /**
     * The field <tt>upper</tt> contains the uppercase mapping.
     */
    private char[] upper = new char[256];

    /**
     * The field <tt>lower</tt> contains the lowercase mapping.
     */
    private char[] lower = new char[256];

    /**
     * The field <tt>resource</tt> contains the name of the resource to read.
     */
    private String resource = null;

    /**
     * The field <tt>finder</tt> contains the resource finder.
     */
    private ResourceFinder finder;

    /**
     * The field <tt>properties</tt> contains the properties.
     */
    private Properties properties;

    /**
     * Creates a new object.
     */
    public CsfSorter() {

        for (int i = 0; i < 128; i++) {
            ord[i] = i;
            lower[i] = (char) i;
            upper[i] = (char) i;
        }
        for (int i = 128; i < 256; i++) {
            ord[i] = Integer.MAX_VALUE;
            lower[i] = (char) i;
            upper[i] = (char) i;
        }
    }

    /**
     * Creates a new object.
     * 
     * @param resource the name of the resource to read
     */
    public CsfSorter(String resource) {

        this();
        this.resource = resource;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Entry a, Entry b) {

        String ka = a.getSortKey();

        if (ka == null) {
            ka = a.getKey();
        }

        String kb = b.getSortKey();

        if (kb == null) {
            kb = b.getKey();
        }

        int ia = 0;
        int ib = 0;
        int ca;
        int cb;
        do {
            for (ca = -1; ia < ka.length() && ca < 0; ia++) {
                ca = ord[ka.charAt(ia)];
            }
            for (cb = -1; ib < kb.length() && cb < 0; ib++) {
                cb = ord[kb.charAt(ib)];
            }
        } while (ca >= 0 && ca == cb);

        return ca - cb;
    }

    /**
     * Get the lower counterpart for a character. If a character has no such
     * counterpart the character itself is returned.
     * 
     * @param c the character
     * 
     * @return the lower case character
     */
    int getLower(char c) {

        return lower[c];
    }

    /**
     * Get the upper counterpart for a character. If a character has no such
     * counterpart the character itself is returned.
     * 
     * @param c the character
     * 
     * @return the uppercase character
     */
    int getUpper(char c) {

        return upper[c];
    }

    /**
     * Setter for the lowercase counterpart of a uppercase character.
     * 
     * @param uc the uppercase character
     * @param lc the lowercase character
     */
    public void setLower(char uc, char lc) {

        lower[uc] = lc;
    }

    /**
     * Setter for the order mapping.
     * 
     * @param c the character
     * @param on the ordinal number
     */
    public void setOrder(char c, int on) {

        ord[c] = on;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.resource.PropertyAware#setProperties(java.util.Properties)
     */
    public void setProperties(Properties properties) {

        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.resource.ResourceAware#setResourceFinder(org.extex.resource.ResourceFinder)
     */
    public void setResourceFinder(ResourceFinder f) {

        this.finder = f;
    }

    /**
     * Setter for the uppercase counterpart of a lowercase character.
     * 
     * @param lc the lowercase character
     * @param uc the uppercase character
     */
    public void setUpper(char lc, char uc) {

        upper[lc] = uc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.db.sorter.Sorter#sort(java.util.List)
     */
    public void sort(List<Entry> list) throws ConfigurationException {

        Collections.sort(list, this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.db.sorter.Startable#start()
     */
    public void start() throws ExBibException {

        if (resource == null || resource.equals("")) {
            return;
        }

        InputStream is = finder.findResource(resource, "csf");
        if (is == null) {
            throw new ExBibCsfNotFoundException(resource);
        }
        try {
            try {
                String encoding =
                        properties.getProperty(ExBib.PROP_CSF_ENCODING);
                new CsfReader().read(encoding == null //
                        ? new InputStreamReader(is)
                        : new InputStreamReader(is, encoding), this);
            } finally {
                is.close();
            }
        } catch (IOException e) {
            throw new ExBibException(e);
        }

    }

    /**
     * Translate a character to upper case.
     * 
     * @param c the input character
     * 
     * @return the lowered character or c itself
     */
    public char toLowerCase(char c) {

        return lower[c];
    }

    /**
     * Translate a character to lower case.
     * 
     * @param c the input character
     * 
     * @return the uppered character or c itself
     */
    public char toUpperCase(char c) {

        return upper[c];
    }

}
