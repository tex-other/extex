/*
 * Copyright (C) 2007-2009 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.makeindex.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.extex.exindex.core.exception.RawIndexEofException;
import org.extex.exindex.core.exception.RawIndexException;
import org.extex.exindex.core.exception.RawIndexMissingCharException;
import org.extex.exindex.makeindex.Entry;
import org.extex.exindex.makeindex.Index;
import org.extex.exindex.makeindex.MakeindexParameters;
import org.extex.exindex.makeindex.Parameters;
import org.extex.exindex.makeindex.exceptions.MissingSymbolException;
import org.extex.exindex.makeindex.exceptions.StyleNotFoundException;
import org.extex.exindex.makeindex.exceptions.UnknownOptionException;
import org.extex.exindex.makeindex.normalizer.MakeindexCollator;
import org.extex.exindex.makeindex.normalizer.MakeindexGermanCollator;
import org.extex.exindex.makeindex.pages.MakeindexPageProcessor;
import org.extex.exindex.makeindex.pages.PageProcessor;
import org.extex.exindex.makeindex.parser.MakeindexParser;
import org.extex.exindex.makeindex.parser.Parser;
import org.extex.exindex.makeindex.writer.IndexWriter;
import org.extex.exindex.makeindex.writer.MakeindexWriter;
import org.extex.framework.i18n.Localizer;
import org.extex.framework.i18n.LocalizerFactory;
import org.extex.logging.LogFormatter;

/**
 * This is the main program for an indexer a la Makeindex.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:7779 $
 */
public class Makeindex {

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private static final Localizer LOCALIZER =
            LocalizerFactory.getLocalizer(Makeindex.class);

    /**
     * The field <tt>REVISION</tt> contains the revision number.
     */
    public static final String REVISION =
            "$Revision:7779 $".replaceAll("[^0-9]", "");

    /**
     * The field <tt>VERSION</tt> contains the version number.
     */
    public static final String VERSION = "0.1";

    /**
     * This is the command line interface to the indexer.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.exit(new Makeindex().run(args));
    }

    /**
     * The field <tt>banner</tt> contains the indicator that the banner needs to
     * be written.
     */
    private boolean banner = true;

    /**
     * The field <tt>collateGerman</tt> contains the indicator to recognize
     * german.sty.
     */
    private boolean collateGerman = false;

    /**
     * The field <tt>collateSpaces</tt> contains the indicator to collate
     * spaces.
     */
    private boolean collateSpaces = false;

    /**
     * The field <tt>comparisons</tt> contains the number of comparisons
     * performed.
     */
    private long comparisons;

    /**
     * The field <tt>comp</tt> contains the comparator.
     */
    private Comparator<Entry> comp = new Comparator<Entry>() {

        /**
         * Compare two entries.
         * 
         * @param o1 the first entry
         * @param o2 the second entry
         * 
         * @return the result
         */
        public int compare(Entry o1, Entry o2) {

            comparisons++;
            char c1 = o1.getHeading();
            char c2 = o2.getHeading();
            if (c1 < c2) {
                return -1;
            } else if (c1 > c2) {
                return 1;
            }

            String[] ka1 = o1.getKey();
            String[] ka2 = o2.getKey();
            int len = (ka1.length < ka2.length ? ka1.length : ka2.length);

            for (int i = 0; i < len; i++) {
                String v1 = ka1[i];
                String v2 = ka2[i];
                if (v1 == null) {
                    if (v2 != null) {
                        return 1;
                    }
                } else if (v2 == null) {
                    return -1;
                } else {
                    int cmp = v1.compareToIgnoreCase(v2);
                    if (cmp != 0) {
                        return cmp;
                    }
                }
            }
            if (ka1.length < ka2.length) {
                return -1;
            } else if (ka1.length > ka2.length) {
                return 1;
            }

            ka1 = o1.getValue();
            ka2 = o2.getValue();
            len = (ka1.length < ka2.length ? ka1.length : ka2.length);

            for (int i = 0; i < len; i++) {
                String v1 = ka1[i];
                String v2 = ka2[i];
                if (v1 == null) {
                    if (v2 != null) {
                        return 1;
                    }
                } else if (v2 == null) {
                    return -1;
                } else {
                    int cmp = v1.compareToIgnoreCase(v2);
                    if (cmp != 0) {
                        return cmp;
                    }
                }
            }
            if (ka1.length < ka2.length) {
                return 1;
            } else if (ka1.length > ka2.length) {
                return -1;
            }
            return 0;
        }

    };

    /**
     * The field <tt>consoleHandler</tt> contains the handler writing to the
     * console.
     */
    private Handler consoleHandler;

    /**
     * The field <tt>fileHandler</tt> contains the file handler for logging.
     */
    private FileHandler fileHandler = null;

    /**
     * The field <tt>files</tt> contains the input files.
     */
    private List<String> files = new ArrayList<String>();

    /**
     * The field <tt>letterOrdering</tt> contains the indicator for letter
     * ordering.
     */
    private boolean letterOrdering = false;

    /**
     * The field <tt>logger</tt> contains the logger for messages.
     */
    private Logger logger = null;

    /**
     * The field <tt>output</tt> contains the name of the output file or
     * <code>null</code> for stdout.
     */
    private String output = null;

    /**
     * The field <tt>pageCompression</tt> contains the indicator for page range
     * compression.
     */
    private boolean pageCompression = true;

    /**
     * The field <tt>startPage</tt> contains the start page specification.
     */
    private String startPage;

    /**
     * The field <tt>styles</tt> contains the style files.
     */
    private ArrayList<String> styles = new ArrayList<String>();

    /**
     * The field <tt>log</tt> contains the name of the transcript file.
     */
    private String transcript;

    /**
     * The field <tt>inputEncoding</tt> contains the input encoding.
     */
    private String inputEncoding = "utf-8";

    /**
     * The field <tt>outputEncoding</tt> contains the output encoding.
     */
    private String outputEncoding = "utf-8";

    /**
     * The field <tt>styleEncoding</tt> contains the encoding for index style
     * files.
     */
    private String styleEncoding = "utf-8";

    /**
     * Creates a new object.
     * 
     */
    public Makeindex() {

        super();
    }

    /**
     * Add an index file.
     * 
     * @param file the index file. If the file is <code>null</code> then the
     *        content is read from stdin instead.
     */
    public void addIndexFile(String file) {

        files.add(file);
    }

    /**
     * Add a style.
     * 
     * @param style the style to add
     */
    public void addStyle(String style) {

        if (style == null) {
            throw new IllegalArgumentException("style");
        }
        styles.add(style);
    }

    /**
     * Get a numbered argument from a list or produce an appropriate error
     * message.
     * 
     * @param args the arguments
     * @param i the index
     * @param option the flag currently processed
     * 
     * @return the argument: arg[i]
     * 
     * @throws UnknownOptionException if the argument is out of bounds
     */
    protected String argument(String[] args, int i, String option)
            throws UnknownOptionException {

        if (i >= args.length) {
            throw new UnknownOptionException(LocalizerFactory.getLocalizer(
                getClass()).format("MissingArgument", option));
        }
        return args[i];
    }

    /**
     * Getter for the files.
     * 
     * @return the files
     */
    public List<String> getFiles() {

        return files;
    }

    /**
     * Getter for the inputEncoding.
     * 
     * @return the inputEncoding
     */
    public String getInputEncoding() {

        return inputEncoding;
    }

    /**
     * Getter for logger. If none is set then a new one is created.
     * 
     * @return the logger
     */
    public Logger getLogger() {

        if (logger == null) {
            logger = Logger.getLogger(Makeindex.class.getName());
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.INFO);
            consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new LogFormatter());
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

        }
        return logger;
    }

    /**
     * Getter for the output.
     * 
     * @return the output
     */
    public String getOutput() {

        return output;
    }

    /**
     * Getter for the outputEncoding.
     * 
     * @return the outputEncoding
     */
    public String getOutputEncoding() {

        return outputEncoding;
    }

    /**
     * Getter for the startPage.
     * 
     * @return the startPage
     */
    public String getStartPage() {

        return startPage;
    }

    /**
     * Getter for the styleEncoding.
     * 
     * @return the styleEncoding
     */
    public String getStyleEncoding() {

        return styleEncoding;
    }

    /**
     * Getter for the styles.
     * 
     * @return the styles
     */
    public List<String> getStyles() {

        return styles;
    }

    /**
     * Getter for the transcript.
     * 
     * @return the transcript
     */
    public String getTranscript() {

        return transcript;
    }

    /**
     * Write an info message to the log.
     * 
     * @param key the message key
     * @param args the arguments
     */
    protected void info(String key, Object... args) {

        getLogger().log(Level.INFO, LOCALIZER.format(key, args));
    }

    /**
     * Getter for the collateGerman.
     * 
     * @return the collateGerman
     */
    public boolean isCollateGerman() {

        return collateGerman;
    }

    /**
     * Getter for the collateSpaces.
     * 
     * @return the collateSpaces
     */
    public boolean isCollateSpaces() {

        return collateSpaces;
    }

    /**
     * Getter for the letterOrdering.
     * 
     * @return the letterOrdering
     */
    public boolean isLetterOrdering() {

        return letterOrdering;
    }

    /**
     * Getter for the pageCompression.
     * 
     * @return the pageCompression
     */
    public boolean isPageCompression() {

        return pageCompression;
    }

    /**
     * Write an error message to the log. This method assures that the banner is
     * printed if required.
     * 
     * @param message the message
     */
    protected void log(String message) {

        if (banner) {
            getLogger().log(Level.INFO,
                LOCALIZER.format("Banner", VERSION, REVISION, //
                    System.getProperty("java.version")));
            banner = false;
        }
        if (message != null) {
            getLogger().log(Level.SEVERE, message);
        }
    }

    /**
     * Log a {@link java.lang.Throwable Throwable} including its stack trace to
     * the logger.
     * 
     * @param text the prefix text to log
     * @param e the Throwable to log
     */
    protected void logException(String text, Throwable e) {

        log(text == null ? "" : text);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream w = new PrintStream(out);
        e.printStackTrace(w);
        w.close();
        logger.log(Level.FINE, out.toString(), e);
    }

    /**
     * Process a combined command line argument consisting of several single
     * letter options.
     * 
     * @param a the argument to process including a leading -
     * 
     * @return <code>true</code> iff all letters could be digested
     */
    protected boolean processShortArguments(String a) {

        for (char c : a.toCharArray()) {
            switch (c) {
                case '-':
                    break;
                case 'g':
                    setCollateGerman(true);
                    break;
                case 'i':
                    addIndexFile(null);
                    break;
                case 'l':
                    setLetterOrdering(true);
                    break;
                case 'q':
                    getLogger().removeHandler(consoleHandler);
                    break;
                case 'r':
                    setPageCompression(false);
                    break;
                case 'c':
                    setCollateSpaces(true);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    /**
     * Run the indexer with some parameters from an array.
     * 
     * @param args the command line arguments
     * 
     * @return the exit code
     */
    public int run(String[] args) {

        if (args == null) {
            throw new IllegalArgumentException();
        }

        try {
            for (int i = 0; i < args.length; i++) {
                String a = args[i];
                if (a == null || "".equals(a)) {
                    // a strange argument is ignored
                    continue;
                }

                if (a.startsWith("--")) {
                    a = a.substring(1);
                }
                if (!a.startsWith("-")) {
                    addIndexFile(a);
                } else if ("-".equals(a)) {
                    addIndexFile(argument(args, ++i, a));

                } else if ("-collateSpaces".startsWith(a)) {
                    setCollateSpaces(true);

                } else if ("-german".startsWith(a)) {
                    setCollateGerman(true);

                } else if ("-input".startsWith(a)) {
                    addIndexFile(null);

                } else if ("-letterOrdering".startsWith(a)) {
                    setLetterOrdering(true);

                } else if ("-output".startsWith(a)) {
                    a = argument(args, ++i, a);
                    if (a.equals("") || a.equals("-")) {
                        a = null;
                    }
                    setOutput(a);

                } else if (a.startsWith("-output=")) {
                    a = a.substring(8);
                    if (a.equals("") || a.equals("-")) {
                        a = null;
                    }
                    setOutput(a);

                } else if ("-page".startsWith(a)) {
                    setStartPage(argument(args, ++i, a));

                } else if (a.startsWith("-page=")) {
                    setStartPage(a.substring(6));

                } else if ("-quiet".startsWith(a)) {
                    getLogger().removeHandler(consoleHandler);

                } else if ("-r".startsWith(a)) {
                    setPageCompression(false);

                } else if ("-style".startsWith(a)) {
                    addStyle(argument(args, ++i, a));

                } else if (a.startsWith("-style=")) {
                    addStyle(a.substring(7));

                } else if ("-transcript".startsWith(a)) {
                    setTranscript(argument(args, ++i, a));

                } else if (a.startsWith("-transcript=")) {
                    setTranscript(a.substring(12));

                } else if ("-Version".startsWith(a)) {
                    log(null);
                    return 1;

                } else if ("-help".startsWith(a)) {
                    log(LOCALIZER.format("Usage", "Indexer"));
                    return 1;

                } else if (!processShortArguments(a)) {
                    log(LOCALIZER.format("UnknownArgument", a));
                    return -1;
                }
            }

            if (transcript == null && files.size() != 0 && files.get(0) != null) {
                transcript = files.get(0).replaceAll("\\.idx$", "") + ".ilg";
            }

            if (transcript != null) {
                fileHandler = new FileHandler(transcript);
                fileHandler.setFormatter(new LogFormatter());
                fileHandler.setLevel(Level.INFO);
                getLogger().addHandler(fileHandler);
            }

            Index index = new Index();

            for (String s : styles) {
                scanStyle(s, index.getParams());
            }

            if (files.size() == 0) {
                scanInput(null, index);
            } else {
                for (String s : files) {
                    scanInput(s, index);
                }
            }
            writeOutput(index);

            if (transcript != null) {
                info("Transcript", transcript);
            }

        } catch (StyleNotFoundException e) {
            log(LOCALIZER.format("StyleNotFound", e.getMessage()));
            return -1;
        } catch (FileNotFoundException e) {
            log(LOCALIZER.format("FileNotFound", e.getMessage()));
            return -1;
        } catch (UnknownOptionException e) {
            log(e.getMessage());
            return -1;
        } catch (Exception e) {
            logException(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return -1;
        } finally {
            if (fileHandler != null) {
                logger.removeHandler(fileHandler);
                fileHandler.close();
            }
            if (consoleHandler != null) {
                logger.removeHandler(consoleHandler);
            }
        }
        return 0;
    }

    /**
     * Load data into the index.
     * 
     * @param file the name of the input file
     * @param index the index
     * 
     * @throws IOException in case of an I/O error
     * @throws RawIndexMissingCharException in case of an error
     * @throws RawIndexEofException in case of an error
     */
    protected void scanInput(String file, Index index)
            throws IOException,
                RawIndexEofException,
                RawIndexMissingCharException {

        log(null);

        Reader reader;
        String fmt;
        String fileName = file;

        if (file == null) {
            fmt = "ScanningStandardInput";
            reader = new InputStreamReader(System.in, inputEncoding);
        } else {
            File f = new File(file);
            if (!f.exists()) {
                fileName = file + ".idx";
                f = new File(fileName);
                if (!f.exists()) {
                    throw new FileNotFoundException(file);
                }
                if (output == null) {
                    output = file + ".ind";
                }
            } else if (output == null) {
                output = file.replaceAll(".idx$", "") + ".ind";
            }
            reader = new InputStreamReader(new FileInputStream(f), //
                inputEncoding);
            fmt = "ScanningInput";
        }
        try {
            info(fmt, fileName);
            Parser parser = new MakeindexParser();
            int[] count = parser.load(reader, file, index, //
                (collateGerman //
                        ? new MakeindexGermanCollator(collateSpaces)
                        : new MakeindexCollator(collateSpaces)));
            info("ScanningInputDone", //
                Integer.toString(count[0]), Integer.toString(count[1]));
        } finally {
            reader.close();
        }
    }

    /**
     * Merge a style into the one already loaded.
     * 
     * @param file the name of the resource
     * @param params the parameters
     * 
     * @throws IOException in case of an I/O error
     * @throws RawIndexException in case of an error
     * @throws RawIndexEofException in case of an error
     * @throws MissingSymbolException in case of an error
     */
    protected void scanStyle(String file, Parameters params)
            throws IOException,
                MissingSymbolException,
                RawIndexEofException,
                RawIndexException {

        log(null);
        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(file), //
                styleEncoding);
        } catch (FileNotFoundException e) {
            throw new StyleNotFoundException(file);
        }
        info("ScanningStyle", file);
        try {
            int[] count =
                    MakeindexParameters.load(reader, file, params, logger);
            info("ScanningStyleDone", //
                Integer.toString(count[0]), Integer.toString(count[1]));
        } finally {
            reader.close();
        }
    }

    /**
     * Setter for the collateGerman.
     * 
     * @param collateGerman the collateGerman to set
     */
    public void setCollateGerman(boolean collateGerman) {

        this.collateGerman = collateGerman;
    }

    /**
     * Setter for the collateSpaces.
     * 
     * @param collateSpaces the collateSpaces to set
     */
    public void setCollateSpaces(boolean collateSpaces) {

        this.collateSpaces = collateSpaces;
    }

    /**
     * Setter for the inputEncoding.
     * 
     * @param inputEncoding the inputEncoding to set
     */
    public void setInputEncoding(String inputEncoding) {

        this.inputEncoding = inputEncoding;
    }

    /**
     * Setter for the letterOrdering.
     * 
     * @param letterOrdering the letterOrdering to set
     */
    public void setLetterOrdering(boolean letterOrdering) {

        this.letterOrdering = letterOrdering;
    }

    /**
     * Setter for logger.
     * 
     * @param logger the logger to set
     */
    public void setLogger(Logger logger) {

        this.logger = logger;
    }

    /**
     * Setter for the output.
     * 
     * @param output the output to set
     */
    public void setOutput(String output) {

        this.output = output;
    }

    /**
     * Setter for the outputEncoding.
     * 
     * @param outputEncoding the outputEncoding to set
     */
    public void setOutputEncoding(String outputEncoding) {

        this.outputEncoding = outputEncoding;
    }

    /**
     * Setter for the pageCompression.
     * 
     * @param pageCompression the pageCompression to set
     */
    public void setPageCompression(boolean pageCompression) {

        this.pageCompression = pageCompression;
    }

    /**
     * Setter for the startPage.
     * 
     * @param startPage the startPage to set
     */
    public void setStartPage(String startPage) {

        this.startPage = startPage;
    }

    /**
     * Setter for the styleEncoding.
     * 
     * @param styleEncoding the styleEncoding to set
     */
    public void setStyleEncoding(String styleEncoding) {

        this.styleEncoding = styleEncoding;
    }

    /**
     * Setter for the transcript.
     * 
     * @param transcript the transcript to set
     */
    public void setTranscript(String transcript) {

        this.transcript = transcript;
    }

    /**
     * Generate the output.
     * 
     * @param index the index
     * 
     * @throws IOException in case of an I/O error
     */
    protected void writeOutput(Index index) throws IOException {

        Writer w;
        String fmt;
        if (output == null) {
            fmt = "GeneratingOutput";
            w = new OutputStreamWriter(System.out, outputEncoding);
        } else {
            fmt = "GeneratingOutputFile";
            w = new OutputStreamWriter(new FileOutputStream(output), //
                outputEncoding);
        }
        try {
            Parameters params = index.getParams();
            IndexWriter indexWriter = new MakeindexWriter(w, params);
            int[] warn = {0};
            PageProcessor pageProcessor =
                    new MakeindexPageProcessor(params, logger);

            info("Sorting");
            comparisons = 0;
            List<Entry> entries = index.sort(comp, pageProcessor, warn);
            info("SortingDone", Long.toString(comparisons));
            info(fmt, output);

            int[] count = indexWriter.write(entries, logger, startPage);

            info("GeneratingOutputDone", //
                Integer.toString(count[0]), //
                Integer.toString(count[1] + warn[0]));
        } finally {
            w.close();
        }
        if (output == null) {
            info("StandardOutput", output);
        } else {
            info("Output", output);
        }
    }

}