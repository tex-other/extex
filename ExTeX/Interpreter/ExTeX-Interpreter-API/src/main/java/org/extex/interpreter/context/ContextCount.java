/*
 * Copyright (C) 2003-2011 The ExTeX Group and individual authors listed below
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

package org.extex.interpreter.context;

import org.extex.core.count.Count;
import org.extex.core.exception.helping.HelpingException;

/**
 * This interface describes the container for count registers of an interpreter
 * context.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public interface ContextCount {

    /**
     * Getter for the {@link org.extex.core.count.Count count} register. Count
     * registers are named, either with a number or an arbitrary string.
     * <p>
     * The numbered registers where limited to 256 in <logo>T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo>. This restriction does not longer hold for
     * <logo>&epsilon;&chi;T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo>.
     * </p>
     * <p>
     * The return value is guaranteed to be a valid object. <code>null</code>
     * will never be returned.
     * </p>
     * 
     * @param name the name or number of the count register
     * 
     * @return the count register or <code>null</code> if it is not defined
     * 
     * @see #setCount(String, long, boolean)
     */
    Count getCount(String name);

    /**
     * Setter for the {@link org.extex.core.count.Count count} register in all
     * requested groups. Count registers are named, either with a number or an
     * arbitrary string. The numbered registers where limited to 256 in
     * <logo>T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo>. This restriction does no longer hold for
     * <logo>&epsilon;&chi;T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo>.
     * 
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *        groups; otherwise the current group is affected only
     * 
     * @throws HelpingException in case of an error
     * 
     * @see #getCount(String)
     */
    void setCount(String name, long value, boolean global)
            throws HelpingException;

}
