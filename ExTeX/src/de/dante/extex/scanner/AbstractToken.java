/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.scanner;

import de.dante.util.UnicodeChar;

/**
 * This is the abstract base class for all Tokens.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public abstract class AbstractToken implements Token {

    /**
     * The field <tt>value</tt> contains the string value. In case that the
     * token has a single character value only this may also be
     * <code>null</code>
     * or it is used to cache the string representation of the character.
     */
    private String value = null;

    /**
     * The field <tt>uniCode</tt> ...
     */
    private UnicodeChar uniCode = null;

    /**
     * Creates a new object.
     *
     * @param aValue the value of the token
     */
    protected AbstractToken(final String aValue) {
        super();
        this.value = aValue;
    }

    /**
     * Creates a new object.
     *
     * @param uc the value of the token
     */
    protected AbstractToken(final UnicodeChar uc) {
        super();
        this.uniCode = uc;
    }

    /**
     * This abstract class forces a derived class to overwrite this definition.
     *
     * @see de.dante.extex.scanner.Token#getCatcode()
     */
    public abstract Catcode getCatcode();

    /**
     * Return the printable representation of this object.
     *
     * @return the printable representation
     */
    public abstract String toString();

    /**
     * Return the text representation of this object.
     *
     * @return the text representation
     */
    public String toText() {

        if (value == null) {
            value = uniCode.toString();
        }
        return value;
    }

    /**
     * @see de.dante.extex.scanner.Token#getChar()
     */
    public UnicodeChar getChar() {

        return uniCode;
    }

    /**
     * @see de.dante.extex.scanner.Token#getValue()
     */
    public String getValue() {

        if (value == null) {
            value = uniCode.toString();
        }
        return value;
    }

    /**
     * @see de.dante.extex.scanner.Token#equals(de.dante.extex.scanner.Token)
     */
    public boolean equals(final Token t) {

        return this == t
               || (getCatcode() == t.getCatcode() && getValue()
                       .equals(t.getValue()));
    }

    /**
     * @see de.dante.extex.scanner.Token#equals(Catcode,String)
     */
    public boolean equals(final Catcode cc, final String s) {

        return getCatcode() == cc && getValue().equals(s);
    }

    /**
     * @see de.dante.extex.scanner.Token#equals(de.dante.extex.scanner.Catcode,
     *      char)
     */
    public boolean equals(final Catcode cc, final char c) {

        return getCatcode() == cc && getValue().length() == 1
               && value.charAt(0) == c;
    }

    /**
     * @see de.dante.extex.scanner.Token#equals(char)
     */
    public boolean equals(final char c) {

        return value.length() == 1 && getValue().charAt(0) == c;
    }

    /**
     * @see de.dante.extex.scanner.Token#isa(Catcode)
     */
    public boolean isa(final Catcode cc) {

        return getCatcode() == cc;
    }

}
