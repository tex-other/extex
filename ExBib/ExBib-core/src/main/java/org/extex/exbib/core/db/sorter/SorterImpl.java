/*
 * This file is part of ExBib a BibTeX compatible database.
 * Copyright (C) 2003-2008 Gerd Neugebauer
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package org.extex.exbib.core.db.sorter;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.extex.exbib.core.db.Entry;
import org.extex.framework.configuration.Configuration;
import org.extex.framework.configuration.exception.ConfigurationException;
import org.extex.framework.configuration.exception.ConfigurationMissingException;
import org.extex.framework.configuration.exception.ConfigurationWrapperException;

/**
 * ...
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class SorterImpl implements Comparator<Entry>, Sorter {

    /**
     * The field <tt>collator</tt> contains the collator.
     */
    private Collator collator;

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

        return collator.compare(ka, kb);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.code.AbstractCode#configure(
     *      org.extex.framework.configuration.Configuration)
     */
    public void configure(Configuration config) throws ConfigurationException {

        String order = config.getValue("Order");
        // TODO: use config option locale

        try {
            collator = new RuleBasedCollator(order);
        } catch (ParseException e) {
            throw new ConfigurationWrapperException(e);
        }
    }

    /**
     * Sort the given list
     * 
     * @param list the list to be sorted
     * 
     * @throws ConfigurationMissingException in case of an error
     */
    public void sort(List<Entry> list) throws ConfigurationMissingException {

        if (collator == null) {
            throw new ConfigurationMissingException("SorterImpl");
        }

        Collections.sort(list, this);
    }

}