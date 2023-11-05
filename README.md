# Metacritic Scraper

Welcome to the Metacritic Scraper project. This application is crafted to provide a seamless experience for scraping and retrieving data from Metacritic, accommodating easy data manipulation and display based on user-defined criteria.

## Overview

The Metacritic Scraper is a web-based application that allows users to:

- Scrape data from Metacritic by selecting specific criteria such as media type, platform, and genre.
- Persist the scraped data into a structured relational database for subsequent access.
- Display the stored data in a user-friendly format.
- Manage the database effectively, including the functionality to purging old tables.

## Quick Links

- **Project Landing Page**: [Metacritic Scraper Main Page](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/)
- **API Endpoint**: [API Endpoint](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet)
- **Javadoc Documentation**: [API Documentation](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/javadoc)
- **Github Repository**: [Github Repo](https://github.com/AndrewGendy/MetaCriticsScrapper)

## Workflow

1. **Data Scraping**: Initiated by the "Scrape New Data" command, the application fetches and processes relevant data from Metacritic based on user-defined media types, platforms, and genres.
   
2. **Data Storage**: Post-scraping, the data is systematically stored in a relational database in one table that stores media type to later filter entries. ~~with separate tables for each media category (Games, Movies, TV)~~.

3. **Data Retrieval & Display**: After storing, the application immediately queries the database to retrieve and display the newly saved data attributes to the user. The retrieved data is presented in a structured format, allowing users to easily browse through the results.

## Sample Query Strings

To demonstrate the application's capabilities, here are some sample query strings to interact with the API:

- **Fetch All Stored Data**: [Sample Link](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchAllData)
  
- **Fetch All Movie Data**: [Sample Link](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchFilteredData&mediaType=movie)
  
- **Game Data for PS5 Platform, Action Genre**: [Sample Link](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchFilteredData&mediaType=game&platform=PS5&genre=action)

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
   
3. **Scrape**: Click "Scrape New Data" to initiate the scraping process. A brief wait is required.
   
4. **View All Data**: Click "All Local Data" to see all the data that has been stored in the local database so far.

5. **View Filtered Data**: 
   - Select your desired criteria using the dropdown menus.
   - Click "Filter Local Data" to view data that matches your selected criteria. The results will be displayed on the page. This data will be pulled from the local database which only saves data that has been scrapped earlier in the session.

6. **View Data**: The results will automatically be displayed on the page no matter which option you chose.
   
7. **Drop Selected Tables**: ~~Use checkboxes to select tables and click "Drop Selected Tables" for removal. (Deprecated Functionality)~~  The database table will be dropped automatically when the servlet is unloaded.

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

## Acknowledgements

This project, developed by Andrew Gendy for CSC521 at Kutztown University, is currently in its first major release, version 1.0. I am open to feedback and questions and would like to extend my gratitude to all who take the time to explore the Metacritic Scraper.
