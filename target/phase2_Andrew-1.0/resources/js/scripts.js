$(document).ready(function () {
    const gamePlatformsDiv = $('#gamePlatformsDiv');
    const gameGenresDiv = $('#gameGenresDiv');
    const loader = $('#loader');
    const results = $('#results');

    function toggleGameSections(display) {
        // Change the display values of the game sections
        gamePlatformsDiv.css('display', display);
        gameGenresDiv.css('display', display);
    }

    $('#mediaType').on('change', function () {
        // Log the value of the mediaType input
        console.log("mediaType changed to: ", this.value);

        // Toggle the game sections based on the mediaType value
        if (this.value === 'game') {
            toggleGameSections('block');
        } else {
            toggleGameSections('none');
        }
    });

    function showLoader() {
        // Show the loader element
        loader.css('display', 'block');
    }

    function hideLoader() {
        // Hide the loader element
        loader.css('display', 'none');
    }

    // A generic function that handles the AJAX requests
    function ajaxRequest(type, url, data, successCallback) {
        // Log the arguments of the ajaxRequest function
        console.log("ajaxRequest called with type: ", type, "url: ", url, "data: ", data);

        // Show the loader before making the request
        showLoader();

        $.ajax({
            type: type,
            url: url,
            data: data,
            success: function (response) {
                // Log the response from the server
                console.log("AJAX Request succeeded with response: ", response);

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
        console.log("scrapeDataBtn clicked");

        let mediaType = $("#mediaType").val();
        let platform = $("#platforms").val();
        let genre = $("#gameGenres").val();

        // Use the generic function with the appropriate arguments
        ajaxRequest("POST", "/phase2_Andrew-1.0/MetacriticServlet", { action: 'scrapeData', mediaType: mediaType, platform: platform, genre: genre }, function (response) {
            // Log that the response was received and display it in the results element
            console.log("Response received from scrapeData action");
            results.html(response);
        });
    });

    $("#showDataBtn").click(function (e) {
        e.preventDefault();
        // Log that the showDataBtn was clicked
        console.log("showDataBtn clicked");

        // Use the generic function with the appropriate arguments
        ajaxRequest('GET', '/phase2_Andrew-1.0/MetacriticServlet', { action: 'fetchAllData' }, function (data) {
            // Log that the data was received and call populateGamesTable function with it
            console.log("Data received from fetchAllData action");
            populateGamesTable(data);
        });
    });

    $('#dropTablesBtn').click(function () {

        // Log that the dropTablesBtn was clicked
        console.log("dropTablesBtn clicked");

        // Use the generic function with the appropriate arguments
        ajaxRequest('POST', '/phase2_Andrew-1.0/MetacriticServlet', { action: 'dropTables' }, function (response) {
            // Log that the tables were dropped and display a success message to the user
            console.log("Tables dropped successfully.", response);
            results.html("<p>Tables dropped successfully.</p>");
        });
    });

    // Use a common selector for the flip elements and the panel elements
    $('.flip').click(function () {
        let link = $(this);
        // Use the data attribute to store and access the corresponding panel selector
        let panel = $(link.data('panel'));

        // Log the link and panel elements that were clicked
        console.log("flip element clicked: ", link);
        console.log("panel element toggled: ", panel);

        panel.slideToggle('slow', function () {
            if ($(this).is(":visible")) {
                link.text('Close');
            } else {
                link.text('Read More');
            }
        });
    });
    function populateGamesTable(data) {
        console.log("populateGamesTable got called");
        var table = document.querySelector("#gamesTable");
        var tableBody = table.querySelector("tbody");

        // Clear previous data from the table
        tableBody.innerHTML = '';

        // Check if thead already exists, if not add it
        if (!table.querySelector("thead")) {
            var thead = table.createTHead();
            var headerRow = thead.insertRow();

            ["Picture", "Title", "Description", "Platform", "Genre", "Release Date", "Rated Score"].forEach(function (headerText) {
                var th = document.createElement("th");
                th.textContent = headerText;
                headerRow.appendChild(th);
            });
        }

        data.forEach(function (game) {
            var row = tableBody.insertRow();

            var picCell = row.insertCell();
            var titleCell = row.insertCell();
            var descCell = row.insertCell();
            var platCell = row.insertCell();
            var genreCell = row.insertCell();
            var dateCell = row.insertCell();
            var scoreCell = row.insertCell();

            picCell.innerHTML = '<a href="https://www.metacritic.com' + game.originalURL + '"><img src="' + game.pictureUrl + '" alt="' + game.title + '" height="132" width="88"></a>';
            titleCell.innerHTML = '<a href="https://www.metacritic.com' + game.originalURL + '">' + game.title + '</a>';
            descCell.textContent = game.description;
            platCell.textContent = game.platform;
            genreCell.textContent = game.genre;
            dateCell.textContent = game.releaseDate;
            scoreCell.textContent = game.ratedScore;
        });
    }
});
