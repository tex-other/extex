/*
 * Copyright (C) 2003-2008 The ExTeX Group and individual authors listed below
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

package org.extex.exbib.core.bst.code.impl;

import org.extex.exbib.core.bst.BstProcessor;
import org.extex.exbib.core.bst.code.AbstractCode;
import org.extex.exbib.core.bst.node.TokenFactory;
import org.extex.exbib.core.bst.node.impl.TString;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.io.Locator;

/**
 * B<small>IB</small>T<sub>E</sub>X built-in function
 * <code>substring$</code>
 * <p>
 * This function computes the substring of a string. It takes three arguments
 * from the stack. The first argument is an integer denoting the maximal length
 * of the substring. At most this many character units will be extracted.
 * </p>
 * <p>
 * The second argument is the start index of the substring &ndash; an integer.
 * If start is positive then the first characters are extracted. The index 1
 * denotes the first character. If start is negative then the last characters
 * are extracted. The index -1 denotes the last character.
 * </p>
 * <p>
 * The third argument is the string to take the substring from. The string is
 * interpreted taking blocks in braces into account. A block in braces counts as
 * one character unit. Thus it is guaranteed that blocks &ndash; which have a
 * special meaning in T<sub>E</sub>X and L<sup>A</sup>T<sub>E</sub>X
 * &ndash; are never broken.
 * </p>
 * <p>
 * The resulting substring is pushed back to the stack.
 * </p>
 * <p>
 * If the stack has less than three arguments when the function is invoked or
 * the types of the arguments do not fit then an error is raised.
 * </p>
 * <img src="doc-files/substring.png"/>
 * <p>
 * The following example is taken from <tt>alpha.bst</tt>:
 * </p>
 * 
 * <pre>
 *   cite$ #1 #3 substring$
 * </pre>
 * 
 * <hr />
 * 
 * <dl>
 * <dt>B<small>IB</small>T<sub>E</sub>X documentation:
 * <dt>
 * <dd> Pops the top three literals (they are the two integers literals <i>len</i>
 * and <i>start</i>, and a string literal, in that order). It pushes the
 * substring of the (at most) <i>len</i> consecutive characters starting at the
 * <i>start<i>th character (assuming 1-based indexing) if <i>start</i> is
 * positive, and ending at the <i>-start</i>th character from the end if
 * <i>start</i> is negative (where the first character from the end is the last
 * character). </dd>
 * </dl>
 * 
 * <dl>
 * <dt>B<small>IB</small>T<sub>E</sub>X web documentation:</dt>
 * <dd> The <code>built_in</code> function <code>substring$</code> pops the
 * top three literals (they are the two integers literals <code>pop_lit1</code>
 * and <code>pop_lit2</code> and a string literal, in that order). It pushes
 * the substring of the (at most) <code>pop_lit1</code> consecutive characters
 * starting at the <code>pop_lit2</code>th character (assuming 1-based
 * indexing) if <code>pop_lit2</code> is positive, and ending at the
 * <code>-pop_lit2</code>th character from the end if <code>pop_lit2</code>
 * is negative (where the first character from the end is the last character).
 * If any of the types is incorrect, it complain and pushes the null string.
 * </dd>
 * </dl>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Substring extends AbstractCode {

    /**
     * Create a new object.
     */
    public Substring() {

        super();
    }

    /**
     * Creates a new object.
     * 
     * @param name the function name in the processor context
     */
    public Substring(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.code.AbstractCode#execute(
     *      BstProcessor, org.extex.exbib.core.db.Entry,
     *      org.extex.exbib.core.io.Locator)
     */
    @Override
    public void execute(BstProcessor processor, Entry entry, Locator locator)
            throws ExBibException {

        int len = processor.popInteger(locator).getInt();
        int start = processor.popInteger(locator).getInt();
        TString t = processor.popString(locator);
        String s = t.getValue();
        int slen = s.length();
        int end;

        if (len <= 0 || start == 0) {
            start = 0;
            end = 0;
        } else if (start-- > 0) {
            end = (start + len >= slen ? slen : start + len);
        } else {
            end = slen + start + 2;
            start = (end <= len ? 0 : end - len);
        }

        if (start >= end) {
            processor.push(TokenFactory.T_EMPTY);
        } else if (start == 0 && end >= slen) {
            processor.push(t);
        } else {
            processor.push(new TString(s.substring(start, end), locator));
        }
    }

}
