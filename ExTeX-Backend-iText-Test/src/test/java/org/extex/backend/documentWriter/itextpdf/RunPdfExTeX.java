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

package org.extex.backend.documentWriter.itextpdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.extex.ExTeX;
import org.extex.core.exception.helping.HelpingException;

/**
 * run ExTeX with the pdf backend.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class RunPdfExTeX {

    /**
     * main.
     * 
     * @param args The command line arguments.
     * @throws Exception if an error occurred.
     */
    public static void main(String[] args) throws Exception {

        Properties prop = new Properties(System.getProperties());
        prop.setProperty("extex.output", "itext");
        File file = new File("test.pdf");
        File filexml = new File("test.xml");
        run(prop, "\\font\\hugo=LinLibertine " + "\\hugo " + "Hugo " + "\\end",
            new FileOutputStream(file));
        System.out.println("create " + file.getPath());

        prop.setProperty("extex.output", "xml");
        run(prop, "\\font\\hugo=LinLibertine " + "\\hugo " + "Hugo " + "\\end",
            new FileOutputStream(filexml));
        System.out.println("create " + file.getPath());

    }

    /**
     * Run ExTeX.
     * 
     * @param properties The properties.
     * @param code The code.
     * @param out The output stream.
     * @throws HelpingException if an error occurred.
     */
    public static void run(Properties properties, String code, OutputStream out)
            throws HelpingException {

        properties.setProperty("extex.code", code);
        properties.setProperty("extex.file", "");
        properties.setProperty("extex.nobanner", "true");
        properties.setProperty("extex.config", "base-test.xml");

        ExTeX extex = new ExTeX(properties);
        extex.setOutStream(out);

        try {
            extex.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}