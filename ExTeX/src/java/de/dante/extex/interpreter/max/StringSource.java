/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.max;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides a token source which is fed from a sting.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class StringSource extends Moritz {

    /**
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision$
     */
    private class TStream implements TokenStream {

        /**
         * The field <tt>ptr</tt> contains the ...
         */
        private int ptr = 0;

        /**
         * The field <tt>cs</tt> contains the ...
         */
        private CharSequence cs;

        /**
         * The field <tt>stack</tt> contains the ...
         */
        private List stack = new ArrayList();

        /**
         * Creates a new object.
         *
         * @param cs the character sequence to read from
         */
        protected TStream(final CharSequence cs) {

            this.cs = cs;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
         */
        public boolean closeFileStream() {

            ptr = cs.length();
            return false;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#get(
         *      de.dante.extex.scanner.type.TokenFactory,
         *      de.dante.extex.interpreter.Tokenizer)
         */
        public Token get(final TokenFactory factory, final Tokenizer tokenizer)
                throws ScannerException {

            int size = stack.size();
            if (size > 0) {
                return (Token) stack.remove(size - 1);
            }
            if (ptr < cs.length()) {
                UnicodeChar c = new UnicodeChar(cs.charAt(ptr++));
                try {
                    return factory.createToken(tokenizer.getCatcode(c), c,
                            Namespace.DEFAULT_NAMESPACE);
                } catch (CatcodeException e) {
                    throw new ScannerException(e);
                }
            }
            return null;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
         */
        public Locator getLocator() {

            return new Locator("", 0, cs.toString(), ptr);
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#isEof()
         */
        public boolean isEof() throws ScannerException {

            return ptr >= cs.length();
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
         */
        public boolean isFileStream() {

            return false;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#put(
         *      de.dante.extex.scanner.type.Token)
         */
        public void put(final Token token) {

            stack.add(token);
        }
    }

    /**
     * Creates a new object.
     *
     * @param cs the character sequence to read from
     *
     * @throws ConfigurationException in case of errors in the configuration
     */
    public StringSource(final CharSequence cs) throws ConfigurationException {

        super();
        addStream(new TStream(cs));
    }

}