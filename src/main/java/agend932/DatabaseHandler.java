package agend932;

import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import agend932.beans.Media;

/**
 * This class serves as the database handler for the Metacritic Scraper application.
 * It provides functionality to establish database connections, execute queries, and manage database tables.
 */
public class DatabaseHandler {

    private DataSource dataSource;

    /**
     * Initializes a new DatabaseHandler object. It sets up the DataSource by looking up the JNDI context.
     * It is assumed that the DataSource is configured with the name "jdbc/DatabaseConnectionConfiguration" in the web server context.
     * This DataSource is then used to provide database connections throughout the application.
     */
    public DatabaseHandler() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/DatabaseConnectionConfiguration");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtains a database connection from the configured DataSource.
     * This method is used internally to acquire connections for executing SQL operations.
     *
     * @return A Connection object to interact with the database, or null if a connection cannot be established.
     */
    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if a specific table exists in the database.
     *
     * @param tableName The name of the table to check.
     * @param con       The database connection to use.
     * @return True if the table exists, false otherwise.
     * @throws SQLException If there is an error executing the query.
     */
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

    /**
     * Creates a new table in the database if it does not already exist.
     *
     * @param tableName The name of the table to create.
     * @param con       The database connection to use.
     * @throws SQLException If there is an error creating the table.
     */
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
                            "metascore VARCHAR2(5), " +
                            "extraInfo VARCHAR2(1000))",
                    tableName);
            try (PreparedStatement pstmt = con.prepareStatement(createTableString)) {
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * Saves a list of media entries to the specified table in the database.
     *
     * @param tableName The name of the table where the media will be stored.
     * @param mediaList A list of {@code Media} objects to be saved to the database.
     * @param response  The HttpServletResponse object used for error handling.
     */
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

    /**
     * Executes a simple query on the database for demonstration purposes and sends the results back to the client.
     *
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     */
    public void simpleQuery(HttpServletResponse response) {
        String sql = "SELECT releaseDate, TO_NUMBER(SUBSTR(releaseDate, -4)) AS ExtractedYear FROM agend932MediasDB";
        
        try (Connection con = getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String releaseDate = rs.getString("releaseDate");
                int extractedYear = rs.getInt("ExtractedYear");
                
                handleError(response, "Release Date: " + releaseDate + ", Extracted Year: " + extractedYear);
            }
        } catch (SQLException e) {
            handleError(response, "An error occurred while executing the simpleQuery: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fetches all media data from the database and returns it as a list.
     *
     * @param response The HttpServletResponse object used for error handling.
     * @return A list of {@code Media} objects containing all the media data from the database.
     */
    public List<Media> fetchAllData(HttpServletResponse response) { // could use dynamic table name just like in line 112, but at this point of writing the code I had made the decision to only use one table.
        List<Media> medias = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM agend932MediasDB");
        try (Connection con = getConnection();
            PreparedStatement pstmt = con.prepareStatement(queryBuilder.toString())) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Media media = new Media();
                media.setMediaType(rs.getString("mediaType"));
                media.setPictureUrl(rs.getString("pictureUrl"));
                media.setTitle(rs.getString("title"));
                media.setDescription(rs.getString("description"));
                media.setPlatform(rs.getString("platform"));
                media.setGenre(rs.getString("genre"));
                media.setReleaseDate(rs.getString("releaseDate"));
                media.setRatedScore(rs.getString("ratedScore"));
                media.setOriginalURL(rs.getString("originalURL"));
                media.setMetascore(rs.getString("metascore"));
                media.setExtraInfo(rs.getString("extraInfo"));
    
                medias.add(media);
            }
            // Using the setters above instead of the constructor below because I updated the Media class to a JavaBean that use getters and setters instead of a constructor

            // while (rs.next()) {
            //     Media media = new Media(
            //             rs.getString("mediaType"),
            //             rs.getString("pictureUrl"),
            //             rs.getString("title"),
            //             rs.getString("description"),
            //             rs.getString("platform"),
            //             rs.getString("genre"),
            //             rs.getString("releaseDate"),
            //             rs.getString("ratedScore"),
            //             rs.getString("originalURL"),
            //             rs.getString("metascore"),
            //             rs.getString("extraInfo"));
            //     medias.add(media);
            // }
        } catch (SQLException e) {
            if (e.getErrorCode() == 942) {
                handleError(response, "There is no table in the DB with that name. Code: " + e.getMessage());
            } else {
                handleError(response, "An error occurred while fetching data: " + e.getMessage());
            }
        }
        return medias;
    }

    /**
     * Fetches filtered media data based on provided search criteria and returns it as a list.
     *
     * @param response      The HttpServletResponse object used for error handling.
     * @param searchKeyword The keyword to filter by the title.
     * @param mediaType     The media type to filter by.
     * @param platform      The platform to filter by.
     * @param genre         The genre to filter by.
     * @param sortOption    The sorting option for the results.
     * @param minMetascore  The minimum metascore for filtering results.
     * @param maxMetascore  The maximum metascore for filtering results.
     * @param beforeYear    The year before which the media was released for filtering results.
     * @param afterYear     The year after which the media was released for filtering results.
     * @return A list of {@code Media} objects containing the filtered media data from the database.
     */
    public List<Media> fetchFilteredData(HttpServletResponse response, String searchKeyword, String mediaType, String platform, String genre, String sortOption, Integer minMetascore, Integer maxMetascore, String beforeYear, String afterYear) {
        List<Media> medias = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM agend932MediasDB WHERE 1=1"); // using WHERE 1=1 to make an always true condition to simplify appending below
        
        // Check if mediaType is specified and not a special value like "ALL"
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            queryBuilder.append(" AND UPPER(title) like UPPER(?)");
        }
        if (mediaType != null && !mediaType.isEmpty() && !mediaType.equalsIgnoreCase("ALL")) {
            queryBuilder.append(" AND UPPER(mediaType) = UPPER(?)");
        }
        if (platform != null && !platform.isEmpty() && !platform.equalsIgnoreCase("ALL")) {
            queryBuilder.append(" AND UPPER(platform) = UPPER(?)");
        }
        if (genre != null && !genre.isEmpty() && !genre.equalsIgnoreCase("ALL")) {
            queryBuilder.append(" AND UPPER(genre) = UPPER(?)");
        }
        if (minMetascore != null) {
            queryBuilder.append(" AND metascore >= ?");
        }
        if (maxMetascore != null) {
            queryBuilder.append(" AND metascore <= ?");
        }
        if (beforeYear != null && !beforeYear.isEmpty()) {
            queryBuilder.append(" AND TO_NUMBER(SUBSTR(releaseDate, -4)) <= ?");
        }
        if (afterYear != null && !afterYear.isEmpty()) {
            queryBuilder.append(" AND TO_NUMBER(SUBSTR(releaseDate, -4)) >= ?");
        }
        if ("metascore_asc".equals(sortOption)) {
            queryBuilder.append(" ORDER BY metascore ASC");
        } else if ("metascore_desc".equals(sortOption)) {
            queryBuilder.append(" ORDER BY metascore DESC");
        } else if ("year_asc".equals(sortOption)) {
            queryBuilder.append(" ORDER BY TO_DATE(releaseDate, 'Mon DD, YYYY') ASC"); // convert to date format to fix the sorting problem I was running into
        } else if ("year_desc".equals(sortOption)) {
            queryBuilder.append(" ORDER BY TO_DATE(releaseDate, 'Mon DD, YYYY') DESC"); // same as above lol
        }
        // handleError(response, "Executing SQL: " + queryBuilder.toString()); // debugging
        try (Connection con = getConnection();
            PreparedStatement pstmt = con.prepareStatement(queryBuilder.toString())) {
            
            int paramIndex = 1;
            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
            }
            if (mediaType != null && !mediaType.isEmpty() && !mediaType.equalsIgnoreCase("ALL")) {
                pstmt.setString(paramIndex++, mediaType);
            }
            if (platform != null && !platform.isEmpty() && !platform.equalsIgnoreCase("ALL")) {
                pstmt.setString(paramIndex++, platform);
            }
            if (genre != null && !genre.isEmpty() && !genre.equalsIgnoreCase("ALL")) {
                pstmt.setString(paramIndex++, genre);
            }
            if (minMetascore != null) {
                pstmt.setInt(paramIndex++, minMetascore);
            }
            if (maxMetascore != null) {
                pstmt.setInt(paramIndex++, maxMetascore);
            }
            if (beforeYear != null && !beforeYear.isEmpty()) {
                pstmt.setString(paramIndex++, beforeYear);
            }
            if (afterYear != null && !afterYear.isEmpty()) {
                pstmt.setString(paramIndex++, afterYear);
            }

            ResultSet rs = pstmt.executeQuery();
            
            
            while (rs.next()) {
                Media media = new Media();
                media.setMediaType(rs.getString("mediaType"));
                media.setPictureUrl(rs.getString("pictureUrl"));
                media.setTitle(rs.getString("title"));
                media.setDescription(rs.getString("description"));
                media.setPlatform(rs.getString("platform"));
                media.setGenre(rs.getString("genre"));
                media.setReleaseDate(rs.getString("releaseDate"));
                media.setRatedScore(rs.getString("ratedScore"));
                media.setOriginalURL(rs.getString("originalURL"));
                media.setMetascore(rs.getString("metascore"));
                media.setExtraInfo(rs.getString("extraInfo"));
    
                medias.add(media);
            }
            // Using the setters above instead of the constructor below because I updated the Media class to a JavaBean that use getters and setters instead of a constructor
            
            // while (rs.next()) {
            //     Media media = new Media(
            //             rs.getString("mediaType"),
            //             rs.getString("pictureUrl"),
            //             rs.getString("title"),
            //             rs.getString("description"),
            //             rs.getString("platform"),
            //             rs.getString("genre"),
            //             rs.getString("releaseDate"),
            //             rs.getString("ratedScore"),
            //             rs.getString("originalURL"),
            //             rs.getString("metascore"),
            //             rs.getString("extraInfo"));
            //     medias.add(media);
            // }
        } catch (SQLException e) {
            handleError(response, "An error occurred while fetching filtered data: " + e.getMessage());
        }
        return medias;
    }

    /**
     * Drops the specified tables from the database.
     */
    public void dropTables() {
        try (Connection con = getConnection();
            Statement stmt = con.createStatement()) {
            stmt.executeUpdate("DROP TABLE agend932MediasDB");
        } catch (SQLException e) {
            // handleError(response, "An error occurred while dropping the tables: " + e.getMessage()); // can't use this now since I am not passing response anymore
            System.err.println("An error occurred while dropping the tables: " + e.getMessage());
        }
    }

    /**
     * Handles errors by sending a response message to the client.
     *
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     * @param message  The message to be sent to the client.
     */
    private void handleError(HttpServletResponse response, String message) {
        try {
            PrintWriter out = response.getWriter();
            out.println(message);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
