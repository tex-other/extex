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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.extex.exbib.core.db.Entry;
import org.extex.framework.configuration.Configuration;

/**
 * This class provides a Sorter compatible to the BibT<sub>E</sub>X 0.99c
 * sorting routine. The sorting order is determined by the byte order of the
 * internal representation of the sorting key. Thus accented characters are
 * located behind all not accented characters.
 * 
 * <p>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 * </p>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Sorter099Impl implements Comparator<Entry>, Sorter {

    /**
     * Provide a Comparator which just uses the appropriate keys and compares
     * them without respect to the case.
     * 
     * @param a the first entry
     * @param b the second entry
     * 
     * @return ...
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

        return ka.compareToIgnoreCase(kb);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.code.AbstractCode#configure(
     *      org.extex.framework.configuration.Configuration)
     */
    public void configure(Configuration config) {

        //
    }

    /**
     * Sort the given List according to the predefined order. As a side effect
     * the list is modified to reflect the new order.
     * 
     * @param list the list to sort
     */
    public void sort(List<Entry> list) {

        Collections.sort(list, this);
    }

}