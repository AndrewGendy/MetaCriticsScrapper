package agend932;

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
    private String extraInfo;
    
    // Constructor
    public Media(String mediaType, String pictureUrl, String title, String description, String platform, String genre, String releaseDate, String ratedScore, String originalURL, String extraInfo) {
        this.mediaType = mediaType;
        this.pictureUrl = pictureUrl;
        this.title = title;
        this.description = description;
        this.platform = platform;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.ratedScore = ratedScore;
        this.originalURL = originalURL;
        this.extraInfo = extraInfo;
    }

    // Getters
    public String getMediaType() {
        return mediaType;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPlatform() {
        return platform;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRatedScore() {
        return ratedScore;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public String getExtraInfo() {
        return extraInfo;
    }
}
