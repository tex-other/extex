/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter;

import java.io.Serializable;

import de.dante.util.Locator;

/**
 * This class represents a Confitional for a normal \if \else \fi construct.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Conditional implements Serializable {

    /**
     * The field <tt>locator</tt> contains the locator to the position of the
     * opening <tt>\if</tt>.
     */
    private Locator locator;

    /**
     * Creates a new object.
     *
     * @param aLocator the locator
     */
    public Conditional(final Locator aLocator) {

        super();
        this.locator = aLocator;
    }

    /**
     * Getter for the locator of this conditional.
     * The locator points to the initiating <tt>\if</tt>.
     *
     * @return the locator
     */
    public Locator getLocator() {

        return locator;
    }

    /**
     * Getter for the value of the conditional.
     * If it has the value <code>true</code> then the conditional is one of the
     * if-then-else constructs. Otherwise it is a <tt>\ifcase</tt> construction.
     *
     * @return the value
     */
    public boolean getValue() {

        return true;
    }

}