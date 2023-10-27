package agend932;

import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class DatabaseHandler {

    private static final String USERNAME = "agend932";
    private static final String PASSWORD = "W3jV5pXK";
    private static final String JDBC_URL = "jdbc:oracle:thin:@csdb.kutztown.edu:1521:orcl";

    // Establishes a connection to the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    // Creates a table if it doesn't exist
    private void createTableIfNotExists(String tableName, Connection con) throws SQLException {
        String createTableString = String.format(
                "CREATE TABLE %s (" +
                        "mediaType VARCHAR2(50), " +
                        "title VARCHAR2(255) NOT NULL UNIQUE, " +
                        "description VARCHAR2(2000), " +
                        "platform VARCHAR2(50), " +
                        "genre VARCHAR2(50), " +
                        "pictureUrl VARCHAR2(255), " +
                        "releaseDate VARCHAR2(50), " +
                        "ratedScore VARCHAR2(50), " +
                        "originalURL VARCHAR2(1000), " +
                        "extraInfo VARCHAR2(1000))",
                tableName);
        try (PreparedStatement pstmt = con.prepareStatement(createTableString)) {
            pstmt.executeUpdate();
        }
    }

    // Saves the list of media to the database
    public void saveResultsToDB(String tableName, List<Media> mediaList, HttpServletResponse response) {
        try (Connection con = getConnection()) {
            createTableIfNotExists(tableName, con);
            for (Media media : mediaList) {
                String mediaInsertString = String.format(
                        "BEGIN " +
                        "INSERT INTO %s (mediaType, pictureUrl, title, description, platform, genre, releaseDate, ratedScore, originalURL, extraInfo) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?); " +
                        "EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; " +
                        "END;",
                        tableName);
                try (PreparedStatement pstmt = con.prepareStatement(mediaInsertString)) {
                    pstmt.setString(1, media.getMediaType());
                    pstmt.setString(2, media.getPictureUrl());
                    pstmt.setString(3, media.getTitle());
                    pstmt.setString(4, media.getDescription());
                    pstmt.setString(5, media.getPlatform());
                    pstmt.setString(6, media.getGenre());
                    pstmt.setString(7, media.getReleaseDate());
                    pstmt.setString(8, media.getRatedScore());
                    pstmt.setString(9, media.getOriginalURL());
                    pstmt.setString(10, media.getExtraInfo());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            handleError(response, "An error occurred while saving to the database: " + e.getMessage());
        }
    }

    // Fetches all media data from the database
    public List<Media> fetchAllData(HttpServletResponse response) {
        List<Media> medias = new ArrayList<>();
        try (Connection con = getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM agend932MediasDB")) {
            while (rs.next()) {
                Media media = new Media(
                        rs.getString("mediaType"),
                        rs.getString("pictureUrl"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("platform"),
                        rs.getString("genre"),
                        rs.getString("releaseDate"),
                        rs.getString("ratedScore"),
                        rs.getString("originalURL"),
                        rs.getString("extraInfo"));
                medias.add(media);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 942) {
                handleError( response, "There is no table in the DB. Code: " + e.getMessage());
            } else {
            handleError(response, "An error occurred while fetching data: " + e.getMessage());
        }
    }
        return medias;
    }


    // Drops the specified tables from the database
    public void dropTables(HttpServletResponse response, String... tableNames) {
        try (Connection con = getConnection();
                Statement stmt = con.createStatement()) {
            for (String tableName : tableNames) {
                stmt.executeUpdate("DROP TABLE " + tableName);
            }
        } catch (SQLException e) {
            handleError(response, "An error occurred while dropping the tables: " + e.getMessage());
        }
    }

    // Handles errors by sending a response to the client
    private void handleError(HttpServletResponse response, String message) {
        try {
            PrintWriter out = response.getWriter();
            out.println(message);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
