/*
 * Copyright (C) 2008-2009 The ExTeX Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.extex.maven.latex.builder.exception;

/**
 * This is a base exception for the make.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class MakeException extends Exception {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    private static final long serialVersionUID = 2008L;

    /**
     * Creates a new object.
     * 
     */
    public MakeException() {

        super();
    }

    /**
     * Creates a new object.
     * 
     * @param message the message
     */
    public MakeException(String message) {

        super(message);
    }

    /**
     * Creates a new object.
     * 
     * @param message the message
     * @param cause the cause
     */
    public MakeException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * Creates a new object.
     * 
     * @param cause the cause
     */
    public MakeException(Throwable cause) {

        super(cause);
    }

}