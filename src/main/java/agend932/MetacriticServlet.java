package agend932;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

/**
 * The {@code MetacriticServlet} class handles all the HTTP requests for the Metacritic Scraper application.
 * It processes requests to scrape data from Metacritic, manage the database, and perform queries.
 */
public class MetacriticServlet extends HttpServlet {

    private DatabaseHandler dbHandler;

    /**
     * Initializes the servlet. It retrieves the DataSource from the JNDI context and stores it for later use.
     * This method is called automatically by the servlet container when the servlet is first loaded into memory.
     *
     * @throws ServletException if an error occurs during the initialization process.
     */
    @Override
    public void init() throws ServletException {
        // Create a new instance of DatabaseHandler using the context parameters
        dbHandler = new DatabaseHandler();
    }

    /**
     * Handles the HTTP GET requests.
     * 
     * @param request The HttpServletRequest object that contains the request the client made of the servlet.
     * @param response The HttpServletResponse object that contains the response the servlet returns to the client.
     * @throws ServletException If the request could not be handled.
     * @throws IOException If an input or output error occurs while the servlet is handling the request.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
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

    /**
     * Handles the HTTP POST requests.
     * 
     * @param request The HttpServletRequest object that contains the request the client made of the servlet.
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     * @throws ServletException If the request could not be handled.
     * @throws IOException If an input or output error is detected when the servlet handles the POST request.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
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

    /**
     * Called by the server to allow a servlet to handle a POST request by dropping tables.
     * The servlet container calls the {@code destroy} method before removing a servlet instance from service.
     */
    @Override
    public void destroy() {
        dropTables();
        // Call the parent destroy method (not mandatory but can be good practice)
        super.destroy();
    }

    /**
     * Executes a simple query to the database for testing purposes.
     * 
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     */
    private void simpleQuery(HttpServletResponse response){
        dbHandler.simpleQuery(response);
    }

    /**
     * Processes a request to scrape data from Metacritic based on parameters specified by the client.
     * 
     * @param request The HttpServletRequest object that contains the request the client made of the servlet.
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the request.
     */
    private void processScrapeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mediaType = request.getParameter("mediaType");
        String platform = request.getParameter("platform");
        String genre = request.getParameter("genre");
        String platformParam = "&platform=";

        if ("movie".equals(mediaType) || "tv".equals(mediaType)) {
            platformParam = "&network=";
        }

        String queryType = "/browse/";
        String basePart = "all/all/all-time/metascore/?";
        String urlRequest = "https://www.metacritic.com" + queryType + mediaType + "/" + basePart + platformParam + platform + "&genre=" + genre + "&page=";
        List<Media> scrapedMediaList = MetacriticBrowseScrapper.scrapeMetacritic(urlRequest, mediaType, platform, genre);
    
        // Save the scraped results to the database
        dbHandler.saveResultsToDB("agend932MediasDB", scrapedMediaList, response);
    }

    /**
     * Fetches all data stored in the database and sends it back to the client as a JSON array.
     * 
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the request.
     */
    private void fetchAllData(HttpServletResponse response) throws IOException {
        List<Media> mediaList = dbHandler.fetchAllData(response);

        String json = new Gson().toJson(mediaList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    /**
     * Fetches filtered data based on search criteria provided by the client and sends it back as a JSON array.
     * 
     * @param request The HttpServletRequest object that contains the request the client made of the servlet.
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the request.
     */
    private void fetchFilteredData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String searchKeyword = request.getParameter("searchKeyword");
        String mediaType = request.getParameter("mediaType");
        String platform = request.getParameter("platform");
        String genre = request.getParameter("genre");
        String sortOption = request.getParameter("sort");
        Integer minMetascore = (request.getParameter("minMetascore") != null && !request.getParameter("minMetascore").isEmpty()) ? Integer.parseInt(request.getParameter("minMetascore")) : null;
        Integer maxMetascore = (request.getParameter("maxMetascore") != null && !request.getParameter("maxMetascore").isEmpty()) ? Integer.parseInt(request.getParameter("maxMetascore")) : null;
        String beforeYear = request.getParameter("beforeYear");
        String afterYear = request.getParameter("afterYear");


        // Check for null values and set to empty string if null
        searchKeyword = (searchKeyword == null) ? "" : searchKeyword;
        mediaType = (mediaType == null) ? "" : mediaType;
        platform = (platform == null) ? "" : platform;
        genre = (genre == null) ? "" : genre;
        beforeYear = (beforeYear == null) ? "" : beforeYear;
        afterYear = (afterYear == null) ? "" : afterYear;
        
        List<Media> mediaList = dbHandler.fetchFilteredData(response, searchKeyword, mediaType, platform, genre, sortOption, minMetascore, maxMetascore, beforeYear, afterYear);

        String json = new Gson().toJson(mediaList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    /* A new, better, and more modular way to extrat parameters from the request. I need to test it more, but it should work.
    private String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value != null ? value : "";
    }
    
    private Integer getIntegerParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return (value != null && !value.isEmpty()) ? Integer.parseInt(value) : null;
    }
    
    private void fetchFilteredData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String searchKeyword = getParameter(request, "searchKeyword");
        String mediaType = getParameter(request, "mediaType");
        String platform = getParameter(request, "platform");
        String genre = getParameter(request, "genre");
        String sortOption = getParameter(request, "sort");
        Integer minMetascore = getIntegerParameter(request, "minMetascore");
        Integer maxMetascore = getIntegerParameter(request, "maxMetascore");
        String beforeYear = getParameter(request, "beforeYear");
        String afterYear = getParameter(request, "afterYear");
    
        List<Media> mediaList = dbHandler.fetchFilteredData(response, searchKeyword, mediaType, platform, genre, sortOption, minMetascore, maxMetascore, beforeYear, afterYear);
    
        String json = new Gson().toJson(mediaList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
    */

    /**
     * Drops the database table used to store media information.
     * This method is typically called when cleaning up resources during application undeployment or shutdown.
     * It should be used with caution as it will result in loss of all data in the table.
     */
    private void dropTables() {
        dbHandler.dropTables();
    }

    /**
     * Sends a plain text response message to the client.
     * 
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     * @param message The string message to be sent to the client.
     */
    private void sendResponseMessage(HttpServletResponse response, String message) {
        try {
            PrintWriter out = response.getWriter();
            out.println(message);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
