/*
 * Copyright (C) 2006-2011 The ExTeX Group and individual authors listed below
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

package org.extex.base.parser.dimen;

import org.extex.core.exception.helping.HelpingException;

/**
 * This interface describes a binary function.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public interface Function2 {

    /**
     * Apply the operation on the arguments.
     * 
     * @param arg1 the first argument. It m,ight be altered by the function and
     *        returned
     * @param arg2 the second argument. It will not be altered in any way
     * 
     * @return the function's result
     * 
     * @throws HelpingException in case of an error
     */
    Accumulator apply(Accumulator arg1, Accumulator arg2)
            throws HelpingException;

}
