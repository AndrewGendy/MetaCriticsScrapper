package agend932;

public class Game {
    private String pictureUrl;
    private String title;
    private String description;
    private String releaseDate;
    private String ratedScore;
    private String gameURL;
    private String extraInfo;
    
    // Constructor
    public Game(String pictureUrl, String title, String description, String releaseDate, String ratedScore, String gameURL, String extraInfo) {
        this.pictureUrl = pictureUrl;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.ratedScore = ratedScore;
        this.gameURL = gameURL;
        this.extraInfo = extraInfo;
    }

    // Getters
    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRatedScore() {
        return ratedScore;
    }

    public String getGameURL() {
        return gameURL;
    }

    public String getExtraInfo() {
        return extraInfo;
    }
}
