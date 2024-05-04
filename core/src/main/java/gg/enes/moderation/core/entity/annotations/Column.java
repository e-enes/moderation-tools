package gg.enes.moderation.core.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * The name of the column in the database.
     *
     * @return The name of the column.
     */
    String name();

    /**
     * The type of the column in the database.
     *
     * @return The type of the column.
     */
    boolean nullable() default true;

    /**
     * The default value of the column in the database.
     *
     * @return The default value of the column.
     */
    String defaultValue() default "";
}
