/*
 * Copyright (C) 2003-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.tex.register;

import org.extex.core.UnicodeChar;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.exception.helping.HelpingException;
import org.extex.interpreter.exception.helping.InvalidCodeException;
import org.extex.interpreter.type.AbstractAssignment;
import org.extex.interpreter.type.Theable;
import org.extex.scanner.CountConvertible;
import org.extex.scanner.CountParser;
import org.extex.scanner.type.Catcode;
import org.extex.scanner.type.CatcodeException;
import org.extex.scanner.type.tokens.Tokens;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;

/**
 * This class provides an implementation for the primitive <code>\catcode</code>.
 * 
 * <doc name="catcode">
 * <h3>The Primitive <tt>\catcode</tt></h3>
 * <p>
 * The primitive <tt>\catcode</tt> can be used to influence the tokenizer of
 * <logo>ExTeX</logo>. This is done by assigning category codes to single
 * characters. Whenever characters are read tokens are generated and passed on.
 * Those tokens carry the category code into the interpreter. The interpreter
 * considers always tokens, i.e. characters and category codes. Thus the same
 * character can be treated differently depending on the catcode of the token.
 * </p>
 * <p>
 * The assignment of a catcode for a character is controlled by the prefix
 * primitive <tt>\global</tt> and the count parameter <tt>\globaldefs</tt>.
 * Usually the assignment is acting on the current group only. If the count
 * parameter <tt>\globaldefs</tt> is greater than 0 or the prefix
 * <tt>\global</tt> is given then the assignment is applied to all groups.
 * </p>
 * <p>
 * The following table contains the category codes with their meaning and the
 * mapping to numerical values.
 * </p>
 * <table format="lrp{.6\textwidth}">
 * <tr>
 * <td>ESCAPE</td>
 * <td>0</td>
 * <td>This character code signals the beginning of an escape sequence. The
 * following letters are absorbed into the name. If the following token is not a
 * letter then only this single character is absorbed.</td>
 * </tr>
 * <tr>
 * <td>LEFTBRACE</td>
 * <td>1</td>
 * <td>This character is a left brace. It is used for grouping.</td>
 * </tr>
 * <tr>
 * <td>RIGHTBRACE</td>
 * <td>2</td>
 * <td>This character is a right brace. It is used for grouping.</td>
 * </tr>
 * <tr>
 * <td>MATHSHIFT</td>
 * <td>3</td>
 * <td>This character is used to switch to math mode. A single one switches to
 * inline math mode and a double one to display math mode.</td>
 * </tr>
 * <tr>
 * <td>TABMARK</td>
 * <td>4</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>CR</td>
 * <td>5</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>MACROPARAM</td>
 * <td>6</td>
 * <td>This character is a macro parameter. It is meaningful under certain
 * circumstances only.</td>
 * </tr>
 * <tr>
 * <td>SUPMARK</td>
 * <td>7</td>
 * <td>This character is an indicator the the following material should be
 * typeset as superscript. It is meaningful in math mode only. This character
 * class is used to parse characters from their code point as well.</td>
 * </tr>
 * <tr>
 * <td>SUBMARK</td>
 * <td>8</td>
 * <td>This character is an indicator the the following material should be
 * typeset as subscript. It is meaningful in math mode only.</td>
 * </tr>
 * <tr>
 * <td>IGNORE</td>
 * <td>9</td>
 * <td>This character is ignored during paring. It is dropped silently. </td>
 * </tr>
 * <tr>
 * <td>SPACE</td>
 * <td>10</td>
 * <td>This character is a white-space character. It is treated as if a simple
 * space has been found. Under some circumstances it is ignored. </td>
 * </tr>
 * <tr>
 * <td>LETTER</td>
 * <td>11</td>
 * <td>This character is a letter. As such it can be part of an escape
 * sequence.</td>
 * </tr>
 * <tr>
 * <td>OTHER</td>
 * <td>12</td>
 * <td>This character is another simple character which is not a letter. </td>
 * </tr>
 * <tr>
 * <td>ACTIVE</td>
 * <td>13</td>
 * <td>This character is an active character. This means that some code could
 * be bound to it. Active characters do not need the leading escape symbol like
 * escape sequences.</td>
 * </tr>
 * <tr>
 * <td>COMMENT</td>
 * <td>14</td>
 * <td>This character signals the beginning of a comment. The comment reaches
 * to the end of the current line.</td>
 * </tr>
 * <tr>
 * <td>INVALID</td>
 * <td>15</td>
 * <td>This character is invalid and will be dropped.</td>
 * </tr>
 * </table>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;catcode&rang;
 *      &rarr; &lang;optional prefix&rang; <tt>\catcode</tt> {@linkplain
 *          org.extex.scanner.CountParser#scanNumber(Context,TokenSource,Typesetter)
 *          &lang;8-bit&nbsp;number&rang;} {@linkplain
 *          org.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *          &lang;equals&rang;} {@linkplain
 *          org.extex.scanner.CountParser#scanNumber(Context,TokenSource,Typesetter)
 *          &lang;4-bit&nbsp;number&rang;}
 *
 *    &lang;optional prefix&rang;
 *      &rarr;
 *       |  &lang;global&rang; &lang;optional prefix&rang;  </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \catcode `\%=12  </pre>
 *  <pre class="TeXSample">
 *    \global\catcode `\%=11  </pre>
 * 
 * <h4><tt>\catcode</tt> as a Count Value</h4>
 * 
 * <p>
 * <tt>\catcode</tt> can be used wherever a count value is required.
 * </p>
 * 
 * 
 * </doc>
 * 
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 4770 $
 */
public class CatcodePrimitive extends AbstractAssignment
        implements
            CountConvertible,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for
     * serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     * 
     * @param name the name for debugging
     */
    public CatcodePrimitive(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.interpreter.type.Code#execute(
     *      org.extex.interpreter.Flags, org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    public void assign(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        UnicodeChar charCode =
                source.scanCharacterCode(context, typesetter, getName());
        source.getOptionalEquals(context);
        long ccNumber = CountParser.scanInteger(context, source, typesetter);

        try {
            context.setCatcode(charCode, Catcode.toCatcode((int) ccNumber),
                prefix.clearGlobal());
        } catch (CatcodeException e) {
            throw new InvalidCodeException(Long.toString(ccNumber), //
                Integer.toString(Catcode.getCatcodeMax()));
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.scanner.CountConvertible#convertCount(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    public long convertCount(Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        UnicodeChar charCode =
                source.scanCharacterCode(context, typesetter, getName());

        return context.getCatcode(charCode).getCode();
    }

    /**
     * This method is the getter for the description of the primitive.
     * 
     * @param context the interpreter context
     * @param source the source for further tokens to qualify the request
     * @param typesetter the typesetter to use
     * 
     * @return the description of the primitive as list of Tokens
     * @throws CatcodeException in case of an error in token creation
     * @see org.extex.interpreter.type.Theable#the(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    public Tokens the(Context context, TokenSource source, Typesetter typesetter)
            throws CatcodeException,
                HelpingException,
                TypesetterException {

        return context.getTokenFactory().toTokens( //
            convertCount(context, source, typesetter));
    }

}
