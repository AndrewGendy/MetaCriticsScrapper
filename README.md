# Metacritic Scraper

Welcome to the Metacritic Scraper project. This application is designed to provide users with a seamless experience in scraping and storing data from Metacritic's website, allowing for easy retrieval and display based on various criteria.

## Overview

The Metacritic Scraper is a web-based application that allows users to:

- Scrape data from Metacritic based on selected criteria (media type, platform, and genre).
- Store the scraped data in a relational database for future retrieval.
- Display the stored data in a user-friendly format.
- Manage the stored data, including purging old tables.

## Links

- **Project Landing Page**: [Metacritic Scraper Main Page](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/)
- **Javadoc Documentation**: [API Documentation](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/javadoc)
- **Github Repository**: [Github Repo](https://github.com/AndrewGendy/MetaCriticsScrapper)

## Workflow

1. **Data Scraping**: When a user selects specific criteria and clicks on "Scrape New Data", the application navigates to Metacritic and begins the scraping process. It fetches relevant data based on the selected media type, platform, and genre.
   
2. **Data Storage**: Once the scraping is complete, the data is stored in a relational database. Each media type (Game, Movie, TV) has its designated table.

3. **Data Retrieval**: After storing, the application immediately queries the database to retrieve and display the newly saved data attributes to the user.

4. **Data Display**: The retrieved data is presented in a structured format, allowing users to easily browse through the results.

## Sample Query Strings

To demonstrate the application's capabilities, here are some sample query strings to interact with the API:

- **Fetch All Stored Data**: [Sample Link](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchAllData)
  
- **Fetch All Movie Data**: [Sample Link](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchFilteredData&mediaType=movie)
  
- **Game Data for PS5 Platform, Action Genre**: [Sample Link](https://agend932.kutztown.edu:8443/phase2_Andrew-1.0/MetacriticServlet?action=fetchFilteredData&mediaType=game&platform=PS5&genre=action)

## Design Decisions

### Database Handling

- **Dynamic Storage**: The application dynamically creates tables based on the media type, ensuring organized storage.

- **Table Management**: To maintain efficiency and avoid overloading the database, the application will drop the tables as soon as the servlet is unloaded.

### MVC Architecture

The application adheres to the Model-View-Controller (MVC) architecture, ensuring separation of concerns and modular design:

- **Model**: Represents data structures, primarily the Media class.
  
- **View**: Comprises HTML, CSS, and JavaScript, presenting data to users.
  
- **Controller**: The servlet acts as the controller, managing requests, interacting with the model, and updating the view.

## How to Use

1. **Start**: Navigate to the project's main page.
   
2. **Select Criteria**: Use the dropdown menus to specify your criteria.
   
3. **Scrape**: Click "Scrape New Data" to initiate the scraping process. A brief wait is required.
   
4. **View Data**: The results will be displayed on the page.
   
5. **View All Data**: Click "Show all data from DB" to see all stored data.

6. **View Filtered Data**: 
   - Select your desired criteria using the dropdown menus.
   - Click "Show Filtered Data from DB" to view data that matches your selected criteria. The results will be displayed on the page.
   
7. ~~**Drop Selected Tables**: Use checkboxes to select tables and click "Drop Selected Tables" for removal.~~ (Deprecated)

Thank you for using the Metacritic Scraper! We hope you find it useful and intuitive.
