/*
 * Copyright (C) 2008 The ExTeX Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.extex.exbib.main;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

/**
 * This is a test suite for different encodings.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class ExBibI18NTest {

    /**
     * The field <tt>DATA_DIR</tt> contains the directory containing database,
     * styles and results.
     */
    private static final String DATA_DIR = "src/test/resources/bibtex/i18n";

    /**
     * The field <tt>STYLE_DIR</tt> contains the style directory.
     */
    private static final String STYLE_DIR = "src/test/resources/bibtex/base";

    /**
     * Creates a new object.
     */
    public ExBibI18NTest() {

        super();
    }

    /**
     * <testcase> ... </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testGreek() throws Exception {

        File data =
                BibTester.makeFile("target/data.bib", "UTF-8", //
                    "@Book{ aristotle,\n" //
                            + "  author =       {Aριστoτέλης},\n"
                            + "  title =        {title},\n"
                            + "  publisher =    {publisher},\n"
                            + "  year =         {year}\n" + "}\n" + "");
        File aux = BibTester.makeFile("test.aux", "ISO-8859-1", //
            "\\relax\n" //
                    + "\\citation{*}\n" //
                    + "\\bibstyle{" + STYLE_DIR + "/alpha.bst" + "}\n" //
                    + "\\bibdata{target/data.bib}\n");
        try {
            TRunner.runTest(aux, new File(DATA_DIR, "greek.result"),
                "--enc=UTF-8");
        } finally {
            if (aux.exists() && !aux.delete()) {
                assertTrue(aux.toString() + ": deletion failed", false);
            }
            if (data.exists() && !data.delete()) {
                assertTrue(data.toString() + ": deletion failed", false);
            }
        }
    }

}
