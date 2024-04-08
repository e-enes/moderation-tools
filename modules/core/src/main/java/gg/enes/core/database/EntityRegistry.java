package gg.enes.core.database;

import gg.enes.core.entity.annotations.Table;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Set;

public class EntityRegistry {
    private static final String ENTITY_PACKAGE = "gg.enes.core.entity";

    /**
     * Uses Reflections to find all entity classes annotated with @Table within the entity package.
     *
     * @return A set of entity classes.
     */
    public static Set<Class<?>> getEntityClasses() {
        Reflections reflections = new Reflections(ENTITY_PACKAGE, Scanners.TypesAnnotated.with(Table.class));
        return reflections.getTypesAnnotatedWith(Table.class);
    }
}
