$(document).ready(function () {
    const gamePlatformsDiv = $('#gamePlatformsDiv');
    const gameGenresDiv = $('#gameGenresDiv');
    const loader = $('#loader');
    const results = $('#results');

    function toggleGameSections(display) {
        gamePlatformsDiv.css('display', display);
        gameGenresDiv.css('display', display);
    }

    $('#mediaType').on('change', function () {
        if (this.value === 'game') {
            toggleGameSections('block');
        } else {
            toggleGameSections('none');
        }
    });

    function showLoader() {
        loader.css('display', 'block');
    }

    function hideLoader() {
        loader.css('display', 'none');
    }

    $("#metacriticForm").on('submit', function (event) {
        event.preventDefault();
        showLoader();

        $.ajax({
            type: "POST",
            url: "/phase2_Andrew-1.0/MetacriticServlet",
            data: $(this).serialize(),
            success: function (response) {
                hideLoader();
                results.html(response);
            },
            error: function (xhr, status, error) {
                hideLoader();
                console.error("An error occurred: ", error);
                results.html("<p>An error occurred while processing your request.</p>");
            }
        });
    });
    $("#showGamesBtn").click(function () {
        // console.log("Show Data Button clicked");
        showLoader();  // Show the loader when the button is clicked

        $.ajax({
            type: 'GET',
            url: '/phase2_Andrew-1.0/MetacriticServlet',
            data: { action: 'getAllData' },
            // dataType: 'json',  // Comment or remove this line
            success: function (data) {
                hideLoader();  // Hide the loader when the request succeeds
                console.log("AJAX Request succeeded");
                console.log(data);  // log the data received from the server
                populateGamesTable(data); // call populateGamesTable function below
            },
            error: function (error) {
                hideLoader();  // Hide the loader when the request fails
                console.log("AJAX Request failed");
                console.log(error);  // log the error object
            },
            complete: function (xhr, status) {
                console.log("AJAX Request completed");  // Debugging line
                console.log("Status: ", status);  // log the status of the request
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
            console.log("No thead found, creating one.");

            var thead = table.createTHead(); // this will create a new <thead> within thead
            if (thead) {
                console.log("Created thead successfully.");
            } else {
                console.log("Failed to create thead.");
            }

            var headerRow = thead.insertRow(); // this will create a new <tr> within thead
            if (headerRow) {
                console.log("Created header row successfully.");
            } else {
                console.log("Failed to create header row.");
            }

            ["Picture", "Title", "Description", "Platform", "Genre", "Release Date", "Rated Score"].forEach(function (headerText) {
                var th = document.createElement("th");
                th.textContent = headerText;
                headerRow.appendChild(th);
                console.log("Appended header:", headerText);
            });
        } else {
            console.log("thead already exists.");
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

            picCell.innerHTML = '<a href=https://www.metacritic.com' + game.gameURL + '><img src="' + game.pictureUrl + '" alt="' + game.title + '" height="132" width="88"></a>';
            titleCell.innerHTML = '<a href=https://www.metacritic.com' + game.gameURL + '>' + game.title + '</a>';
            descCell.textContent = game.description;
            platCell.textContent = game.description;
            genreCell.textContent = game.description;
            dateCell.textContent = game.releaseDate;
            scoreCell.textContent = game.ratedScore;
        });
    }
});
