package gg.enes.moderation.core.repository;

public interface BaseRepository<T, ID> {
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
     * @return The entity associated with the ID or null if not found.
     */
    T read(ID id);

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
    void delete(ID id);
}
