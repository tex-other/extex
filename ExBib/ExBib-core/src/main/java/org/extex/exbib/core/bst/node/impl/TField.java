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

package org.extex.exbib.core.bst.node.impl;

import java.io.IOException;

import org.extex.exbib.core.bst.BstProcessor;
import org.extex.exbib.core.bst.exception.ExBibMissingEntryException;
import org.extex.exbib.core.bst.node.AbstractToken;
import org.extex.exbib.core.bst.node.Token;
import org.extex.exbib.core.bst.node.TokenVisitor;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.io.Locator;

/**
 * This class represents a field of an entry. This value is usually read from an
 * external source.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TField extends AbstractToken implements Token {

    /**
     * Create a new object.
     * 
     * @param locator the locator
     * @param value the name of the field
     */
    public TField(String value, Locator locator) {

        super(value, locator);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.node.AbstractToken#execute(
     *      BstProcessor, org.extex.exbib.core.db.Entry,
     *      org.extex.exbib.core.io.Locator)
     */
    @Override
    public void execute(BstProcessor processor, Entry entry, Locator locator)
            throws ExBibException {

        if (entry == null) {
            throw new ExBibMissingEntryException(null, locator);
        }

        String field = getValue();
        String result = entry.getExpandedValue(field, processor.getDB());

        // TODO: eliminate brain-dead compatibility code
        if (field.equals("crossref") && result != null) {
            result = result.toLowerCase();
        }

        processor.push(new TString(result, locator));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.node.AbstractToken#visit(
     *      org.extex.exbib.core.bst.node.TokenVisitor)
     */
    @Override
    public void visit(TokenVisitor visitor) throws IOException {

        visitor.visitField(this);
    }

}
