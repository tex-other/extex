/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives.string;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.ExpandableCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.LetterToken;
import de.dante.extex.scanner.OtherToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\lowercase</code>.
 *
 * <doc name="lowercase">
 * <h3>The Primitive <tt>\lowercase</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\lowercase</tt> &lang;...&rang; </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \lowercase ...  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Lowercase extends AbstractCode implements ExpandableCode {

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Lowercase(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        expand(prefix, context, source, typesetter);
        return true;
    }

    /**
     * @see de.dante.extex.interpreter.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Tokens toks = source.getTokens();
        String namespace = context.getNamespace();

        if (toks == null) {
            throw new RuntimeException("unimplemented");
            //TODO unimplemented
        }
        Token[] result = new Token[toks.length()];
        TokenFactory factory = context.getTokenFactory();
        Token t;

        for (int i = 0; i < toks.length(); i++) {
            t = toks.get(i);
            if (t instanceof LetterToken || t instanceof OtherToken) {
                UnicodeChar uc = context.getLccode(t.getChar());
                if (uc != null && //
                    uc.getCodePoint() != 0 && //
                    !uc.equals(t.getChar())) {
                    t = factory.newInstance(t.getCatcode(), uc, namespace);
                }
            }
            result[i] = t;
        }

        source.push(result);
    }
}
