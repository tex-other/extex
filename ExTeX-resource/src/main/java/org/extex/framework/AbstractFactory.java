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

package org.extex.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.extex.framework.configuration.Configurable;
import org.extex.framework.configuration.Configuration;
import org.extex.framework.configuration.ConfigurationFactory;
import org.extex.framework.configuration.exception.ConfigurationClassNotFoundException;
import org.extex.framework.configuration.exception.ConfigurationException;
import org.extex.framework.configuration.exception.ConfigurationInstantiationException;
import org.extex.framework.configuration.exception.ConfigurationInvalidClassException;
import org.extex.framework.configuration.exception.ConfigurationInvalidConstructorException;
import org.extex.framework.configuration.exception.ConfigurationMissingAttributeException;
import org.extex.framework.configuration.exception.ConfigurationMissingException;
import org.extex.framework.logger.LogEnabled;
import org.extex.resource.ResourceAware;
import org.extex.resource.ResourceFinder;

/**
 * This is the abstract base class for factories. It contains some common
 * methods which should make it easy to create a custom factory.
 * <p>
 * The abstract factory supports utility events:
 * <ul>
 * <li>If the instantiated class implements the interface
 * {@link org.extex.framework.configuration.Configurable Configurable} then the
 * associated method is used to pass on the configuration to the new instance.
 * </li>
 * <li>If the instantiated class implements the interface
 * {@link org.extex.framework.logger.LogEnabled LogEnabled} then the associated
 * method is used to pass on the logger to the new instance. </li>
 * </ul>
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public abstract class AbstractFactory
        implements
            Configurable,
            LogEnabled,
            ResourceAware,
            RegistrarObserver {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the class name.
     */
    protected static final String CLASS_ATTRIBUTE = "class";

    /**
     * The constant <tt>DEFAULT_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the default configuration.
     */
    protected static final String DEFAULT_ATTRIBUTE = "default";

    /**
     * The constant <tt>SELECT_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the default configuration.
     */
    protected static final String SELECT_ATTRIBUTE = "select";

    /**
     * Configure an instance if this instance supports configuration. If
     * configuration is not supported then nothing is done.
     *
     * @param instance the instance to configure
     * @param configuration the configuration to use. If this parameter is
     *        <code>null</code> then it is not passed to the instance.
     *
     * @throws ConfigurationException in case of an error
     */
    public static void configure(Object instance, Configuration configuration)
            throws ConfigurationException {

        if (configuration != null && instance instanceof Configurable) {
            ((Configurable) instance).configure(configuration);
        }
    }

    /**
     * Utility method to pass a logger to an object if it has a method to take
     * it. If the logger is <code>null</code> then this method simply does
     * nothing.
     *
     * @param instance the instance to pass the logger to
     * @param logger the logger to pass. If the logger is <code>null</code>
     *        then nothing is done.
     */
    public static void enableLogging(Object instance, Logger logger) {

        if (logger != null && instance instanceof LogEnabled) {
            ((LogEnabled) instance).enableLogging(logger);
        }
    }

    /**
     * The field <tt>configuration</tt> contains the configuration of the
     * factory which is also passed to the new instances.
     */
    private Configuration configuration = null;

    /**
     * The field <tt>logger</tt> contains the logger to pass to the new
     * typesetters.
     */
    private Logger logger = null;

    /**
     * The field <tt>resourceFinder</tt> contains the resource finder.
     */
    private ResourceFinder resourceFinder = null;

    /**
     * Creates a new factory object.
     */
    public AbstractFactory() {

        super();
    }

    /**
     * Creates a new factory object.
     *
     * @param configuration the configuration object to consider
     */
    public AbstractFactory(Configuration configuration) {

        super();
        configure(configuration);
    }

    /**
     * Configure an object according to a given Configuration.
     *
     * @param configuration the configuration object to consider
     *
     * @throws ConfigurationException in case that something went wrong
     *
     * @see org.extex.framework.configuration.Configurable#configure(
     *      org.extex.framework.configuration.Configuration)
     */
    public void configure(Configuration configuration)
            throws ConfigurationException {

        this.configuration = configuration;
    }

    /**
     * Get an instance.
     *
     * @param target the expected class or interface
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstance(Class<?> target)
            throws ConfigurationException {

        return createInstanceForConfiguration(configuration, target);
    }

    /**
     * Get a new instance. This method selects one of the entries in the
     * configuration. The selection is done with the help of a type String. If
     * the type is <code>null</code> or the empty string then the default from
     * the configuration is used.
     *
     * @param target the expected class or interface
     * @param arg the argument for the constructor
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstance(Class<?> target, Object arg)
            throws ConfigurationException {

        return createInstanceForConfiguration(configuration, target, arg);
    }

    /**
     * Create a new instance for a given configuration with an additional
     * argument for the constructor.
     *
     * @param type the type to use
     * @param target the expected class or interface
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstance(String type, Class<?> target)
            throws ConfigurationException {

        return createInstanceForConfiguration(selectConfiguration(type), target);
    }

    /**
     * Create a new instance for a given configuration with an additional
     * argument for the constructor.
     *
     * @param type the type to use
     * @param target the expected class or interface
     * @param argClass the class of the argument
     * @param arg the argument
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstance(String type, Class<?> target,
            Class<?> argClass, Object arg) throws ConfigurationException {

        return createInstanceForConfiguration(selectConfiguration(type),
            target, argClass, arg);
    }

    /**
     * Create a new instance for a given configuration.
     *
     * @param config the configuration to use
     * @param target the expected class or interface
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    @SuppressWarnings("unchecked")
    protected Object createInstanceForConfiguration(Configuration config,
            Class<?> target) throws ConfigurationException {

        if (config == null) {
            throw new ConfigurationMissingException("");
        }
        String className = config.getAttribute(CLASS_ATTRIBUTE);

        if (className == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                config);
        }

        Class<?> theClass;
        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(className, config);
        }

        if (!target.isAssignableFrom(theClass)) {
            throw new ConfigurationInvalidClassException(theClass.getName(),
                target.getName(), config);
        }

        try {
            for (Constructor<?> constructor : theClass.getConstructors()) {
                Class[] args = constructor.getParameterTypes();
                Object instance = null;
                switch (args.length) {
                    case 0:
                        return createInstanceForConfiguration0(config,
                            className, theClass);
                    case 1:
                        instance =
                                createInstanceForConfiguration1(config, target,
                                    className, constructor, args[0]);
                        break;
                    case 2:
                        instance =
                                createInstanceForConfiguration2(config, target,
                                    className, constructor, args[0], args[1]);
                        break;
                    default:
                        // Consider the next constructor
                }
                if (instance != null) {
                    return instance;
                }
            }
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof ConfigurationException) {
                throw (ConfigurationException) cause;
            }
            throw new ConfigurationInstantiationException(e);
        }

        throw new ConfigurationInvalidClassException(theClass.getName(), //
            target.getName(), config);
    }

    /**
     * Create a new instance for a given configuration with an additional
     * argument for the constructor.
     *
     * @param config the configuration to use
     * @param target the expected class or interface
     * @param argClass the class of the argument
     * @param arg the argument
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstanceForConfiguration(Configuration config,
            Class<?> target, Class<?> argClass, Object arg)
            throws ConfigurationException {

        if (config == null) {
            throw new ConfigurationMissingException("");
        }

        String className = config.getAttribute(CLASS_ATTRIBUTE);

        if (className == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                config);
        }

        Class<?> theClass;
        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e1) {
            throw new ConfigurationClassNotFoundException(className, config);
        }

        if (!target.isAssignableFrom(theClass)) {
            throw new ConfigurationInvalidClassException(theClass.getName(),
                target.getName(), config);
        }

        try {
            Object instance = null;

            for (Constructor<?> constructor : theClass.getConstructors()) {
                Class<?>[] args = constructor.getParameterTypes();
                switch (args.length) {
                    case 1:
                        if (args[0].isAssignableFrom(argClass)) {
                            instance = constructor.newInstance(arg);
                            enableLogging(instance, logger);
                            configure(instance, config);
                            return instance;
                        }
                        break;

                    case 2:
                        if (args[1].isAssignableFrom(argClass)) {
                            if (args[0].isAssignableFrom(Configuration.class)
                                    && args[1].isAssignableFrom(argClass)) {
                                instance = constructor.newInstance(//
                                    config, arg);
                                enableLogging(instance, logger);
                                return instance;
                            } else if (args[0].isAssignableFrom(Logger.class)
                                    && args[1].isAssignableFrom(argClass)) {
                                instance = constructor.newInstance(//
                                    config, arg);
                                configure(instance, config);
                                return instance;
                            }
                        }
                        break;
                    default: // Fall through to exception
                }
            }

        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            Throwable c = e.getCause();
            if (c != null && c instanceof ConfigurationException) {
                throw (ConfigurationException) c;
            }
            throw new ConfigurationInstantiationException(e);
        }

        throw new ConfigurationInvalidClassException(theClass.getName(), //
            target.getName(), config);
    }

    /**
     * Create a new instance for a given configuration.
     *
     * @param config the configuration to use
     * @param target the expected class or interface
     * @param arg the constructor argument
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstanceForConfiguration(Configuration config,
            Class<?> target, Object arg) throws ConfigurationException {

        String className = config.getAttribute(CLASS_ATTRIBUTE);

        if (className == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                config);
        }

        try {
            Class<?> theClass = Class.forName(className);

            if (!target.isAssignableFrom(theClass)) {
                throw new ConfigurationInvalidClassException(
                    theClass.getName(), target.getName(), config);
            }

            Object instance = null;

            for (Constructor<?> constructor : theClass.getConstructors()) {
                Class<?>[] args = constructor.getParameterTypes();
                switch (args.length) {
                    case 1:
                        if (args[0].isAssignableFrom(arg.getClass())) {
                            instance = constructor.newInstance(arg);
                            enableLogging(instance, logger);
                            configure(instance, config);
                            return instance;
                        }
                        break;

                    default: // Fall through to exception
                }
            }

        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(className, config);
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof ConfigurationException) {
                throw (ConfigurationException) cause;
            }
            throw new ConfigurationInstantiationException(e);
        }

        throw new ConfigurationInvalidConstructorException(className, config);
    }

    /**
     * Create a new instance for a given configuration using a constructor
     * without arguments.
     *
     * @param config the configuration to us
     * @param className the name of the class to instantiate
     * @param theClass the class to instantiate
     *
     * @return a new instance
     *
     * @throws InstantiationException in case of an instantiation error
     * @throws IllegalAccessException in case of an access error
     * @throws ConfigurationException in case of a configuration error
     */
    private Object createInstanceForConfiguration0(Configuration config,
            String className, Class<?> theClass)
            throws InstantiationException,
                IllegalAccessException,
                ConfigurationException {

        Object instance = theClass.newInstance();
        enableLogging(instance, logger);
        configure(instance, config);
        return instance;
    }

    /**
     * Create a new instance for a given configuration using a constructor with
     * one argument.
     *
     * @param config the configuration to us
     * @param target the expected class or interface
     * @param className the name of the class to instantiate
     * @param constructor the constructor to use
     * @param arg the only argument for the constructor
     *
     * @return a new instance or <code>null</code> if the constructor is not
     *         of a supported type
     *
     * @throws InstantiationException in case of an instantiation error
     * @throws IllegalAccessException in case of an access error
     * @throws InvocationTargetException in case of an invocation error
     * @throws ConfigurationException in case of a configuration error
     */
    private Object createInstanceForConfiguration1(Configuration config,
            Class<?> target, String className, Constructor<?> constructor,
            Class<?> arg)
            throws InstantiationException,
                IllegalAccessException,
                InvocationTargetException,
                ConfigurationException {

        Object instance;
        if (arg.isAssignableFrom(Configuration.class)) {
            instance = constructor.newInstance(config);
            enableLogging(instance, logger);
            return instance;
        } else if (arg.isAssignableFrom(Logger.class)) {
            instance = constructor.newInstance(logger);
            configure(instance, config);
            return instance;
        } else {
            return null;
        }
    }

    /**
     * Create a new instance for a given configuration using a constructor with
     * two arguments.
     *
     * @param config the configuration to us
     * @param target the expected class or interface
     * @param className the name of the class to instantiate
     * @param constructor the constructor to use
     * @param arg1 the first argument for the constructor
     * @param arg2 the second argument for the constructor
     *
     * @return a new instance or <code>null</code> if the constructor is not
     *         of a supported type
     *
     * @throws InstantiationException in case of an instantiation error
     * @throws IllegalAccessException in case of an access error
     * @throws InvocationTargetException in case of an invocation error
     */
    private Object createInstanceForConfiguration2(Configuration config,
            Class<?> target, String className, Constructor<?> constructor,
            Class<?> arg1, Class<?> arg2)
            throws InstantiationException,
                IllegalAccessException,
                InvocationTargetException {

        Object instance;
        if (arg1.isAssignableFrom(Configuration.class)
                && arg2.isAssignableFrom(Logger.class)) {
            instance = constructor.newInstance(config, logger);
            return instance;
        } else if (arg1.isAssignableFrom(Logger.class)
                && arg2.isAssignableFrom(Configuration.class)) {
            instance = constructor.newInstance(logger, config);
            return instance;
        } else {
            return null;
        }
    }

    /**
     * @see org.extex.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * Getter for configuration.
     *
     * @return the configuration.
     */
    public Configuration getConfiguration() {

        return this.configuration;
    }

    /**
     * Getter for logger.
     *
     * @return the logger.
     */
    public Logger getLogger() {

        return this.logger;
    }

    /**
     * Getter for resourceFinder.
     *
     * @return the resourceFinder
     */
    public ResourceFinder getResourceFinder() {

        return resourceFinder;
    }

    /**
     * @see org.extex.framework.RegistrarObserver#reconnect(java.lang.Object)
     */
    public Object reconnect(Object instance) throws RegistrarException {

        try {
            if (instance instanceof LogEnabled) {
                ((LogEnabled) instance).enableLogging(logger);
            }
            if (instance instanceof Configurable) {
                ((Configurable) instance).configure(configuration);
            }
        } catch (ConfigurationException e) {
            throw new RegistrarException(e);
        }

        return instance;
    }

    /**
     * Select a sub-configuration with a given name. If this does not exist then
     * the attribute <tt>default</tt> is used to find an alternative.
     *
     * @param type the tag name for the sub-configuration to find
     *
     * @return the desired sub-configuration
     *
     * @throws ConfigurationException in case of an error
     */
    protected Configuration selectConfiguration(String type)
            throws ConfigurationException {

        if (this.configuration == null) {
            throw new ConfigurationMissingException(this.getClass().getName());
        }

        String base = this.configuration.getAttribute("base");
        if (base != null) {
            String t = type;
            if (t == null || t.equals("")) {
                t = this.configuration.getAttribute(DEFAULT_ATTRIBUTE);
                if (t == null) {
                    throw new ConfigurationMissingAttributeException(
                        DEFAULT_ATTRIBUTE, this.configuration);
                }
            }

            return ConfigurationFactory.newInstance(base + t + ".xml");
        }

        Configuration config = this.configuration.findConfiguration(type);
        if (config == null) {
            String fallback =
                    this.configuration.getAttribute(DEFAULT_ATTRIBUTE);
            if (fallback == null) {
                throw new ConfigurationMissingAttributeException(
                    DEFAULT_ATTRIBUTE, configuration);
            }
            config = configuration.findConfiguration(fallback);
            if (config == null) {
                throw new ConfigurationMissingAttributeException(fallback,
                    this.configuration);
            }
        }
        return config;
    }

    /**
     * Setter for the resource finder.
     *
     * @param finder the resource finder
     *
     * @see org.extex.resource.ResourceAware#setResourceFinder(
     *      org.extex.resource.ResourceFinder)
     */
    public void setResourceFinder(ResourceFinder finder) {

        this.resourceFinder = finder;
    }
}
