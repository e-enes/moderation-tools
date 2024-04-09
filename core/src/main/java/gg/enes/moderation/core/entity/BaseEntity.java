package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseEntity {
    private final Map<String, String> fieldToColumnMapping = new HashMap<>();

    /**
     * Constructs a new BaseEntity and initializes field-to-column mappings.
     */
    public BaseEntity() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                fieldToColumnMapping.put(field.getName(), column.name());
            }
        }
    }

    /**
     * Retrieves the field-to-column name mappings.
     *
     * @return A map of entity field names to database column names.
     */
    public Map<String, String> getColumns() {
        return fieldToColumnMapping;
    }
}
