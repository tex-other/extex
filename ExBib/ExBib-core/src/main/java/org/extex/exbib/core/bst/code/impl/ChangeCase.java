/*
 * Copyright (C) 2003-2008 Gerd Neugebauer
 * This file is part of ExBib a BibTeX compatible database.
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

package org.extex.exbib.core.bst.code.impl;

import org.extex.exbib.core.bst.Processor;
import org.extex.exbib.core.bst.code.AbstractCode;
import org.extex.exbib.core.bst.exception.ExBibIllegalValueException;
import org.extex.exbib.core.bst.node.impl.TString;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.io.Locator;
import org.extex.framework.i18n.Localizer;
import org.extex.framework.i18n.LocalizerFactory;

/**
 * BibT<sub>E</sub>X built-in function <code>change.case</code>
 * 
 * <dl>
 * <dt>BibT<sub>E</sub>X documentation:</dt>
 * <dd> Pops the top two (string) literals; it changes the case of the second
 * according to the specifications of the first, as follows. (Note: The word
 * `letters' in the next sentence refers only to those at brace-level 0, the
 * top-most brace level; no other characters are changed, except perhaps for
 * `special characters'', described in Section 4.) <br>
 * If the first literal is the string `<code>t</code>', it converts to lower
 * case all letters except the very first character in the string, which it
 * leaves alone, and except the first character following any colon and then
 * nonnull white space, which it also leaves alone; if it's the string `<code>l</code>',
 * it converts all letters to lower case; and if it's the string `<code>u</code>',
 * it converts all letters to upper case. It then pushes this resulting string.
 * If either type is incorrect, it complains and pushes the null string;
 * however, if both types are correct but the specification string (i.e., the
 * first string) isn't one of the legal ones, it merely pushes the second back
 * onto the stack, after complaining. (Another note: It ignores case differences
 * in the specification string; for example, the strings <code>t</code> and
 * <code>T<code> are equivalent for the purposes of this built-in
 *  function.)
 * </dd></dl>
 *
 * <dl><dt>BibT<sub>E</sub>X web documentation:</dt><dd>
 *  The <code>built_in</code> function <code>change.case$</code> pops
 *  the top two (string) literals; it changes the case of the second
 *  according to the specifications of the first, as follows. (Note:
 *  The word `letters' in the next sentence refers only to those at
 *  brace-level 0, the top-most brace level; no other characters are
 *  changed, except perhaps for special characters, described
 *  shortly.) If the first literal is the string <code>t</code>, it
 *  converts to lower case all letters except the very first character
 *  in the string, which it leaves alone, and except the first
 *  character following any <code>colon</code> and then nonnull
 *  <code>white_space</code>, which it also leaves alone; if it's the
 *  string <code>l</code>, it converts all letters to lower case; if
 *  it's the string <code>u</code>, it converts all letters to upper
 *  case; and if it's anything else, it complains and does no
 *  conversion. It then pushes this resulting string. If either type
 *  is incorrect, it complains and pushes the null string; however, if
 *  both types are correct but the specification string (i.e., the
 *  first string) isn't one of the legal ones, it merely pushes the
 *  second back onto the stack, after complaining. (Another note: It
 *  ignores case differences in the specification string; for example,
 *  the strings <code>t</code> and <code>T</code> are equivalent for
 *  the purposes of this <code>built_in</code> function.)
 * </dd></dl>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ChangeCase extends AbstractCode {

    /**
     * Create a new object.
     */
    public ChangeCase() {

        super();
    }

    /**
     * Creates a new object.
     * 
     * @param name the function name in the processor context
     */
    public ChangeCase(String name) {

        super();
        setName(name);
    }

    /**
     * @see org.extex.exbib.core.bst.Code#execute(org.extex.exbib.core.bst.Processor,
     *      org.extex.exbib.core.db.Entry, org.extex.exbib.core.io.Locator)
     */
    @Override
    public void execute(Processor processor, Entry entry, Locator locator)
            throws ExBibException {

        String fmt = processor.popString(locator).getValue();
        TString ts = processor.popString(locator);
        String s = ts.getValue();
        StringBuffer sb = new StringBuffer(s);
        boolean modified = false;
        int level = 0;

        if (fmt.equals("t") || fmt.equals("T")) {
            boolean sc = true;

            for (int i = 0; i < sb.length(); i++) {
                char c = sb.charAt(i);

                if (c == '{') {
                    level++;
                    sc = false;
                } else if (c == '}') {
                    level--;
                } else if (level > 0) {
                    //
                } else if (c == ':') {
                    if (i == sb.length()) {
                        break;
                    }

                    c = sb.charAt(++i);

                    if (Character.isWhitespace(c)) {
                        i++;
                    }
                } else if (sc && Character.isLetter(c)) {
                    sc = false;
                } else {
                    char lc = Character.toLowerCase(c);

                    if (lc != c) {
                        sb.setCharAt(i, lc);
                        modified = true;
                    }
                }
            }
        } else if (fmt.equals("l") || fmt.equals("L")) {

            for (int i = 0; i < sb.length(); i++) {
                char c = sb.charAt(i);

                if (c == '{') {
                    level++;
                } else if (c == '}') {
                    level--;
                } else if (level < 1) {
                    char lc = Character.toLowerCase(c);

                    if (lc != c) {
                        sb.setCharAt(i, lc);
                        modified = true;
                    }
                }
            }
        } else if (fmt.equals("u") || fmt.equals("U")) {

            for (int i = 0; i < sb.length(); i++) {
                char c = sb.charAt(i);

                if (c == '{') {
                    level++;
                } else if (c == '}') {
                    level--;
                } else if (level == 0) {
                    char lc = Character.toUpperCase(c);

                    if (lc != c) {
                        sb.setCharAt(i, lc);
                        modified = true;
                    }
                }
            }
        } else {
            Localizer localizer = LocalizerFactory.getLocalizer(getClass());
            throw new ExBibIllegalValueException(localizer.format(
                "invalid.spec", fmt), locator);
        }

        processor.push(modified ? new TString(sb.toString()) : ts);
    }
}