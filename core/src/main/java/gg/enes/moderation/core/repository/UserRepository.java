package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.entity.User;

public class UserRepository implements BaseRepository<User, Long> {
    /**
     * Creates a new entity in the repository.
     *
     * @param entity The entity to be created.
     */
    @Override
    public void create(final User entity) {
    }

    /**
     * Retrieves an entity from the repository by its ID.
     *
     * @param id The ID of the entity to be retrieved.
     * @return The entity associated with the ID or null if not found.
     */
    @Override
    public User read(final Long id) {
        return null;
    }

    /**
     * Updates an entity in the repository.
     *
     * @param entity The entity to be updated.
     */
    @Override
    public void update(final User entity) {
    }

    /**
     * Deletes an entity from the repository.
     *
     * @param id The ID of the entity to be deleted.
     */
    @Override
    public void delete(final Long id) {
    }
}
