# Metacritic Scraper

Welcome to the Metacritic Scraper project. This application is crafted to provide a seamless experience for scraping and retrieving data from Metacritic, accommodating easy data manipulation and display based on user-defined criteria.

## Overview

The Metacritic Scraper is a web-based application that allows users to:

- Scrape data from Metacritic by selecting specific criteria such as media type, platform, and genre.
- Persist the scraped data into a structured relational database for subsequent access.
- Display the stored data in a user-friendly format.
- Manage the database effectively, including the functionality to purging old tables.

## Quick Links

- **Project Landing Page**: [Metacritic Scraper Main Page](https://unixweb.kutztown.edu/~agend932/csc521/Metacritic/)
- **API Endpoint**: [API Endpoint](https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet)
- **Javadoc Documentation**: [API Documentation](https://unixweb.kutztown.edu/~agend932/csc521/Metacritic/javadoc)
- **Github Repository**: [Github Repo](https://github.com/AndrewGendy/MetaCriticsScrapper)

## Workflow

1. **Data Scraping**: Initiated by the "Scrape New Data" command, the application fetches and processes relevant data from Metacritic based on user-defined media types, platforms, and genres.
   
2. **Data Storage**: Post-scraping, the data is systematically stored in a relational database in one table that stores media type to later filter entries. ~~with separate tables for each media category (Games, Movies, TV)~~ No Longer using different tables for each media type since the size of the data is good enough for one table.

3. **Data Retrieval & Display**: After storing, the application immediately queries the database to retrieve and display the newly saved data attributes to the user. The retrieved data is presented in a structured format, allowing users to easily browse through the results.

## Sample Query Strings

To demonstrate the application's capabilities, here are some sample query strings to interact with the API:

- **Fetch All Stored Data**: [Sample Link](https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet?action=fetchAllData)
  
- **Query the Top Rated Movies by Metacritic**: [Sample Link](https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet?action=fetchFilteredData&mediaType=movie&sort=metascore_desc)

- **Query PS5 Action Games and Sort Them by Metascore and Only Show Those That Have at Least 70 Score**: [Sample Link](https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet?action=fetchFilteredData&searchKeyword=&mediaType=game&platform=PS5&genre=Action&minMetascore=70&sort=metascore_desc)
  
- **Query the top rated Star Wars movies that were released after 2001**: [Sample Link](https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet?action=fetchFilteredData&searchKeyword=Star%20Wars&mediaType=movie&sort=metascore_desc&afterYear=2001)
  
- **Query all of the PS5 Games and sort them by score**: [Sample Link](https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet?action=fetchFilteredData&mediaType=game&platform=PS5&sort=metascore_desc)
  
- **Query all of the PC Games that were released after 2015 and sort them by score**: [Sample Link](https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet?action=fetchFilteredData&mediaType=game&platform=PC&sort=metascore_desc&afterYear=2015)

- **Query the top rated Star Wars titles that are in the Games genre**: [Sample Link](https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet?action=fetchFilteredData&mediaType=game&sort=metascore_desc&searchKeyword=Star%20Wars)

## Design Decisions

### Database Handling

- **Dynamic Storage**: The application ~~dynamically creates tables based on the media type, ensuring organized storage~~ creates a table in the database if one doesn't exist.

- **Table Management**: To maintain efficiency and avoid overloading the database, the application will drop the tables as soon as the servlet is unloaded.

### MVC Architecture

The application adheres to the Model-View-Controller (MVC) architecture, ensuring separation of concerns and modular design:

- **Model**: Represents data structures, primarily the Media class.
  
- **View**: User interface and presentation layer built with HTML, CSS, and JavaScript.
  
- **Controller**: The servlet acts as the controller, managing requests, interacting with the model, and updating the view.

## How to Use

1. **Start**: Navigate to the project's main page.
   
2. **Select Criteria**: Use the dropdown menus to specify your criteria.
   
3. **Scrape**: Click "Search Results" to initiate the scraping process. A brief wait is required.
   
4. **View All Data**: Click "All Local Data" to see all the data that has been stored in the local database so far.

5. **View Filtered Data**: 
   ~~- Select your desired criteria using the dropdown menus.~~
   ~~- Click "Filter Local Data" to view data that matches your selected criteria. The results will be displayed on the page. This data will be pulled from the local database which only saves data that has been scrapped earlier in the session.~~
   - This is now part of the Search Results button functionality, and gets triggered automatically. The "Filter Local Data" has been removed.

6. **View Data**: The results will automatically be displayed on the page no matter which option you chose.
   
7. **Drop Selected Tables**: The database table will be dropped automatically when the servlet is unloaded. There is also a "Drop Tables" button that allows the user to drop the tables upon request.

## Considerations & Limitations

The current iteration of the Metacritic Scraper, while functional and useful, comes with certain limitations that are important to note:

### API Interaction

- **Scraping Restrictions**: The scraping functionality can only be triggered through the user interface. This restriction is by design, using a POST method in the backend to prevent abuse of the Metacritic website through direct API calls. This is to prevent any unwanted calls to the Metacritic website.

- **GET Method Limitations**: Available GET methods are constrained to operations that fetch data from the database. To input new data into the database, the scraping process must be initiated via the UI.

### Data Handling Limitations

- **Single-Platform/Genre Assignment**: Metacritic's listings don't always provide complete platform and genre information at a glance. Fetching such details would necessitate accessing each media item's individual page, significantly increasing complexity and server load. As a temporary solution, the application assigns user-selected platforms and genres to the scraped results, which may not fully reflect multi-platform and multi-genre attributes of the media.

- **Unique Title Constraint**: The database is configured to reject duplicate titles to prevent redundancy. This means that if a title already exists in the database with a certain genre, subsequent scrapes with different genres that encounter the same title will not be stored, potentially omitting valid multi-genre associations.

### Specific Limitation Details

- **Media Details**: Retrieving comprehensive details like platforms and genres for each media item directly from Metacritic requires visiting individual media pages. This would significantly increase both the complexity of the scraper and the number of requests sent to Metacritic's servers.

- **Workaround and Associated Issues**: The current workaround involves capturing the user's input for platform and genre and assigning these to all scraped items. This method has the drawback of not accurately representing items that may span multiple platforms or genres. For instance, a game available on multiple platforms will only be stored with the platform selected by the user at the time of scraping.

- **Title-Based Uniqueness**: The database is designed to ensure each title is unique to prevent data duplication. This leads to a situation where a media item with the same title but different genres or platforms may not be stored if one entry already exists. For example, if 'Star Wars' movies are first scraped and stored as 'action' genre on 'Disney+', a subsequent scrape for 'adventure' genre won't store 'Star Wars' movies again even if they also belong to the 'adventure' genre. Therefore, it won't show in the results table.

## Additional Notes

### Compilation Instructions
To compile the project using Maven, use the Maven wrapper with the following command:
`mvn package` or `mvn clean package` and `mvn clean install` should also work. Maven should automatically download and install dependencies and libraries required to build the project.

### Script File References
Specific lines in `scripts.js` for displaying all platforms and genres:
(Note: Not recommended for scraping new data; suitable for pulling data from a local database)
- Line 135
- Line 143
- Line 149
- Line 164
(Refer to comments within the file for more details.)

### WAR Output Configuration in Maven
To change the WAR output directory when using the `mvn package` command, modify the `pom.xml`:
- Output directory configuration: Line 98
- Relevant plugin configuration: Lines 93 to 101

### Security and Environment Variables
For security purposes, and to avoid exposing my username and password, I am utilizing environment variables. These are loaded when the servlet is initialized:
- See line 27 in `MetacriticServlet.java` for servlet initialization.
- Instance of `dbHandler` created, which looks up variables saved in `context.xml`: See line 31 in `DatabaseHandler.java`.
- Database configurations are loaded into `dataSource`, and then used to make a connection: See line 47.

### Javadoc Location Recently Changed
Since I removed javadoc from the servlet, it won't be located under `https://agend932.kutztown.edu:8443/Metacritic/javadoc` anymore as specified in older versions. It is now accessible through acad public html: `https://unixweb.kutztown.edu/~agend932/csc521/Metacritic/javadoc`

*Note*: The README on GitHub will always be the most up-to-date version.

## Acknowledgements

This project, developed by Andrew Gendy for CSC521 at Kutztown University. It is currently in its first major release, version 1.1.3 and I am open to all feedback and questions. I would like to extend my gratitude to all who take the time to explore the Metacritic Scraper.

### What's new in 1.1.3
- Merged the functionality of "Scrape New Data" and "Filter Local Data" into one button that does both. The new button is called "Search Results" and will show information to the user according to their search criteria.
- Added a new button "Drop Tables" that will drop the database tables manually. This is an addition to the automatic method used to drop the tables when the servlet is unloaded.
- Moved front-end items outside of the servlet to a different domain.
- Updated API url from `https://agend932.kutztown.edu:8443/phase2_Andrew/MetacriticServlet` to `https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet`
- Added CORS (Cross-Origin Resource Sharing) support to allow the communication between the landing page and the servlet which are being hosted on different domains.
- 