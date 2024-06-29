package gg.enes.moderation.core.database.config;

public class DatabaseConfig {
    /**
     * The type of database to connect to.
     */
    private DatabaseType dbType;

    /**
     * The file name of the SQLite database.
     */
    private String fileName;

    /**
     * The host of the MySQL database.
     */
    private String host;

    /**
     * The port of the MySQL database.
     */
    private int port;

    /**
     * The name of the MySQL database.
     */
    private String databaseName;

    /**
     * The username to connect to the MySQL database.
     */
    private String username;

    /**
     * The password to connect to the MySQL database.
     */
    private String password;

    /**
     * Sets the type of database to connect to.
     *
     * @param newDbType The database type as a DatabaseType enum.
     * @return The current DatabaseConfig instance.
     */
    public DatabaseConfig setDbType(final DatabaseType newDbType) {
        this.dbType = newDbType;
        return this;
    }

    /**
     * Retrieves the type of database to connect to.
     *
     * @return The database type as a DatabaseType enum.
     */
    public DatabaseType getDbType() {
        return this.dbType;
    }

    /**
     * Sets the file name of the SQLite database.
     *
     * @param newFileName The file name of the SQLite database.
     * @return The current DatabaseConfig instance.
     */
    public DatabaseConfig setFileName(final String newFileName) {
        this.fileName = newFileName;
        return this;
    }

    /**
     * Retrieves the file name of the SQLite database.
     *
     * @return The file name of the SQLite database.
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Sets the host of the MySQL database.
     *
     * @param newHost The host of the MySQL database.
     * @return The current DatabaseConfig instance.
     */
    public DatabaseConfig setHost(final String newHost) {
        this.host = newHost;
        return this;
    }

    /**
     * Retrieves the host of the MySQL database.
     *
     * @return The host of the MySQL database.
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Sets the port of the MySQL database.
     *
     * @param newPort The port of the MySQL database.
     * @return The current DatabaseConfig instance.
     */
    public DatabaseConfig setPort(final int newPort) {
        this.port = newPort;
        return this;
    }

    /**
     * Retrieves the port of the MySQL database.
     *
     * @return The port of the MySQL database.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Sets the name of the MySQL database.
     *
     * @param newDatabaseName The name of the MySQL database.
     * @return The current DatabaseConfig instance.
     */
    public DatabaseConfig setDatabaseName(final String newDatabaseName) {
        this.databaseName = newDatabaseName;
        return this;
    }

    /**
     * Retrieves the name of the MySQL database.
     *
     * @return The name of the MySQL database.
     */
    public String getDatabaseName() {
        return this.databaseName;
    }

    /**
     * Sets the username to connect to the MySQL database.
     *
     * @param newUsername The username to connect to the MySQL database.
     * @return The current DatabaseConfig instance.
     */
    public DatabaseConfig setUsername(final String newUsername) {
        this.username = newUsername;
        return this;
    }

    /**
     * Retrieves the username to connect to the MySQL database.
     *
     * @return The username to connect to the MySQL database.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the password to connect to the MySQL database.
     *
     * @param newPassword The new password to connect to the MySQL database.
     * @return The current DatabaseConfig instance.
     */
    public DatabaseConfig setPassword(final String newPassword) {
        this.password = newPassword;
        return this;
    }

    /**
     * Retrieves the password to connect to the MySQL database.
     *
     * @return The password to connect to the MySQL database.
     */
    public String getPassword() {
        return this.password;
    }
}

