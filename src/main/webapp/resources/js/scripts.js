$(document).ready(function () {
    const loader = $('#loader');
    const results = $('#results');

    // // NOT USING THIS ANYMORE SINCE I AM NOT HIDING AND SHOWING DIVs IN THE HTML. I AM NOW KEEPING ONE DIC AND CHANGING IT USING JS
    // const platformsDiv = $('#platformsDiv');
    // const genresDiv = $('#genresDiv');
    // function toggleGameSections(display) {
    //     // Change the display values of the game sections
    //     gamePlatformsDiv.css('display', display);
    //     gameGenresDiv.css('display', display);
    // }

    // $('#mediaType').on('change', function () {
    //     // Log the value of the mediaType input
    //     console.log("mediaType changed to: ", this.value);

    //     // Toggle the game sections based on the mediaType value
    //     if (this.value === 'game') {
    //         toggleGameSections('block');
    //     } else {
    //         toggleGameSections('none');
    //     }
    // });

    const showLoader = function () {
        // Show the loader element
        loader.css('display', 'block');
    }

    const hideLoader = function () {
        // Hide the loader element
        loader.css('display', 'none');
    }

    // A generic function that handles the AJAX requests
    const ajaxRequest = function (type, url, data, successCallback) {
        // Log the arguments of the ajaxRequest function
        // console.log("ajaxRequest called with type: ", type, "url: ", url, "data: ", data);

        // Show the loader before making the request
        showLoader();

        $.ajax({
            type: type,
            url: url,
            data: data,
            success: function (response) {
                // Log the response from the server
                // console.log("AJAX Request succeeded with response: ", response);

                // Hide the loader after receiving the response
                hideLoader();

                // Call the successCallback function with the response
                successCallback(response);
            },
            error: function (xhr, status, error) {
                // Log the error details from the server
                console.error("An error occurred in ajaxRequest function: ", error);
                console.error("Status: ", status);
                console.error("Response: ", xhr.responseText);

                // Hide the loader after receiving the error
                hideLoader();

                // Display an error message to the user
                results.html("<p>An error occurred while processing your request.</p>");
            }
        });
    }

    $("#scrapeDataBtn").click(function (e) {
        e.preventDefault();
        // Log that the scrapeDataBtn was clicked
        // console.log("scrapeDataBtn clicked");

        let sortOption = $("#sortOptions").val();
        let mediaType = $("#mediaType").val();
        let platform = $("#platforms").val();
        let genre = $("#genres").val();

        // Use the generic function with the appropriate arguments
        ajaxRequest("POST", "/phase2_Andrew-1.0/MetacriticServlet", { action: 'scrapeData', mediaType: mediaType, platform: platform, genre: genre }, function (response) {
            // Log that the response was received and display it in the results element
            // console.log("Response received from scrapeData action");
            results.html(response);
            // After scraping, fetch the data based on the selected criteria and populate the table
            $("#showFilteredDataBtn").click();
        });
    });

    $("#showDataBtn").click(function (e) {
        e.preventDefault();
        // Log that the showDataBtn was clicked
        // console.log("showDataBtn clicked");

        // Use the generic function with the appropriate arguments
        ajaxRequest('GET', '/phase2_Andrew-1.0/MetacriticServlet', { action: 'fetchAllData' }, function (data) {
            // Log that the data was received and call populateResultsTable function with it
            // console.log("Data received from fetchAllData action");
            populateResultsTable(data);
        });
    });

    $("#showFilteredDataBtn").click(function (e) {
        e.preventDefault();
        // Log that the showFilteredDataBtn was clicked
        // console.log("showFilteredDataBtn clicked");

        let sortOption = $("#sortOptions").val();
        let mediaType = $("#mediaType").val();
        let platform = $("#platforms").val();
        let genre = $("#genres").val();
        let minMetascore = $("#min-metascore").val();
        let maxMetascore = $("#max-metascore").val();
        let beforeYear = $("#before-year").val();
        let afterYear = $("#after-year").val();

        // Use the generic function with the appropriate arguments
        ajaxRequest('GET', '/phase2_Andrew-1.0/MetacriticServlet', { action: 'fetchFilteredData', mediaType: mediaType, platform: platform, genre: genre,
            sort: sortOption, minMetascore: minMetascore, maxMetascore: maxMetascore, beforeYear: beforeYear, afterYear: afterYear }, function (data) {
            // Log that the data was received and call populateResultsTable function with it
            // console.log("Data received from fetchFilteredData action");
            populateResultsTable(data);
        });
    });

    $('#dropTablesBtn').click(function () {

        // Log that the dropTablesBtn was clicked
        // console.log("dropTablesBtn clicked");

        // Get all checked tableNames values
        const tableNames = $('input[name="tableNames"]:checked').map(function () {
            return $(this).val();
        }).get();

        // Use the generic function with the appropriate arguments
        ajaxRequest('POST', '/phase2_Andrew-1.0/MetacriticServlet', { action: 'dropTables' }, function (response) {
            // Log that the tables were dropped and display a success message to the user
            // console.log("Tables dropped successfully.", response);
            // results.html("<p>Tables dropped successfully.</p>");
        });
    });

    $('#simpleQueryBtn').click(function () {
        ajaxRequest('GET', '/phase2_Andrew-1.0/MetacriticServlet', { action: 'simpleQuery' }, function (response) {
        });
    });

    $('#mediaType').change(function () {
        const mediaType = $(this).val();
        const platformsSelect = $('#platforms');
        const genresSelect = $('#genres');
        platformsSelect.empty(); // Clear current platforms options
        genresSelect.empty(); // Clear current genres options

        if (mediaType === 'game') {
            // platformsSelect.append('<option value=""></option>');
            platformsSelect.append('<option value="PS5">PS5</option>');
            platformsSelect.append('<option value="Xbox-Series-X">Xbox Series X/S</option>');
            platformsSelect.append('<option value="Nintendo-Switch">Nintendo Switch</option>');
            platformsSelect.append('<option value="PC">PC</option>');
            platformsSelect.append('<option value="Mobile">Mobile</option>');
            platformsSelect.append('<option value="Xbox-One">Xbox One</option>');
            // we can add other game platforms here as needed just like done above
            // genresSelect.append('<option value=""></option>');
            genresSelect.append('<option value="Action">Action</option>');
            genresSelect.append('<option value="RPG">RPG</option>');
            genresSelect.append('<option value="Strategy">Strategy</option>');
            genresSelect.append('<option value="Survival">Survival</option>');
            genresSelect.append('<option value="Shooter">Shooter</option>');
            // we can add other game genres here as needed just like done above
        } else if (mediaType === 'movie' || mediaType === 'tv') {
            // platformsSelect.append('<option value=""></option>');
            platformsSelect.append('<option value="Netflix">Netflix</option>');
            platformsSelect.append('<option value="Hulu">Hulu</option>');
            platformsSelect.append('<option value="Starz">Starz</option>');
            platformsSelect.append('<option value="Disney-Plus">Disney+</option>');
            platformsSelect.append('<option value="Prime-Video">Prime Video</option>');
            // we can add other movie and tv platforms here as needed just like done above
            // genresSelect.append('<option value=""></option>');
            genresSelect.append('<option value="Action">Action</option>');
            genresSelect.append('<option value="Comedy">Comedy</option>');
            genresSelect.append('<option value="Drama">Drama</option>');
            genresSelect.append('<option value="Crime">Crime</option>');
            genresSelect.append('<option value="Family">Family</option>');
            // we can add other movie and tv genres here as needed just like done above
        }
    });
});

function populateResultsTable(data) {
    // console.log("populateResultsTable got called");
    const table = document.querySelector("#resultsTable");
    const tableBody = table.querySelector("tbody");

    // Clear previous data from the table
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

    data.forEach(game => {
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

        picCell.innerHTML = `<a href="${game.originalURL}" target="_blank"><img src="${game.pictureUrl}" alt="${game.title}" height="132" width="88"></a>`;
        mediaTypeCell.textContent = capitalizeFirstLetter(game.mediaType);
        titleCell.innerHTML = `<a href="${game.originalURL}" target="_blank">${game.title}</a>`;
        descCell.textContent = game.description;
        platCell.textContent = game.platform;
        genreCell.textContent = game.genre;
        dateCell.textContent = game.releaseDate;
        scoreCell.textContent = game.ratedScore;
        metaCell.textContent = game.metascore;
    });
}
// a simple function to capitalize the first letter of a string
function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
