/*
 * Copyright (C) 2004 Michael Niedermair
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
package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Real;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the real valued primitives. 
 * It sets the named count register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>Example</p>
 * <pre>
 * \pi=3.1415
 * </pre>
 *
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class NamedReal extends AbstractCode implements Theable {

	/**
	 * Creates a new object.
	 *
	 * @param name the name for debugging
	 */
	public NamedReal(String name) {
		super(name);
	}

	/**
	 * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
	 *      de.dante.extex.interpreter.context.Context,
	 *      de.dante.extex.interpreter.TokenSource,
	 *      de.dante.extex.typesetter.Typesetter)
	 */
	public void execute(Flags prefix, Context context, TokenSource source, Typesetter typesetter) throws GeneralException {
		String key = getKey(source);
		source.scanOptionalEquals();

		Real value = scanReal(context, source);
		//TODO: use new interface and cast
	//	context.setReal(key, value, prefix.isGlobal());
		prefix.clear();
		doAfterAssignment(context, source);
	}

	/**
	 * ...
	 *
	 * @param context the interpreter context
	 * @param value ...
	 */
	public void set(Context context, Real value) {
	    //TODO: use new interface and cast
	//	context.setReal(getName(), value);
	}

	/**
	 * ...
	 *
	 * @param context the interpreter context
	 * @param value ...
	 */
	public void set(Context context, String value) throws GeneralException {
	    //TODO: use new interface and cast
	//	context.setReal(getName(), new Real(value));
	}

	/**
	 * @see de.dante.extex.interpreter.Theable#the(de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
	 */
	public Tokens the(Context context, TokenSource source) throws GeneralException {
		String key = getKey(source);
		//TODO: use new interface and cast
	//	String s = context.getReal(key).toString();
	//	Tokens toks = new Tokens(context, s);
	//	return toks;
		return null;
	}

	/**
	 * Return the key (the name of the primitive) for the register.
	 *
	 * @param source ...
	 *
	 * @return ...
	 */
	protected String getKey(TokenSource source) throws GeneralException {
		return getName();
	}

	/**
	 * ...
	 *
	 * @param context ...
	 * @param source ...
	 *
	 * @return ...
	 *
	 * @throws GeneralException ...
	 */
	private Real scanReal(Context context, TokenSource source) throws GeneralException {
		Token t = source.getNonSpace();

		if (t == null) {
			// TODO unimplemented
			return Real.ZERO;
		} else {
			source.push(t);
			//TODO  unimplemented
		}

		return source.scanReal();
	}
}
