$(document).ready(function() {
    let currentPage = 1;
    let totalPages = 1;

    // Attach event listeners
    $(document).on('click', '.play-button', handlePlayButtonClick);
    $('#close-video-player').click(handleCloseButtonClick);

    $('#search-button').click(function() {
        let query = $('#search-bar').val();
        let filter = $('#content-filter').val();
        performSearch(query, filter, currentPage);
    });

    $('#prev-page').click(function() {
        if (currentPage > 1) {
            currentPage--;
            let query = $('#search-bar').val();
            let filter = $('#content-filter').val();
            performSearch(query, filter, currentPage);
        }
    });

    $('#next-page').click(function() {
        if (currentPage < totalPages) {
            currentPage++;
            let query = $('#search-bar').val();
            let filter = $('#content-filter').val();
            performSearch(query, filter, currentPage);
        }
    });

    $('#reset-button').click(function() {
        resetSearch();
    });

    function resetSearch() {
        $('#search-bar').val('');
        $('#content-filter').val('all');
        $('#results-container').empty();
        currentPage = 1;
        performSearch('', 'all', currentPage);
    }

    function performSearch(query, filter, page) {
        $('#status-bar').text('Searching...');
        let startTime = performance.now();
        $.ajax({
            url: '/api/videos/search',
            type: 'GET',
            data: { query: query, filter: filter, page: page },
            success: function(data) {
                console.log('Data received:', data); // Debugging statement

                $('#results-container').empty();

                data.forEach(function(videoMetadata) {
                    if (videoMetadata.imageDataBase64) {
                        let img = $('<img>').attr('src', 'data:image/jpeg;base64,' + videoMetadata.imageDataBase64);

                        let playButton = $('<button>')
                            .addClass('play-button')
                            .attr('title','Play Video')
                            .append($('<img>').attr('src', 'images/Play.png').attr('alt', 'Play'));

                        let dresButton = $('<button>')
                            .addClass('dres-button')
                            .attr('title','Submit to DRES')
                            .append($('<img>').attr('src', 'images/Upload1.png').attr('alt', 'Submit'));

                        let similarityButton = $('<button>')
                            .addClass('similarity-button')
                            .attr('title','Similarity Search')
                            .append($('<img>').attr('src', 'images/Similar.png').attr('alt', 'Check Similarity'));

                        let buttonContainer = $('<div>').addClass('button-container')
                            .append(playButton)
                            .append(dresButton)
                            .append(similarityButton);

                        let div = $('<div>')
                            .addClass('grid-item')
                            .append(buttonContainer)
                            .append(img)
                            .data('videoMetadata', videoMetadata); // Store metadata in the div

                        playButton.click(handlePlayButtonClick);
                        dresButton.click(handleDresButtonClick);
                        similarityButton.click(handleSimilarityButtonClick);

                        $('#results-container').append(div);
                    } else {
                        console.error('No image data for:', videoMetadata);
                    }
                });
                let endTime = performance.now();
                let timeTaken = ((endTime - startTime)/1000).toFixed(1);
                $('#status-bar').text(`Found ${data.length} images in ${timeTaken} seconds`);
                updatePaginationControls();
            },
            error: function(error) {
                console.error('Error fetching search results:', error);
            }
        });
    }

    function handlePlayButtonClick() {
        console.log("Coming to Play Button Click");
        const videoMetadata = $(this).closest('.grid-item').data('videoMetadata');
        const videoPlayer = $('#videoPlayer')[0];
        const videoSource = $('#videoSource')[0];

        // Dummy values for start and end timestamps in milliseconds
        const startTimestamp = 5000; // 5 seconds
        const endTimestamp = 20000; // 20 seconds

        videoSource.src = `images/00112.mp4`; // Update with actual video path logic
        videoPlayer.load(); // Load the new video source

        videoPlayer.currentTime = startTimestamp / 1000; // Convert milliseconds to seconds
        videoPlayer.play();

        videoPlayer.addEventListener('timeupdate', function stopPlayback() {
            if (videoPlayer.currentTime >= endTimestamp / 1000) { // Convert milliseconds to seconds
                videoPlayer.pause();
                videoPlayer.removeEventListener('timeupdate', stopPlayback);
            }
        });

        // Display the video player
        $('#video-player-container').show();
    }

    function handleCloseButtonClick() {
        $('#video-player-container').hide();
        const videoPlayer = $('#videoPlayer')[0];
        videoPlayer.pause();
        videoPlayer.currentTime = 0;
    }

    function handleDresButtonClick() {
        const videoMetadata = $(this).closest('.grid-item').data('videoMetadata');
        // Implement the logic for DRES submission here
        // For example, you can make an AJAX POST request to your backend API
        $.ajax({
            url: '/api/dres/submit',
            type: 'POST',
            data: { videoId: videoMetadata.videoId },
            success: function(response) {
                alert('DRES submission successful!');
            },
            error: function(error) {
                console.error('Error during DRES submission:', error);
                alert('DRES submission failed.');
            }
        });
    }

    function handleSimilarityButtonClick() {
        const videoMetadata = $(this).closest('.grid-item').data('videoMetadata');
        // Implement the logic for checking similarity here
        // For example, you can make an AJAX POST request to your backend API
        $.ajax({
            url: '/api/similarity/check',
            type: 'POST',
            data: { videoId: videoMetadata.videoId },
            success: function(response) {
                // Handle the response from the similarity check
                alert('Similarity check completed!');
            },
            error: function(error) {
                console.error('Error during similarity check:', error);
                alert('Similarity check failed.');
            }
        });
    }

    function updatePaginationControls() {
        $('#page-info').text(`Page ${currentPage} of ${totalPages}`);
        $('#prev-page').prop('disabled', currentPage === 1);
        $('#next-page').prop('disabled', currentPage === totalPages);
    }
});
