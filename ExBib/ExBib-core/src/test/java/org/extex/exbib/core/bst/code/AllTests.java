/*
 * This file is part of ExBib a BibTeX compatible database.
 * Copyright (C) 2003-2008 Gerd Neugebauer
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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class AllTests {
    /**
     * Generate a new test suite
     *
     * @return the new test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.extex.exbib.core.bst.code");

        //$JUnit-BEGIN$
        suite.addTest(TestSkip.suite());

        suite.addTest(TestPop.suite());
        suite.addTest(TestDuplicate.suite());
        suite.addTest(TestSwap.suite());

        suite.addTest(TestWrite.suite());
        suite.addTest(TestNewline.suite());
        suite.addTest(TestWarning.suite());
        suite.addTest(TestTop.suite());
        suite.addTest(TestStack.suite());

        suite.addTest(TestQuote.suite());
        suite.addTest(TestConcat.suite());
        suite.addTest(TestEq.suite());
        suite.addTest(TestGt.suite());
        suite.addTest(TestLt.suite());
        suite.addTest(TestPlus.suite());
        suite.addTest(TestMinus.suite());
        suite.addTest(TestAddPeriod.suite());
        suite.addTest(TestIntToChr.suite());
        suite.addTest(TestIntToStr.suite());
        suite.addTest(TestChrToInt.suite());

        suite.addTest(TestSubstring.suite());
        suite.addTest(TestWidth.suite());
        suite.addTest(TestChangeCase.suite());
        suite.addTest(TestFormatName.suite());
        suite.addTest(TestNumNames.suite());
        suite.addTest(TestTextLength.suite());
        suite.addTest(TestTextPrefix.suite());
        suite.addTest(TestPurify.suite());

        suite.addTest(TestIf.suite());
        suite.addTest(TestWhile.suite());

        suite.addTest(TestSet.suite());

        suite.addTest(TestCite.suite());
        suite.addTest(TestEmpty.suite());
        suite.addTest(TestMissing.suite());
        suite.addTest(TestPreamble.suite());
        suite.addTest(TestType.suite());

        suite.addTest(TestCallType.suite());

        //$JUnit-END$
        return suite;
    }
}