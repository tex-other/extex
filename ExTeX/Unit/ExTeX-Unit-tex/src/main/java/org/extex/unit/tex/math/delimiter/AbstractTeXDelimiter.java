/*
 * Copyright (C) 2003-2011 The ExTeX Group and individual authors listed below
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

package org.extex.unit.tex.math.delimiter;

import org.extex.core.UnicodeChar;
import org.extex.core.exception.helping.EofException;
import org.extex.core.exception.helping.HelpingException;
import org.extex.core.exception.helping.MissingNumberException;
import org.extex.framework.configuration.exception.ConfigurationException;
import org.extex.framework.i18n.Localizer;
import org.extex.framework.i18n.LocalizerFactory;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.type.Code;
import org.extex.interpreter.type.ExpandableCode;
import org.extex.scanner.type.token.CodeToken;
import org.extex.scanner.type.token.OtherToken;
import org.extex.scanner.type.token.Token;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;
import org.extex.typesetter.type.math.MathClass;
import org.extex.typesetter.type.math.MathClassVisitor;
import org.extex.typesetter.type.math.MathDelimiter;
import org.extex.typesetter.type.noad.MathGlyph;
import org.extex.unit.tex.math.AbstractMathCode;

/**
 * This abstract class adds the ability to translate
 * {@link org.extex.typesetter.type.math.MathDelimiter MathDelimiter}s to and
 * from their <logo>T<span style=
 * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 * >e</span>X</logo> encoding as numbers to abstract math code.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:4431 $
 */
public abstract class AbstractTeXDelimiter extends AbstractMathCode {

    /**
     * The constant <tt>CHAR_MASK</tt> contains the character mask.
     */
    private static final int CHAR_MASK = 0xff;

    /**
     * The field <tt>CHAR_MAX</tt> contains the maximal number for a character.
     */
    private static final int CHAR_MAX = 255;

    /**
     * The field <tt>CHAR_MIN</tt> contains the minimal number for a character.
     */
    private static final int CHAR_MIN = 0;

    /**
     * The field <tt>CLASS_MASK</tt> contains the mask for the class. This
     * implies a maximal value.
     */
    private static final int CLASS_MASK = 0xf;

    /**
     * The constant <tt>CLASS_MAX</tt> contains the maximum number for a math
     * class.
     */
    private static final int CLASS_MAX = CLASS_MASK;

    /**
     * The field <tt>CLASS_SHIFT</tt> contains the number of bits to shift the
     * class rightwards in the <logo>T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo> encoding of delimiters.
     */
    private static final int CLASS_SHIFT = 24;

    /**
     * The field <tt>DEL_BINARY</tt> contains the delimiter code for binary.
     */
    private static final Integer DEL_BINARY = new Integer(2);

    /**
     * The field <tt>DEL_CLOSING</tt> contains the delimiter code for closing.
     */
    private static final Integer DEL_CLOSING = new Integer(5);

    /**
     * The field <tt>DEL_LARGE</tt> contains the delimiter code for large.
     */
    private static final Integer DEL_LARGE = new Integer(1);

    /**
     * The field <tt>DEL_OPENING</tt> contains the delimiter code for opening.
     */
    private static final Integer DEL_OPENING = new Integer(4);

    /**
     * The field <tt>DEL_ORDINARY</tt> contains the delimiter code for ordinary.
     */
    private static final Integer DEL_ORDINARY = new Integer(0);

    /**
     * The field <tt>DEL_PUNCTATION</tt> contains the delimiter code for
     * punctation.
     */
    private static final Integer DEL_PUNCTATION = new Integer(6);

    /**
     * The field <tt>DEL_RELATION</tt> contains the delimiter code for relation.
     */
    private static final Integer DEL_RELATION = new Integer(3);

    /**
     * The field <tt>DEL_VARIABLE</tt> contains the delimiter code for variable.
     */
    private static final Integer DEL_VARIABLE = new Integer(7);

    /**
     * The field <tt>FAM_MAX</tt> contains the maximum of the family number.
     */
    private static final int FAM_MAX = 15;

    /**
     * The field <tt>FAM_MIN</tt> contains the minimum of the family number.
     */
    private static final int FAM_MIN = 0;

    /**
     * The field <tt>LARGE_CLASS_OFFSET</tt> contains the offset for large
     * character's class.
     */
    private static final int LARGE_CLASS_OFFSET = 8;

    /**
     * The field <tt>MCV</tt> contains the visitor to map a math class to
     * numbers.
     */
    private static final MathClassVisitor<Integer, Object, Object> MCV =
            new MathClassVisitor<Integer, Object, Object>() {

                /**
                 * Invoke the visitor method for a binary operator.
                 * 
                 * @param ignore the argument
                 * @param ignore2 the second argument
                 * 
                 * @return the result
                 * 
                 * @see org.extex.typesetter.type.math.MathClassVisitor#visitBinary(java.lang.Object,
                 *      java.lang.Object)
                 */
                public Integer visitBinary(Object ignore, Object ignore2) {

                    return DEL_BINARY;
                }

                /**
                 * Invoke the visitor method for a closing delimiter.
                 * 
                 * @param ignore the argument
                 * @param ignore2 the second argument
                 * 
                 * @return the result
                 * 
                 * @see org.extex.typesetter.type.math.MathClassVisitor#visitClosing(java.lang.Object,
                 *      java.lang.Object)
                 */
                public Integer visitClosing(Object ignore, Object ignore2) {

                    return DEL_CLOSING;
                }

                /**
                 * Invoke the visitor method for a large operator.
                 * 
                 * @param ignore the argument
                 * @param ignore2 the second argument
                 * 
                 * @return the result
                 * 
                 * @see org.extex.typesetter.type.math.MathClassVisitor#visitLarge(java.lang.Object,
                 *      java.lang.Object)
                 */
                public Integer visitLarge(Object ignore, Object ignore2) {

                    return DEL_LARGE;
                }

                /**
                 * Invoke the visitor method for a opening delimiter.
                 * 
                 * @param ignore the argument
                 * @param ignore2 the second argument
                 * 
                 * @return the result
                 * 
                 * @see org.extex.typesetter.type.math.MathClassVisitor#visitOpening(java.lang.Object,
                 *      java.lang.Object)
                 */
                public Integer visitOpening(Object ignore, Object ignore2) {

                    return DEL_OPENING;
                }

                /**
                 * Invoke the visitor method for an ordinary symbol .
                 * 
                 * @param ignore the argument
                 * @param ignore2 the second argument
                 * 
                 * @return the result
                 * 
                 * @see org.extex.typesetter.type.math.MathClassVisitor#visitOrdinary(java.lang.Object,
                 *      java.lang.Object)
                 */
                public Integer visitOrdinary(Object ignore, Object ignore2) {

                    return DEL_ORDINARY;
                }

                /**
                 * Invoke the visitor method for a punctation symbol.
                 * 
                 * @param ignore the argument
                 * @param ignore2 the second argument
                 * 
                 * @return the result
                 * 
                 * @see org.extex.typesetter.type.math.MathClassVisitor#visitPunctation(java.lang.Object,
                 *      java.lang.Object)
                 */
                public Integer visitPunctation(Object ignore, Object ignore2) {

                    return DEL_PUNCTATION;
                }

                /**
                 * Invoke the visitor method for a relation operator.
                 * 
                 * @param ignore the argument
                 * @param ignore2 the second argument
                 * 
                 * @return the result
                 * 
                 * @see org.extex.typesetter.type.math.MathClassVisitor#visitRelation(java.lang.Object,
                 *      java.lang.Object)
                 */
                public Integer visitRelation(Object ignore, Object ignore2) {

                    return DEL_RELATION;
                }

                /**
                 * Invoke the visitor method for a variable width symbol.
                 * 
                 * @param ignore the argument
                 * @param ignore2 the second argument
                 * 
                 * @return the result
                 * 
                 * @see org.extex.typesetter.type.math.MathClassVisitor#visitVariable(java.lang.Object,
                 *      java.lang.Object)
                 */
                public Integer visitVariable(Object ignore, Object ignore2) {

                    return DEL_VARIABLE;
                }
            };

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>SMALL_CHAR_OFFSET</tt> contains the offset for the small
     * characters.
     */
    private static final int SMALL_CHAR_OFFSET = 12;

    /**
     * The field <tt>SMALL_CLASS_OFFSET</tt> contains the offset for the small
     * character's class.
     */
    private static final int SMALL_CLASS_OFFSET = 20;

    /**
     * Translate the delimiter into a <logo>T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo> encoded number or throw an exception if this is not
     * possible.
     * 
     * @param del the delimiter to encode
     * 
     * @return the <logo>T<span style=
     *         "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     *         >e</span>X</logo> encoded delimiter
     * 
     * @throws HelpingException in case of an error
     */
    public static long delimiterToLong(MathDelimiter del)
            throws HelpingException {

        if (del == null) {
            return -1;
        }

        long value =
                ((Integer) del.getMathClass().visit(MCV, null, null))
                    .longValue() << CLASS_SHIFT;

        int fam0 = del.getSmallChar().getFamily();
        if (fam0 < FAM_MIN || fam0 > FAM_MAX) {
            throw new HelpingException(getMyLocalizer(), "ExtendedDelimiter");
        }
        int c0 = del.getSmallChar().getCharacter().getCodePoint();
        if (c0 < CHAR_MIN || c0 > CHAR_MAX) {
            throw new HelpingException(getMyLocalizer(), "ExtendedDelimiter");
        }
        int fam1 = del.getLargeChar().getFamily();
        if (fam1 < FAM_MIN || fam1 > FAM_MAX) {
            throw new HelpingException(getMyLocalizer(), "ExtendedDelimiter");
        }
        int c1 = del.getLargeChar().getCharacter().getCodePoint();
        if (c1 < CHAR_MIN || c1 > CHAR_MAX) {
            throw new HelpingException(getMyLocalizer(), "ExtendedDelimiter");
        }
        value |= fam0 << SMALL_CLASS_OFFSET;
        value |= c0 << SMALL_CHAR_OFFSET;
        value |= fam1 << LARGE_CLASS_OFFSET;
        value |= c1;
        return value;
    }

    /**
     * Create a localizer for this class.
     * 
     * @return the localizer
     */
    protected static Localizer getMyLocalizer() {

        return LocalizerFactory.getLocalizer(AbstractTeXDelimiter.class);
    }

    /**
     * Creates a new MathDelimiter object from the <logo>T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo> encoding.
     * <p>
     * The <logo>T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo> encoding interprets the number as 27 bit hex number:
     * <tt>"csyylxx</tt>. Here the digits have the following meaning:
     * <dl>
     * <dt>c</dt>
     * <dd>the math class of this delimiter. It has a range from 0 to 7.</dd>
     * <dt>l</dt>
     * <dd>the family for the large character. It has a range from 0 to 15.</dd>
     * <dt>xx</dt>
     * <dd>the character code of the large character.</dd>
     * <dt>s</dt>
     * <dd>the family for the small character. It has a range from 0 to 15.</dd>
     * <dt>yy</dt>
     * <dd>the character code of the small character.</dd>
     * </dl>
     * </p>
     * 
     * @param delcode the <logo>T<span style=
     *        "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     *        >e</span>X</logo> encoding for the delimiter
     * 
     * @return a new MathDelimiter
     * 
     * @throws HelpingException in case of a parameter out of range
     */
    public static MathDelimiter newMathDelimiter(long delcode)
            throws HelpingException {

        int classCode = (int) ((delcode >> CLASS_SHIFT));

        if (delcode < 0 || classCode > CLASS_MAX) {
            throw new HelpingException(getMyLocalizer(),
                "TTP.BadDelimiterCode", "\"" + Long.toHexString(delcode));
        }
        MathClass mathClass = MathClass.getMathClass(classCode);
        MathGlyph smallChar =
                new MathGlyph(
                    (int) ((delcode >> SMALL_CLASS_OFFSET) & CLASS_MASK),
                    UnicodeChar
                        .get((int) ((delcode >> SMALL_CHAR_OFFSET) & CHAR_MASK)));
        MathGlyph largeChar =
                new MathGlyph(
                    (int) ((delcode >> LARGE_CLASS_OFFSET) & CLASS_MASK),
                    UnicodeChar.get((int) (delcode & CHAR_MASK)));
        return new MathDelimiter(mathClass, smallChar, largeChar);
    }

    /**
     * Parse an extended <logo>&epsilon;&chi;T<span style=
     * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
     * >e</span>X</logo> delimiter from a token source.
     * 
     * @param context the interpreter context
     * @param source the token source to read from
     * @param typesetter the typesetter
     * @param mClass the math class
     * @param primitive the name of the primitive for error handling
     * 
     * @return the MathDelimiter found
     * 
     * @throws HelpingException in case of an error
     * @throws TypesetterException in case of an error in the typesetter
     */
    private static MathDelimiter parse(Context context, TokenSource source,
            Typesetter typesetter, MathClass mClass, CodeToken primitive)
            throws HelpingException,
                TypesetterException {

        int smallFam = (int) source.parseNumber(context, source, typesetter);
        UnicodeChar smallChar =
                source.scanCharacterCode(context, typesetter, primitive);
        int largeFam = (int) source.parseNumber(context, source, typesetter);
        UnicodeChar largeChar =
                source.scanCharacterCode(context, typesetter, primitive);

        return new MathDelimiter(mClass, new MathGlyph(smallFam, smallChar),
            new MathGlyph(largeFam, largeChar));
    }

    /**
     * Parse a math delimiter.
     * 
     * <pre>
     *  \delimiter"1234567
     *  \delimiter open 22 `[ 1 `(
     * </pre>
     * 
     * @param context the interpreter context
     * @param source the token source to read from
     * @param typesetter the typesetter
     * @param primitive the name of the primitive for error handling
     * 
     * @return the MathDelimiter acquired
     * 
     * @throws HelpingException in case of an error
     * @throws ConfigurationException in case of an configuration error
     * @throws TypesetterException in case of an error in the typesetter
     */
    public static MathDelimiter parseDelimiter(Context context,
            TokenSource source, Typesetter typesetter, CodeToken primitive)
            throws HelpingException,
                ConfigurationException,
                TypesetterException {

        for (Token t = source.getToken(context); t != null; t =
                source.getToken(context)) {

            if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof Delimiter) {
                    return newMathDelimiter(source.parseNumber(context, source,
                        typesetter));
                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                        typesetter);
                    // retry within the outer loop
                } else {
                    throw new HelpingException(getMyLocalizer(),
                        "TTP.MissingDelim");
                }
            } else {
                MathDelimiter del = context.getDelcode(t.getChar());
                if (del != null) {
                    return del;
                } else if (t instanceof OtherToken) {
                    source.push(t);
                    try {
                        return newMathDelimiter(source.parseNumber(context,
                            source, typesetter));
                    } catch (MissingNumberException e) {
                        throw new HelpingException(getMyLocalizer(),
                            "TTP.MissingDelim");
                    }
                } else {
                    source.push(t);
                    switch (t.getChar().getCodePoint()) {
                        case 'b':
                            if (source.getKeyword(context, "bin")) {
                                return parse(context, source, typesetter,
                                    MathClass.BINARY, primitive);
                            }
                            break;
                        case 'c':
                            if (source.getKeyword(context, "close")) {
                                return parse(context, source, typesetter,
                                    MathClass.CLOSING, primitive);
                            }
                            break;
                        case 'l':
                            if (source.getKeyword(context, "large")) {
                                return parse(context, source, typesetter,
                                    MathClass.LARGE, primitive);
                            }
                            break;
                        case 'o':
                            if (source.getKeyword(context, "open")) {
                                return parse(context, source, typesetter,
                                    MathClass.OPENING, primitive);
                            } else if (source.getKeyword(context, "ord")) {
                                return parse(context, source, typesetter,
                                    MathClass.ORDINARY, primitive);
                            }
                            break;
                        case 'p':
                            if (source.getKeyword(context, "punct")) {
                                return parse(context, source, typesetter,
                                    MathClass.PUNCTATION, primitive);
                            }
                            break;
                        case 'r':
                            if (source.getKeyword(context, "rel")) {
                                return parse(context, source, typesetter,
                                    MathClass.RELATION, primitive);
                            }
                            break;
                        case 'v':
                            if (source.getKeyword(context, "var")) {
                                return parse(context, source, typesetter,
                                    MathClass.VARIABLE, primitive);
                            }
                            break;
                        default:
                            throw new HelpingException(getMyLocalizer(),
                                "TTP.MissingDelim");
                    }
                }
            }
        }
        throw new EofException();
    }

    /**
     * Creates a new object.
     * 
     * @param token the initial token for the primitive
     */
    public AbstractTeXDelimiter(CodeToken token) {

        super(token);
    }

}
