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

package org.extex.exbib.core.bst.code;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.extex.exbib.core.bst.Processor;
import org.extex.exbib.core.bst.Processor099Impl;
import org.extex.exbib.core.bst.code.impl.Top;
import org.extex.exbib.core.bst.exception.ExBibStackEmptyException;
import org.extex.exbib.core.bst.node.impl.TInteger;
import org.extex.exbib.core.bst.node.impl.TString;
import org.extex.exbib.core.db.impl.DBImpl;
import org.extex.exbib.core.io.StringBufferWriter;

/**
 * Test suite for <tt>top$</tt>.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class TestTop extends TestCase {

    /**
     * The main program just uses the text interface of JUnit.
     * 
     * @param args command line parameters are ignored
     */
    public static void main(String[] args) {

        junit.textui.TestRunner.run(suite());
    }

    /**
     * Generate a new test suite
     * 
     * @return the new test suite
     */
    public static Test suite() {

        return new TestSuite(TestTop.class);
    }

    /**
     * The field <tt>p</tt> contains the processor.
     */
    private Processor p = null;

    /**
     * The field <tt>out</tt> contains the output buffer.
     */
    private StringBuffer out = new StringBuffer();

    /**
     * The field <tt>err</tt> contains the error buffer.
     */
    private Handler err;

    /**
     * Create a new object.
     * 
     * @param name the name
     */
    public TestTop(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {

        Logger logger = Logger.getLogger("test");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        err = new StoringHandler();
        logger.addHandler(err);
        p = new Processor099Impl(new DBImpl(), //
            new StringBufferWriter(out), logger);
    }

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    public void tearDown() {

        p = null;
    }

    /**
     * <testcase> top$ complains about an empty stack. </testcase>
     * 
     * @throws Exception in case of an error
     */
    public void testEmptyStack() throws Exception {

        try {
            new Top("top$").execute(p, null, null);
            assertTrue(false);
        } catch (ExBibStackEmptyException e) {
            assertTrue(true);
        }
    }

    /**
     * <testcase> top$ can pop a number. </testcase>
     * 
     * @throws Exception in case of an error
     */
    public void testTopInteger() throws Exception {

        p.push(new TInteger(1234));
        new Top("top$").execute(p, null, null);
        assertEquals("#1234\n", err.toString());
        assertEquals("", out.toString());
        assertNull(p.popUnchecked());
    }

    /**
     * <testcase> top$ can pop a string. </testcase>
     * 
     * @throws Exception in case of an error
     */
    public void testTopString() throws Exception {

        p.push(new TString("abc"));
        new Top("top$").execute(p, null, null);
        assertEquals("\"abc\"\n", err.toString());
        assertEquals("", out.toString());
        assertNull(p.popUnchecked());
    }

}