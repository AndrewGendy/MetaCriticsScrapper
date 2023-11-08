package agend932;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.HttpStatusException;
import javax.servlet.http.HttpServletResponse;

/**
 * This class contains methods for scraping media information from Metacritic's website.
 * It uses Jsoup to parse HTML and extract media details.
 */
public class MetacriticBrowseScrapper {

    /**
     * Scrapes Metacritic for media information based on provided URL and search criteria.
     * Iterates through multiple pages until no more results are found.
     *
     * @param urlRequest The base URL for the Metacritic search query.
     * @param mediaType  The type of media to scrape (e.g., game, movie, tv).
     * @param platform   The platform for which to scrape media (e.g., PS5, Xbox).
     * @param genre      The genre of media to scrape (e.g., Action, RPG).
     * @return A list of {@code Media} objects containing the scraped media information.
     */
    public static List<Media> scrapeMetacritic(String urlRequest, String mediaType, String platform, String genre) {
        List<Media> mediasList = new ArrayList<>();
        int pageNumber = 1; // starting from page 1
        boolean hasResults = true; // A flag to indicate if the page has results or not

        while (hasResults) { // Loop while the flag is true
            String url = urlRequest + pageNumber;

            try {
                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                Elements results = doc.select("div.c-productListings div.c-finderProductCard");

                if (results.isEmpty()) {
                    hasResults = false;
                    break;
                } else {
                    for (Element result : results) {
                        Media media = extractMediaFromElement(result, mediaType, platform, genre);
                        mediasList.add(media);
                    }
                }

                // After scraping the page, wait for 0.2 seconds before fetching the next page
                Thread.sleep(200);
                pageNumber++;

            } catch (HttpStatusException httpEx) {
                if (httpEx.getStatusCode() == 404) {
                    // This should elemenate the infinite loop we got
                    hasResults = false;
                } else {
                    httpEx.printStackTrace();
                }
            } catch (IOException ioEx) {
                    ioEx.printStackTrace();
                break;
            } catch (InterruptedException ieEx) {
                    ieEx.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }
        }
        return mediasList;
    }

    /**
     * Extracts media details from an HTML element and creates a {@code Media} object.
     *
     * @param result    The Jsoup element containing media details.
     * @param mediaType The type of media to scrape.
     * @param platform  The platform for which to scrape media.
     * @param genre     The genre of media to scrape.
     * @return A {@code Media} object populated with details from the HTML element.
     */
    private static Media extractMediaFromElement(Element result, String mediaType, String platform, String genre) {
        String pictureUrl = result.select("img[src]").attr("src");
        String title = result.select("h3.c-finderProductCard_titleHeading span:nth-child(2)").text();
        String description = result.select("div.c-finderProductCard_description span").text();
        String releaseDate = result.select("span.u-text-uppercase").text();
        String ratedScore = result.select("span:nth-child(3)").text();
        String originalURL = result.select("a.c-finderProductCard_container").attr("href");
        String metascore = result.select("div.c-siteReviewScore span").text();
        String extraInfo = result.select("div.product_details").text();
    
        return new Media(mediaType, pictureUrl, title, description, platform, genre, releaseDate, ratedScore, originalURL, metascore, extraInfo);
    }    
}


// public class MetacriticBrowseScrapper {

//     public static String scrapeMetacritic(String urlRequest, HttpServletResponse response, String mediaType, String platform, String genre) {
//         StringBuilder resultsBuilder = new StringBuilder();
//         List<Media> mediasList = new ArrayList<>();
//         int pageNumber = 1; // starting from page 1      
//         int totalResults = 0; // total number of results
//         boolean hasResults = true; // A flag to indicate if the page has results or not

//         String tableName = "agend932MediasDB"; // temporary table name. CHANGE LATER to DYNAMICALLY GET THE NAME

//         while (hasResults) { // Loop while the flag is true
//             // The URL of the page to scrape
//             // String url = "https://www.metacritic.com/browse/tv/max/all/all-time/metascore/?network=max&releaseYearMin=2022&releaseYearMax=2023&page=" + pageNumber;
//             // String url = "https://www.metacritic.com/browse/tv/netflix/all/all-time/userscore/?network=netflix&releaseYearMin=2022&releaseYearMax=2023&page=" + pageNumber; // Netflix shows
//             String url = urlRequest + pageNumber;

//             try {
//                 // Connect to the URL and get the HTML document
//                 // Document doc = Jsoup.connect(url).get();
//                 Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();

//                 // Get the list of results
//                 Elements results = doc.select("div.c-productListings div.c-finderProductCard");

//                 // Check if the results are empty or not
//                 if (results.isEmpty()) {
//                     // The page has no results, set the flag to false and break the loop
//                     hasResults = false;
//                     break;
//                 } else {
//                     System.out.println("total results: " + results.size());

//                     // Loop over each result
//                     for (Element result : results) {
//                         // Get the picture URL
//                         String pictureUrl = result.select("img[src]").attr("src");

//                         // Get the title
//                         String title = result.select("h3.c-finderProductCard_titleHeading span:nth-child(2)").text();

//                         // Get the description
//                         String description = result.select("div.c-finderProductCard_description span").text();

//                         // Get the release date
//                         String releaseDate = result.select("span.u-text-uppercase").text();

//                         // Get the rated score
//                         String ratedScore = result.select("span:nth-child(3)").text();
                        
//                         // Get the game URL
//                         String originalURL = result.select("a.c-finderProductCard_container").attr("href");

//                         // Get any extra information displayed
//                         String extraInfo = result.select("div.product_details").text();

//                         // Print the data in the console for testing purposes
//                         // System.out.println("Picture URL: " + pictureUrl);
//                         // System.out.println("Title: " + title);
//                         // System.out.println("Description: " + description);
//                         // System.out.println("Release Date: " + releaseDate);
//                         // System.out.println("Rated Score: " + ratedScore);
//                         // System.out.println("Extra Info: " + extraInfo);
//                         // System.out.println();

//                         Media media = new Media(pictureUrl, title, description, releaseDate, ratedScore, originalURL, extraInfo);
//                         mediasList.add(media);

//                         resultsBuilder.append("Picture URL: ").append(pictureUrl).append("<br/>");
//                         resultsBuilder.append("Title: ").append(title).append("<br/>");
//                         resultsBuilder.append("Description: ").append(description).append("<br/>");
//                         resultsBuilder.append("Release Date: ").append(releaseDate).append("<br/>");
//                         resultsBuilder.append("Rated Score: ").append(ratedScore).append("<br/>");
//                         resultsBuilder.append("Original URL: ").append(originalURL).append("<br/>");
//                         resultsBuilder.append("Extra Info: ").append(extraInfo).append("<br/><br/>");

//                         totalResults++;
//                     }
//                 }
            
//             // After scraping the page, wait for 2 seconds before fetching the next page
//             try {
//                 Thread.sleep(2000);  // wait for 2 seconds after each request (an attempt to not hammer the server and potentially get the IP address blocked by the server)
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }

//             // Increment the page number by one
//             pageNumber++;

//             } catch (IOException e) {
//                 // Handle the exception
//                 resultsBuilder.append("Failed to connect to " + url);
//                 e.printStackTrace();
//             }
//         }
//         DatabaseHandler dbHandler = new DatabaseHandler();
//         dbHandler.saveResultsToDB(tableName, mediasList, response);

//         resultsBuilder.append("Total Results: ").append(totalResults).append("<br/>");
//         return resultsBuilder.toString();
//     }
// }