package agend932;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.HttpStatusException;

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
                    // This should elemenate the infinite loop we got if the user requests a page with wrong parameters or if they were null
                    httpEx.printStackTrace();
                    hasResults = false;
                    break;
                } else {
                    httpEx.printStackTrace();
                    hasResults = false;
                    break;
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