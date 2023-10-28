# Metacritic Scraper

Welcome to the Metacritic Scraper project. This application allows users to scrape data from Metacritic's website and store it in a database. The data can be filtered based on media type (Game, Movie, TV), platform, and genre.
## Links

-  **Project Landing Page**: https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/
-  **Javadoc Documentation**: https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/javadoc (Replace when ready if changed)

## Sample Query Strings

To demonstrate how the site was accessed, here are some sample query strings:

    To scrape game data for PC platform and action genre:
    https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchFilteredData&mediaType=game&platform=PS5&genre=action

    To scrape movie data:
    https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchFilteredData&mediaType=movie

    To fetch all stored data:
    https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchAllData

## Design Decisions
### Database Handling

The application uses a relational database to store the scraped data. Each media type (Game, Movie, TV) is stored in a separate table.

Table Purging Strategy:
When a new table is to be added and there are already five tables stored, the oldest table (based on creation date) is purged to make room for the new one. This ensures that the database remains efficient and doesn't get overloaded with old data.
### MVC Architecture

The project follows the Model-View-Controller (MVC) architecture:

    Model: Represents the data structures. The Media class is the primary model.
    View: Consists of the HTML, CSS, and JavaScript files. It presents the data to the user.
    Controller: The servlet acts as the controller, handling incoming requests, interacting with the model, and sending the appropriate response back to the view.

## Directions for Using the Project

    Navigate to the project's main page using the provided link.
    Use the dropdown menus to select the media type, platform, and genre you're interested in.
    Click the "Submit" button to start the scraping process. Please wait a few seconds after clicking.
    The scraped data will be displayed on the same page.
    To view all stored data, click the "Show Games" button.
    To drop specific tables, select the desired tables using the checkboxes and click the "Drop Selected Tables" button.
