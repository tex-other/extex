/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.language.hyphenation.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.ModifiableLanguage;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.language.ligature.LigatureBuilder;
import de.dante.extex.language.word.WordTokenizer;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.UnicodeChar;

/**
 * This class stores the values for hyphenations and hyphenates words.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class BaseHyphenationTable implements ModifiableLanguage {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>exceptionMap</tt> contains the exception words for
     * hyphenation.
     */
    private Map exceptionMap = new HashMap();

    /**
     * The field <tt>hyphenactive</tt> contains the indicator whether these
     * hyphenation is active. If the value is <code>false</code> then no
     * hyphenation points will be inserted.
     */
    private boolean hyphenactive = true;

    /**
     * The field <tt>lefthyphenmin</tt> contains the minimum distance from the
     * left side of a word before hyphenation is performed.
     */
    private long lefthyphenmin = 0;

    /**
     * The field <tt>ligatureBuilder</tt> contains the ligature builder.
     */
    private LigatureBuilder ligatureBuilder = null;

    /**
     * The field <tt>righthyphenmin</tt> contains the minimum distance from the
     * right side of a word before hyphenation is performed.
     */
    private long righthyphenmin = 0;

    /**
     * The field <tt>wordTokenizer</tt> contains the tokenizer to recognize
     * words.
     */
    private WordTokenizer wordTokenizer = null;

    /**
     * Creates a new object.
     */
    public BaseHyphenationTable() {

        super();
    }

    /**
     * @see de.dante.extex.language.Language#addHyphenation(
     *      de.dante.extex.interpreter.type.tokens.Tokens,
     *      TypesetterOptions)
     */
    public void addHyphenation(final Tokens word,
            final TypesetterOptions context) throws HyphenationException {

        try {
            exceptionMap.put(createHyphenation(word, context), word);
        } catch (CatcodeException e) {
            throw new HyphenationException(e);
        }
    }

    /**
     * @see de.dante.extex.language.Language#addPattern(
     *      Tokens)
     */
    public void addPattern(final Tokens pattern) throws HyphenationException {

    }

    /**
     * Create the name for the <code>HyphenationTable</code>.
     *
     * @param pattern the pattern
     * @param context the interpreter context
     *
     * @return the name
     *
     * @throws CatcodeException in case of an error
     */
    protected List createHyphenation(final Tokens pattern,
            final TypesetterOptions context) throws CatcodeException {

        List ret = new ArrayList();
        Token t;
        int length = pattern.length();

        for (int i = 0; i < length; i++) {
            t = pattern.get(i);
            if (t.equals(Catcode.OTHER, (char) 0xad)) {
                ret.add(new UnicodeChar('-'));
            } else if (!t.equals(Catcode.OTHER, '-')) {
                ret.add(new UnicodeChar(t.getChar().toLowerCase()));
            }
        }

        return ret;
    }

    /**
     * @see de.dante.extex.language.Language#getLeftHyphenmin()
     */
    public long getLeftHyphenmin() throws HyphenationException {

        return lefthyphenmin;
    }

    /**
     * @see de.dante.extex.language.Language#getRightHyphenmin()
     */
    public long getRightHyphenmin() throws HyphenationException {

        return righthyphenmin;
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#hyphenate(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode,
     *      de.dante.extex.typesetter.TypesetterOptions,
     *      de.dante.util.UnicodeChar,
     *      int,
     *      boolean,
     *      de.dante.extex.typesetter.type.node.factory.NodeFactory)
     */
    public boolean hyphenate(final HorizontalListNode nodelist,
            final TypesetterOptions context, final UnicodeChar hyphen,
            final int start, final boolean forall, final NodeFactory nodeFactory)
            throws HyphenationException {

        if (hyphen == null || !hyphenactive || nodelist.size() == 0
                || wordTokenizer == null) {
            return false;
        }

        boolean modified = false;
        List word = new ArrayList();
        int i = start;
        int next;

        do {
            next = wordTokenizer.findWord(nodelist, i, word);

            Tokens w = (Tokens) exceptionMap.get(word);
            if (w != null) {

                insertShy(nodelist, i, w);

                modified = true;
            }
            i = next;

        } while (forall && i < nodelist.size());

        return modified;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param nodelist the node list to modify
     * @param start the start index
     * @param w the word
     */
    private void insertShy(final HorizontalListNode nodelist, final int start,
            final Tokens w) {

        int i = start;
        /*
         //TODO gene: modify the list instead of making a new one
         HorizontalListNode nodes = new HorizontalListNode();
         CharNodeFactory nodeFactory = new CharNodeFactory();
         for (int j = 0; j < w.length(); j++) {
         Token t = w.get(j);
         if (t.equals(Catcode.OTHER, '-')) {
         CharNode hyphenNode = nodeFactory.newInstance(context
         .getTypesettingContext(), hyphen);
         if (hyphenNode == null) {
         nodes.add(new DiscretionaryNode(null, null, null));
         } else {
         nodes.add(//
         new DiscretionaryNode(null,
         new HorizontalListNode(hyphenNode),
         null));
         }
         } else {
         nodes.add(nodelist.get(i++));
         }
         }
         */

        int j = 0;
        Node n;

        for (; j < w.length();) {
            n = nodelist.get(i);
            if (n instanceof LigatureNode) {

            } else if (n instanceof CharNode) {
                if (w.get(i).equals(Catcode.OTHER, '-')) {

                }
            } else {
            }
            i++;
        }
    }

    /**
     * @see de.dante.extex.language.ligature.LigatureBuilder#insertLigatures(
     *      de.dante.extex.typesetter.type.NodeList, int)
     */
    public int insertLigatures(final NodeList list, final int start)
            throws HyphenationException {

        return this.ligatureBuilder.insertLigatures(list, start);
    }

    /**
     * @see de.dante.extex.language.Language#isHyphenActive()
     */
    public boolean isHyphenActive() throws HyphenationException {

        return hyphenactive;
    }

    /**
     * @see de.dante.extex.language.Language#setHyphenActive(boolean)
     */
    public void setHyphenActive(final boolean active)
            throws HyphenationException {

        hyphenactive = active;
    }

    /**
     * @see de.dante.extex.language.Language#setLeftHyphenmin(long)
     */
    public void setLeftHyphenmin(final long left) throws HyphenationException {

        lefthyphenmin = left;
    }

    /**
     * @see de.dante.extex.language.ModifiableLanguage#setLigatureBuilder(
     *      de.dante.extex.language.ligature.LigatureBuilder)
     */
    public void setLigatureBuilder(final LigatureBuilder builder) {

        this.ligatureBuilder = builder;
    }

    /**
     * @see de.dante.extex.language.Language#setRightHyphenmin(long)
     */
    public void setRightHyphenmin(final long right) throws HyphenationException {

        righthyphenmin = right;
    }

    /**
     * Setter for wordTokenizer.
     *
     * @param wordTokenizer the wordTokenizer to set
     */
    public void setWordTokenizer(final WordTokenizer wordTokenizer) {

        this.wordTokenizer = wordTokenizer;
    }

    /**
     * @see de.dante.extex.language.word.WordTokenizer#findWord(
     *      de.dante.extex.typesetter.type.NodeList, int, java.util.List)
     */
    public int findWord(final NodeList nodes, final int start, final List word)
            throws HyphenationException {

        return wordTokenizer.findWord(nodes, start, word);
    }

}
