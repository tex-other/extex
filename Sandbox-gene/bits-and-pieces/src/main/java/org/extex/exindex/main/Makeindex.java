/*
 * Copyright (C) 2007 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import org.extex.exindex.core.makeindex.Entry;
import org.extex.exindex.core.makeindex.Index;
import org.extex.exindex.core.makeindex.MakeindexParameters;
import org.extex.exindex.core.makeindex.Parameters;
import org.extex.exindex.core.makeindex.exceptions.MissingSymbolException;
import org.extex.exindex.core.makeindex.normalizer.MakeindexCollator;
import org.extex.exindex.core.makeindex.normalizer.MakeindexGermanCollator;
import org.extex.exindex.core.makeindex.pages.MakeindexPageProcessor;
import org.extex.exindex.core.makeindex.pages.PageProcessor;
import org.extex.exindex.core.makeindex.parser.MakeindexParser;
import org.extex.exindex.core.makeindex.parser.Parser;
import org.extex.exindex.core.makeindex.writer.IndexWriter;
import org.extex.exindex.core.makeindex.writer.MakeindexWriter;
import org.extex.exindex.main.exception.MainException;
import org.extex.framework.i18n.Localizer;
import org.extex.framework.i18n.LocalizerFactory;
import org.extex.logging.LogFormatter;

/**
 * This is the main program for an indexer a la Makeindex or Xindy.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Makeindex {

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private static final Localizer LOCALIZER =
            LocalizerFactory.getLocalizer(Makeindex.class);

    /**
     * Log a {@link java.lang.Throwable Throwable} including its stack trace to
     * the logger.
     * 
     * @param logger the target logger
     * @param text the prefix text to log
     * @param e the Throwable to log
     */
    protected static void logException(Logger logger, String text, Throwable e) {

        logger.log(Level.SEVERE, text == null ? "" : text);
        logger.log(Level.FINE, "", e);
    }

    /**
     * This is the command line interface to the indexer.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            System.exit(new Makeindex().run(args));
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    /**
     * The field <tt>banner</tt> contains the indicator that the banner needs
     * to be written.
     */
    private boolean banner;

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
     * The field <tt>comp</tt> contains the comparator.
     */
    private Comparator<Entry> comp = new Comparator<Entry>() {

        public int compare(Entry o1, Entry o2) {

            String[] ka1 = o1.getKey();
            String[] ka2 = o2.getKey();
            int len = (ka1.length < ka2.length ? ka1.length : ka2.length);

            for (int i = 0; i < len; i++) {
                int cmp = ka1[i].compareToIgnoreCase(ka2[i]);
                if (cmp != 0) {
                    return cmp;
                }
            }
            if (ka1.length < ka2.length) {
                return 1;
            } else if (ka1.length > ka2.length) {
                return -1;
            }
            return o1.getValue().compareTo(o2.getValue());
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
     * The field <tt>index</tt> contains the enclosed index.
     */
    private Index index;

    /**
     * The field <tt>logger</tt> contains the logger for messages.
     */
    private Logger logger;

    /**
     * The field <tt>output</tt> contains the name of the output file or
     * <code>null</code> for stdout.
     */
    private String output = null;

    /**
     * The field <tt>pageCompression</tt> contains the indicator for page
     * range compression.
     */
    private boolean pageCompression = true;

    /**
     * The field <tt>startPage</tt> contains the start page specification.
     */
    private String startPage;

    /**
     * The field <tt>log</tt> contains the name of the transcript file.
     */
    private String transcript;

    /**
     * Creates a new object.
     * 
     * @throws IOException in case of an I/O error when reading the default
     *         settings
     */
    public Makeindex() throws IOException {

        super();

        banner = true;
        index = new Index();

        logger = Logger.getLogger(Makeindex.class.getName());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.INFO);
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LogFormatter());
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
    }

    /**
     * Get a numbered argument from a list or produce an appropriate error
     * message.
     * 
     * @param a the flag currently processed
     * @param args the arguments
     * @param i the index
     * @return the argument: arg[i]
     * 
     * @throws MainException if the argument is out of bounds
     */
    private String getArg(String a, String[] args, int i) throws MainException {

        if (i >= args.length) {
            throw new MainException(LocalizerFactory.getLocalizer(getClass())
                .format("MissingArgument", a));
        }
        return args[i];
    }

    /**
     * Getter for logger.
     * 
     * @return the logger
     */
    public Logger getLogger() {

        return logger;
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
            throw new NullPointerException();
        }
        List<String> files = new ArrayList<String>();
        List<String> styles = new ArrayList<String>();

        try {
            for (int i = 0; i < args.length; i++) {
                String a = args[i];
                if (a == null || "".equals(a)) {
                    // strange argument is ignored
                    continue;
                }

                if (a.startsWith("--")) {
                    a = a.substring(1);
                }
                if (!a.startsWith("-")) {
                    files.add(a);
                } else if ("-".equals(a)) {
                    files.add(getArg(a, args, ++i));

                } else if ("-collateSpaces".startsWith(a)) {
                    collateSpaces = true;

                } else if ("-german".startsWith(a)) {
                    collateGerman = true;

                } else if ("-input".startsWith(a)) {
                    files.add(null);

                } else if ("-letterOrdering".startsWith(a)) {
                    // TODO letter ordering

                } else if ("-output".startsWith(a)) {
                    output = getArg(a, args, ++i);
                    if (output.equals("") || output.equals("-")) {
                        output = null;
                    }

                } else if ("-page".startsWith(a)) {
                    startPage = getArg(a, args, ++i);

                } else if ("-quiet".startsWith(a)) {
                    logger.removeHandler(consoleHandler);

                } else if ("-r".startsWith(a)) {
                    pageCompression = false;

                } else if ("-style".startsWith(a)) {
                    styles.add(getArg(a, args, ++i));

                } else if ("-transcript".startsWith(a)) {
                    transcript = getArg(a, args, ++i);

                } else if ("-Version".startsWith(a)) {
                    showBanner();
                    return 1;

                } else if ("-help".startsWith(a)) {
                    logger.log(Level.SEVERE, LOCALIZER.format("Usage"),
                        "Indexer");
                    return 1;

                } else {
                    throw new MainException(LocalizerFactory.getLocalizer(
                        getClass()).format("UnknownArgument", a));
                }

            }

            if (transcript == null && files.size() != 0 && files.get(0) != null) {
                transcript = files.get(0).replaceAll("\\.idx$", "") + ".ilg";
            }

            if (transcript != null) {
                fileHandler = new FileHandler(transcript);
                fileHandler.setFormatter(new LogFormatter());
                fileHandler.setLevel(Level.INFO);
                logger.addHandler(fileHandler);
            }

            for (String s : styles) {
                scanStyle(s);
            }

            if (files.size() == 0) {
                scanInput(null);
            } else {
                for (String s : files) {
                    scanInput(s);
                }
            }
            writeOutput(logger, index.getParams());

            if (transcript != null) {
                logger.log(Level.INFO, LOCALIZER.format("Transcript",
                    transcript));
            }

        } catch (FileNotFoundException e) {
            showBanner();
            logger.log(Level.SEVERE, LOCALIZER.format("FileNotFound", e
                .getMessage()));
            logger.log(Level.FINE, "", e);
            return -1;
        } catch (Exception e) {
            showBanner();
            logger.log(Level.SEVERE, LOCALIZER.format("SevereError", e
                .toString()));
            logger.log(Level.FINE, "", e);
            e.printStackTrace();
            return -1;
        }
        if (fileHandler != null) {
            fileHandler.flush();
        }
        return 0;
    }

    /**
     * Load data into the index.
     * 
     * @param file the name of the input file
     * 
     * @throws IOException in case of an I/O error
     * @throws RawIndexMissingCharException
     * @throws RawIndexEofException
     */
    protected void scanInput(String file)
            throws IOException,
                RawIndexEofException,
                RawIndexMissingCharException {

        showBanner();

        InputStream stream;

        if (file == null) {
            logger.log(Level.INFO, LOCALIZER.format("ScanningStandardInput"));
            stream = System.in;
        } else {
            logger.log(Level.INFO, LOCALIZER.format("ScanningInput", file));
            File f = new File(file);
            if (!f.exists()) {
                f = new File(file + ".idx");
                if (!f.exists()) {
                    throw new FileNotFoundException(file);
                }
                if (output == null) {
                    output = file + ".ind";
                }
            } else if (output == null) {
                output = file + ".ind";
            }
            stream = new FileInputStream(f);
        }
        int[] count;
        Reader reader = new InputStreamReader(stream);
        try {
            Parser parser = new MakeindexParser();
            count =
                    parser.load(reader, file, index, (collateGerman
                            ? new MakeindexGermanCollator(collateSpaces)
                            : new MakeindexCollator(collateSpaces)));
        } finally {
            reader.close();
        }
        logger.log(Level.INFO, LOCALIZER.format("ScanningInputDone", //
            Integer.toString(count[0]), Integer.toString(count[1])));
    }

    /**
     * Merge a style into the one already loaded.
     * 
     * @param file the name of the resource
     * 
     * @throws IOException in case of an I/O error
     * @throws RawIndexException
     * @throws RawIndexEofException
     * @throws MissingSymbolException
     */
    protected void scanStyle(String file)
            throws IOException,
                MissingSymbolException,
                RawIndexEofException,
                RawIndexException {

        showBanner();
        logger.log(Level.INFO, LOCALIZER.format("ScanningStyle", file));
        Reader reader = new InputStreamReader(new FileInputStream(file));
        int[] count;
        try {
            count = MakeindexParameters.load(reader, file, index.getParams());
        } finally {
            reader.close();
        }
        logger.log(Level.INFO, LOCALIZER.format("ScanningStyleDone", //
            Integer.toString(count[0]), Integer.toString(count[1])));
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
     * Show the program banner.
     */
    private void showBanner() {

        if (banner) {
            String build = "$Revision$".replaceAll("[^0-9]", "");
            logger.log(Level.INFO, LOCALIZER.format("Banner", build, //
                System.getProperty("java.version")));
            banner = false;
        }
    }

    /**
     * Generate the output.
     * 
     * @param logger the logger
     * @param params
     * 
     * @throws IOException in case of an I/O error
     */
    protected void writeOutput(Logger logger, Parameters params)
            throws IOException {

        int[] count;
        Writer w;
        String fmt;
        if (output == null) {
            fmt = "GeneratingOutput";
            w = new PrintWriter(System.out);
        } else {
            fmt = "GeneratingOutputFile";
            w = new FileWriter(output);
        }
        IndexWriter indexWriter = new MakeindexWriter(w, params);
        PageProcessor pageProcessor =
                new MakeindexPageProcessor(params, logger);

        try {
            logger.log(Level.INFO, LOCALIZER.format("Sorting"));
            int i = 0; // TODO sorting info
            logger.log(Level.INFO, LOCALIZER.format("SortingDone", //
                Integer.toString(i)));
            logger.log(Level.INFO, LOCALIZER.format(fmt, output));
            List<Entry> entries = index.sort(comp, pageProcessor);
            count = indexWriter.write(entries, logger, startPage);
        } finally {
            w.close();
        }
        logger.log(Level.INFO, LOCALIZER.format("GeneratingOutputDone", //
            Integer.toString(count[0]), Integer.toString(count[1])));
        if (output == null) {
            logger.log(Level.INFO, LOCALIZER.format("StandardOutput", output));
        } else {
            logger.log(Level.INFO, LOCALIZER.format("Output", output));
        }
    }
}