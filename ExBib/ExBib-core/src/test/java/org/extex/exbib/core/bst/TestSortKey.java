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

package org.extex.exbib.core.bst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.extex.exbib.core.Processor;
import org.extex.exbib.core.bst.node.TokenFactory;
import org.extex.exbib.core.bst.node.impl.TFieldString;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.db.impl.DBImpl;
import org.extex.exbib.core.io.NullWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for sort.key$.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class TestSortKey {

    /**
     * The field <tt>e</tt> contains the entry.
     */
    private Entry e;

    /**
     * The field <tt>p</tt> contains the processor.
     */
    private Processor p = null;

    /**
     * Set-up method.
     * 
     * @throws Exception in case of an error
     */
    @Before
    public void setUp() throws Exception {

        p = new BstProcessor099c(new DBImpl(), new NullWriter(null), null);
        p.addFunction("abc", TokenFactory.T_ONE, null);
        e = p.getDB().makeEntry("book", "k1", null);
    }

    /**
     * Tear-down method.
     */
    @After
    public void tearDown() {

        p = null;
        e = null;
    }

    /**
     * <testcase> sort.key$ is empty if the sort key from the current entry is
     * not set. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testNoSortKey() throws Exception {

        new TFieldString("sort.key$", null).execute(p, e, null);
        assertEquals("", p.popString(null).getValue());
        assertNull(p.popUnchecked());
    }

    /**
     * <testcase> getter and setter coincide. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testSortKey1() throws Exception {

        e.setSortKey("xyz");
        assertEquals("xyz", e.getSortKey());
    }

    /**
     * <testcase> sort.key$ extracts the sort key from the current entry.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testSortKey11() throws Exception {

        e.setSortKey("xyz");
        new TFieldString("sort.key$", null).execute(p, e, null);
        assertEquals("xyz", p.popString(null).getValue());
        assertNull(p.popUnchecked());
    }

}
