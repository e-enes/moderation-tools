package gg.enes.moderation.core.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JoinColumn {
    /**
     * The name of the column in the database.
     *
     * @return The name of the column.
     */
    String name();

    /**
     * The name of the referenced column in the database.
     *
     * @return The name of the referenced column.
     */
    String referencedColumnName();
}

