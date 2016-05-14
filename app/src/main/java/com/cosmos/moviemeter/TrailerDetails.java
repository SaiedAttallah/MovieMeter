package com.cosmos.moviemeter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saied Attallah on 5/14/2016.
 */
public class TrailerDetails {
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public TrailerDetails() {

    }

    public TrailerDetails(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.key = trailer.getString("key");
        this.name = trailer.getString("name");
        this.site = trailer.getString("site");
        this.type = trailer.getString("type");
    }

    public String getId() {
        return id;
    }

    public String getKey() { return key; }

    public String getName() { return name; }

    public String getSite() { return site; }

    public String getType() { return type; }
}
