package org.vpac.ndg.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class is used to annotate the required description of a filter.
 * Currently user can use it to annotate filter name and description.
 * 
 * @author hsumanto
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Description {
	String name() default "";

	String description() default "";
}
