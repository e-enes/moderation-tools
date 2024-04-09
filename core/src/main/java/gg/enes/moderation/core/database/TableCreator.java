package gg.enes.moderation.core.database;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Table;
import gg.enes.moderation.core.utils.DatabaseUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public abstract class TableCreator {
    /**
     * Initializes the database table based on the annotated entity class.
     *
     * @param entityClass The class of the entity to create a table for.
     * @throws SQLException If a database access error occurs.
     */
    public static void initialize(Class<?> entityClass) throws SQLException {
        if (!entityClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("No @Table annotation present on class: " + entityClass.getName());
        }

        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        String tableName = tableAnnotation.name();

        StringBuilder createStatementBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" (");

        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                createStatementBuilder.append(column.name())
                        .append(" ")
                        .append(convertType(field));

                if (column.primary()) {
                    createStatementBuilder.append(" PRIMARY KEY");
                }

                createStatementBuilder.append(", ");
            }
        }

        int lastCommaIndex = createStatementBuilder.lastIndexOf(", ");
        if (lastCommaIndex != -1) {
            createStatementBuilder.delete(lastCommaIndex, createStatementBuilder.length());
        }
        createStatementBuilder.append(");");

        Connection connection = DatabaseManager.getInstance().getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute(createStatementBuilder.toString());
        } finally {
            DatabaseUtil.closeQuietly(connection);
        }
    }

    /**
     * Converts a Java field type to the corresponding SQL type.
     *
     * @param field The field to convert.
     * @return The SQL type as a String.
     */
    private static String convertType(Field field) {
        Class<?> type = field.getType();

        if (type == int.class || type == Integer.class) {
            return "INT";
        } else if (type == String.class) {
            return "VARCHAR(255)";
        } else if (type == boolean.class || type == Boolean.class) {
            return "BOOLEAN";
        } else if (type == LocalDateTime.class) {
            return "DATETIME";
        }

        throw new IllegalArgumentException("Unmapped Java type: " + type.getSimpleName());
    }
}
