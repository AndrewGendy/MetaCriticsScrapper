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

    private boolean doesTableExist(String tableName, Connection con) throws SQLException {
        String checkTableExistsQuery = "SELECT COUNT(*) FROM user_tables WHERE table_name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(checkTableExistsQuery)) {
            pstmt.setString(1, tableName.toUpperCase());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private void createTableIfNotExists(String tableName, Connection con) throws SQLException {
        if (!doesTableExist(tableName, con)) {
            String createTableString = String.format(
                    "CREATE TABLE %s (" +
                            "mediaType VARCHAR2(50), " +
                            "title VARCHAR2(255) NOT NULL UNIQUE, " +
                            "description VARCHAR2(4000), " +
                            "platform VARCHAR2(50), " +
                            "genre VARCHAR2(50), " +
                            "pictureUrl VARCHAR2(255), " +
                            "releaseDate VARCHAR2(50), " +
                            "ratedScore VARCHAR2(50), " +
                            "originalURL VARCHAR2(1000), " +
                            "metascore VARCHAR2(20), " +
                            "extraInfo VARCHAR2(1000))",
                    tableName);
            try (PreparedStatement pstmt = con.prepareStatement(createTableString)) {
                pstmt.executeUpdate();
            }
        }
    }

    // Saves the list of media to the database
    public void saveResultsToDB(String tableName, List<Media> mediaList, HttpServletResponse response) {
        try (Connection con = getConnection()) {
            createTableIfNotExists(tableName, con);
            String mediaInsertString = String.format(
                    "INSERT INTO %s (mediaType, pictureUrl, title, description, platform, genre, releaseDate, ratedScore, originalURL, metascore, extraInfo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    tableName);

            for (Media media : mediaList) {
                try (PreparedStatement pstmt = con.prepareStatement(mediaInsertString)) {
                    pstmt.setString(1, media.getMediaType());
                    pstmt.setString(2, media.getPictureUrl());
                    pstmt.setString(3, media.getTitle());
                    pstmt.setString(4, media.getDescription());
                    pstmt.setString(5, media.getPlatform());
                    pstmt.setString(6, media.getGenre());
                    pstmt.setString(7, media.getReleaseDate());
                    pstmt.setString(8, media.getRatedScore());
                    pstmt.setString(9, "https://www.metacritic.com" + media.getOriginalURL());
                    pstmt.setString(10, media.getMetascore());
                    pstmt.setString(11, media.getExtraInfo());
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    // Check if the error code corresponds to a UNIQUE constraint violation
                    if (e.getErrorCode() != 1) {  // Error code 1 corresponds to a UNIQUE constraint violation in Oracle
                        handleError(response, "An error occurred while saving to the database: " + e.getMessage());
                    }
                    // If it's a UNIQUE constraint violation, just continue to the next iteration
                }
            }
        } catch (SQLException e) {
            handleError(response, "An error occurred while connecting to or working with the database: " + e.getMessage());
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
                        rs.getString("metascore"),
                        rs.getString("extraInfo"));
                medias.add(media);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 942) {
                handleError( response, "There is no table in the DB with that name. Code: " + e.getMessage());
            } else {
            handleError(response, "An error occurred while fetching data: " + e.getMessage());
        }
    }
        return medias;
    }

    // Fetches Filtered media data from the database
    public List<Media> fetchFilteredData(HttpServletResponse response, String mediaType, String platform, String genre) {
        List<Media> medias = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM agend932MediasDB WHERE 1=1"); // Always true condition to simplify appending below
        
        // Check if mediaType is specified and not a special value like "ALL"
        if (mediaType != null && !mediaType.isEmpty() && !mediaType.equalsIgnoreCase("ALL")) {
            queryBuilder.append(" AND UPPER(mediaType) = UPPER(?)");
        }
        
        // Check if platform is specified and not "ALL"
        if (platform != null && !platform.isEmpty() && !platform.equalsIgnoreCase("ALL")) {
            queryBuilder.append(" AND UPPER(platform) = UPPER(?)");
        }
        
        // Check if genre is specified and not "ALL"
        if (genre != null && !genre.isEmpty() && !genre.equalsIgnoreCase("ALL")) {
            queryBuilder.append(" AND UPPER(genre) = UPPER(?)");
        }
        
        try (Connection con = getConnection();
            PreparedStatement pstmt = con.prepareStatement(queryBuilder.toString())) {
            
            int paramIndex = 1;
            if (mediaType != null && !mediaType.isEmpty() && !mediaType.equalsIgnoreCase("ALL")) {
                pstmt.setString(paramIndex++, mediaType);
            }
            if (platform != null && !platform.isEmpty() && !platform.equalsIgnoreCase("ALL")) {
                pstmt.setString(paramIndex++, platform);
            }
            if (genre != null && !genre.isEmpty() && !genre.equalsIgnoreCase("ALL")) {
                pstmt.setString(paramIndex++, genre);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
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
                        rs.getString("metascore"),
                        rs.getString("extraInfo"));
                medias.add(media);
            }
        } catch (SQLException e) {
            handleError(response, "An error occurred while fetching filtered data: " + e.getMessage());
        }
        return medias;
    }

    // Drops the specified tables from the database
    public void dropTables() {
        try (Connection con = getConnection();
            Statement stmt = con.createStatement()) {
            stmt.executeUpdate("DROP TABLE agend932MediasDB");
        } catch (SQLException e) {
            // handleError(response, "An error occurred while dropping the tables: " + e.getMessage()); // can't use this now since I am not passing response anymore
            System.err.println("An error occurred while dropping the tables: " + e.getMessage());
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
