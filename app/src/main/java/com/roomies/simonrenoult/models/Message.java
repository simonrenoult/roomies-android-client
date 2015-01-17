package com.roomies.simonrenoult.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Message {
    
    private String _content;
    private String _creationDate;
    private Integer _collocationId;
    private Integer _roomyId;
    private Roomy _roomy;
    
    public Message(String _content, Integer _collocationId, Roomy roomy) {
        this._content = _content;
        this._collocationId = _collocationId;
        this._roomyId = roomy.getId();
        this._roomy = roomy;
    }

    public Message(JSONObject json) {
        try {
            _content = json.getString("content");
            _creationDate = json.getString("createdAt");
            _collocationId = json.getInt("CollocationId");
            _roomyId = json.getInt("RoomyId");
            _roomy = new Roomy(json.getJSONObject("Roomy"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Message> fromJSON(JSONArray json) {
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                messages.add(new Message(json.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        return messages;
    }

    public String getContent() {
        return _content;
    }

    public Roomy getRoomy() {
        return _roomy;
    }

    @Override
    public String toString() {
        return "#" + _roomyId + " created \"" + _content + "\"" +
                " on " + _creationDate + " for #" + _collocationId;
    }
}
