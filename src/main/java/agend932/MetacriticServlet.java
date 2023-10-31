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
        // sendResponseMessage(response, "doGet called with action: " + action);
        if (action == null) {
            sendResponseMessage(response, "Action parameter is missing or null.");
            return;
        }
        switch (action) {
            case "fetchAllData":
                fetchAllData(response);
                break;
            case "fetchFilteredData":
                fetchFilteredData(request, response);
                break;
            case "simpleQuery":
                simpleQuery(response);
                break;
            default:
                sendResponseMessage(response, "Invalid action in doGet.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        // sendResponseMessage(response, "doPost called with action: " + action);
        if (action == null) {
            sendResponseMessage(response, "Action parameter is missing or null.");
            return;
        }
        switch (action) {
            case "dropTables":
                dropTables();
                break;
            case "scrapeData":
                processScrapeRequest(request, response);
                break;
            default:
                sendResponseMessage(response, "Invalid action in doPost.");
        }
    }

    // @Override
    // public void destroy() {
    //     // Call the dropTables function
    //     dropTables();
    //     // Call the parent destroy method (not mandatory but can be good practice)
    //     super.destroy();
    // }

    private void simpleQuery(HttpServletResponse response){
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.simpleQuery(response);
    }
    private void processScrapeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mediaType = request.getParameter("mediaType");
        String platform = request.getParameter("platform");
        String genre = request.getParameter("genre");
        String platformParam = "&platform=";

        if ("movie".equals(mediaType) || "tv".equals(mediaType)) {
            platformParam = "&network=";
        }
        sendResponseMessage(response, "Processing scrape request for mediaType: " + mediaType + ", platform: " + platform + ", genre: " + genre);

        String queryType = "/browse/";
        String basePart = "all/all/all-time/metascore/?";
        String urlRequest = "https://www.metacritic.com" + queryType + mediaType + "/" + basePart + platformParam + platform + "&genre=" + genre + "&page=";
        sendResponseMessage(response, "Constructed URL for scraping: " + urlRequest);
        List<Media> scrapedMediaList = MetacriticBrowseScrapper.scrapeMetacritic(urlRequest, mediaType, platform, genre);
    
        // Save the scraped results to the database
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.saveResultsToDB("agend932MediasDB", scrapedMediaList, response);
        // sendResponseMessage(response, "Scraped and saved " + scrapedMediaList.size() + " media entries to the database.");
    }

    private void fetchAllData(HttpServletResponse response) throws IOException {
        // sendResponseMessage(response, "Fetching all data from the database.");
        DatabaseHandler dbHandler = new DatabaseHandler();
        List<Media> mediaList = dbHandler.fetchAllData(response);

        String json = new Gson().toJson(mediaList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private void fetchFilteredData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mediaType = request.getParameter("mediaType");
        String platform = request.getParameter("platform");
        String genre = request.getParameter("genre");
        String sortOption = request.getParameter("sort");
        Integer minMetascore = (request.getParameter("minMetascore") != null && !request.getParameter("minMetascore").isEmpty()) ? Integer.parseInt(request.getParameter("minMetascore")) : null;
        Integer maxMetascore = (request.getParameter("maxMetascore") != null && !request.getParameter("maxMetascore").isEmpty()) ? Integer.parseInt(request.getParameter("maxMetascore")) : null;
        String beforeYear = request.getParameter("beforeYear");
        String afterYear = request.getParameter("afterYear");


        // Check for null values and set to empty string if null
        mediaType = (mediaType == null) ? "" : mediaType;
        platform = (platform == null) ? "" : platform;
        genre = (genre == null) ? "" : genre;
        beforeYear = (beforeYear == null) ? "" : beforeYear;
        afterYear = (afterYear == null) ? "" : afterYear;
        
        DatabaseHandler dbHandler = new DatabaseHandler();
        List<Media> mediaList = dbHandler.fetchFilteredData(response, mediaType, platform, genre, sortOption, minMetascore, maxMetascore, beforeYear, afterYear);

        String json = new Gson().toJson(mediaList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private void dropTables() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.dropTables();
    }

    // Handles errors by sending a response to the client
    private void sendResponseMessage(HttpServletResponse response, String message) {
        try {
            PrintWriter out = response.getWriter();
            out.println(message);
        } catch (IOException ioEx) {
            // ioEx.printStackTrace();
        }
    }
}
