/*
 * Copyright (C) 2007 The ExTeX Group and individual authors listed below
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

package org.extex.ocpware.type;

/**
 * This enumeration provides the types for an argument.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public enum OcpArgumentType {

    /**
     * The field <tt>NUMBER</tt> contains an indicator for a number. The
     * corresponding argument is a number.
     */
    NUMBER,
    /**
     * The field <tt>CHARACTER</tt> contains an indicator for a character
     * code.
     */
    CHARACTER,
    /**
     * The field <tt>LABEL</tt> contains the indicator for a label; i.e. a
     * jump destination.
     */
    LABEL,
    /**
     * The field <tt>STATE</tt> contains an indicator for a state index.
     */
    STATE;
    
}