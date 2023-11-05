package agend932;

/**
 * Represents a media item scraped from Metacritic, encapsulating all the
 * relevant details of a media entity such as a game, movie, or TV show.
 */
public class Media {
    private String mediaType;
    private String pictureUrl;
    private String title;
    private String description;
    private String platform;
    private String genre;
    private String releaseDate;
    private String ratedScore;
    private String originalURL;
    private String metascore;
    private String extraInfo;
    
    /**
     * Constructs a new {@code Media} object with detailed information.
     *
     * @param mediaType    The type of the media (e.g., game, movie, tv).
     * @param pictureUrl   The URL of the media's thumbnail image.
     * @param title        The title of the media.
     * @param description  A brief description of the media.
     * @param platform     The platform for which the media is available.
     * @param genre        The genre of the media.
     * @param releaseDate  The release date of the media.
     * @param ratedScore   The score rated by users.
     * @param originalURL  The original URL to the media's Metacritic page.
     * @param metascore    The metascore assigned by Metacritic.
     * @param extraInfo    Any additional information available for the media.
     */
    public Media(String mediaType, String pictureUrl, String title, String description, String platform, String genre, String releaseDate, String ratedScore, String originalURL, String metascore, String extraInfo) {
        this.mediaType = mediaType;
        this.pictureUrl = pictureUrl;
        this.title = title;
        this.description = description;
        this.platform = platform;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.ratedScore = ratedScore;
        this.originalURL = originalURL;
        this.metascore = metascore;
        this.extraInfo = extraInfo;
    }

    // Getters for each property with appropriate Javadoc comments

    /**
     * Gets the media type.
     *
     * @return The media type.
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Gets the URL of the media's thumbnail image.
     *
     * @return The thumbnail image URL.
     */
    public String getPictureUrl() {
        return pictureUrl;
    }

    /**
     * Gets the title of the media.
     *
     * @return The media title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description of the media.
     *
     * @return The media description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the platform for which the media is available.
     *
     * @return The media platform.
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Gets the genre of the media.
     *
     * @return The media genre.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Gets the release date of the media.
     *
     * @return The media release date.
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Gets the user-rated score of the media.
     *
     * @return The rated score.
     */
    public String getRatedScore() {
        return ratedScore;
    }

    /**
     * Gets the original URL to the media's Metacritic page.
     *
     * @return The original Metacritic URL.
     */
    public String getOriginalURL() {
        return originalURL;
    }

    /**
     * Gets the metascore of the media.
     *
     * @return The metascore.
     */
    public String getMetascore() {
        return metascore;
    }

    /**
     * Gets any additional information about the media.
     *
     * @return The extra information.
     */
    public String getExtraInfo() {
        return extraInfo;
    }
}