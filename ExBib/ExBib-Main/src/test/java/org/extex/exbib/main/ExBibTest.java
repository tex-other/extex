/*
 * Copyright (C) 2008 The ExTeX Group and individual authors listed below
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

package org.extex.exbib.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

/**
 * This is a test suite for {@link ExBib}.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class ExBibTest {

    /**
     * The field <tt>YEAR</tt> contains the current year as four-digit string.
     */
    private static final String YEAR =
            Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

    /**
     * Run the command line test.
     * 
     * @param code the expected exit code
     * @param msg the expected error output
     * @param args the invocation arguments
     * 
     * @throws Exception in case of an error
     */
    private void runTest(int code, String msg, String... args) throws Exception {

        runTest("test", null, code, msg, args);
    }

    /**
     * Run the command line test. The aux file is written temporarily in the
     * current directory under the name <tt>test.aux</tt>. The contents can
     * be given as argument.
     * 
     * @param basename TODO
     * @param auxx the contents of the aux file
     * @param exitCode the exit code
     * @param out the expected error output
     * @param args the invocation arguments
     * 
     * @return the instance used
     * 
     * @throws Exception in case of an error
     */
    private ExBib runTest(String basename, String auxx, int exitCode,
            String out, String... args) throws Exception {

        File aux = new File(basename + ".aux");
        if (auxx != null) {
            Writer w = new FileWriter(aux);
            try {
                w.write(auxx);
            } finally {
                w.close();
            }
        }

        Locale.setDefault(Locale.ENGLISH);
        PrintStream err = System.err;
        ExBib exBib = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setErr(new PrintStream(baos));
            exBib = new ExBib();
            assertEquals(exitCode, exBib.processCommandLine(args));
            assertEquals(out, baos.toString().replaceAll("\r", ""));
        } finally {
            System.setErr(err);
            if (exBib != null) {
                exBib.close();
            }
            aux.delete();
            new File(basename + ".bbl").delete();
            new File(basename + ".blg").delete();
        }
        return exBib;
    }

    /**
     * Run the command line test.
     * 
     * @param code the expected exit code
     * @param msg the expected error output
     * @param args the invocation arguments
     * 
     * @throws Exception in case of an error
     */
    private void runTestStarting(int code, String msg, String... args)
            throws Exception {

        Locale.setDefault(Locale.ENGLISH);
        PrintStream err = System.err;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setErr(new PrintStream(baos));
            assertEquals(code, ExBib.commandLine(args));
            String s = baos.toString();
            assertTrue(s, s.startsWith(msg));
        } finally {
            System.setErr(err);
        }
    }

    /**
     * <testcase> Test that no command line option at all leads to an error.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void test001() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "Missing aux file parameter.\n");
    }

    /**
     * <testcase> Test that the command line option <tt>--</tt> needs an
     * argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void test002() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'--\' needs a parameter.\n", "--");
    }

    /**
     * <testcase> Test that the command line option <tt>-</tt> needs an
     * argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void test003() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'-\' needs a parameter.\n", "-");
    }

    /**
     * <testcase> Test that multiple files lead to an error. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void test004() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "Only one aux file can be processed.\n", "abc.aux", "abc.aux");
    }

    /**
     * <testcase> Test that multiple files lead to an error. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void test005() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "Only one aux file can be processed.\n", "abc.aux", "--",
            "abc.aux");
    }

    /**
     * <testcase> Test that multiple files lead to an error. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void test006() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "Only one aux file can be processed.\n", "--", "abc.aux",
            "abc.aux");
    }

    /**
     * <testcase> Test that an empty aux file is reported. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testAux01() throws Exception {

        runTest("test", "", 1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "I found no style file while reading test.aux\n"
                + "I found no \\bibdata commands while reading test.aux\n"
                + "I found no \\citation commands while reading test.aux\n",
            "test.aux");
    }

    /**
     * <testcase> Test that a non-existent aux file is reported. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testAux02() throws Exception {

        File aux = new File("undefined.aux");
        assertFalse(aux.exists());

        runTest("undefined", null, 1, "This is exbib, Version " + ExBib.VERSION
                + "\n" + "I couldn\'t open file undefined.aux\n",
            "undefined.aux");
    }

    /**
     * <testcase> Test that multiple files lead to an error. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testAux03() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "Only one aux file can be processed.\n", "abc.aux", "abc.aux");
    }

    /**
     * <testcase> Test that multiple files lead to an error. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testAux04() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "Only one aux file can be processed.\n", "abc.aux", "--",
            "abc.aux");
    }

    /**
     * <testcase> Test that a missing bst in the aux file is reported.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testAux10() throws Exception {

        runTest("test", "\\bibstyle{xyzzy}\n", 1, "This is exbib, Version "
                + ExBib.VERSION + "\n"
                + "I found no \\bibdata commands while reading test.aux\n"
                + "I found no \\citation commands while reading test.aux\n"
                + "I couldn\'t open style file xyzzy\n", "test.aux");
    }

    /**
     * <testcase> Test that a missing bst in the aux file is reported.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testAux11() throws Exception {

        runTest("test", "\\bibstyle{xyzzy.bst}\n", 1, "This is exbib, Version "
                + ExBib.VERSION + "\n"
                + "I found no \\bibdata commands while reading test.aux\n"
                + "I found no \\citation commands while reading test.aux\n"
                + "I couldn\'t open style file xyzzy.bst\n", "test.aux");
    }

    /**
     * <testcase> Test that a missing bst in the aux file can be specified on
     * the command line. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testAux12() throws Exception {

        runTest("test", "", 1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "I found no style file while reading test.aux\n"
                + "I found no \\bibdata commands while reading test.aux\n"
                + "I found no \\citation commands while reading test.aux\n"
                + "I couldn\'t open style file xyzzy\n", "test.aux", "-bst",
            "xyzzy");
    }

    /**
     * <testcase> Test that a missing bibliography in the aux file is reported.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testAux13() throws Exception {

        runTest("test", "\\bibstyle{src/test/resources/bibtex/base/plain}\n",
            1, "This is exbib, Version " + ExBib.VERSION + "\n"
                    + "I found no \\bibdata commands while reading test.aux\n"
                    + "I found no \\citation commands while reading test.aux\n"
                    + "(There were 2 errors)\n", "test.aux");
    }

    /**
     * <testcase> Test that the command line option <tt>--bst</tt> needs an
     * argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testBst1() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'--bst\' needs a parameter.\n", "--bst");
    }

    /**
     * <testcase> Test that the command line option <tt>--config</tt> needs an
     * argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testConfig1() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'--config\' needs a parameter.\n", "--config");
    }

    /**
     * <testcase> Test that the command line option <tt>--config</tt> needs an
     * existing configuration. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testConfig2() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "Configuration `exbib/undef\' not found.\n", "--config",
            "undef");
    }

    /**
     * <testcase> Test that the command line option <tt>--config</tt> needs a
     * valid configuration. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testConfig3() throws Exception {

        runTest(
            1,
            // TODO where is this strange message coming from?
            "[Fatal Error] :1:1: Premature end of file.\n"
                    + "This is exbib, Version "
                    + ExBib.VERSION
                    + "\n"
                    + "Configuration syntax error Premature end of file. in config/exbib/empty.xml\n",
            "--config", "empty");
    }

    /**
     * <testcase> Test that the command line option <tt>--config</tt> needs a
     * valid configuration. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testConfig4() throws Exception {

        runTest(
            1,
            // TODO where is this strange message coming from?
            "[Fatal Error] :4:1: XML document structures must start and end within the same entity.\n"
                    + "This is exbib, Version "
                    + ExBib.VERSION
                    + "\n"
                    + "Configuration syntax error XML document structures must start and end within\n"
                    + "the same entity. in config/exbib/incomplete.xml\n",
            "--config", "incomplete");
    }

    /**
     * <testcase> Test that the command line option <tt>--copying</tt> works.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testCopying1() throws Exception {

        runTestStarting(1,
            "                 GNU LESSER GENERAL PUBLIC LICENSE\n" + "",
            "--copying");
    }

    /**
     * <testcase> Test that the command line option <tt>--help</tt> works.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testHelp1() throws Exception {

        runTest(
            1,
            "This is exbib, Version "
                    + ExBib.VERSION
                    + "\n"
                    + "Usage: exbib <options> file\n"
                    + "The following options are supported:\n"
                    + "\t-help\n"
                    + "\t    Show a short list of command line arguments. \n"
                    + "\t-copying\n"
                    + "\t    Display the copyright conditions. \n"
                    + "\t-quiet\n"
                    + "\t-terse\n"
                    + "\t    Act quietly; some informative messages are suppressed. \n"
                    + "\t-verbose\n"
                    + "\t    Act verbosely; some additional informational messages are displayed. \n"
                    + "\t-dump\n"
                    + "\t-trace\n"
                    + "\t    Show a detailed trace of many operations. \n"
                    + "\t-version\n"
                    + "\t    Print the version information and exit. \n"
                    + "\t-release\n"
                    + "\t    Print the release number and exit. \n"
                    + "\t-strict\n"
                    + "\t    use the settings for BibTEX 0.99c. \n"
                    + "\t-config file\n"
                    + "\t-minCrossrefs value\n"
                    + "\t    Set the value for min.crossrefs. The default is 2. \n"
                    + "\t-bst style\n" + "\t-logfile file\n"
                    + "\t-outfile file\n", "--help");
    }

    /**
     * <testcase> Test that the command line option <tt>--logfile</tt> needs
     * an argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testLogfile01() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'--logfile\' needs a parameter.\n", "--logfile");
    }

    /**
     * <testcase> Test that the command line option <tt>--logfile</tt> can be
     * used to redirect the log output. It is tested that the log file is
     * created and the ExBib instance reports the log file with getLogfile().
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testLogfile02() throws Exception {

        File log = new File("test.lg");
        log.delete();
        assertFalse(log.exists());

        try {
            ExBib exbib =
                    runTest(
                        "test",
                        "",
                        1,
                        "This is exbib, Version "
                                + ExBib.VERSION
                                + "\n"
                                + "I found no style file while reading test.aux\n"
                                + "I found no \\bibdata commands while reading test.aux\n"
                                + "I found no \\citation commands while reading test.aux\n",
                        "test.aux", "-log", log.toString());
            assertTrue(log.exists());
            assertNotNull(exbib);
            assertEquals("test.lg", exbib.getLogfile());

        } finally {
            log.delete();
        }
    }

    /**
     * <testcase> Test that the command line option <tt>--logfile</tt> can be
     * used to redirect the log output. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testLogfile03() throws Exception {

        runTest("test", "", 1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "I found no style file while reading test.aux\n"
                + "I found no \\bibdata commands while reading test.aux\n"
                + "I found no \\citation commands while reading test.aux\n",
            "test.aux", "-log", "");
    }

    /**
     * <testcase> Test that the command line option <tt>--min_crossrefs</tt>
     * needs an argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testMinCrossrefs1() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'--min_crossrefs\' needs a parameter.\n",
            "--min_crossrefs");
    }

    /**
     * <testcase> Test that the command line option <tt>--min_crossrefs</tt>
     * needs an argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testMinCrossrefs2() throws Exception {

        runTest(
            1,
            "This is exbib, Version "
                    + ExBib.VERSION
                    + "\n"
                    + "The option \'--min_crossrefs\' needs an integer parameter.\n",
            "--min_crossrefs", "abc");
    }

    /**
     * <testcase> Test that the command line option <tt>--min.crossrefs</tt>
     * needs an argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testMinCrossrefs3() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'--min.crossrefs\' needs a parameter.\n",
            "--min.crossrefs");
    }

    /**
     * <testcase> Test that the command line option <tt>--min.crossrefs</tt>
     * needs an argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testMinCrossrefs4() throws Exception {

        runTest(
            1,
            "This is exbib, Version "
                    + ExBib.VERSION
                    + "\n"
                    + "The option \'--min.crossrefs\' needs an integer parameter.\n",
            "--min.crossrefs", "abc");
    }

    /**
     * <testcase> Test that the command line option <tt>--outfile</tt> needs
     * an argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testOutfile1() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'--outfile\' needs a parameter.\n", "--outfile");
    }

    /**
     * <testcase> Test that the command line option <tt>--progname</tt> needs
     * an argument. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testProgName1() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "The option \'--progname\' needs a parameter.\n",
            "--progname");
    }

    /**
     * <testcase> Test that the command line option <tt>--progname</tt> can be
     * used to set the program name. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testProgName2() throws Exception {

        runTest(1, "This is abc, Version " + ExBib.VERSION + "\n"
                + "Missing aux file parameter.\n", "--progname", "abc");
    }

    /**
     * <testcase> Test that the command line option <tt>--quiet</tt> is
     * honored. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testQuiet1() throws Exception {

        runTest(1, "Missing aux file parameter.\n", "--quiet");
    }

    /**
     * <testcase> Test that the command line option <tt>--release</tt> works.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testRelease1() throws Exception {

        runTest(1, ExBib.VERSION + "\n", "--release");
    }

    /**
     * <testcase> Test that the command line option <tt>--terse</tt> is
     * honored. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testTerse1() throws Exception {

        runTest(1, "Missing aux file parameter.\n", "--terse");
    }

    /**
     * <testcase> Test that an undefined command line option is reported.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testUndefined1() throws Exception {

        runTest(1, "This is exbib, Version " + ExBib.VERSION + "\n"
                + "Unknown option \'undefined\' ignored.\n"
                + "Missing aux file parameter.\n", "--undefined");
    }

    /**
     * <testcase> Test that the command line option <tt>--version</tt> works.
     * </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testVersion1() throws Exception {

        runTest(
            1,
            "This is exbib, Version "
                    + ExBib.VERSION
                    + "\n"
                    + "Copyright (C) 2002-"
                    + YEAR
                    + " Gerd Neugebauer (mailto:gene@gerd-neugebauer.de).\n"
                    + "There is NO warranty.  Redistribution of this software is\n"
                    + "covered by the terms of the GNU Library General Public License.\n"
                    + "For more information about these matters, use the command line\n"
                    + "switch -copying.\n", "--version");
    }

    /**
     * <testcase> Test that the command line option <tt>--version</tt> works
     * when abbreviated as <tt>--vers</tt>. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testVersion2() throws Exception {

        runTest(
            1,
            "This is exbib, Version "
                    + ExBib.VERSION
                    + "\n"
                    + "Copyright (C) 2002-"
                    + YEAR
                    + " Gerd Neugebauer (mailto:gene@gerd-neugebauer.de).\n"
                    + "There is NO warranty.  Redistribution of this software is\n"
                    + "covered by the terms of the GNU Library General Public License.\n"
                    + "For more information about these matters, use the command line\n"
                    + "switch -copying.\n", "--vers");
    }

}
