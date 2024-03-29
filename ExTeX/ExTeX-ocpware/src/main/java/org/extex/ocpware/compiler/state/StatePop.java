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

package org.extex.ocpware.compiler.state;

import java.io.IOException;

import org.extex.ocpware.compiler.parser.CompilerState;
import org.extex.ocpware.type.OcpCode;

/**
 * This state change instruction pops the current state from the state stack.
 * In the input stream it is represented by a sequence of the following form:
 * <pre>
 *  &lt;pop:&gt;   </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:6007 $
 */
public class StatePop implements RightState {

    /**
     * Creates a new object.
     *
     */
    public StatePop() {

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.ocpware.compiler.state.RightState#compile(
     *      org.extex.ocpware.compiler.parser.CompilerState, boolean)
     */
    public void compile(CompilerState cs, boolean withOffset) throws IOException {

        cs.putInstruction(OcpCode.OP_STATE_POP);
    }
    
    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "<pop:>";
    }

}
