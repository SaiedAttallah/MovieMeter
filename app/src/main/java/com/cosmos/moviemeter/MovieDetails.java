package com.cosmos.moviemeter;

/**
 * Created by Saied Attallah on 5/6/2016.
 */
public class MovieDetails {
    private String voteAverage;
    private String releaseDate;
    private String movieOverview;
    private String originalTitle;
    private String movieId;
    private String posterPath;

    public MovieDetails() {
    }

    public MovieDetails(String voteAverage, String releaseDate, String movieOverview, String originalTitle, String movieId, String posterPath) {
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.movieOverview = movieOverview;
        this.originalTitle = originalTitle;
        this.movieId = movieId;
        this.posterPath = posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
