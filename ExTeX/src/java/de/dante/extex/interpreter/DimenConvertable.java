/*
 * Copyright (C) 2004 Gerd Neugebauer, Michael Niedermair
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

import de.dante.util.GeneralException;

/**
 * This is an interface which describes the feature to be convertibe into a dimen.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public interface DimenConvertable {

	/**
	 * Convert ot a <code>Dimen</code>.
	 *
	 * @param context	the interpreter context
	 * @param source	the source for new tokens
	 * @return the converted value
	 * @throws GeneralException in case of an error
	 */
	long convertDimen(Context context, TokenSource source) throws GeneralException;
}
