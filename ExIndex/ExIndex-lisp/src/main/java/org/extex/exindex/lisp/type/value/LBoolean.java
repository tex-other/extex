/*
 * Copyright (C) 2007 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.lisp.type.value;

import java.io.PrintStream;

/**
 * This class is a node containing a boolean.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class LBoolean implements LValue {

    /**
     * The field <tt>TRUE</tt> contains the constant for true.
     */
    public static final LBoolean TRUE = new LBoolean(true);

    /**
     * The field <tt>FALSE</tt> contains the constant for false.
     */
    public static final LBoolean FALSE = new LBoolean(false);

    /**
     * Get the appropriate instance for a boolean value.
     * 
     * @param value the boolean value
     * 
     * @return the corresponding LBoolean
     */
    public static LBoolean valueOf(boolean value) {

        return value ? TRUE : FALSE;
    }

    /**
     * The field <tt>value</tt> contains the value.
     */
    private boolean value;

    /**
     * Creates a new object.
     * 
     * @param value the value
     */
    private LBoolean(boolean value) {

        super();
        this.value = value;
    }

    /**
     * Getter for value.
     * 
     * @return the value
     */
    public boolean booleanValue() {

        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exindex.lisp.type.value.LValue#print(java.io.PrintStream)
     */
    public void print(PrintStream stream) {

        stream.print(value ? "t" : "nil");
    }

}