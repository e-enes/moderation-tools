package gg.enes.moderation.core;

import gg.enes.moderation.core.database.EntityRegistry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

public class EntityTest {
    /**
     * Tests the entity registry.
     */
    @Test
    public void testRegistry() {
        Set<Class<?>> entityRegistry = EntityRegistry.getEntityClasses();

        for (Class<?> entity : entityRegistry) {
            Field[] fields = entity.getDeclaredFields();

            for (Field field : fields) {
                ModerationLogger.debug(Arrays.toString(field.getAnnotations()));
            }
        }
    }
}
