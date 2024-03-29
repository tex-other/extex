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

package org.extex.font;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.extex.core.dimen.Dimen;
import org.extex.core.dimen.FixedDimen;
import org.junit.Test;

/**
 * Test suite for the font key.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class FontKeyTest extends TestCase {

    /**
     * Test method for {@link java.lang.Object#toString()}.
     */
    @Test
    public final void testToString1() {

        FontKey key = new FontKey("cmr10");
        key.put(FontKey.SIZE, Dimen.ONE_INCH);

        assertEquals("cmr10 size=72.26999pt", key.toString());
    }

    /**
     * Test method for {@link java.lang.Object#toString()}.
     */
    @Test
    public final void testToString2() {

        FontKey key = new FontKey("cmr10");
        key.put(FontKey.SIZE, (FixedDimen) null);

        assertEquals("cmr10 size=null", key.toString());
    }

    /**
     * Test method for {@link java.lang.Object#toString()}.
     */
    @Test
    public final void testToString3() {

        FontKey key = new FontKey("cmr10");
        key.put(FontKey.SIZE, (FixedDimen) null);
        java.util.List<String> list = new ArrayList<String>();
        list.add("kern");
        list.add("latn");
        key.add(list);

        assertEquals("cmr10 size=null kern latn", key.toString());
    }

}
