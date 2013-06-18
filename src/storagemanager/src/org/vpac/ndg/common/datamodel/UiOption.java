package org.vpac.ndg.common.datamodel;

import java.util.EnumSet;

/**
 * An extension for enum types that allows them to be displayed to the user and
 * queried without reflection.
 * @author adfries
 *
 * @param <T> The concrete type of the class. For example:
 * <pre>public enum Foo implements UiOption&lt;Foo&gt; {...}</pre>
 */
public interface UiOption<T extends Enum<T> & UiOption<T>> {
	/**
	 * @return A string representation of this value that is suitable for
	 * displaying to the user.
	 */
	public String toHumanString();
	
	/**
	 * @return A list of the values that this option can take. Note that this
	 * method is not static, so a prototype instance is required.
	 */
	public T[] getValues();

	/**
	 * @param enumSet The specified enum set which contain desired values.
	 * @return A list of values from the specified enum set that this option can take.
	 */
	public T[] getEnumSetValues(EnumSet<T> enumSet);
	
	/**
	 * Get the enum value that corresponds to a string representation. This is
	 * not the <em>human</em> string (see {@link #toHumanString()}), but the
	 * value returned by {@link Enum#toString()}. Note that this method is not
	 * static, so a prototype instance is required.
	 * 
	 * @param identifier
	 *            The string representation of the enumeration.
	 * @return The value that corresponds to the string.
	 * @see Enum#valueOf(Class, String)
	 */
	public T getValueOf(String identifier);
}
