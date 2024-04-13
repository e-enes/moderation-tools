package gg.enes.moderation.core;

import gg.enes.moderation.core.database.EntityRegistry;
import gg.enes.moderation.core.database.TableCreator;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Set;

public class DatabaseTest {
    @Test
    public void testTable() throws SQLException {
        Set<Class<?>> entityRegistry = EntityRegistry.getEntityClasses();

        for (Class<?> entity : entityRegistry) {
            String query = TableCreator.initialize(entity);
            System.out.println(query);
        }
    }
}
