/*
 * Copyright (C) 2007-2011 The ExTeX Group and individual authors listed below
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

package org.extex.scanner.api.exception;

import org.extex.core.UnicodeChar;

/**
 * This exception signals that an invalid character has been encountered.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:5563 $
 */
public class InvalidCharacterScannerException extends ScannerException {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    private static final long serialVersionUID = 2007L;

    /**
     * The field <tt>c</tt> contains the invalid character.
     */
    private UnicodeChar c;

    /**
     * Creates a new object.
     * 
     * @param c the character
     */
    public InvalidCharacterScannerException(UnicodeChar c) {

        this.c = c;
    }

    /**
     * Getter for the invalid character.
     * 
     * @return the invalid character
     */
    public UnicodeChar getC() {

        return c;
    }

    /**
     * Creates a localized description of this throwable.
     * 
     * @return the localized description of this throwable
     * 
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    @Override
    public String getLocalizedMessage() {

        String s;
        if (c == null) {
            s = "";
        } else if (c.isPrintable()) {
            s = c.toString();
        } else {
            s = "^^" + Integer.toString(c.getCodePoint());
        }
        return getLocalizer().format("Text", s);
    }

}
