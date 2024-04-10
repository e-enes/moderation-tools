package gg.enes.moderation.core.database;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;
import gg.enes.moderation.core.utils.DatabaseUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.UUID;

public abstract class TableCreator {
    /**
     * Initializes the database table based on the annotated entity class.
     *
     * @param entityClass The class of the entity to create a table for.
     * @throws SQLException If a database access error occurs.
     */
    public static String initialize(Class<?> entityClass) throws SQLException {
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
            if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);

                createStatementBuilder
                        .append(column.name())
                        .append(" ")
                        .append(convertType(field))
                        .append(" AUTO_INCREMENT PRIMARY KEY");

                createStatementBuilder.append(", ");
            } else if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);

                createStatementBuilder
                        .append(column.name())
                        .append(" ")
                        .append(convertType(field));

                if (!column.nullable()) {
                    createStatementBuilder.append(" NOT NULL");
                }

                if (!column.defaultValue().isEmpty()) {
                    createStatementBuilder.append(" DEFAULT ");

                    String defaultValue = column.defaultValue();
                    if ("true".equals(defaultValue) || "false".equals(defaultValue)) {
                        createStatementBuilder.append(defaultValue.toUpperCase());
                    } else if ("CURRENT_TIMESTAMP".equals(defaultValue)) {
                        createStatementBuilder.append(defaultValue.toUpperCase());
                    } else {
                        try {
                            Double.parseDouble(defaultValue);
                            createStatementBuilder.append(defaultValue);
                        } catch (NumberFormatException ignored) {
                            createStatementBuilder
                                    .append(" '")
                                    .append(defaultValue)
                                    .append("'");
                        }
                    }
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

        return createStatementBuilder.toString();
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
        } else if (type == long.class || type == Long.class) {
            return "BIGINT";
        } else if (type == String.class || type == UUID.class) {
            return "VARCHAR(255)";
        } else if (type == boolean.class || type == Boolean.class) {
            return "BOOLEAN";
        } else if (type == Timestamp.class) {
            return "TIMESTAMP";
        }

        throw new IllegalArgumentException("Unmapped Java type: " + type.getSimpleName());
    }
}
