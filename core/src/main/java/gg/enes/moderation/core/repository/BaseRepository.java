package gg.enes.moderation.core.repository;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface BaseRepository<ID, T> {
    /**
     * Creates a new entity in the repository.
     *
     * @param entity The entity to be created.
     */
    void create(T entity);

    /**
     * Retrieves an entity from the repository by its ID.
     *
     * @param id The ID of the entity to be retrieved.
     * @param force Whether to force a read from the database.
     * @return The entity associated with the ID or null if not found.
     */
    T read(@Nullable ID id, @Nullable Boolean force);

    /**
     * Updates an entity in the repository.
     *
     * @param entity The entity to be updated.
     */
    void update(T entity);

    /**
     * Deletes an entity from the repository.
     *
     * @param id The ID of the entity to be deleted.
     */
    void delete(@Nullable ID id);
}
