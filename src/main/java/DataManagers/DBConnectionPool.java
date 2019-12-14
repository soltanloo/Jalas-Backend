package DataManagers;

import java.sql.Connection;

public interface DBConnectionPool {
    Connection getPoolConnection();
    boolean releasePoolConnection(Connection connection);
    String getUrl();
}