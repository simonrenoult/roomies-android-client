package com.roomies.simonrenoult.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Collocation implements Serializable {
    
    private Integer _id;
    private String _uuid;
    private String _name;
    private String _address;
    private ArrayList<Roomy> _roomies;
    
    public Collocation(JSONObject json) {
        try {
            _id = json.getInt("id");
            _uuid = json.getString("uuid");
            _name = json.getString("name");
            _address = json.getString("address");
            _roomies = Roomy.fromJSON(json.getJSONArray("Roomies"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Collocation> fromJSON(JSONArray json) {
        ArrayList<Collocation> collocations = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                collocations.add(new Collocation(json.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        return collocations;
    }

    public String getName() {
        return _name;
    }

    public String getAddress() {
        return _address;
    }

    public String getUuid() {
        return _uuid;
    }

    public Integer getId() {
        return _id;
    }

    public ArrayList<Roomy> getRoomies() {
        return _roomies;
    }
    
    @Override
    public String toString() {
        return "ID: " + _id + " " +
                "UUID: " + _uuid + " " +
                "NAME: " + _name + " " +
                "ADDRESS: " + _address +
                "ROOMIES: " + _roomies;
                
    }
}
