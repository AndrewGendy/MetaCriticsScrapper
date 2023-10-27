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
        switch (action) {
            case "fetchAllData":
                fetchAllData(response);
                break;
            default:
                sendErrorResponse(response, "Invalid action in doGet.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "dropTables":
                dropSelectedTables(request, response);
                break;
            case "scrapeData":
                processScrapeRequest(request, response);
                break;
            default:
                sendErrorResponse(response, "Invalid action in doPost.");
        }
    }    

    private void processScrapeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        String queryType = "/browse/"; // later we could scrape the search page which works slightly differently
        String mediaType = request.getParameter("mediaType");
        String platform = request.getParameter("platform");
        String genre = request.getParameter("genre");

        String basePart = "all/all/all-time/metascore/?";
        String urlRequest = "https://www.metacritic.com" + queryType + mediaType + "/" + basePart + "&platform=" + platform + "&genre=" + genre + "&page=";
        
        List<Media> scrapedMediaList = MetacriticBrowseScrapper.scrapeMetacritic(urlRequest, mediaType, platform, genre);
    
        // Save the scraped results to the database
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.saveResultsToDB("agend932MediasDB", scrapedMediaList, response); // "agend932MediasDB" is table name for now
    
        out.println("Scraped and saved " + scrapedMediaList.size() + " media entries to the database.");
    }

    private void fetchAllData(HttpServletResponse response) throws IOException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        List<Media> mediaList = dbHandler.fetchAllData(response);

        String json = new Gson().toJson(mediaList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private void dropSelectedTables(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("Request: " + request);
        String[] tableNames = request.getParameterValues("tableNames");
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.dropTables(response, tableNames);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(message);
    }
}
