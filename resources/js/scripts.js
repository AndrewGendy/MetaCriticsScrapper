/**
 * This script handles the dynamic interaction with the Metacritic Scraper web application.
 * It initializes event handlers for the document and AJAX requests for data processing.
 */
$(document).ready(function () {
    const loader = $('#loader');
    const results = $('#results');
    const urlForAPI = 'https://agend932.kutztown.edu:8443/Metacritic/MetacriticServlet'; // create a variable instead of changing it in multiple places every time.
    // const urlForAPI = 'http://localhost:8080/Metacritic/MetacriticServlet'; // for local testing.
    updatePlatformAndGenreSelects() // to populate Platforms and Genres when page is refreashed.

    // Show or hide the loader graphic
    const showLoader = function () {
        loader.css('display', 'block');
    };
    const hideLoader = function () {
        loader.css('display', 'none');
    };

    /**
     * Performs an AJAX request to the server.
     * @param {string} type - The HTTP method to use for the request (e.g. "POST", "GET")
     * @param {string} url - The URL to which the request is sent
     * @param {Object} data - Data to be sent to the server
     * @param {Function} successCallback - Function to be called if the request succeeds
     */
    const ajaxRequest = function (type, url, data, successCallback) {
        showLoader();
        $.ajax({
            type: type,
            url: url,
            data: data,
            success: function (response) {
                hideLoader();
                successCallback(response);
            },
            error: function (xhr, status, error) {
                console.error("An error occurred in ajaxRequest function: ", error);
                hideLoader();
                results.html("<p>An error occurred while processing your request.<br/>Maybe the local table is empty. Try to scrape data first.</p>");
            }
        });
    };
    
    checkLoginStatus(); // Run this function when the page loads to check if the user is logged in or not

    // Check if the user is logged in or not
    function checkLoginStatus() {
        ajaxRequest('GET', urlForAPI, { action: 'checkLogin' }, function(response) {
            if (response) {
                // User is logged in
                $('#loginBtn').hide();
                $('#logoutBtn').show();
                $('#logoutBtn').before('<span id="welcomeUser">Welcome, ' + response + '</span> ');
            } else {
                // User is not logged in
                $('#loginBtn').show();
                $('#logoutBtn').hide();
                $('#welcomeUser').remove();
            }
        });
    }

    // Event handler for the 'Logout' button click
    $("#logoutBtn").click(function(e) {
        e.preventDefault();
        ajaxRequest('POST', urlForAPI, { action: 'logout' }, function(response) {
            // If need be, handle the response after logout
            checkLoginStatus(); // Recheck login status to update UI
        });
    });

    // Event handler for the 'Scrape Data' button click
    $("#scrapeDataBtn").click(function (e) {
        e.preventDefault();
        let mediaType = $("#mediaType").val();
        let platform = $("#platforms").val();
        let genre = $("#genres").val();
        ajaxRequest("POST", urlForAPI, { action: 'scrapeData', mediaType: mediaType, platform: platform, genre: genre }, function (response) {
            results.html(response);
            $("#showFilteredDataBtn").click();
        });
    });
    // Event handler for the 'Show Data' button click
    $("#showDataBtn").click(function (e) {
        e.preventDefault();
        ajaxRequest('GET', urlForAPI, { action: 'fetchAllData' }, function (data) {
            populateResultsTable(data);
        });
    });

    // Event handler for the 'Show Filtered Data' button click
    $("#showFilteredDataBtn").click(function (e) {
        e.preventDefault();

        let searchKeyword = $("#searchBar").val();
        let sortOption = $("#sortOptions").val();
        let mediaType = $("#mediaType").val();
        let platform = $("#platforms").val();
        let genre = $("#genres").val();
        let minMetascore = $("#min-metascore").val();
        let maxMetascore = $("#max-metascore").val();
        let beforeYear = $("#before-year").val();
        let afterYear = $("#after-year").val();

        ajaxRequest('GET', urlForAPI, {
            action: 'fetchFilteredData', searchKeyword: searchKeyword, mediaType: mediaType, platform: platform, genre: genre,
            sort: sortOption, minMetascore: minMetascore, maxMetascore: maxMetascore, beforeYear: beforeYear, afterYear: afterYear
        }, function (data) {
            populateResultsTable(data);
        });
    });

    // Event handler for the 'Drop Tables' button click
    $('#dropTablesBtn').click(function () {
        const tableNames = $('input[name="tableNames"]:checked').map(function () {
            return $(this).val();
        }).get();
        ajaxRequest('POST', urlForAPI, { action: 'dropTables' }, function (response) {
        });
    });

    // Event handler for the 'Simple Query' button click
    $('#simpleQueryBtn').click(function () {
        ajaxRequest('GET', urlForAPI, { action: 'simpleQuery' }, function (response) {
        });
    });

    // Event handler for when 'Media Type' changes
    $('#mediaType').change(function () {
        updatePlatformAndGenreSelects();
    });
});

// Function to update Platform and Genre dropdowns based on the selected media type
function updatePlatformAndGenreSelects() {
    let mediaType = $('#mediaType').val();
    let platformsSelect = $('#platforms');
    let genresSelect = $('#genres');
    platformsSelect.empty(); // Clear current platforms options
    genresSelect.empty(); // Clear current genres options

    if (mediaType === 'game') {
        // platformsSelect.append('<option value=""></option>'); // uncomment this line if you want to give the user the ability to search for all games platforms (this feature will cause some problems. See the README file for more details)
        platformsSelect.append('<option value="PS5">PS5</option>');
        platformsSelect.append('<option value="Xbox-Series-X">Xbox Series X/S</option>');
        platformsSelect.append('<option value="Nintendo-Switch">Nintendo Switch</option>');
        platformsSelect.append('<option value="PC">PC</option>');
        platformsSelect.append('<option value="Mobile">Mobile</option>');
        platformsSelect.append('<option value="Xbox-One">Xbox One</option>');
        // more platforms options could be added here similar to the above append statements
        // genresSelect.append('<option value=""></option>'); // uncomment this line if you want to give the user the ability to search for all games genres (this feature will cause some problems. See the README file for more details)
        genresSelect.append('<option value="Action">Action</option>');
        genresSelect.append('<option value="RPG">RPG</option>');
        genresSelect.append('<option value="Strategy">Strategy</option>');
        genresSelect.append('<option value="Survival">Survival</option>');
        genresSelect.append('<option value="Shooter">Shooter</option>');
        // more gernes options could be added here similar to the above append statements
    } else if (mediaType === 'movie' || mediaType === 'tv') {
        // platformsSelect.append('<option value=""></option>'); // uncomment this line if you want to give the user the ability to search for all movies and tvplatforms (this feature will cause some problems. See the README file for more details)
        platformsSelect.append('<option value="Netflix">Netflix</option>');
        platformsSelect.append('<option value="Hulu">Hulu</option>');
        platformsSelect.append('<option value="Starz">Starz</option>');
        platformsSelect.append('<option value="Disney-Plus">Disney+</option>');
        platformsSelect.append('<option value="Prime-Video">Prime Video</option>');
        // more platforms options could be added here similar to the above append statements
        // genresSelect.append('<option value=""></option>'); // uncomment this line if you want to give the user the ability to search for all movies and tv genres (this feature will cause some problems. See the README file for more details)
        genresSelect.append('<option value="Action">Action</option>');
        genresSelect.append('<option value="Comedy">Comedy</option>');
        genresSelect.append('<option value="Drama">Drama</option>');
        genresSelect.append('<option value="Crime">Crime</option>');
        genresSelect.append('<option value="Family">Family</option>');
        // more gernes options could be added here similar to the above append statements
    }
}

// Function to populate the results table with the fetched data
function populateResultsTable(data) {
    const table = document.querySelector("#resultsTable");
    const tableBody = table.querySelector("tbody");
    tableBody.innerHTML = '';
    if (!table.querySelector("thead")) {
        const thead = table.createTHead();
        const headerRow = thead.insertRow();
        ["Picture", "Media Type", "Title", "Description", "Platform", "Genre", "Release Date", "Rated Score", "Meta Score"].forEach(headerText => {
            const th = document.createElement("th");
            th.textContent = headerText;
            headerRow.appendChild(th);
        });
    }
    data.forEach(media => {
        const row = tableBody.insertRow();
        const picCell = row.insertCell();
        const mediaTypeCell = row.insertCell();
        const titleCell = row.insertCell();
        const descCell = row.insertCell();
        const platCell = row.insertCell();
        const genreCell = row.insertCell();
        const dateCell = row.insertCell();
        const scoreCell = row.insertCell();
        const metaCell = row.insertCell();

        picCell.innerHTML = `<a href="${media.originalURL}" target="_blank"><img src="${media.pictureUrl}" alt="${media.title}" height="132" width="88"></a>`;
        mediaTypeCell.textContent = capitalizeFirstLetter(media.mediaType);
        titleCell.innerHTML = `<a href="${media.originalURL}" target="_blank">${media.title}</a>`;
        descCell.textContent = media.description;
        platCell.textContent = media.platform;
        genreCell.textContent = media.genre;
        dateCell.textContent = media.releaseDate;
        scoreCell.textContent = media.ratedScore;
        metaCell.textContent = media.metascore;
    });
}

// Utility function to capitalize the first letter of a string
function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
