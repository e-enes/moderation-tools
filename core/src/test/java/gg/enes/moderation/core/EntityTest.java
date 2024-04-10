package gg.enes.moderation.core;

import gg.enes.moderation.core.database.EntityRegistry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

public class EntityTest {
    @Test
    public void testRegistry() {
        Set<Class<?>> entityRegistry = EntityRegistry.getEntityClasses();

        for (Class<?> entity : entityRegistry) {
            Field[] fields = entity.getFields();

            for (Field field : fields) {
                System.out.println(Arrays.toString(field.getAnnotations()));
            }
        }
    }
}
