/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.typesetter;


import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @see "TeX -- The Program [211]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public interface Typesetter extends ListMaker {

    /**
     * Setter for the document writer.
     * The document writer is addressed whenever a complete page has to be
     * shipped out.
     *
     * @param doc the new document writer
     */
    void setDocumentWriter(DocumentWriter doc);

    /**
     * Getter for the CharNodeFactory.
     *
     * @return the character node factory
     */
    CharNodeFactory getCharNodeFactory();

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    void finish() throws GeneralException;

    /**
     * Send a list of nodes to the document writer.
     *
     * @param nodes the nodes to send to the typesetter
     *
     * @throws GeneralException in case of an error
     */
    void shipout(NodeList nodes) throws GeneralException;

    /**
     * ...
     */
    void openHbox();

    /**
     * ...
     */
    void openVbox();

}
