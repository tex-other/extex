/*
 * Copyright (C) 2003-2008 The ExTeX Group and individual authors listed below
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

package org.extex.exbib.core.bst.command.impl;

import java.io.IOException;

import org.extex.exbib.core.bst.BstProcessor;
import org.extex.exbib.core.bst.command.AbstractCommand;
import org.extex.exbib.core.bst.command.CommandVisitor;
import org.extex.exbib.core.bst.exception.ExBibIllegalValueException;
import org.extex.exbib.core.bst.token.Token;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.io.Locator;
import org.extex.framework.i18n.Localizer;
import org.extex.framework.i18n.LocalizerFactory;

/**
 * This class represents an <tt>ITERATE</tt> command.
 * <p>
 * The <tt>iterate</tt> command iterates over the entries in the order they
 * are currently in the entry list from the beginning to the end. Each entry is
 * considered as current entry and the function in the argument is executed.
 * </p>
 * <p>
 * The following example is taken from <tt>alpha.bst</tt>:
 * </p>
 * 
 * <pre>
 *   ITERATE{call.type$}
 * </pre>
 * 
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class BstIterate extends AbstractCommand {

    /**
     * Creates a new object.
     * 
     * @param token the token
     * @param locator the locator
     * 
     * @throws ExBibException in case of an error
     */
    public BstIterate(Token token, Locator locator) throws ExBibException {

        super(token, locator);

        if (token == null) {
            Localizer localizer = LocalizerFactory.getLocalizer(getClass());
            throw new ExBibIllegalValueException(localizer
                .format("empty.token"), locator);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.command.Command#execute( BstProcessor,
     *      org.extex.exbib.core.io.Locator)
     */
    public void execute(BstProcessor processor, Locator locator)
            throws ExBibException {

        Token token = getValue();

        if (token == null) {
            Localizer localizer = LocalizerFactory.getLocalizer(getClass());
            throw new ExBibIllegalValueException(localizer
                .format("empty.token"), locator);
        }

        for (Entry entry : processor.getDB().getEntries()) {
            token.execute(processor, entry, getLocator());
        }
    }

    /**
     * Compute a printable string representation for this object.
     * 
     * @return the string representation
     */
    @Override
    public String toString() {

        return "ITERATE { " + getValue().toString() + " }";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.command.Command#visit(
     *      org.extex.exbib.core.bst.command.CommandVisitor)
     */
    public void visit(CommandVisitor visitor) throws IOException {

        visitor.visitIterate(this);
    }

}
