/*
 * Copyright (C) 2005-2007 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.file.xslt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import org.extex.core.exception.helping.HelpingException;
import org.extex.core.exception.helping.NoHelpException;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.resource.ResourceFinder;
import org.extex.scanner.stream.TokenStreamFactory;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;
import org.extex.unit.base.file.AbstractFileCode;

import de.dante.util.xslt.Transform;

/**
 * This class provides an implementation for the primitive
 * <code>\inputXSLT</code>. It transform a XML file with a XSLT
 * transformation.
 * <p>
 * It use <code>\javadef</code>.
 * </p>
 * 
 * <doc name="inputXSLT">
 * <h3>The Primitive <tt>\inputXSLT</tt></h3>
 * <p>
 * The primitive <tt>\inputXSLT</tt> takes as argument two file names. The
 * first one is a XML file, the second one is a XSLT file. After the
 * transformation the result is taken as input stream for the tokenizer.
 * </p>
 * <p>
 * If the file can not be opened for reading then an error is raised.
 * </p>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;inputXSLT&rang;
 *       &rarr; <tt>\inputXSLT</tt> &lang;xml-file name&rang;
 *                                  &lang;xsl-file name&rang;</pre>
 * 
 * <h4>Examples</h4>
 * <p>
 * 
 * <pre class="TeXSample">
 *    \inputXSLT{file.name.xml}{file.name.xsl}  </pre>
 * 
 * </p>
 * </doc>
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */

public class InputXslt extends AbstractFileCode {

    /**
     * The field <tt>serialVersionUID</tt> ...
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new object.
     * 
     * @param codeName the CodeName
     */
    public InputXslt(String codeName) {

        super(codeName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.interpreter.type.AbstractCode#execute(org.extex.interpreter.Flags,
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    public void execute(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        // MGN incomplete
        try {

            String xmlfilename = scanFileName(context, source);
            String xslfilename = scanFileName(context, source);

            System.out.println(xmlfilename + "    " + xslfilename);

            TokenStreamFactory factory = source.getTokenStreamFactory();

            ResourceFinder finder = factory.getResourceFinder();

            InputStream xmlin = finder.findResource(xmlfilename, "");
            InputStream xslin = finder.findResource(xslfilename, "");
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Transform.transform(new StreamSource(xmlin),
                new StreamSource(xslin), null, out);

            Reader reader =
                    new InputStreamReader(new ByteArrayInputStream(out
                        .toByteArray()));

            source.addStream(factory.getStream(reader));

        } catch (TransformerException e) {
            throw new NoHelpException(e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new NoHelpException(e);
        } catch (IOException e) {
            throw new NoHelpException(e);
        }

        System.out.println("execute");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.unit.base.file.AbstractFileCode#scanFileName(org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource)
     */
    protected String scanFileName(Context context, TokenSource source)
            throws HelpingException,
                TypesetterException {

        return source.scanTokensAsString(context, getName());
    }
}
