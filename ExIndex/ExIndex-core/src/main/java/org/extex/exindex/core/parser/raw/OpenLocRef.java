/*
 * Copyright (C) 2007-2008 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.core.parser.raw;

import java.util.List;
import java.util.logging.Logger;

import org.extex.exindex.core.Indexer;
import org.extex.exindex.core.type.CrossrefClassContainer;
import org.extex.exindex.core.type.attribute.AttributesContainer;

/**
 * This interface describes an open location specification.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:6617 $
 */
public class OpenLocRef extends LocRef {

    /**
     * Creates a new object.
     * 
     * @param location the location
     * @param layer the layer
     */
    public OpenLocRef(String location, String layer) {

        super(location, layer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exindex.core.parser.raw.LocRef#check(
     *      java.util.logging.Logger,
     *      org.extex.exindex.core.parser.raw.RawIndexentry,
     *      org.extex.exindex.core.Indexer,
     *      org.extex.exindex.core.type.CrossrefClassContainer, java.util.List,
     *      org.extex.exindex.core.type.attribute.AttributesContainer)
     */
    @Override
    public boolean check(Logger logger, RawIndexentry entry, Indexer index,
            CrossrefClassContainer crossrefClass, List<OpenLocRef> openPages,
            AttributesContainer attributes) {

        openPages.add(this);
        return super.check(logger, entry, index, crossrefClass, openPages,
            attributes);
    }
}
