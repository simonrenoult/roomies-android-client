package com.roomies.simonrenoult.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class APIManager {

    private String _base;
    private String _resource;
    private String _method;
    private JSONObject _body;
    private Map<String, String> _headers;
    
    public void APIManager() {
        _headers = new HashMap<String, String>();
    }
    
    public String getBase() {
        return _base;
    }
    
    public String getResource() {
        return _resource;
    }

    public String getMethod() {
        return _method;
    }
    
    public Map<String, String> getHeaders() {
        if(_headers == null) {
            _headers = new HashMap<String, String>();
        }
        return _headers;
    }
    
    public JSONObject getBody() {
        if(_body == null) {
            _body = new JSONObject();
        }
        return _body;
    }
    
    public APIManager setBase(String base) {
        this._base = base;
        return this;
    }
    
    public APIManager setResource(String resource) {
        this._resource = resource;
        return this;
    }

    
    public APIManager setMethod(String method) {
        method = method.toUpperCase();
        if(method != "GET" && method != "POST" && method != "PUT" && method != "DELETE") {
            throw new Error("Method must be GET/POST/PUT/DELETE");
        }
        
        this._method = method;
        
        return this;
    }
  
    public APIManager addBody(String key, String value) {
        if(_body == null) {
            _body = new JSONObject();
        }
        
        try {
            _body.put(key, value);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        
        return this;
    } 
    
    public APIManager addBody(String key, int value) {
        if(_body == null) {
            _body = new JSONObject();
        }

        try {
            _body.put(key, value);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return this;
    }
    
    public APIManager addHeader(String key, String value) {
        if(_headers == null) {
            _headers = new HashMap<String, String>();
        }
        _headers.put(key, value);
        return this;   
    }
    
    public JSONObject execute() {
        if (getBase() == null || getBase().isEmpty()) {
            setBase("https://secret-shelf-2410.herokuapp.com");
        }
        
        if (getResource() == null || getResource().isEmpty()) {
            throw new Error("Resource URL is missing");
        }
        
        if (getMethod() == null || getMethod().isEmpty()) {
            setMethod("GET");
        }
        
        JSONObject res = null;
        if (getMethod() == "GET") {
            res = get();
        } else if (getMethod() == "POST") {
            res = post();
        } else if (getMethod() == "PUT") {
            res = put();
        } else if (getMethod() == "DELETE") {
            // res = delete();
        }
        
        return res;
    }
    
    private JSONObject get() {
        HttpClient client = null;
        HttpGet req = null;
        HttpResponse res = null;
        
        client = new DefaultHttpClient();
        req = new HttpGet(getBase() + getResource());
        req.setHeader("Content-Type", "application/json");

        for (Map.Entry<String, String> entry: getHeaders().entrySet()) {
            req.setHeader(entry.getKey(), entry.getValue());
        }

        try {
            res = client.execute(req);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseApiResponse(res);
    }
    
    private JSONObject post() {
        HttpClient client = null;
        HttpPost req = null;
        HttpResponse res = null;
        
        client = new DefaultHttpClient();
        req = new HttpPost(getBase() + getResource());
        req.setHeader("Content-Type", "application/json");

        if(!getHeaders().isEmpty()) {
            for (Map.Entry<String, String> entry: getHeaders().entrySet()) {
                req.setHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            req.setEntity(new StringEntity(_body.toString(), "UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            res = client.execute(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return parseApiResponse(res);
    }
    
    private JSONObject put() {
        HttpClient client = null;
        HttpPut req = null;
        HttpResponse res = null;
        
        client = new DefaultHttpClient();
        req = new HttpPut(getBase() + getResource());
        req.setHeader("Content-Type", "application/json");

        if(!getHeaders().isEmpty()) {
            for (Map.Entry<String, String> entry: getHeaders().entrySet()) {
                req.setHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            req.setEntity(new StringEntity(_body.toString(), "UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            res = client.execute(req);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseApiResponse(res);
    }
    
    private JSONObject parseApiResponse(HttpResponse res) {
        InputStream inputStream = null;
        InputStreamReader streamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = null;
        String line = "";
        String result = "";
        JSONObject resultAsJson = null;
        
        try {
            inputStream = res.getEntity().getContent();
            streamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader =  new BufferedReader(streamReader, 8);
            stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + '\n');
            }
            
            inputStream.close();
            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            resultAsJson = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return resultAsJson;
    }

    @Override
    public String toString() {
        return getMethod() + " " + getBase() + " " + getResource() + "\r" + getBody();
    }
}
