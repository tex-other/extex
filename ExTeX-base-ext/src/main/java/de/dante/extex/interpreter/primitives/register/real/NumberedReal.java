/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.real;

import org.extex.base.parser.CountParser;
import org.extex.core.exception.helping.HelpingException;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;

/**
 * This class provides an implementation for the primitive <code>\real</code>.
 * It sets the named real register to the value given, and as a side effect all
 * prefixes are zeroed.
 * 
 * <p>
 * All features are inherited from
 * {@link de.dante.extex.interpreter.primitives.register.real.NamedReal Real}.
 * Just the key has to be provided under which this Real has to be stored. This
 * key is constructed from the name, a hash mark and the running number.
 * </p>
 * 
 * <p>
 * Example
 * </p>
 * 
 * <pre>
 * \real12=345.67
 * </pre>
 * 
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class NumberedReal extends NamedReal {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     * 
     * @param name the name for debugging
     */
    public NumberedReal(String name) {

        super(name);
    }

    /**
     * Return the key (the number) for the register.
     * 
     * {@inheritDoc}
     * 
     * @see de.dante.extex.interpreter.primitives.register.real.NamedReal#getKey(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    protected String getKey(Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        return getName()
                + "#"
                + Long.toString(CountParser.scanNumber(context, source,
                    typesetter));
    }
}
