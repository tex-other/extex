/*
 * Copyright (C) 2004-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.tex.typesetter.mark;

import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.scanner.type.token.CodeToken;
import org.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\mark</code>.
 * 
 * <doc name="mark">
 * <h3>The Primitive <tt>\mark</tt></h3>
 * <p>
 * The primitive <tt>\mark</tt> places its argument as a mark node on the
 * current node list. The argument is expanded during this operation as in
 * {@link org.extex.unit.tex.macro.Edef <tt>\edef</tt>}.
 * </p>
 * <p>
 * The tokens are stored in the current node list. They are not affected by
 * grouping in any way.
 * </p>
 * <p>
 * Suppose we have several pages. Page 1 contains no mark. Page 2 contains the
 * marks <tt>a</tt> and <tt>b</tt>. Page 3 does not contain any marks. Page
 * 4 contains the mark <tt>c</tt> and page 5 does not contain any marks. The
 * marks and the expansion text of the primitives
 * {@link org.extex.unit.tex.typesetter.mark.Topmark <tt>\topmark</tt>},
 * {@link org.extex.unit.tex.typesetter.mark.Firstmark <tt>\firstmark</tt>},
 * and {@link org.extex.unit.tex.typesetter.mark.Botmark <tt>\botmark</tt>} are
 * shown in the table below.
 * </p>
 * <table format="ccccccc">
 * <tr>
 * <td align="center"></td>
 * <td align="center">on page 1</td>
 * <td align="center">on page 2</td>
 * <td align="center">on page 3</td>
 * <td align="center">on page 4</td>
 * <td align="center">on page 5</td>
 * </tr>
 * <tr>
 * <td>marks</td>
 * <td align="center"></td>
 * <td align="center"><tt>\mark{a}</tt><br/><tt>\mark{b}</tt></td>
 * <td align="center"></td>
 * <td align="center"><tt>\mark{c}</tt></td>
 * <td align="center"></td>
 * </tr>
 * <tr>
 * <td><tt>\topmark</tt></td>
 * <td align="center"></td>
 * <td align="center">a</td>
 * <td align="center">b</td>
 * <td align="center">b</td>
 * <td align="center">c</td>
 * </tr>
 * <tr>
 * <td><tt>\firstmark</tt></td>
 * <td align="center"></td>
 * <td align="center">a</td>
 * <td align="center">b</td>
 * <td align="center">c</td>
 * <td align="center">c</td>
 * </tr>
 * <tr>
 * <td><tt>\botmark</tt></td>
 * <td align="center"></td>
 * <td align="center">b</td>
 * <td align="center">b</td>
 * <td align="center">c</td>
 * <td align="center">c</td>
 * </tr>
 * </table>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;mark&rang;
 *      &rarr; <tt>\mark</tt> &lang;expanded tokens&rang;  </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \mark{abc}  </pre>
 * 
 * </doc>
 * 
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:4431 $
 */
public class Mark extends Marks {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for
     * serialization.
     */
    protected static final long serialVersionUID = 2007L;

    /**
     * Creates a new object.
     * 
     * @param token the initial token for the primitive
     */
    public Mark(CodeToken token) {

        super(token);
    }

    /**
     * Get the key for this mark.
     * 
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * 
     * @return the key for the mark primitive
     * 
     * @see org.extex.unit.tex.typesetter.mark.AbstractMarkCode#getKey(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    @Override
    protected String getKey(Context context, TokenSource source,
            Typesetter typesetter) {

        return "0";
    }

}
