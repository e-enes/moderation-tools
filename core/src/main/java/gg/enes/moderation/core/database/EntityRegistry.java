package gg.enes.moderation.core.database;

import gg.enes.moderation.core.entity.annotations.Table;
import org.reflections.Reflections;

import java.util.Set;

public final class EntityRegistry {
    /**
     * The package where entity classes are located.
     */
    private static final String ENTITY_PACKAGE = "gg.enes.moderation.core.entity";

    private EntityRegistry() {
    }

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
