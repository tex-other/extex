/*
 * Copyright (C) 2003-2008 Gerd Neugebauer
 * This file is part of ExBib a BibTeX compatible database.
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

package org.extex.exbib.core.bst.code.impl;

import org.extex.exbib.core.bst.Processor;
import org.extex.exbib.core.bst.code.AbstractCode;
import org.extex.exbib.core.bst.exception.ExBibIllegalValueException;
import org.extex.exbib.core.bst.node.impl.TString;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.io.Locator;
import org.extex.framework.i18n.Localizer;
import org.extex.framework.i18n.LocalizerFactory;

/**
 * BibT<sub>E</sub>X built-in function <code>int.to.chr$</code>
 * 
 * <dl>
 * <dt>BibT<sub>E</sub>X documentation:
 * <dt>
 * <dd> Pops the top (integer) literal, interpreted as the ASCII integer value
 * of a single character, converts it to the corresponding single-character
 * string, and pushes this string. </dd>
 * </dl>
 * 
 * <dl>
 * <dt>BibT<sub>E</sub>X web documentation:</dt>
 * <dd> The <code>built_in</code> function <code>int.to.chr$</code> pops the
 * top (integer) literal, interpreted as the <code>ASCII_code</code> of a
 * single character, converts it to the corresponding single-character string,
 * and pushes this string. If the literal isn't an appropriate integer, it
 * complains and pushes the null string. </dd>
 * </dl>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class IntToChr extends AbstractCode {

    /**
     * Create a new object.
     */
    public IntToChr() {

        super();
    }

    /**
     * Creates a new object.
     * 
     * @param name the function name in the processor context
     */
    public IntToChr(String name) {

        super();
        setName(name);
    }

    /**
     * @see org.extex.exbib.core.bst.Code#execute(org.extex.exbib.core.bst.Processor,
     *      org.extex.exbib.core.db.Entry, org.extex.exbib.core.io.Locator)
     */
    @Override
    public void execute(Processor processor, Entry entry, Locator locator)
            throws ExBibException {

        int i = processor.popInteger(locator).getInt();

        if (i < 0) {
            Localizer localizer = LocalizerFactory.getLocalizer(getClass());
            throw new ExBibIllegalValueException(localizer.format(
                "negative.argument", Integer.toString(i)), locator);
        }

        processor.push(new TString(String.valueOf((char) i)));
    }
}