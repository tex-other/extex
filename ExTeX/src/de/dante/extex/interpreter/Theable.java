/*
 * Copyright (C) 2003  Gerd Neugebauer
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

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Tokens;

import de.dante.util.GeneralException;

/**
 * This is a interface to mark those classes which are able to provide a description.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public interface Theable {
    /**
     * ...
     *
     * @param context ...
     * @param source ...
     *
     * @return ...
     *
     * @throws GeneralException ...
     */
    public abstract Tokens the(Context context, TokenSource source)
                        throws GeneralException;

}
