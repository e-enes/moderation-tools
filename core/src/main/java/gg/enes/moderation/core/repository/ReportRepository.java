package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.cache.CacheManager;
import gg.enes.moderation.core.cache.CaffeineCacheManager;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.Report;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ReportRepository implements BaseRepository<Long, Report> {
    /**
     * The singleton instance of the report repository.
     */
    private static ReportRepository instance;

    /**
     * The cache manager of the report repository.
     */
    private final CacheManager<Long, Report> cacheManager;

    private ReportRepository() {
        this.cacheManager = new CaffeineCacheManager<>(CacheConfig.build());
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

    @SuppressWarnings("checkstyle:LineLength")
    @Override
    public void create(final Report entity) {
        String sql =
                "INSERT INTO mt_reports (report_id, reporter_id, reported_id, server, reason, details, treated, treated_by, treated_at, reported_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getId());
            stmt.setLong(2, entity.getReporterId());
            stmt.setLong(3, entity.getReportedId());
            stmt.setString(4, entity.getServer());
            stmt.setString(5, entity.getReason());
            stmt.setString(6, entity.getDetails());
            stmt.setBoolean(7, entity.getTreated());
            stmt.setObject(8, entity.getTreatedBy());
            stmt.setObject(9, entity.getTreatedAt());
            stmt.setObject(10, entity.getReportedAt());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while creating a report entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (Report): " + entity);
        }
        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public Report read(final Long id, final Boolean force) {
        Report report = this.cacheManager.get(id);
        if (report != null && (force == null || !force)) {
            return report;
        }

        String sql = "SELECT * FROM mt_reports WHERE report_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    report = new Report()
                            .setId(rs.getLong("report_id"))
                            .setReporterId(rs.getLong("reporter_id"))
                            .setReportedId(rs.getLong("reported_id"))
                            .setServer(rs.getString("server"))
                            .setReason(rs.getString("reason"))
                            .setDetails(rs.getString("details"))
                            .setTreated(rs.getBoolean("treated"))
                            .setTreatedBy((Long) rs.getObject("treated_by"))
                            .setTreatedAt(rs.getObject("treated_at", LocalDateTime.class))
                            .setReportedAt(rs.getObject("reported_at", LocalDateTime.class));
                    this.cacheManager.set(id, report);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading a report entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Report): " + id);
        }

        return report;
    }

    @SuppressWarnings("checkstyle:LineLength")
    @Override
    public void update(final Report entity) {
        String sql =
                "UPDATE mt_reports SET reporter_id = ?, reported_id = ?, server = ?, reason = ?, details = ?, treated = ?, treated_by = ?, treated_at = ?, reported_at = ? WHERE report_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getReporterId());
            stmt.setLong(2, entity.getReportedId());
            stmt.setString(3, entity.getServer());
            stmt.setString(4, entity.getReason());
            stmt.setString(5, entity.getDetails());
            stmt.setBoolean(6, entity.getTreated());
            stmt.setObject(7, entity.getTreatedBy());
            stmt.setObject(8, entity.getTreatedAt());
            stmt.setObject(9, entity.getReportedAt());
            stmt.setLong(10, entity.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while updating a report entity.", e);
        }
        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public void delete(final Long id) {
        String sql = "DELETE FROM mt_reports WHERE report_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting a report entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Report): " + id);
        }
        this.cacheManager.del(id);
    }

    /**
     * Retrieves all reports made by a given user.
     *
     * @param reporterId The ID of the reporter.
     * @param treated The treated status of the reports.
     * @return A list of reports made by the user.
     */
    public List<Report> findByReporterId(final Long reporterId, @Nullable final Boolean treated) {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM mt_reports WHERE reporter_id = ?";

        if (treated != null) {
            sql += " AND treated = ?";
        }
        sql += " ORDER BY reported_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, reporterId);
            if (treated != null) {
                stmt.setBoolean(2, treated);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report()
                            .setId(rs.getLong("report_id"))
                            .setReporterId(rs.getLong("reporter_id"))
                            .setReportedId(rs.getLong("reported_id"))
                            .setServer(rs.getString("server"))
                            .setReason(rs.getString("reason"))
                            .setDetails(rs.getString("details"))
                            .setTreated(rs.getBoolean("treated"))
                            .setTreatedBy((Long) rs.getObject("treated_by"))
                            .setTreatedAt(rs.getObject("treated_at", LocalDateTime.class))
                            .setReportedAt(rs.getObject("reported_at", LocalDateTime.class));
                    reports.add(report);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while retrieving reports for reporter ID: " + reporterId, e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Reporter): " + reporterId);
        }
        return reports;
    }

    /**
     * Retrieves all reports made against a given user.
     *
     * @param reportedId The ID of the reported user.
     * @param treated The treated status of the reports.
     * @return A list of reports made against the user.
     */
    public List<Report> findByReportedId(final Long reportedId, @Nullable final Boolean treated) {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM mt_reports WHERE reported_id = ?";

        if (treated != null) {
            sql += " AND treated = ?";
        }
        sql += " ORDER BY reported_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, reportedId);
            if (treated != null) {
                stmt.setBoolean(2, treated);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report()
                            .setId(rs.getLong("report_id"))
                            .setReporterId(rs.getLong("reporter_id"))
                            .setReportedId(rs.getLong("reported_id"))
                            .setServer(rs.getString("server"))
                            .setReason(rs.getString("reason"))
                            .setDetails(rs.getString("details"))
                            .setTreated(rs.getBoolean("treated"))
                            .setTreatedBy((Long) rs.getObject("treated_by"))
                            .setTreatedAt(rs.getObject("treated_at", LocalDateTime.class))
                            .setReportedAt(rs.getObject("reported_at", LocalDateTime.class));
                    reports.add(report);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while retrieving reports for reported ID: " + reportedId, e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Reported): " + reportedId);
        }
        return reports;
    }

    /**
     * Retrieves all treated or untreated reports.
     *
     * @param treated The treated status of the reports.
     * @return A list of reports based on their treated status.
     */
    public List<Report> findByTreatedStatus(final Boolean treated) {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM mt_reports WHERE treated = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setBoolean(1, treated);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report()
                            .setId(rs.getLong("report_id"))
                            .setReporterId(rs.getLong("reporter_id"))
                            .setReportedId(rs.getLong("reported_id"))
                            .setServer(rs.getString("server"))
                            .setReason(rs.getString("reason"))
                            .setDetails(rs.getString("details"))
                            .setTreated(rs.getBoolean("treated"))
                            .setTreatedBy((Long) rs.getObject("treated_by"))
                            .setTreatedAt(rs.getObject("treated_at", LocalDateTime.class))
                            .setReportedAt(rs.getObject("reported_at", LocalDateTime.class));
                    reports.add(report);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while retrieving reports with treated status: " + treated, e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Treated: " + treated);
        }
        return reports;
    }

    /**
     * Retrieves all reports.
     *
     * @return A list of all reports.
     */
    public List<Report> findAll() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM mt_reports";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Report report = new Report()
                        .setId(rs.getLong("report_id"))
                        .setReporterId(rs.getLong("reporter_id"))
                        .setReportedId(rs.getLong("reported_id"))
                        .setServer(rs.getString("server"))
                        .setReason(rs.getString("reason"))
                        .setDetails(rs.getString("details"))
                        .setTreated(rs.getBoolean("treated"))
                        .setTreatedBy((Long) rs.getObject("treated_by"))
                        .setTreatedAt(rs.getObject("treated_at", LocalDateTime.class))
                        .setReportedAt(rs.getObject("reported_at", LocalDateTime.class));
                reports.add(report);
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while retrieving all reports.", e);

            ModerationLogger.debug("SQL: " + sql);
        }
        return reports;
    }
}
