package gg.enes.moderation.core;

import org.junit.jupiter.api.Test;

public class EntityTest {
    /**
     * Tests the entity registry.
     */
    @Test
    public void testRegistry() {
        System.setProperty("development", "true");

//        Set<Class<?>> entityRegistry = EntityRegistry.getEntityClasses();
//
//        for (Class<?> entity : entityRegistry) {
//            Field[] fields = entity.getDeclaredFields();
//
//            for (Field field : fields) {
//                ModerationLogger.debug(Arrays.toString(field.getAnnotations()));
//            }
//        }
    }
}
