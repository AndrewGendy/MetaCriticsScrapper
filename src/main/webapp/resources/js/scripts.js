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

    // A generic function that handles the AJAX requests
    function ajaxRequest(type, url, data, successCallback) {
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
                hideLoader();
                console.error("An error occurred in ajaxRequest function: ", error);
                console.error("Status: ", status);
                console.error("Response: ", xhr.responseText);
                results.html("<p>An error occurred while processing your request.</p>");
            }            
        });
    }

    $("#metacriticForm").on('submit', function (event) {
        event.preventDefault();
        // Use the generic function with the appropriate arguments
        ajaxRequest("POST", "/phase2_Andrew-1.0/MetacriticServlet", $(this).serialize(), function (response) {
            results.html(response);
        });
    });

    $("#showDataBtn").click(function () {
        // Use the generic function with the appropriate arguments
        ajaxRequest('GET', '/phase2_Andrew-1.0/MetacriticServlet', { action: 'fetchAllData' }, function (data) {
            console.log("AJAX Request succeeded");
            console.log(data);  // log the data received from the server
            populateGamesTable(data); // call populateGamesTable function below
        });
    });

    $('#dropTablesBtn').click(function() {
        console.log("Drop Tables Button clicked");
        // Use the generic function with the appropriate arguments
        ajaxRequest('POST', '/phase2_Andrew-1.0/MetacriticServlet', { action: 'dropTables' }, function (response) {
            console.log("Tables dropped successfully.", response);
            // I can further update the UI or notify the user of success here.
        });
    });

    // Use a common selector for the flip elements and the panel elements
    $('.flip').click(function () {
        var link = $(this);
        // Use the data attribute to store and access the corresponding panel selector
        var panel = $(link.data('panel'));
        panel.slideToggle('slow', function () {
            if ($(this).is(":visible")) {
                link.text('Close');
            } else {
                link.text('Read More');
            }
        });
    });
});
