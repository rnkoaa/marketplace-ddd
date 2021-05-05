package com.marketplace.eventstore.jdbc;

import com.marketplace.eventstore.framework.event.EventRecord;
import com.marketplace.eventstore.framework.event.ImmutableEventRecord;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTemplate {

    String connectionString = "jdbc:sqlite:data/marketplace.db";
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);
    private Connection connection;

    /**
     * Connect to a sample database
     */
    public static Connection connect(String url) throws SQLException {
        Connection conn = null;
        // create a connection to the database
        conn = DriverManager.getConnection(url);
        if (conn != null) {
            LOGGER.debug("Connection to SQLite has been established.");
            DatabaseMetaData meta = conn.getMetaData();
            LOGGER.debug("The driver name is " + meta.getDriverName());
        }

        return conn;
    }

    private Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = connect(connectionString);
        }
        return connection;
    }

    void executeBatchQuery() {

    }

    void executeQuery() {

    }

    void update() {

    }

    void insert() {

    }

    public ResultSet executeFindQuery(String query, List<Object> params) throws SQLException {
        Objects.requireNonNull(query, "sql query cannot be null");
        connection = getConnection();
        if (connection == null) {
            return null;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int idx = 0; idx < params.size(); idx++) {
            Object param = params.get(idx);
            if (param instanceof String s) {
                preparedStatement.setString(idx += 1, s);
            }
        }

        return preparedStatement.executeQuery();
    }

    // public void insert(String name, double capacity) {
    //        String sql = "INSERT INTO employees(name, capacity) VALUES(?,?)";
    //
    //        try{
    //            Connection conn = this.connect();
    //            PreparedStatement pstmt = conn.prepareStatement(sql);
    //            pstmt.setString(1, name);
    //            pstmt.setDouble(2, capacity);
    //            pstmt.executeUpdate();
    //        } catch (SQLException e) {
    //            System.out.println(e.getMessage());
    //        }
    //    }

    //  public void selectAll(){
    //        String sql = "SELECT * FROM employees";
    //
    //        try {
    //            Connection conn = this.connect();
    //            Statement stmt  = conn.createStatement();
    //            ResultSet rs    = stmt.executeQuery(sql);
    //
    //            // loop through the result set
    //            while (rs.next()) {
    //                System.out.println(rs.getInt("id") +  "\t" +
    //                                   rs.getString("name") + "\t" +
    //                                   rs.getDouble("capacity"));
    //            }
    //        } catch (SQLException e) {
    //            System.out.println(e.getMessage());
    //        }
    //    }

    // https://github.com/rnkoaa/book-info/blob/6dc0ef3c5e730f84205f515e42d23cd06c98083b/goodreads-source-processor/src/main/java/com/books/goodreads/sqlite/repository/GoodReadsRepository.java#L174
    //   /**
    //     * Create a new table in the test database
    //     */
    //    public static void createTables(Connection connection) {
    //
    //        // SQL statement for creating a new table
    //        String[] sql = {
    //            "CREATE TABLE IF NOT EXISTS book (id integer PRIMARY KEY, data text);",
    //            "CREATE TABLE IF NOT EXISTS book_author (book_id integer, author_id integer, data text, PRIMARY KEY(book_id, author_id));",
    //            "CREATE TABLE IF NOT EXISTS book_review (book_id integer, review_id text, data text, PRIMARY KEY(book_id, review_id));",
    //        };
    //
    //        try {
    //            Statement statement = connection.createStatement();
    //            for (String query : sql) {
    //                statement.execute(query);
    //            }
    //        } catch (SQLException ex) {
    //            System.out.println(ex.getMessage());
    //        }
    //
    ////    try (Connection conn = DriverManager.getConnection(url);
    ////        Statement stmt = conn.createStatement()) {
    ////      // create a new table
    ////      stmt.execute(sql);
    ////    } catch (SQLException e) {
    ////      System.out.println(e.getMessage());
    ////    }
    //    }
    //
    //    /**
    //     * select all rows in the warehouses table
    //     */
    ////  public void selectAll(){
    ////    String sql = "SELECT id, name, capacity FROM warehouses";
    ////
    ////    try (Connection conn = this.connect();
    ////        Statement stmt  = conn.createStatement();
    ////        ResultSet rs    = stmt.executeQuery(sql)){
    ////
    ////      // loop through the result set
    ////      while (rs.next()) {
    ////        System.out.println(rs.getInt("id") +  "\t" +
    ////            rs.getString("name") + "\t" +
    ////            rs.getDouble("capacity"));
    ////      }
    ////    } catch (SQLException e) {
    ////      System.out.println(e.getMessage());
    ////    }
    ////  }

    //  PreparedStatement bookAuthorInsert = insertStatements.get("bookAuthorInsert");
    //        if (bookAuthorInsert == null) {
    //            throw new IllegalArgumentException("Book Author Insert PreparedStatement not found");
    //        }
    //        for (Author author : authors) {
    //            int authorId = NumberUtils.toInt(author.getId());
    //            String authorJson = objectMapper.writeValueAsString(author);
    //            bookAuthorInsert.setInt(1, bookId);
    //            bookAuthorInsert.setInt(2, authorId);
    //            bookAuthorInsert.setString(3, authorJson);
    //            bookAuthorInsert.addBatch();
    //        }
    //        bookAuthorInsert.executeBatch();

}
