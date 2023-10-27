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
        switch (action) {
            case "fetchAllData":
                fetchAllData(response);
                break;
            default:
                sendResponseMessage(response, "Invalid action in doGet.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        sendResponseMessage(response, "doPost called with action: " + action);
        switch (action) {
            case "dropTables":
                // sendResponseMessage(response, "dropTables called in the servlet.");
                dropSelectedTables(request, response);
                break;
            case "scrapeData":
                // sendResponseMessage(response, "scrapeData Called in the servlet.");
                processScrapeRequest(request, response);
                break;
            default:
                sendResponseMessage(response, "Invalid action in doPost.");
        }
    }    

    private void processScrapeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mediaType = request.getParameter("mediaType");
        String platform = request.getParameter("platform");
        String genre = request.getParameter("genre");
        sendResponseMessage(response, "Processing scrape request for mediaType: " + mediaType + ", platform: " + platform + ", genre: " + genre);

        String queryType = "/browse/";
        String basePart = "all/all/all-time/metascore/?";
        String urlRequest = "https://www.metacritic.com" + queryType + mediaType + "/" + basePart + "&platform=" + platform + "&genre=" + genre + "&page=";
        sendResponseMessage(response, "Constructed URL for scraping: " + urlRequest);
        
        List<Media> scrapedMediaList = MetacriticBrowseScrapper.scrapeMetacritic(urlRequest, mediaType, platform, genre);
    
        // Save the scraped results to the database
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.saveResultsToDB("agend932MediasDB", scrapedMediaList, response);
    
        sendResponseMessage(response, "Scraped and saved " + scrapedMediaList.size() + " media entries to the database.");
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

    private void dropSelectedTables(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = request.getParameterValues("tableNames");
        sendResponseMessage(response, "Dropping tables: " + String.join(", ", tableNames));
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.dropTables(response, tableNames);
    }

    // Handles errors by sending a response to the client
    private void sendResponseMessage(HttpServletResponse response, String message) {
        try {
            PrintWriter out = response.getWriter();
            out.println(message);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
