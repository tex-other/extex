/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package org.extex.interpreter.primitives.color;


import org.extex.color.model.GrayscaleColor;
import org.extex.color.model.RgbColor;
import org.extex.interpreter.Interpreter;
import org.extex.interpreter.context.Color;
import org.extex.test.NoFlagsButGlobalPrimitiveTester;


/**
 * This is a test suite for the primitive <tt>\color</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4732 $
 */
public class ColorTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ColorTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ColorTest(final String arg) {

        super(arg, "color", "{.1 .2 .3}", "");
        setConfig("colorextex");
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMissing1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\color .1 .2 .3",
                //--- log message ---
                "Missing left brace for color value");
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> needs a left brace to start a
     *  RGB color triple.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMissing2() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\color rgb .1 .2 .3",
                //--- log message ---
                "Missing left brace for color value");
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDefault1() throws Exception {

        Interpreter interpreter = assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\color {.1 .2 .3}",
                //--- log message ---
                "");
        Color color = interpreter.getContext().getTypesettingContext()
                .getColor();
        assertTrue(color instanceof RgbColor);
        RgbColor c = (RgbColor) color;
        assertEquals(6553, c.getRed());
        assertEquals(13106, c.getGreen());
        assertEquals(19660, c.getBlue());
        assertEquals(0, c.getAlpha());
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDefault2() throws Exception {

        Interpreter interpreter = assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\color alpha .5 {.1 .2 .3}",
                //--- log message ---
                "");
        Color color = interpreter.getContext().getTypesettingContext()
                .getColor();
        assertTrue(color instanceof RgbColor);
        RgbColor c = (RgbColor) color;
        assertEquals(6553, c.getRed());
        assertEquals(13106, c.getGreen());
        assertEquals(19660, c.getBlue());
        assertEquals(0xffff / 2, c.getAlpha());
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRgb1() throws Exception {

        Interpreter interpreter = assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\color rgb {.1 .2 .3}",
                //--- log message ---
                "");
        Color color = interpreter.getContext().getTypesettingContext()
                .getColor();
        assertTrue(color instanceof RgbColor);
        RgbColor c = (RgbColor) color;
        assertEquals(6553, c.getRed());
        assertEquals(13106, c.getGreen());
        assertEquals(19660, c.getBlue());
        assertEquals(0, c.getAlpha());
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRgb2() throws Exception {

        Interpreter interpreter = assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\color rgb alpha .5 {.1 .2 .3}",
                //--- log message ---
                "");
        Color color = interpreter.getContext().getTypesettingContext()
                .getColor();
        assertTrue(color instanceof RgbColor);
        RgbColor c = (RgbColor) color;
        assertEquals(6553, c.getRed());
        assertEquals(13106, c.getGreen());
        assertEquals(19660, c.getBlue());
        assertEquals(0xffff / 2, c.getAlpha());
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRgb3() throws Exception {

        Interpreter interpreter = assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\color alpha .5 rgb {.1 .2 .3}",
                //--- log message ---
                "");
        Color color = interpreter.getContext().getTypesettingContext()
                .getColor();
        assertTrue(color instanceof RgbColor);
        RgbColor c = (RgbColor) color;
        assertEquals(6553, c.getRed());
        assertEquals(13106, c.getGreen());
        assertEquals(19660, c.getBlue());
        assertEquals(0xffff / 2, c.getAlpha());
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGray1() throws Exception {

        Interpreter interpreter = assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\color gray {.1}",
                //--- log message ---
                "");
        Color color = interpreter.getContext().getTypesettingContext()
                .getColor();
        assertTrue(color instanceof GrayscaleColor);
        GrayscaleColor c = (GrayscaleColor) color;
        assertEquals(6553, c.getGray());
        assertEquals(0, c.getAlpha());
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGray2() throws Exception {

        Interpreter interpreter = assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\color gray alpha .5 {.1}",
                //--- log message ---
                "");
        Color color = interpreter.getContext().getTypesettingContext()
                .getColor();
        assertTrue(color instanceof GrayscaleColor);
        GrayscaleColor c = (GrayscaleColor) color;
        assertEquals(6553, c.getGray());
        assertEquals(0xffff / 2, c.getAlpha());
    }

    /**
     * <testcase primitive="\color">
     *  Test case checking that <tt>\color</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGray3() throws Exception {

        Interpreter interpreter = assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\color alpha .5 gray {.1}",
                //--- log message ---
                "");
        Color color = interpreter.getContext().getTypesettingContext()
                .getColor();
        assertTrue(color instanceof GrayscaleColor);
        GrayscaleColor c = (GrayscaleColor) color;
        assertEquals(6553, c.getGray());
        assertEquals(0xffff / 2, c.getAlpha());
    }

}