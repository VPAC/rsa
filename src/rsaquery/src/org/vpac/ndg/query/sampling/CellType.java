package org.vpac.ndg.query.sampling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.vpac.ndg.query.Filter;
import org.vpac.ndg.query.math.Element;

import ucar.ma2.DataType;

/**
 * This annotation allows fields such as {@link Cell Cells} in a {@link Filter}
 * to inherit their type from {@link PixelSource PixelSources}. The
 * <em>type</em> in this case refers to {@link Element} and its subclasses. This
 * is to allow a filter to determine what type to use for processing.
 *
 * @author Alex Fraser
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
public @interface CellType {
	/**
	 * The type to use for the field. This may be a single identifier, in which
	 * case the cell will be scalar, or it may be a comma-separated list of
	 * identifiers, in which case the cell will be vector. Types are resolved as
	 * follows:
	 *
	 * <ol>
	 * <li>If an identifier matches a type name in the {@link DataType} enum,
	 * then that data type is used.</li>
	 * <li>Otherwise, the type will be inherited from another field of the same
	 * name. If no such field exists, an exception will be thrown.</li>
	 * </ol>
	 *
	 * <p>
	 * Examples:
	 * </p>
	 *
	 * <pre>
	 * # Same type as inputA (scalar or vector).
	 * @CellType("inputA")
	 * public Cell foo;
	 *
	 * # Vector with a single byte component.
	 * @CellType("byte")
	 * public Cell bar;
	 *
	 * # Vector consisting of the types of inputA and inputB.
	 * @CellType("inputA,inputB")
	 * public Cell baz;
	 * </pre>
	 *
	 * @return The type to use for the field.
	 */
	String value();

	/**
	 * @return The type to reinterpret the inherited types as.
	 */
	String as() default "";
}
