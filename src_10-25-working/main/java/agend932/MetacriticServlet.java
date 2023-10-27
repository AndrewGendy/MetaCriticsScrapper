package agend932;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class MetacriticServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("getAllData".equals(action)) {
            getAllData(response);
        } else {
            doPost(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String results = "";

        String actionChoice = (request.getParameter("action") != null) ? request.getParameter("action") + "/" : "browse/";
        actionChoice = "browse/"; // temporary setting every call to browse until I work on search
        String mediaType = (request.getParameter("mediaType") != null) ? request.getParameter("mediaType") + "/" : "game/";

        // Handle platforms (which can have multiple values)
        StringBuilder gamePlatformsBuilder = new StringBuilder();
        String[] platforms = request.getParameterValues("platforms");
        if (platforms != null) {
            for (String platform : platforms) {
                gamePlatformsBuilder.append("&platform=").append(platform); // Appending each platform
            }
        }

        // Handle genres (which can have multiple values)
        StringBuilder gameGenresBuilder = new StringBuilder();
        String[] genres = request.getParameterValues("gameGenres");
        if (genres != null) {
            for (String genre : genres) {
                gameGenresBuilder.append("&genre=").append(genre);
            }
        }

        String basePart = "all/all/all-time/metascore/?";

        String urlRequest = "https://www.metacritic.com/" + actionChoice + mediaType + basePart + gamePlatformsBuilder
                + gameGenresBuilder + "&page=";

        // if ("browse/".equals(actionChoice)) {
            // Call the MetacriticBrowseScrapper and store results in 'results' variable
            // urlRequest =
            // "https://www.metacritic.com/browse/movie/hulu/action/all-time/metascore/?network=hulu&genre=action&releaseYearMin=2020&releaseYearMax=2023&network=netflix&page=";
            // urlRequest =
            // "https://www.metacritic.com/browse/game/all/action/all-time/metascore/?platform=ps5&platform=xbox-series-x&genre=action&releaseYearMin=1910&releaseYearMax=2023&page=";
            results = MetacriticBrowseScrapper.scrapeMetacritic(urlRequest, response);
        // } else if ("search/".equals(actionChoice)) {
        //     // urlRequest = "https://www.metacritic.com/search/%22Star%20wars%22/?page=";
        //     results = MetacriticsSearchScrapper.scrapeMetacritic(urlRequest);
        // } else {
        //     results = "Invalid choice or choice not received!";
        // }

        // Display results
        // out.println("Action Choice: " + actionChoice);
        out.println("URL: " + urlRequest + "<br>");
        out.println(results);

    }

    private void getAllData(HttpServletResponse response) throws IOException {
        // Query the database
        GamesDB gamesDB = new GamesDB();
        List<Game> gameList = gamesDB.fetchAllGames(response);

        // Convert the result to JSON
        String json = new Gson().toJson(gameList);

        // Write the JSON to the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // this is what i had originally
        // response.setCharacterEncoding("ISO-8859-1");  // see if this fixes the issue by matching the response headers
        response.getWriter().write(json);
    }
}
