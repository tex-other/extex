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

package org.extex.exbib.core.bst.code.impl;

import java.util.List;

import org.extex.exbib.core.Processor;
import org.extex.exbib.core.bst.code.AbstractCode;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.io.Locator;
import org.extex.exbib.core.node.Name;
import org.extex.exbib.core.node.TokenFactory;
import org.extex.exbib.core.node.impl.TInteger;
import org.extex.exbib.core.node.impl.TString;

/**
 * B<small>IB</small>T<sub>E</sub>X built-in function
 * <code>num.names$</code>
 * 
 * <dl>
 * <dt>B<small>IB</small>T<sub>E</sub>X documentation:
 * <dt>
 * <dd> Pops the top (string) literal and pushes the number of names the string
 * represents---one plus the number of occurrences of the substring ``and''
 * (ignoring case differences) surrounded by non-null white-space at the top
 * brace level. </dd>
 * </dl>
 * 
 * <dl>
 * <dt>B<small>IB</small>T<sub>E</sub>X web documentation:</dt>
 * <dd>
 * 
 * </dd>
 * </dl>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class NumNames extends AbstractCode {

    /**
     * Create a new object.
     */
    public NumNames() {

        super();
    }

    /**
     * Creates a new object.
     * 
     * @param name the function name in the processor context
     */
    public NumNames(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.code.AbstractCode#execute(
     *      org.extex.exbib.core.Processor, org.extex.exbib.core.db.Entry,
     *      org.extex.exbib.core.io.Locator)
     */
    @Override
    public void execute(Processor processor, Entry entry, Locator locator)
            throws ExBibException {

        TString names = processor.popString(locator);
        List<Name> namelist = names.getNames();

        if (namelist == null) {
            namelist = Name.parse(names.getValue(), locator);
        }

        int n = namelist.size();
        processor.push((n == 0 ? TokenFactory.T_ZERO : n == 1
                ? TokenFactory.T_ONE
                : new TInteger(n, locator)));
    }

}
