/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.exception.helping;

import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This exception is raised when an unexpected end of file is encountered.
 * <p>
 *  The localization format is taken from the Localizer under the key
 *  <tt>UnexpectedEofIn</tt>.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class EofException extends HelpingException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     */
    public EofException() {

        super(LocalizerFactory.getLocalizer(EofException.class.getName()),
                "UnexpectedEof");
    }

    /**
     * Creates a new object.
     *
     * @param localizer the localizer to use
     * @param messageTag the name of the message
     * @param a the argument
     */
    public EofException(final Localizer localizer, final String messageTag,
            final String a) {

        super(localizer, messageTag, a);
    }

    /**
     * Creates a new object.
     *
     * @param macro the name of the macro in which the eof has been encountered.
     *  If the value is <code>null</code> then a shortened error message is
     *  used.
     */
    public EofException(final String macro) {

        super(LocalizerFactory.getLocalizer(EofException.class.getName()),
                (macro != null ? "UnexpectedEofIn" : "UnexpectedEof"), macro);
    }

}
