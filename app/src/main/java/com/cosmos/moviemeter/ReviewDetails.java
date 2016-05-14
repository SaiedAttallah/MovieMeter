package com.cosmos.moviemeter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saied Attallah on 4/23/2016.
 */
public class ReviewDetails {
    private String id;
    private String author;
    private String content;

    public ReviewDetails() {

    }

    public ReviewDetails(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.author = trailer.getString("author");
        this.content = trailer.getString("content");
    }

    public String getId() { return id; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }
}
