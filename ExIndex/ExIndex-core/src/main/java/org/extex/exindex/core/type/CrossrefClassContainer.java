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

package org.extex.exindex.core.type;

/**
 * This interface describes a read-only container for cross reference classes.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public interface CrossrefClassContainer {

    /**
     * Define a cross-reference class.
     * 
     * @param name the name of the cross-reference class
     * @param verified the indicator for verified classes
     */
    void defineCrossrefClass(String name, Boolean verified);

    /**
     * Getter for a cross-reference class.
     * 
     * @param name the name of the cross-reference class
     * 
     * @return the indicator for verified cross-reference classes or
     *         <code>null</code> if the cross-reference class is not defined
     */
    Boolean lookupCrossrefClass(String name);

}