package gg.enes.moderation.core.database;

import gg.enes.moderation.core.entity.annotations.Table;
import org.reflections.Reflections;

import java.util.Set;

public class EntityRegistry {
    private static final String ENTITY_PACKAGE = "gg.enes.moderation.core.entity";

    /**
     * Uses Reflections to find all entity classes annotated with @Table within the entity package.
     *
     * @return A set of entity classes.
     */
    public static Set<Class<?>> getEntityClasses() {
        Reflections reflections = new Reflections(ENTITY_PACKAGE);
        return reflections.getTypesAnnotatedWith(Table.class);
    }
}
