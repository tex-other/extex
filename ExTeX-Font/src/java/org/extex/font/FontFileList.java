/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package org.extex.font;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for a <code>FontFile</code>List.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 4728 $
 */
public class FontFileList implements Serializable {

    /**
     * The field <tt>serialVersionUID</tt> ...
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list
     */
    private List<FontByteArray> list;

    /**
     * Create a new object.
     */
    public FontFileList() {

        super();
        list = new ArrayList<FontByteArray>();
    }

    /**
     * Create a new object.
     * @param initsize  the initsize for the list
     */
    public FontFileList(int initsize) {

        super();
        list = new ArrayList<FontByteArray>(initsize);
    }

    /**
     * Return the size of the list
     * @return  the size of the list
     */
    public int size() {

        return list.size();
    }

    /**
     * Add a <code>FontFile</code>
     * @param fontfile  the font file to add
     */
    public void add(FontByteArray fontfile) {

        list.add(fontfile);
    }

    /**
     * Return the font file at position idx.
     * @param idx   the position
     * @return  the font file at position idx
     */
    public FontByteArray getFontFile(int idx) {

        return list.get(idx);
    }

}