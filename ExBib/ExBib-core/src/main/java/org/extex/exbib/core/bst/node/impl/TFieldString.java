/*
 * This file is part of ExBib a BibTeX compatible database.
 * Copyright (C) 2003-2008 Gerd Neugebauer
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

package org.extex.exbib.core.bst.node.impl;

import java.io.IOException;

import org.extex.exbib.core.bst.Processor;
import org.extex.exbib.core.bst.exception.ExBibMissingEntryException;
import org.extex.exbib.core.bst.node.AbstractToken;
import org.extex.exbib.core.bst.node.Token;
import org.extex.exbib.core.bst.node.TokenFactory;
import org.extex.exbib.core.bst.node.TokenVisitor;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.db.VString;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.io.Locator;

/**
 * This class represents a string valued field local to an entry. This class is
 * not related to externally stored values but used internally only.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TFieldString extends AbstractToken implements Token {

    /**
     * Create a new object.
     * 
     * @param value
     */
    public TFieldString(String value) {

        super(value, null);
    }

    /**
     * Create a new object.
     * 
     * @param value
     * @param locator
     */
    public TFieldString(String value, Locator locator) {

        super(value, locator);
    }

    /**
     * @see org.extex.exbib.core.bst.Code#execute(
     *      org.extex.exbib.core.bst.Processor, org.extex.exbib.core.db.Entry,
     *      org.extex.exbib.core.io.Locator)
     */
    @Override
    public void execute(Processor processor, Entry entry, Locator locator)
            throws ExBibException {

        if (entry == null) {
            throw new ExBibMissingEntryException(null, locator);
        }

        VString val = (VString) entry.getLocal(getValue());
        processor.push(val == null ? TokenFactory.T_EMPTY : new TString(val
            .getContent()));
    }

    /**
     * @see org.extex.exbib.core.bst.node.Token#visit(
     *      org.extex.exbib.core.bst.node.TokenVisitor)
     */
    @Override
    public void visit(TokenVisitor visitor) throws IOException {

        visitor.visitFieldString(this);
    }
}