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

package org.extex.latexParser.impl.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;

import org.extex.latexParser.impl.Macro;
import org.extex.latexParser.impl.Parser;
import org.extex.latexParser.impl.SystemException;
import org.extex.latexParser.impl.macro.GenericMacro;
import org.extex.scanner.api.exception.ScannerException;

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public final class DefinitionLoader {

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param stream
     * @param parser
     * @throws IOException
     * @throws ScannerException
     */
    public static void load(InputStream stream, Parser parser)
            throws IOException,
                ScannerException {

        LineNumberReader r =
                new LineNumberReader(new InputStreamReader(stream));
        try {
            for (String s = r.readLine(); s != null; s = r.readLine()) {
                if (s.startsWith("\\")) {
                    loadMacro(s, parser);
                } else if (s.matches("[ ]*%.*")) {
                    // ignore comments
                } else {
                    loadActive(s, parser);
                }
            }
        } finally {
            r.close();
        }

    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param s
     * @param parser
     */
    private static void loadActive(String s, Parser parser) {

        char c = s.charAt(0);
        if (s.length() == 1) {
            parser.def(c, new GenericMacro(null));
            return;
        }
        // TODO gene: loadActive unimplemented
        throw new RuntimeException("loadActive unimplemented");
    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param s
     * @param parser
     * @throws SystemException in case of an error
     */
    private static void loadMacro(String s, Parser parser)
            throws SystemException {

        int i = s.indexOf('[');
        if (i > 0) {
            int eq = s.indexOf('=', 2);
            if (eq > 0) {

                // TODO gene: loadMacro unimplemented
                throw new RuntimeException("loadMacro []= unimplemented");
            }

            parser.def(s.substring(1, i), new GenericMacro(s.substring(i)));
            return;
        }
        i = s.indexOf('=', 2);
        if (i > 0) {
            String name = s.substring(1, i);
            parser.def(name, makeMacro(s.substring(i + 1), null));
        }

        parser.def(s.substring(1), new GenericMacro(null));
    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param className
     * @param spec
     * @return
     * @throws SystemException in case of an error
     */
    private static Macro makeMacro(String className, String spec)
            throws SystemException {

        try {
            Class<?> theClass = Class.forName(className);
            return (Macro) theClass.getConstructor(String.class).newInstance(
                spec);
        } catch (ClassNotFoundException e) {
            throw new SystemException("", e);
        } catch (ClassCastException e) {
            throw new SystemException("", e);
        } catch (InstantiationException e) {
            throw new SystemException("", e);
        } catch (IllegalAccessException e) {
            throw new SystemException("", e);
        } catch (IllegalArgumentException e) {
            throw new SystemException("", e);
        } catch (SecurityException e) {
            throw new SystemException("", e);
        } catch (InvocationTargetException e) {
            throw new SystemException("", e);
        } catch (NoSuchMethodException e) {
            throw new SystemException("", e);
        }
    }

    /**
     * Creates a new object.
     */
    private DefinitionLoader() {

        super();
    }

}