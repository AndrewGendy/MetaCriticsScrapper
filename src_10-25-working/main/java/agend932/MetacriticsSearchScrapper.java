package agend932;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MetacriticsSearchScrapper {

    public static String scrapeMetacritic(String urlRequest) {
        StringBuilder resultsBuilder = new StringBuilder();
        int pageNumber = 1; // starting from page 1      
        int totalResults = 0; // total number of results
        boolean hasResults = true; // A flag to indicate if the page has results or not
        while (hasResults) { // Loop while the flag is true
            // The URL of the page to scrape
            // String url = "https://www.metacritic.com/browse/tv/max/all/all-time/metascore/?network=max&releaseYearMin=2022&releaseYearMax=2023&page=" + pageNumber;
            //String url = "https://www.metacritic.com/browse/tv/netflix/all/all-time/userscore/?network=netflix&releaseYearMin=2022&releaseYearMax=2023&page=" + pageNumber; // Netflix shows
            String url = urlRequest + pageNumber;

            try {
                // Connect to the URL and get the HTML document
                // Document doc = Jsoup.connect(url).get();
                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get(); //Defining user agent

                // Get the list of results
                Elements results = doc.select("div.a.c-pageSiteSearch-results-item");

                // Check if the results are empty or not
                if (results.isEmpty()) {
                    // The page has no results, set the flag to false and break the loop
                    hasResults = false;
                    break;
                } else {
                    System.out.println("total results: " + results.size());

                    // Loop over each result
                    for (Element result : results) {
                        // Get the picture URL
                        String pictureUrl = result.select("img[src]").attr("src");

                        // Get the title
                        String title = result.select("h3.c-pageSiteSearch-results-item-title").text();

                        // Get the category
                        String category = result.select("span.c-pageSiteSearch-results-item-category").text();

                        // Get the release date
                        String releaseDate = result.select("span.c-pageSiteSearch-results-item-date").text();

                        // Get the rated score
                        String ratedScore = result.select("span.c-pageSiteSearch-results-item-score").text();

                        // Get the platform
                        String platform  = result.select("span.c-pageSiteSearch-results-item-platform").text();

                        // Print the data
                        System.out.println("Picture URL: " + pictureUrl);
                        System.out.println("Title: " + title);
                        System.out.println("Category: " + category);
                        System.out.println("Release Date: " + releaseDate);
                        System.out.println("Rated Score: " + ratedScore);
                        System.out.println("Platform: " + platform );
                        System.out.println();

                        resultsBuilder.append("Picture URL: ").append(pictureUrl).append("<br/>");
                        resultsBuilder.append("Title: ").append(title).append("<br/>");
                        resultsBuilder.append("Category: ").append(category).append("<br/>");
                        resultsBuilder.append("Release Date: ").append(releaseDate).append("<br/>");
                        resultsBuilder.append("Rated Score: ").append(ratedScore).append("<br/>");
                        resultsBuilder.append("Platform: ").append(platform).append("<br/><br/>");


                        totalResults++;
                    }
                }

            // After scraping the page, wait for 2 seconds before fetching the next page
            try {
                Thread.sleep(2000);  // Sleep for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Increment the page number by one
            pageNumber++;

            } catch (IOException e) {
                // Handle the exception
                System.err.println("Failed to connect to " + url);
                e.printStackTrace();
            }
        }
        resultsBuilder.append("Total Results: ").append(totalResults).append("<br/>");
        return resultsBuilder.toString();
    }
}