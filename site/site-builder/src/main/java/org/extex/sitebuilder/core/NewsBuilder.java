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

package org.extex.sitebuilder.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class NewsBuilder {

    /**
     * The field <tt>levelMap</tt> contains the mapping from Velocity error
     * levels to those of the Java logger.
     */
    private static Map<Integer, Level> levelMap = new HashMap<Integer, Level>();

    static {
        levelMap.put(Integer.valueOf(LogChute.ERROR_ID), Level.SEVERE);
        levelMap.put(Integer.valueOf(LogChute.WARN_ID), Level.WARNING);
        levelMap.put(Integer.valueOf(LogChute.INFO_ID), Level.INFO);
        levelMap.put(Integer.valueOf(LogChute.DEBUG_ID), Level.FINE);
        levelMap.put(Integer.valueOf(LogChute.TRACE_ID), Level.FINEST);
    }

    /**
     * The field <tt>ls</tt> contains the adapter for the logging.
     */
    private LogChute logChute = new LogChute() {

        public void init(RuntimeServices rs) throws Exception {

            //
        }

        public boolean isLevelEnabled(int level) {

            return false;
        }

        public void log(int id, String message) {

            Level l = levelMap.get(Integer.valueOf(id));
            logger.log(l != null ? l : Level.FINER, message);
        }

        public void log(int id, String message, Throwable cause) {

            Level l = levelMap.get(Integer.valueOf(id));
            logger.log(l != null ? l : Level.FINER, message, cause);
        }

    };

    /**
     * The field <tt>baseDirectory</tt> contains the name of the base
     * directory for the files to be transformed.
     */
    private File baseDirectory = new File("src/site/news");

    /**
     * The field <tt>logger</tt> contains the logger.
     */
    private Logger logger;

    /**
     * The field <tt>max</tt> contains the maximal number of articles to be
     * published.
     */
    private int max = 8;

    /**
     * The field <tt>targetFile</tt> contains the target directory.
     */
    private File targetFile = new File("target/site/rss/2.0/news.rss");

    /**
     * The field <tt>template</tt> contains the name of the template.
     */
    private String template = "org/extex/sitebuilder/news.vm";

    /**
     * Creates a new object.
     */
    public NewsBuilder() {

        logger = Logger.getLogger(SiteBuilder.class.getName());
    }

    /**
     * Getter for the logger.
     * 
     * @return the logger
     */
    public Logger getLogger() {

        return logger;
    }

    /**
     * Create a new engine and initialize it.
     * 
     * @param context the context
     * 
     * @return the engine
     * 
     * @throws Exception in case of an error
     */
    private VelocityEngine makeEngine(VelocityContext context) throws Exception {

        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, logChute);

        Properties prop = new Properties();
        String name = getClass().getName().replace('.', '/') + ".properties";
        InputStream stream =
                getClass().getClassLoader().getResourceAsStream(name);
        if (stream == null) {
            throw new FileNotFoundException(name);
        }
        try {
            prop.load(stream);
        } finally {
            stream.close();
        }
        engine.init(prop);
        for (Entry<Object, Object> kv : prop.entrySet()) {
            context.put(kv.getKey().toString(), kv.getValue().toString());
        }
        return engine;
    }

    /**
     * Generate the target directory structure and populate it.
     * 
     * @throws Exception in case of an error
     */
    public void run() throws Exception {

        if (!baseDirectory.isDirectory()) {
            return;
        }

        targetFile.getParentFile().mkdirs();

        int count = max;
        File[] files = baseDirectory.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {

                return name.endsWith(".xml");
            }
        });
        Arrays.sort(files, new Comparator<File>() {

            public int compare(File o1, File o2) {

                return o2.toString().compareTo(o1.toString());
            }

        });

        StringBuilder buffer = new StringBuilder();

        for (File f : files) {
            if (count-- < 0) {
                break;
            }
            Reader in = new BufferedReader(new FileReader(f));
            try {
                for (int c = in.read(); c >= 0 && c != '>'; c = in.read()) {
                    // skip the XML declaration
                }
                for (int c = in.read(); c >= 0; c = in.read()) {
                    buffer.append((char) c);
                }
            } finally {
                in.close();
            }
        }
        VelocityContext context = new VelocityContext();
        context.put("content", buffer.toString());
        context.put("currentDate", new Date());
        context.put("dateFormat", new SimpleDateFormat("d/m/yyyy",
            Locale.ENGLISH));
        VelocityEngine engine = makeEngine(context);
        Writer writer = new BufferedWriter(new FileWriter(targetFile));
        try {
            engine.getTemplate(template).merge(context, writer);
        } finally {
            writer.close();
        }
    }

    /**
     * Setter for the baseDirectory.
     * 
     * @param baseDirectory the baseDirectory to set
     */
    protected void setBaseDirectory(File baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Setter for the logger.
     * 
     * @param logger the logger to set
     */
    protected void setLogger(Logger logger) {

        this.logger = logger;
    }

    /**
     * Setter for the max.
     * 
     * @param max the max to set
     */
    protected void setMax(int max) {

        this.max = max;
    }

    /**
     * Setter for the target file.
     * 
     * @param targetFile the target file to set
     */
    protected void setTargetFile(File targetFile) {

        this.targetFile = targetFile;
    }

    /**
     * Setter for the template.
     * 
     * @param template the template to set
     */
    protected void setTemplate(String template) {

        this.template = template;
    }

}
