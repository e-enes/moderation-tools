package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.cache.CacheManager;
import gg.enes.moderation.core.cache.CaffeineCacheManager;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ReportRepository {
    /**
     * The singleton instance of the report repository.
     */
    private static ReportRepository instance;

    /**
     * The cache manager of the report repository for reported.
     */
    private final CacheManager<UUID, List<Report>> reportedCacheManager;

    /**
     * The cache manager of the report repository for reporters.
     */
    private final CacheManager<UUID, List<Report>> reporterCacheManager;

    private ReportRepository() {
        this.reportedCacheManager = new CaffeineCacheManager<>(CacheConfig.getInstance());
        this.reporterCacheManager = new CaffeineCacheManager<>(CacheConfig.getInstance());
    }

    /**
     * Retrieves the singleton instance of the report repository.
     *
     * @return The singleton instance of the report repository.
     */
    public static ReportRepository getInstance() {
        if (instance == null) {
            instance = new ReportRepository();
        }
        return instance;
    }

    /**
     * Creates a new report entity in the database.
     *
     * @param entity The report entity to create.
     */
    public void create(final Report entity) {
        String sql = "INSERT INTO mt_reports (reporter_uuid, reported_uuid, server, reason, details, treated, treated_by, treated_at, reported_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, entity.getReporter().getUuid().toString());
            stmt.setString(2, entity.getReported().getUuid().toString());
            stmt.setString(3, entity.getServer());
            stmt.setString(4, entity.getReason());
            stmt.setString(5, entity.getDetails());
            stmt.setBoolean(6, entity.getTreated());
            stmt.setString(7, entity.getTreatedBy() != null ? entity.getTreatedBy().toString() : null);
            stmt.setObject(8, entity.getTreatedAt());
            stmt.setObject(9, entity.getReportedAt());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while creating a report entity.", e);
        }

        addToCache(entity.getReporter().getUuid(), "reporter", entity);
        addToCache(entity.getReported().getUuid(), "reported", entity);
    }

    /**
     * Retrieves all reports for a given reporter UUID.
     *
     * @param reporterUuid The UUID of the reporter to retrieve reports for.
     * @return A list of reports associated with the reporter.
     */
    public List<Report> readAllByReporter(final UUID reporterUuid) {
        return readAll(reporterUuid, "reporter");
    }

    /**
     * Retrieves all reports for a given reported UUID.
     *
     * @param reportedUuid The UUID of the reported to retrieve reports for.
     * @return A list of reports associated with the reported.
     */
    public List<Report> readAllByReported(final UUID reportedUuid) {
        return readAll(reportedUuid, "reported");
    }

    /**
     * Common method to retrieve all reports for a given UUID and type (reporter or reported).
     *
     * @param reportUuid The UUID of the user (reporter or reported).
     * @param type       The type of reports to retrieve ("reporter" or "reported").
     * @return A list of reports associated with the given UUID and type.
     */
    private List<Report> readAll(final UUID reportUuid, final String type) {
        CacheManager<UUID, List<Report>> cacheManager = type.equals("reporter") ? this.reporterCacheManager : this.reportedCacheManager;
        List<Report> reports = cacheManager.get(reportUuid);
        if (reports != null) {
            return reports;
        }

        reports = new ArrayList<>();
        String sql = type.equals("reporter") ? "SELECT * FROM mt_reports WHERE reporter_uuid = ?" : "SELECT * FROM mt_reports WHERE reported_uuid = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, reportUuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reports.add(new Report()
                            .setId(rs.getLong("report_id"))
                            .setReporter(UUID.fromString(rs.getString("reporter_uuid")))
                            .setReported(UUID.fromString(rs.getString("reported_uuid")))
                            .setServer(rs.getString("server"))
                            .setReason(rs.getString("reason"))
                            .setDetails(rs.getString("details"))
                            .setTreated(rs.getBoolean("treated"))
                            .setTreatedBy(rs.getString("treated_by") != null ? UUID.fromString(rs.getString("treated_by")) : null)
                            .setTreatedAt(rs.getObject("treated_at", LocalDateTime.class))
                            .setReportedAt(rs.getObject("reported_at", LocalDateTime.class)));
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading reports for " + type + " " + reportUuid, e);
        }

        cacheManager.set(reportUuid, reports);
        return reports;
    }

    /**
     * Updates an existing report entity in the database.
     *
     * @param entity The report entity to update.
     */
    public void update(final Report entity) {
        String sql = "UPDATE mt_reports SET reporter_uuid = ?, reported_uuid = ?, server = ?, reason = ?, details = ?, treated = ?, treated_by = ?, treated_at = ?, reported_at = ? WHERE report_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, entity.getReporter().getUuid().toString());
            stmt.setString(2, entity.getReported().getUuid().toString());
            stmt.setString(3, entity.getServer());
            stmt.setString(4, entity.getReason());
            stmt.setString(5, entity.getDetails());
            stmt.setBoolean(6, entity.getTreated());
            stmt.setString(7, entity.getTreatedBy() != null ? entity.getTreatedBy().toString() : null);
            stmt.setObject(8, entity.getTreatedAt());
            stmt.setObject(9, entity.getReportedAt());
            stmt.setLong(10, entity.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while updating a report entity.", e);
        }

        this.reportedCacheManager.del(entity.getReported().getUuid());
        this.reporterCacheManager.del(entity.getReporter().getUuid());
    }

    /**
     * Deletes a report entity from the database.
     *
     * @param reportId The ID of the report entity to delete.
     */
    public void deleteById(final Long reportId) {
        String sql = "DELETE FROM mt_reports WHERE report_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, reportId);
            stmt.executeUpdate();

            this.reporterCacheManager.clear();
            this.reportedCacheManager.clear();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting a report entity.", e);
        }
    }

    /**
     * Adds a report to the cache based on the type (reporter or reported).
     *
     * @param uuid   The UUID of the user (reporter or reported).
     * @param type   The type of cache ("reporter" or "reported").
     * @param report The report to add to the cache.
     */
    private void addToCache(final UUID uuid, final String type, final Report report) {
        CacheManager<UUID, List<Report>> cacheManager = type.equals("reporter") ? this.reporterCacheManager : this.reportedCacheManager;
        List<Report> reports = cacheManager.get(uuid);
        if (reports == null) {
            reports = new ArrayList<>();
        }
        reports.add(report);
        cacheManager.set(uuid, reports);
    }
}
