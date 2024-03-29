/*
 * Copyright (C) 2008-2010 The ExTeX Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.extex.exbib.bst2groovy.compiler;

import java.io.IOException;

import org.extex.exbib.bst2groovy.Compiler;
import org.extex.exbib.bst2groovy.data.GCode;
import org.extex.exbib.bst2groovy.data.GenericCode;
import org.extex.exbib.bst2groovy.data.processor.EntryReference;
import org.extex.exbib.bst2groovy.data.processor.Evaluator;
import org.extex.exbib.bst2groovy.data.processor.ProcessorState;
import org.extex.exbib.bst2groovy.data.types.GStringConstant;
import org.extex.exbib.bst2groovy.data.types.ReturnType;
import org.extex.exbib.bst2groovy.io.CodeWriter;
import org.extex.exbib.bst2groovy.linker.LinkContainer;

/**
 * This class implements the analyzer for the <code>*</code> built-in.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class ConcatCompiler implements Compiler {

    /**
     * This inner class is the expression for the * built-in in the target
     * program.
     */
    private static class Concat extends GenericCode {

        /**
         * Creates a new object.
         * 
         * @param a the first argument
         * @param b the second argument
         */
        public Concat(GCode a, GCode b) {

            super(ReturnType.STRING, "++", a, b);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.extex.exbib.bst2groovy.data.GenericCode#optimize()
         */
        @Override
        public GCode optimize() {

            GCode a = getArg(0).optimize();
            GCode b = getArg(1).optimize();
            if (a instanceof GStringConstant
                    && ((GStringConstant) a).getValue().equals("")) {
                return b.optimize();
            } else if (b instanceof GStringConstant
                    && ((GStringConstant) b).getValue().equals("")) {
                return a.optimize();
            } else if (a instanceof GStringConstant
                    && b instanceof GStringConstant) {
                return new GStringConstant(((GStringConstant) a).getValue()
                        + ((GStringConstant) b).getValue());
            }
            return this;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.extex.exbib.bst2groovy.data.GCode#print(CodeWriter,
         *      java.lang.String)
         */
        @Override
        public void print(CodeWriter writer, String prefix) throws IOException {

            getArg(0).print(writer, prefix);
            writer.write(" + ");
            getArg(1).print(writer, prefix);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.bst2groovy.Compiler#evaluate(org.extex.exbib.bst2groovy.data.processor.EntryReference,
     *      org.extex.exbib.bst2groovy.data.processor.ProcessorState,
     *      org.extex.exbib.bst2groovy.data.processor.Evaluator,
     *      org.extex.exbib.bst2groovy.linker.LinkContainer)
     */
    public void evaluate(EntryReference entry, ProcessorState state,
            Evaluator evaluator, LinkContainer linkData) {

        GCode b = state.pop();
        GCode a = state.pop();
        state.push(new Concat(a, b));
    }

}
