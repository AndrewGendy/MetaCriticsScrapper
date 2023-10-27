package agend932;

import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class GamesDB {

    private static final String USERNAME = "agend932";
    private static final String PASSWORD = "W3jV5pXK";
    private static final String JDBC_URL = "jdbc:oracle:thin:@csdb.kutztown.edu:1521:orcl";

    
    public void saveResultsToDB(List<Game> gameList, HttpServletResponse response) {
        try (Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            Statement stmt = con.createStatement();
            PrintWriter out = response.getWriter();
            // out.println("Successfully connected to the database!<br/>");
            // Create table         
            try {
                ResultSet rs = stmt.executeQuery("SELECT 1 FROM agend932GameDB WHERE ROWNUM = 1");
                // stmt.executeUpdate("DROP TABLE agend932GameDB"); // for testing purposes. remove later
                // out.println("A table with that name already exists.<br/>");
            } catch (SQLException e) {
                if (e.getErrorCode() == 942) { // if error code 942 means that table doesn't exist, so create it.
                    String createTableString = "CREATE TABLE agend932GameDB ("
                            + "title VARCHAR2(255) NOT NULL UNIQUE, "
                            + "description VARCHAR2(1000), "
                            + "pictureUrl VARCHAR2(255), "
                            + "releaseDate VARCHAR2(50), "
                            + "ratedScore VARCHAR2(50), "
                            + "gameURL VARCHAR2(1000), "
                            + "extraInfo VARCHAR2(1000))";
                    try (PreparedStatement pstmt = con.prepareStatement(createTableString)) {
                        pstmt.executeUpdate();
                    }
                    // out.println("Successfully created the table!<br/>");
                } else {
                    out.println("Some other error: " + e.getMessage() + "<br/>");
                    throw e;
                }
            }

            // DatabaseMetaData dbm = con.getMetaData();
            // ResultSet tables = dbm.getTables(null, null, "agend932GameDB", null);
            // if (!tables.next()) {
            // // The table does not exist, create it
            // String createTableString = "CREATE TABLE agend932GameDB ("
            // + "pictureUrl VARCHAR2(255), "
            // + "title VARCHAR2(255), "
            // + "description VARCHAR2(1000), "
            // + "releaseDate VARCHAR2(50), "
            // + "ratedScore VARCHAR2(50), "
            // + "gameURL VARCHAR2(1000), "
            // + "extraInfo VARCHAR2(1000))";
            // try (PreparedStatement pstmt = con.prepareStatement(createTableString)) {
            // pstmt.executeUpdate();
            // }
            // out.println("Successfully created the table!<br/>");
            // } else {
            // out.println("A table with that name already exists.<br/>");
            // }

            // Insert each game into the database
            for (Game game : gameList) {
                String gameInsertString = "BEGIN "
                        + "INSERT INTO agend932GameDB (pictureUrl, title, description, releaseDate, ratedScore, gameURL, extraInfo) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?); "
                        + "EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; " // This line handles the unique constraint violation (ignores the record and moves the the next one)
                        + "END;";
                try (PreparedStatement pstmt = con.prepareStatement(gameInsertString)) {
                    pstmt.setString(1, game.getPictureUrl());
                    pstmt.setString(2, game.getTitle());
                    pstmt.setString(3, game.getDescription());
                    pstmt.setString(4, game.getReleaseDate());
                    pstmt.setString(5, game.getRatedScore());
                    pstmt.setString(6, game.getGameURL());
                    pstmt.setString(7, game.getExtraInfo());
                    pstmt.executeUpdate();
                    // out.println(gameInsertString + "<br/>"); // for testing. delete before
                    // release
                }

                // out.println("Successfully stored game: " + game.getTitle() + "<br/>");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            try {
                PrintWriter out = response.getWriter();
                out.println("An error occurred: " + e.getMessage() + "<br/>");
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
    }

    public List<Game> fetchAllGames(HttpServletResponse response) {
        List<Game> games = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            PrintWriter out = response.getWriter(); // Obtain the PrintWriter
            // out.println("Successfully connected to the database!<br/>");

            String query = "SELECT * FROM agend932GameDB";
            try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                // out.println("Query executed successfully!<br/>");

                while (rs.next()) {
                    // Create a new Game object using the constructor
                    Game game = new Game(
                            rs.getString("pictureUrl"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("releaseDate"),
                            rs.getString("ratedScore"),
                            rs.getString("gameURL"),
                            rs.getString("extraInfo"));
                    // Add the Game object to the list
                    games.add(game);
                    // out.println("Game added: " + game.getTitle() + "<br/>"); // Output each game
                    // title
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Optionally, output the error message to the browser
            try {
                PrintWriter out = response.getWriter();
                out.println("An error occurred: " + e.getMessage() + "<br/>");
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
        return games;
    }
}