package com.roomies.simonrenoult.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Roomy implements Serializable{

    private Integer _id;
    private String _uuid;
    private String _email;
    private String _username;
    private String _password;
    private String _token;
    private Integer _collocationId;
    private double _lat;
    private double _lng;
    private Collocation _collocation;

    public static ArrayList<Roomy> fromJSON(JSONArray json) {
        ArrayList<Roomy> roomies = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                roomies.add(new Roomy(json.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return roomies;
    }

    public Roomy(JSONObject json) {
        try {
            _id = json.getInt("id");
            _uuid = json.getString("uuid");
            _email = json.getString("email");
            _password = json.getString("password");
            _username = json.getString("username");
            _token = json.getString("token");
            _lat = json.isNull("lat") ? -1 : json.getDouble("lat");
            _lng = json.isNull("lng") ? -1 : json.getDouble("lng");
            _collocationId = json.isNull("CollocationId") ? -1 : json.getInt("CollocationId");
            _collocation = new Collocation(json.getJSONObject("Collocation"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public boolean hasCollocation() {
        return this._collocationId != null && this._collocationId != -1;
    }

    public String getToken() {
        return this._token;
    }

    public String getUsername() {
        return _username;
    }

    public double getLat() {
        return _lat;
    }

    public double getLng() {
        return _lng;
    }

    public String getUuid() {
        return _uuid;
    }

    public Collocation getCollocation() {
        return _collocation;
    }
    
    @Override
    public String toString() {
        return "\rID: " + _id + " " +
               "\rUUID: " + _uuid +
               "\rEMAIL: " + _email +
               "\rPASSWORD: " + _password +
                "\rUSERNAME: " + _username +
                "\rLAT: " + _lat +
                "\rLNG: " + _lng +
               "\rTOKEN: " + _token +
                "\rCollocationId: " + _collocationId +
                "\rCollocation: " + _collocation;
    }

    public Integer getId() {
        return _id;
    }
}
