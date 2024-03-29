/*
 * Copyright (C) 2006-2011 The ExTeX Group and individual authors listed below
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

package org.extex.unit.omega.math;

import static org.junit.Assert.assertEquals;

import org.extex.core.UnicodeChar;
import org.extex.interpreter.Interpreter;
import org.extex.test.NoFlagsButGlobalPrimitiveTester;
import org.extex.typesetter.type.math.MathClass;
import org.extex.typesetter.type.math.MathCode;
import org.extex.unit.tex.math.AbstractMathTester;
import org.junit.Test;

/**
 * This is a test suite for the primitive <tt>\omathcode</tt>.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4808 $
 */
public class OmathcodeTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Constructor for MathcodeTest.
     */
    public OmathcodeTest() {

        super("omathcode", "12=32 ");
        setConfig("omega-test");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> works for an ordinary character definition.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void test100() throws Exception {

        assertSuccess(
        // --- input code ---
            AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                    + "\\omathcode`. \"41" + "$a.b$\\end",
            // --- output message ---
            "aAb" + TERM);
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> can be used to define active characters. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testActive1() throws Exception {

        assertSuccess(
        // --- input code ---
            AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH + DEFINE_BRACES
                    + "\\catcode`\\.=13 \\def.{xxx}" + "\\catcode`\\.=12 "
                    + "\\omathcode`. \"8000000" + "$a.b$\\end",
            // --- output message ---
            "axxxb" + TERM);
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> complains about an undefined active character
     * definition. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testActiveErr1() throws Exception {

        assertFailure(
        // --- input code ---
            AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH + DEFINE_BRACES
                    + "\\omathcode`. \"8000000" + "$a.b$\\end",
            // --- output message ---
            "Undefined control sequence .");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> sets the code in the Context for binary characters
     * correctly. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testBin1() throws Exception {

        Interpreter interpreter = assertSuccess(
        // --- input code ---
            "\\omathcode`. \"2000041" + "\\end",
            // --- output message ---
            "");
        MathCode mc =
                interpreter.getContext().getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.BINARY, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals(65, mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> sets the code in the Context for an closing character
     * correctly. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testClose1() throws Exception {

        Interpreter interpreter = assertSuccess(
        // --- input code ---
            "\\omathcode`. \"5000041" + "\\end",
            // --- output message ---
            "");
        MathCode mc =
                interpreter.getContext().getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.CLOSING, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals(65, mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> is convertible into a count. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testCount1() throws Exception {

        assertSuccess(
        // --- input code ---
            "\\omathcode`.=1234567 "
                    + "\\count0=\\omathcode`.\\the\\count0\\end",
            // --- output message ---
            "1234567" + TERM);
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> needs an argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testError0() throws Exception {

        assertFailure(
        // --- input code ---
            "\\omathcode",
            // --- output message ---
            "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> complains about negative values. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testError1() throws Exception {

        assertFailure(
        // --- input code ---
            "\\omathcode`. -1 " + " \\end",
            // --- output message ---
            "Bad mathchar (-1)");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> complains about too large values. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testError2() throws Exception {

        assertFailure(
        // --- input code ---
            "\\omathcode`. 999999999" + " \\end",
            // --- output message ---
            "Bad mathchar (999999999)");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> complains about too large values. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testError3() throws Exception {

        assertFailure(
        // --- input code ---
            "\\omathcode`. \"8000001" + " \\end",
            // --- output message ---
            "Bad mathchar (134217729)");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> sets the code in the Context for large characters
     * correctly. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testLarge1() throws Exception {

        Interpreter interpreter = assertSuccess(
        // --- input code ---
            "\\omathcode`. \"1000041" + "\\end",
            // --- output message ---
            "");
        MathCode mc =
                interpreter.getContext().getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.LARGE, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals(65, mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> sets the code in the Context for an opening character
     * correctly. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testOpen1() throws Exception {

        Interpreter interpreter = assertSuccess(
        // --- input code ---
            "\\omathcode`. \"4000041" + "\\end",
            // --- output message ---
            "");
        MathCode mc =
                interpreter.getContext().getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.OPENING, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals(65, mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> sets the code in the Context for ordinary characters
     * correctly. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testOrdinary1() throws Exception {

        Interpreter interpreter = assertSuccess(
        // --- input code ---
            "\\omathcode`. \"41" + "\\end",
            // --- output message ---
            "");
        MathCode mc =
                interpreter.getContext().getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.ORDINARY, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals(65, mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> sets the code in the Context for a punctation
     * character correctly. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testPunc1() throws Exception {

        Interpreter interpreter = assertSuccess(
        // --- input code ---
            "\\omathcode`. \"6000041" + "\\end",
            // --- output message ---
            "");
        MathCode mc =
                interpreter.getContext().getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.PUNCTATION, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals(65, mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> accepts a value of 0. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testRange0() throws Exception {

        assertSuccess(
        // --- input code ---
            "\\omathcode`. 0" + " \\end",
            // --- output message ---
            "");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> accepts a value of 32767. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testRange1() throws Exception {

        assertSuccess(
        // --- input code ---
            "\\omathcode`. 32767" + " \\end",
            // --- output message ---
            "");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> accepts a value of 32768, i.e. more than TeX
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testRange2() throws Exception {

        assertSuccess(
        // --- input code ---
            "\\omathcode`. 32768" + " \\end",
            // --- output message ---
            "");
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> sets the code in the Context for a relational
     * character correctly. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testRel1() throws Exception {

        Interpreter interpreter = assertSuccess(
        // --- input code ---
            "\\omathcode`. \"3000041" + "\\end",
            // --- output message ---
            "");
        MathCode mc =
                interpreter.getContext().getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.RELATION, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals(65, mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> is theable. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testThe1() throws Exception {

        assertSuccess(
        // --- input code ---
            "\\omathcode`.=1234567 " + "\\the\\omathcode`.\\end",
            // --- output message ---
            "1234567" + TERM);
    }

    /**
     * <testcase primitive="\omathcode"> Test case checking that
     * <tt>\omathcode</tt> sets the code in the Context for a variable character
     * correctly. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testVar1() throws Exception {

        Interpreter interpreter = assertSuccess(
        // --- input code ---
            "\\omathcode`. \"7000041" + "\\end",
            // --- output message ---
            "");
        MathCode mc =
                interpreter.getContext().getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.VARIABLE, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals(65, mc.getMathGlyph().getCharacter().getCodePoint());
    }

    // TODO implement more primitive specific test cases
}
