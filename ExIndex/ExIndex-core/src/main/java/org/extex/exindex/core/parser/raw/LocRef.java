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
import org.extex.exindex.core.util.StringUtils;
import org.extex.framework.i18n.LocalizerFactory;

/**
 * This interface describes a location specification.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:6617 $
 */
public class LocRef implements RefSpec {

    /**
     * The field <tt>location</tt> contains the location.
     */
    private String location;

    /**
     * The field <tt>layer</tt> contains the layer.
     */
    private String layer;

    /**
     * Creates a new object.
     * 
     * @param location the location
     * @param layer the layer
     */
    public LocRef(String location, String layer) {

        super();
        this.location = location;
        this.layer = layer;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exindex.core.parser.raw.LocRef#check(
     *      java.util.logging.Logger,
     *      org.extex.exindex.core.parser.raw.RawIndexentry,
     *      org.extex.exindex.core.Indexer,
     *      org.extex.exindex.core.type.CrossrefClassContainer,
     *      java.util.List,
     *      org.extex.exindex.core.type.attribute.AttributesContainer)
     */
    public boolean check(Logger logger, RawIndexentry entry, Indexer index,
            CrossrefClassContainer crossrefClass, List<OpenLocRef> openPages,
            AttributesContainer attributes) {

        String attr = entry.getRef().getLayer();
        if (attr != null && !attributes.isAttributeDefined(attr)) {
            logger.severe(LocalizerFactory.getLocalizer(Indexer.class).format(
                "AttributeUnknown", attr));
            return false;
        }

        // TODO gene: check unimplemented
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exindex.core.parser.raw.RefSpec#getLayer()
     */
    public String getLayer() {

        return layer;
    }

    /**
     * Getter for the location.
     * 
     * @return the location
     */
    public String getLocation() {

        return location;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        StringUtils.putPrintable(sb, location);
        return sb.toString();
    }

}
