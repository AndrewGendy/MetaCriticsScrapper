package agend932;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an action:");
        System.out.println("1. Browse Metacritic");
        System.out.println("2. Search Metacritic");
        System.out.print("Enter your choice (1/2): ");

        // int choice = scanner.nextInt();
        // String url;

        // switch (choice) {
        //     case 1:
        //         // Example URL for browsing; modify this or prompt the user for more specifics
        //         url = "https://www.metacritic.com/browse/movie/hulu/action/all-time/metascore/?network=hulu&genre=action&releaseYearMin=2020&releaseYearMax=2023&page=";
        //         MetacriticBrowseScrapper browseScrapper = new MetacriticBrowseScrapper();
        //         browseScrapper.scrapeMetacritic(url);
        //         break;
        //     case 2:
        //         // Example URL for searching; modify this or prompt the user for more specifics
        //         url = "https://www.metacritic.com/search/%22Star%20wars%22/?page=";
        //         MetacriticsSearchScrapper searchScrapper = new MetacriticsSearchScrapper();
        //         searchScrapper.scrapeMetacritic(url);
        //         break;
        //     default:
        //         System.out.println("Invalid choice.");
        // }

        // scanner.close();
    }
}
