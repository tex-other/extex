/*
 * Copyright (C) 2005-2007 The ExTeX Group and individual authors listed below
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

package org.extex.scanner.type.token;

import junit.framework.TestCase;

import org.extex.core.UnicodeChar;
import org.extex.scanner.type.Catcode;
import org.extex.scanner.type.Namespace;

/**
 * Test cases for control sequence tokens.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4756 $
 */
public class ControlSequenceTokenTest extends TestCase {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(String[] args) {

        junit.textui.TestRunner.run(ControlSequenceTokenTest.class);
    }

    /**
     * The field <tt>t</tt> contains the reference token.
     */
    private static ControlSequenceToken t =
            new ControlSequenceToken(UnicodeChar.get('\\'), "x",
                Namespace.DEFAULT_NAMESPACE);

    /**
     */
    public void testGetCatcode() {

        assertEquals(Catcode.ESCAPE, t.getCatcode());
    }

    /**
     */
    public void testGetChar() {

        assertEquals("\\", t.getChar().toString());
    }

    /**
     */
    public void testToString() {

        assertEquals("the control sequence \\x", t.toString());
    }

    /**
     */
    public void testName1() {

        assertEquals("x", t.getName());
    }

    /**
     */
    public void testNamespace1() {

        assertEquals("", t.getNamespace());
    }

    /**
     */
    public void testToText1() {

        assertEquals("\\x", t.toText());
    }

    /**
     */
    public void testToText2() {

        Token ta =
                new ControlSequenceToken(null, "a", Namespace.DEFAULT_NAMESPACE);
        assertEquals("a", ta.toText());
    }

    /**
     */
    public void testEqualsToken0() {

        assertTrue(t.equals(t));
    }

    /**
     */
    public void testEqualsToken1() {

        Token t1 = new ControlSequenceToken(UnicodeChar.get('\\'), " ", "");
        Token t2 = new OtherToken(UnicodeChar.get(' '));
        assertFalse(t1.equals(t2));
    }

    /**
     */
    public void testEqualsCatcodeString0() {

        assertTrue(t.eq(Catcode.ESCAPE, "x"));
    }

    /**
     */
    public void testEqualsCatcodeString1() {

        assertFalse(t.eq(Catcode.OTHER, "x"));
    }

    /**
     */
    public void testEqualsCatcodechar0() {

        assertTrue(t.eq(Catcode.ESCAPE, 'x'));
    }

    /**
     */
    public void testEqualsCatcodechar1() {

        assertFalse(t.eq(Catcode.OTHER, ' '));
    }

    /**
     */
    public void testEqualschar0() {

        boolean b = t.eq('x');
        assertTrue(b);
    }

    /**
     */
    public void testEqualschar1() {

        assertFalse(t.eq('.'));
    }

    /**
     */
    public void testIsa0() {

        assertFalse(t.isa(Catcode.SPACE));
    }

    /**
     */
    public void testIsa1() {

        assertFalse(t.isa(Catcode.ACTIVE));
    }

    /**
     */
    public void testIsa2() {

        assertFalse(t.isa(Catcode.COMMENT));
    }

    /**
     */
    public void testIsa3() {

        assertFalse(t.isa(Catcode.CR));
    }

    /**
     */
    public void testIsa4() {

        assertTrue(t.isa(Catcode.ESCAPE));
    }

    /**
     */
    public void testIsa5() {

        assertFalse(t.isa(Catcode.IGNORE));
    }

    /**
     */
    public void testIsa6() {

        assertFalse(t.isa(Catcode.INVALID));
    }

    /**
     */
    public void testIsa7() {

        assertFalse(t.isa(Catcode.LEFTBRACE));
    }

    /**
     */
    public void testIsa8() {

        assertFalse(t.isa(Catcode.LETTER));
    }

    /**
     */
    public void testIsa9() {

        assertFalse(t.isa(Catcode.MACROPARAM));
    }

    /**
     */
    public void testIsa10() {

        assertFalse(t.isa(Catcode.MATHSHIFT));
    }

    /**
     */
    public void testIsa11() {

        assertFalse(t.isa(Catcode.OTHER));
    }

    /**
     */
    public void testIsa12() {

        assertFalse(t.isa(Catcode.RIGHTBRACE));
    }

    /**
     */
    public void testIsa13() {

        assertFalse(t.isa(Catcode.SUBMARK));
    }

    /**
     */
    public void testIsa14() {

        assertFalse(t.isa(Catcode.SUPMARK));
    }

    /**
     */
    public void testIsa15() {

        assertFalse(t.isa(Catcode.TABMARK));
    }

}
