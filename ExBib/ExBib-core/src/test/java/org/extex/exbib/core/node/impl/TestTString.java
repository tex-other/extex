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

package org.extex.exbib.core.node.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.extex.exbib.core.Processor;
import org.extex.exbib.core.bst.BstProcessor099c;
import org.extex.exbib.core.db.impl.DBImpl;
import org.extex.exbib.core.io.NullWriter;
import org.extex.exbib.core.node.TokenFactory;
import org.extex.exbib.core.node.TokenVisitor;

/**
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TestTString extends TestCase implements TokenVisitor {

    /**
     * The main program.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        junit.textui.TestRunner.run(suite());
    }

    /**
     * Getter for the suite.
     * 
     * @return the suite
     */
    public static Test suite() {

        return new TestSuite(TestTString.class);
    }

    /**
     * The field <tt>p</tt> contains the processor.
     */
    private Processor p = null;

    /**
     * The field <tt>visit</tt> contains the indicator for the visited.
     */
    private boolean visit = false;

    /**
     * Creates a new object.
     * 
     * @param name the name
     */
    public TestTString(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {

        p = new BstProcessor099c(new DBImpl(), new NullWriter(null), null);
        p.addFunction("abc", TokenFactory.T_ONE, null);
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
     * <testcase> A TString can be executed and returns itself. </testcase>
     * 
     * @throws Exception in case of an error
     */
    public void testExecute() throws Exception {

        TString t = new TString("987", null);
        t.execute(p, null, null);
        assertEquals("987", p.popString(null).getValue());
        assertNull(p.popUnchecked());
    }

    /**
     * <testcase> getValue() of a null value returns the empty string.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    public void testGetValue1() throws Exception {

        TString t = new TString(null, null);
        assertEquals("", t.getValue());
    }

    /**
     * <testcase> isNull() can detect a null value. </testcase>
     * 
     * @throws Exception in case of an error
     */
    public void testIsNull1() throws Exception {

        assertTrue(new TString(null, null).isNull());
    }

    /**
     * <testcase> isNull() can detect a null value. </testcase>
     * 
     * @throws Exception in case of an error
     */
    public void testIsNull2() throws Exception {

        assertFalse(new TString("", null).isNull());
    }

    /**
     * <testcase> Test the visiting. </testcase>
     * 
     * @throws Exception in case of an error
     */
    public void testVisit() throws Exception {

        TString t = new TString("x-1", null);
        t.visit(this);
        assertTrue(visit);
        assertEquals("x-1", t.getValue());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitBlock(
     *      org.extex.exbib.core.node.impl.TBlock)
     */
    public void visitBlock(TBlock block) {

        assertTrue("should not be visited", false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitChar(
     *      org.extex.exbib.core.node.impl.TChar)
     */
    public void visitChar(TChar c) {

        assertTrue("should not be visited", false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitField(
     *      org.extex.exbib.core.node.impl.TField)
     */
    public void visitField(TField field) {

        assertTrue("should not be visited", false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitFieldInteger(
     *      org.extex.exbib.core.node.impl.TFieldInteger)
     */
    public void visitFieldInteger(TFieldInteger integer) {

        assertTrue("should not be visited", false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitFieldString(
     *      org.extex.exbib.core.node.impl.TFieldString)
     */
    public void visitFieldString(TFieldString string) {

        assertTrue("should not be visited", false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitInteger(
     *      org.extex.exbib.core.node.impl.TInteger)
     */
    public void visitInteger(TInteger integer) {

        assertTrue("should not be visited", false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitLiteral(
     *      org.extex.exbib.core.node.impl.TLiteral)
     */
    public void visitLiteral(TLiteral literal) {

        assertTrue("should not be visited", false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitQLiteral(
     *      org.extex.exbib.core.node.impl.TQLiteral)
     */
    public void visitQLiteral(TQLiteral qliteral) {

        assertTrue("should not be visited", false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitString(
     *      org.extex.exbib.core.node.impl.TString)
     */
    public void visitString(TString string) {

        visit = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.node.TokenVisitor#visitTokenList(
     *      org.extex.exbib.core.node.impl.TokenList)
     */
    public void visitTokenList(TokenList string) {

        assertTrue("should not be visited", false);
    }

}