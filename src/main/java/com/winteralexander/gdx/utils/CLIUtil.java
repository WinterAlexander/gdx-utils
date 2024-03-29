package com.winteralexander.gdx.utils;

import com.badlogic.gdx.utils.Array;

/**
 * Utility class user-system interactions from CLI
 * <p>
 * Created on 2018-11-19.
 *
 * @author Alexander Winter
 */
public class CLIUtil {
	private CLIUtil() {}

	/**
	 * Gets from the specified set of arguments the value with key that
	 * correspond to the one of the specified parameter aliases. Only the value
	 * is returned. Parameter alias comparison is done ignoring case. If the
	 * parameter is present but no value is provided, an empty string is
	 * returned. If it's not found, null is returned.
	 *
	 * @param args       command line arguments
	 * @param paramAlias aliases of the parameter
	 * @return value of the parameter, or null if not found
	 */
	public static String getParamValue(String[] args, String... paramAlias) {
		if(paramAlias.length == 0)
			return null;

		for(int i = 0; i < args.length; i++)
			for(String param : paramAlias)
				if(args[i].equalsIgnoreCase(param))
					return i + 1 == args.length ? "" : args[i + 1];
		return null;
	}

	public static String[] getArgsWithoutParams(String[] args) {
		Array<String> newArgs = new Array<>();

		for(int i = 0; i < args.length; i++)
			if(!args[i].startsWith("-"))
				newArgs.add(args[i]);
			else
				i++;

		return newArgs.toArray(String.class);
	}
}
