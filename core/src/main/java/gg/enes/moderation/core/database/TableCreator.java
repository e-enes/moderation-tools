package gg.enes.moderation.core.database;

import gg.enes.moderation.core.database.config.DatabaseType;
import gg.enes.moderation.core.entity.User;
import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;
import gg.enes.moderation.core.utils.DatabaseUtil;
import gg.enes.moderation.core.utils.EnvironmentUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TableCreator {
    private TableCreator() {
    }

    /**
     * Initializes a table in the database based on the provided entity class.
     *
     * @param entityClass The entity class to initialize a table for.
     * @return The SQL query used to create the table.
     * @throws SQLException If an error occurs while creating the table.
     */
    public static String initialize(final Class<?> entityClass) throws SQLException {
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
                        .append(" PRIMARY KEY AUTO_INCREMENT");

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
                                    .append("'")
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

        String query = createStatementBuilder.toString();

        if (DatabaseManager.getDatabaseType() == DatabaseType.SQLITE) {
            query = query
                    .replace(" AUTO_INCREMENT", " AUTOINCREMENT")
                    .replace(" INT", " INTEGER")
                    .replace(" BIGINT", " INTEGER")
                    .replace(" VARCHAR(255)", " TEXT")
                    .replace(" TIMESTAMP", " DATETIME");
        }

        if (!EnvironmentUtil.isTestEnvironment()) {
            Connection connection = DatabaseManager.getConnection();

            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            } finally {
                DatabaseUtil.closeQuietly(connection);
            }
        }

        return createStatementBuilder.toString();
    }

    /**
     * Converts a Java field type to the corresponding SQL type.
     *
     * @param field The field to convert.
     * @return The SQL type as a String.
     */
    private static String convertType(final Field field) {
        Class<?> type = field.getType();

        if (type == int.class || type == Integer.class) {
            return "INT";
        } else if (type == Long.class) {
            return "BIGINT";
        } else if (type == String.class || type == UUID.class || type == User.class) {
            return "VARCHAR(255)";
        } else if (type == Boolean.class) {
            return "BOOLEAN";
        } else if (type == Timestamp.class || type == LocalDateTime.class) {
            return "TIMESTAMP";
        }

        throw new IllegalArgumentException("Unmapped Java type: " + type.getSimpleName());
    }
}
