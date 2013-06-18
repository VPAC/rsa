package org.vpac.ndg.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Public fields in filters may be configured using a config file. This
 * annotation may be applied to set constraints on those fields.
 *
 * @author Alex Fraser
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Constraint {
	int dimensions() default 0;
}
