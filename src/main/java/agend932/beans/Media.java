package agend932.beans;

// Converted the Media class to a JavaBean by adding a no-argument constructor, getters/setters for each property, and using the Serializable interface.

import java.io.Serializable;

public class Media implements Serializable {
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
    
    // /**
    //  * Constructs a new {@code Media} object with detailed information.
    //  *
    //  * @param mediaType    The type of the media (e.g., game, movie, tv).
    //  * @param pictureUrl   The URL of the media's thumbnail image.
    //  * @param title        The title of the media.
    //  * @param description  A brief description of the media.
    //  * @param platform     The platform for which the media is available.
    //  * @param genre        The genre of the media.
    //  * @param releaseDate  The release date of the media.
    //  * @param ratedScore   The score rated by users.
    //  * @param originalURL  The original URL to the media's Metacritic page.
    //  * @param metascore    The metascore assigned by Metacritic.
    //  * @param extraInfo    Any additional information available for the media.
    //  */
    // public Media(String mediaType, String pictureUrl, String title, String description, String platform, String genre, String releaseDate, String ratedScore, String originalURL, String metascore, String extraInfo) {
    //     this.mediaType = mediaType;
    //     this.pictureUrl = pictureUrl;
    //     this.title = title;
    //     this.description = description;
    //     this.platform = platform;
    //     this.genre = genre;
    //     this.releaseDate = releaseDate;
    //     this.ratedScore = ratedScore;
    //     this.originalURL = originalURL;
    //     this.metascore = metascore;
    //     this.extraInfo = extraInfo;
    // }

    /**
     * No-argument constructor for the {@code Media} class, as per the JavaBeans specification.
     * This constructor is empty because it is used when creating a new instance of the {@code Media} class without providing any initial values for its properties.
     */
    public Media() {
    }

    // Getters and Setters for each property with appropriate Javadoc comments

    /**
     * Gets the media type.
     *
     * @return The media type.
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Sets the media type of the object.
     * 
     * @param mediaType the media type to be set
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
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
     * Sets the URL of the media's thumbnail image.
     * 
     * @param pictureUrl thumbnail image URL to be set
     */
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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
     * Sets the title of the media.
     * 
     * @param title the title to be set
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Sets the description of the media.
     * 
     * @param description the description to be set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Sets the platform for which the media is available.
     * 
     * @param platform the platform to be set
     */
    public void setPlatform(String platform) {
        this.platform = platform;
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
     * Sets the genre of the media.
     * 
     * @param genre the genre to be set
     */
    public void setGenre(String genre) {
        this.genre = genre;
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
     * Sets the release date of the media.
     * 
     * @param releaseDate the release date to be set
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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
     * Sets the user-rated score of the media.
     * 
     * @param ratedScore the rated score to be set
     */
    public void setRatedScore(String ratedScore) {
        this.ratedScore = ratedScore;
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
     * Sets the original URL to the media's Metacritic page.
     * 
     * @param originalURL the original Metacritic URL to be set
     */
    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
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
     * Sets the metascore of the media.
     * 
     * @param metascore the metascore to be set
     */
    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    /**
     * Gets any additional information about the media.
     *
     * @return The extra information.
     */
    public String getExtraInfo() {
        return extraInfo;
    }

    /**
     * Sets any additional information about the media.
     * 
     * @param extraInfo the extra information to be set
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}