/*
 * Copyright (C) 2003-2007 The ExTeX Group and individual authors listed below
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */

package org.extex.scanner.stream;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.extex.framework.AbstractFactory;
import org.extex.framework.configuration.Configuration;
import org.extex.framework.configuration.exception.ConfigurationClassNotFoundException;
import org.extex.framework.configuration.exception.ConfigurationException;
import org.extex.framework.configuration.exception.ConfigurationInstantiationException;
import org.extex.framework.configuration.exception.ConfigurationMissingAttributeException;
import org.extex.framework.configuration.exception.ConfigurationNoSuchMethodException;
import org.extex.resource.ResourceFinder;
import org.extex.scanner.TokenStream;
import org.extex.scanner.stream.exception.MissingResourceFinderException;
import org.extex.scanner.stream.observer.file.OpenFileObservable;
import org.extex.scanner.stream.observer.file.OpenFileObserver;
import org.extex.scanner.stream.observer.file.OpenFileObserverList;
import org.extex.scanner.stream.observer.reader.OpenReaderObservable;
import org.extex.scanner.stream.observer.reader.OpenReaderObserver;
import org.extex.scanner.stream.observer.reader.OpenReaderObserverList;
import org.extex.scanner.stream.observer.string.OpenStringObservable;
import org.extex.scanner.stream.observer.string.OpenStringObserver;
import org.extex.scanner.stream.observer.string.OpenStringObserverList;

/**
 * This is the factory to provide an instance of a
 * {@link org.extex.scanner.TokenStream TokenStream}. Like any good factory it
 * is controlled by its configuration.
 * <h3>Configuration</h3>
 * <p>
 * Mainly the configuration needs to specify which class to use for the
 * TokenStream. The name of the class is given as the argument <tt>class</tt>
 * as shown below.
 * 
 * <pre>
 *   &lt;Scanner class="the.pack.age.TheClass"/&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * The class given must implement the interface
 * {@link org.extex.scanner.TokenStream TokenStream}. In addition an
 * appropriate constructor is required:
 * 
 * <pre>
 *     public TheClass(Configuration config, Reader reader, Boolean isFile,
 *          String theSource) throws IOException
 * </pre>
 * 
 * </p>
 * <p>
 * If the Token stream is fed from a file then the additional parameter
 * <tt>buffersize</tt> is taken into account. This parameter is optional. Its
 * usage can look as follows:
 * 
 * <pre>
 *   &lt;Scanner class="the.pack.age.TheClass"
 *         buffersize="0"/&gt;
 * </pre>
 * 
 * The value given is a number. If this number is positive then it is
 * interpreted as the size of the buffer for the file reading operation. If it
 * is 0 or empty then no buffer will be used. If it is negative, then the
 * default buffer size will be used.
 * </p>
 * <p>
 * In addition to the class for the Token stream the reader class can be
 * specified for the case that reading from a file is requested. In this case
 * the mapping from bytes to characters according to an encoding. The name is
 * given as the parameter <tt>reader</tt> as shown below:
 * 
 * <pre>
 *   &lt;Scanner class="the.pack.age.TheClass"
 *         reader="another.pack.age.TheReaderClass"/&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * Note that the attribute <tt>reader</tt> is optional. If none is given or
 * the value is the empty string then <tt>java.io.InputStreamReader</tt> is
 * used instead.
 * </p>
 * <h3>Observable Events</h3>
 * <p>
 * Observers can be registered for several events:
 * </p>
 * <dl>
 * <dt><tt>file</tt></dt>
 * <dd>This event is triggered by the request for a TokenStream fed from a
 * file. It is deferred until the file has been found and opened. The name of
 * the file is passed as argument to the observer. </dd>
 * <dt><tt>reader</tt></dt>
 * <dd>This event is triggered by the request for a TokenStream fed from an
 * arbitrary Reader. The reader is passed as argument to the observer. </dd>
 * <dt><tt>string</tt></dt>
 * <dd>This event is triggered by the request for a TokenStream fed from a
 * String. The string is passed as argument to the observer. </dd>
 * </dl>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:5563 $
 */
public class TokenStreamFactory extends AbstractFactory
        implements
            OpenFileObservable,
            OpenStringObservable,
            OpenReaderObservable {

    /**
     * The constant <tt>BUFFERSIZE_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the buffer size.
     */
    private static final String BUFFERSIZE_ATTRIBUTE = "buffersize";

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the class name.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The field <tt>bufferSize</tt> contains the buffer size. A value less
     * than 1 indicates that the default should be used.
     */
    private int bufferSize;

    /**
     * The field <tt>configuration</tt> contains the configuration for this
     * instance.
     */
    private Configuration configuration;

    /**
     * The field <tt>decorators</tt> contains the list of decorators for input
     * streams acquired from a resource.
     */
    private List<InputStreamInterceptor> decorators = null;

    /**
     * The field <tt>interceptors</tt> contains the list of decorators for
     * readers.
     */
    private List<ReaderInterceptor> interceptors = null;

    /**
     * The field <tt>openFileObservers</tt> contains the observers registered
     * for the "file" event.
     */
    private OpenFileObserver openFileObservers = null;

    /**
     * The field <tt>openReaderObservers</tt> contains the observers
     * registered for the "reader" event.
     */
    private OpenReaderObserver openReaderObservers = null;

    /**
     * The field <tt>openStringObservers</tt> contains the observers
     * registered for the "string" event.
     */
    private OpenStringObserver openStringObservers = null;

    /**
     * The field <tt>options</tt> contains the options for the token stream.
     */
    private TokenStreamOptions options = null;

    /**
     * The field <tt>readerConstructor</tt> contains the constructor for the
     * reader variant.
     */
    private Constructor<?> readerConstructor;

    /**
     * The field <tt>tag</tt> contains the tag name of the sub-configuration
     * to use.
     */
    private String tag;

    /**
     * Creates a new object.
     * 
     * @param tag the tag name of the sub-configuration to use
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStreamFactory(String tag) throws ConfigurationException {

        super();
        this.tag = tag;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.framework.AbstractFactory#configure(
     *      org.extex.framework.configuration.Configuration)
     */
    @Override
    public void configure(Configuration config) throws ConfigurationException {

        super.configure(config);
        this.configuration = selectConfiguration(tag);
        String classname = configuration.getAttribute(CLASS_ATTRIBUTE);
        if (classname == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                configuration);
        }
        try {
            readerConstructor =
                    Class.forName(classname).getConstructor(
                        new Class[]{Configuration.class,
                                TokenStreamOptions.class, Reader.class,
                                Boolean.class, String.class});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname,
                configuration);
        }
        String bs = config.getAttribute(BUFFERSIZE_ATTRIBUTE);
        if (bs != null && bs.matches("^[0-9]+$")) {
            bufferSize = Integer.parseInt(bs);
        } else {
            bufferSize = 0;
        }
    }

    /**
     * Provide a new instance of a token stream reading from a string.
     * 
     * @param line the line of input to read from
     * @return the new instance
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStream getStream(CharSequence line)
            throws ConfigurationException {

        TokenStream stream = getStream(new StringReader(line.toString()), //
            Boolean.FALSE, "*");

        if (openStringObservers != null) {
            openStringObservers.update(line);
        }

        return stream;
    }

    /**
     * Provide a new instance of a token stream reading from a Reader.
     * 
     * @param reader the reader to get new characters from
     * @return the new instance
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStream getStream(Reader reader) throws ConfigurationException {

        TokenStream stream = getStream(reader, Boolean.FALSE, "*");

        if (openReaderObservers != null) {
            openReaderObservers.update(reader);
        }

        return stream;
    }

    /**
     * Provide a new instance of a token stream reading from a Reader.
     * 
     * @param reader the reader to get new characters from
     * @param isFile the indicator for file readers
     * @param source the description of the source
     * 
     * @return the new instance
     * 
     * @throws ConfigurationException in case of an error in the configuration
     */
    protected TokenStream getStream(Reader reader, Boolean isFile, String source)
            throws ConfigurationException {

        if (reader == null) {
            throw new IllegalArgumentException("reader");
        }
        Reader r = reader;

        if (interceptors != null) {
            for (ReaderInterceptor inter : interceptors) {
                r = inter.pipe(r);
            }
        }

        TokenStream stream;
        try {

            stream = (TokenStream) readerConstructor.newInstance(//
                new Object[]{configuration, options, r, isFile, source});

        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationInstantiationException(e);
        }

        enableLogging(stream, getLogger());

        return stream;
    }

    /**
     * Provide a new instance of a token stream reading from a file or other
     * resource.
     * 
     * @param name the name of the file to be read
     * @param type the type of the file to be read
     * @param encoding the name of the encoding to use
     * 
     * @return the new instance or <code>null</code> if the resource could not
     *         be located
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStream getStream(String name, String type, String encoding)
            throws ConfigurationException {

        ResourceFinder resourceFinder = getResourceFinder();
        if (resourceFinder == null) {
            throw new MissingResourceFinderException("");
        }
        InputStream istream = resourceFinder.findResource(name, type);

        if (istream == null) {
            return null;
        }
        if (bufferSize > 0) {
            istream = new BufferedInputStream(istream, bufferSize);
        } else {
            istream = new BufferedInputStream(istream);
        }

        if (decorators != null) {
            for (InputStreamInterceptor dec : decorators) {
                istream = dec.pipe(istream);
            }
        }

        TokenStream stream =
                getStream(new InputStreamReader(istream), Boolean.TRUE, name);

        if (openFileObservers != null) {
            openFileObservers.update(name, type, istream);
        }

        return stream;
    }

    /**
     * Register a reader decorator to be applied for each token stream
     * originated at a resource.
     * 
     * @param decorator the additional decorator
     */
    public void register(ReaderInterceptor decorator) {

        if (interceptors == null) {
            interceptors = new ArrayList<ReaderInterceptor>();
        }
        interceptors.add(decorator);
    }

    /**
     * Register a input stream decorator to be applied for each token stream
     * originated at a resource.
     * 
     * @param decorator the additional decorator
     */
    public void register(InputStreamInterceptor decorator) {

        if (decorators == null) {
            decorators = new ArrayList<InputStreamInterceptor>();
        }
        decorators.add(decorator);
    }

    /**
     * @see org.extex.scanner.stream.observer.file.OpenFileObservable#registerObserver(
     *      org.extex.scanner.stream.observer.file.OpenFileObserver)
     */
    public void registerObserver(OpenFileObserver observer) {

        openFileObservers =
                OpenFileObserverList.register(openFileObservers, observer);
    }

    /**
     * @see org.extex.scanner.stream.observer.reader.OpenReaderObservable#registerObserver(
     *      org.extex.scanner.stream.observer.reader.OpenReaderObserver)
     */
    public void registerObserver(OpenReaderObserver observer) {

        openReaderObservers =
                OpenReaderObserverList.register(openReaderObservers, observer);
    }

    /**
     * @see org.extex.scanner.stream.observer.string.OpenStringObservable#registerObserver(
     *      org.extex.scanner.stream.observer.string.OpenStringObserver)
     */
    public void registerObserver(OpenStringObserver observer) {

        openStringObservers =
                OpenStringObserverList.register(openStringObservers, observer);
    }

    /**
     * Setter for options.
     * 
     * @param options the options to set.
     */
    public void setOptions(TokenStreamOptions options) {

        this.options = options;
    }

    /**
     * Construct a Writer for an output stream. This may build up a pipe of
     * output stream and writers to perform all desirable steps.
     * <p>
     * The encoding can be given as a hint.
     * </p>
     * 
     * @param stream the stream to put bytes to
     * @param encoding the optional encoding. If the encoding is
     *        <code>null</code> then it is ignored
     * 
     * @return the writer for th task
     */
    public Writer writerStream(OutputStream stream, String encoding) {

        return new OutputStreamWriter(stream);
    }

}
