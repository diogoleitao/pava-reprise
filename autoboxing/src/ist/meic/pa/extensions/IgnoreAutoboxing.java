package ist.meic.pa.extensions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * If this annotation is added to a method, then all of the autoboxing
 * operations that occur on its body are ignored and not profiled.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreAutoboxing {

}
