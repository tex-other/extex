/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context;

import de.dante.extex.hyphenation.HyphenationManager;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.framework.AbstractFactory;

/**
 * This class provides a factory for a
 * {@link de.dante.extex.interpreter.context.TypesettingContext
 *  TypesettingContext}.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class TypesettingContextFactory extends AbstractFactory {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the
     * attribute used to extract the class name fron the given configuration.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The field <tt>hyphenationManager</tt> contains the hyphenation manager.
     */
    private transient HyphenationManager hyphenationManager = null;

    /**
     * The field <tt>theClass</tt> contains the class to instantiate. It is
     * kept here to speed up the method
     * <tt>{@link #newInstance() newInstance()}</tt>.
     */
    private Class theClass;

    /**
     * Creates a new object.
     */
    public TypesettingContextFactory() {

        super();
    }

    /**
     * Configure the factory according to a given Configuration.
     *
     * @param configuration the configuration for this factory
     *
     * @throws ConfigurationException in case of an error
     * <ul>
     * <li>ConfigurationMissingAttributeException in case that the
     *      attribute <tt>CLASS_ATTRIBUTE</tt> is not set for the given
     *      configuration.</li>
     * <li>ConfigurationInstantiationException in case that the
     *      instantiation of the given class causes a SecurityException.</li>
     * <li>ConfigurationClassNotFoundException in case that the named class
     *      could not be loaded.</li>
     * </ul>
     *
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration configuration)
            throws ConfigurationException {

        super.configure(configuration);

        String classname = configuration.getAttribute(CLASS_ATTRIBUTE);
        if (classname == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    configuration);
        }

        try {
            theClass = Class.forName(classname);
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname,
                    configuration);
        }
    }

    /**
     * Factory method to acquire an instance of the TypesettingContext.
     *
     * @return an appropriate instance of the TypesettingContext.
     *
     * @throws ConfigurationInstantiationException in case that the
     *             instantiation of the class failed.
     */
    public TypesettingContext newInstance()
            throws ConfigurationInstantiationException {

        TypesettingContext context;

        try {
            context = (TypesettingContext) (theClass.newInstance());
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        }

        return context;
    }

    /**
     * Factory method to acquire an instance of the TypesettingContext.
     *
     * @param context the typesetting context to clone
     * @param color the new value for the color
     *
     * @return an appropriate instance of the TypesettingContext.
     *
     * @throws ConfigurationInstantiationException in case that the
     *             instantiation of the class failed.
     */
    public TypesettingContext newInstance(final TypesettingContext context,
            final Color color) throws ConfigurationInstantiationException {

        TypesettingContext c = newInstance();
        c.set(context);
        c.setColor(color);

        return c;
    }

    /**
     * Factory method to acquire an instance of the TypesettingContext.
     *
     * @param context the typesetting context to clone
     * @param direction the new value for the direction
     *
     * @return an appropriate instance of the TypesettingContext.
     *
     * @throws ConfigurationInstantiationException in case that the
     *             instantiation of the class failed.
     */
    public TypesettingContext newInstance(final TypesettingContext context,
            final Direction direction)
            throws ConfigurationInstantiationException {

        TypesettingContext c = newInstance();
        c.set(context);
        c.setDirection(direction);

        return c;
    }

    /**
     * Factory method to acquire an instance of the TypesettingContext.
     *
     * @param context the typesetting context to clone
     * @param font the new value for the font
     *
     * @return an appropriate instance of the TypesettingContext.
     *
     * @throws ConfigurationInstantiationException in case that the
     *             instantiation of the class failed.
     */
    public TypesettingContext newInstance(final TypesettingContext context,
            final Font font) throws ConfigurationInstantiationException {

        TypesettingContext c = newInstance();
        c.set(context);
        c.setFont(font);

        return c;
    }

    /**
     * Factory method to acquire an instance of the TypesettingContext with a
     * new value for the language. The laguage might be loaded if neccesary.
     *
     * @param context the typesetting context to clone
     * @param language the new value for the hyphenation table
     *
     * @return an appropriate instance of the TypesettingContext.
     *
     * @throws ConfigurationException in case of a configuration problem
     */
    public TypesettingContext newInstance(final TypesettingContext context,
            final String language) throws ConfigurationException {

        TypesettingContext c = newInstance();
        c.set(context);
        c.setLanguage(hyphenationManager.useHyphenationTable(language));

        return c;
    }

    /**
     * Setter for the hyphenation manager.
     *
     * @param hyphenationManager the new hyphenation manager
     */
    public void setHyphenationManager(
            final HyphenationManager hyphenationManager) {

        this.hyphenationManager = hyphenationManager;
    }

}