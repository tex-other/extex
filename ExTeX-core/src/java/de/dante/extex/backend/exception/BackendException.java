/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.exception;

import org.extex.util.exception.GeneralException;

/**
 * This exception signals that something went wrong in the back-end.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 3070 $
 */
public class BackendException extends GeneralException {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     */
    public BackendException() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param message the message
     */
    public BackendException(final String message) {

        super(message);
    }

    /**
     * Creates a new object.
     *
     * @param message the message
     * @param cause the cause of all evil
     */
    public BackendException(final String message, final Throwable cause) {

        super(message, cause);
    }

    /**
     * Creates a new object.
     *
     * @param cause the cause of all evil
     */
    public BackendException(final Throwable cause) {

        super(cause);
    }

}
