/*
 * Copyright (C) 2003-2008 The ExTeX Group and individual authors listed below
 * This file is part of ExBib a BibTeX compatible database.
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

package org.extex.exbib.core.db;

import java.io.IOException;
import java.util.Iterator;

/**
 * This class represents a variable {@link ValueItem ValueItem}. The value is
 * defined as the macro stored under the name given.
 * <p>
 * Example:
 * </p>
 * 
 * <pre>abc.def-ghi</pre>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class VMacro extends AbstractValueItem {

    /**
     * Creates a new object.
     * 
     * @param contents the contents
     */
    public VMacro(String contents) {

        super(contents);
    }

    /**
     * The value of a Macro expands to the concatenation of all constituents,
     * where undefined items are expanded to the empty string.
     * 
     * @see org.extex.exbib.core.db.ValueItem#expand(java.lang.StringBuffer,
     *      org.extex.exbib.core.db.DB)
     */
    @Override
    public void expand(StringBuffer sb, DB db) {

        Value val = db.getMacro(getContent());

        if (val != null) {
            Iterator<ValueItem> iterator = val.iterator();

            while (iterator.hasNext()) {
                iterator.next().expand(sb, db);
            }
        }
    }

    /**
     * Return a String representation of the object suitable for a BibT<sub>E</sub>X
     * file.
     * 
     * @return the string representation
     */
    @Override
    public String toString() {

        return getContent();
    }

    /**
     * This is a method which is invoked when this object is visited.
     * 
     * @param visitor the visitor
     * @param db the database context
     * 
     * @throws IOException just in case
     */
    @Override
    public void visit(ValueVisitor visitor, DB db) throws IOException {

        visitor.visitMacro(this, db);
    }

}