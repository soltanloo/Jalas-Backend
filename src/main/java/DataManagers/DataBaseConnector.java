package DataManagers;

import org.apache.commons.dbcp.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DataBaseConnector implements DBConnectionPool{
    private String url;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static DataBaseConnector DBConPool;
    private static final int INITIAL_POOL_SIZE = 20;
    private Semaphore sem;
    static final String DB_URL = "jdbc:sqlite:jalas.DB";

    public static void init() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        createDatabaseIfNotExists();

        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection());
        }

        DBConPool = new DataBaseConnector(DB_URL, pool);
    }

    private DataBaseConnector() {}

    private DataBaseConnector(String url, List<Connection> pool) {
        this.url = url;
        this.sem = new Semaphore(1);
        connectionPool = pool;
    }

//    private static void createDatabaseIfNotExists() {
//        Connection conn;
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "root");
//            Statement s = conn.createStatement();
//            s.executeUpdate("CREATE DATABASE IF NOT EXISTS jaboonjaDB;");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public static Connection getConnection() {
        return DBConPool.getPoolConnection();
    }

    @Override
    public Connection getPoolConnection() {
        Connection connection = null;
        try {
            sem.acquire();
            connection = connectionPool.remove(connectionPool.size() - 1);
            usedConnections.add(connection);
            sem.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void releaseConnection(Connection con) {
        DBConPool.releasePoolConnection(con);
    }

    @Override
    public boolean releasePoolConnection(Connection connection) {
        try {
            sem.acquire();
            connectionPool.add(connection);
            sem.release();
            return usedConnections.remove(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    public String getUrl() {
        return url;
    }

}
