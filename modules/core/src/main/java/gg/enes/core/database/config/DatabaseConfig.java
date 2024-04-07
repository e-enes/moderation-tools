package gg.enes.core.database.config;

public class DatabaseConfig {
    private String dbType;
    private String fileName;

    private String host;
    private int port = 3306;
    private String databaseName;
    private String username;
    private String password;

    public DatabaseConfig setDbType(String dbType) {
        this.dbType = dbType;
        return this;
    }

    public String getDbType() {
        return this.dbType;
    }

    public DatabaseConfig setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileName() {
        return this.fileName;
    }

    public DatabaseConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getHost() {
        return this.host;
    }

    public DatabaseConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public DatabaseConfig setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public DatabaseConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public DatabaseConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPassword() {
        return this.password;
    }
}
