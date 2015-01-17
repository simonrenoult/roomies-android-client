package com.roomies.simonrenoult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roomies.simonrenoult.models.Collocation;
import com.roomies.simonrenoult.models.Roomy;
import com.roomies.simonrenoult.roomies.R;
import com.roomies.simonrenoult.utils.APIManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    public static final String ROOMY = "com.roomies.simonrenoult.LoginActivity.roomy";
    public static final String COLLOCATION = "com.roomies.simonrenoult.LoginActivity.collocation";

    private Roomy _roomy;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void doLogin(View v){
        String email    = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is missing", Toast.LENGTH_SHORT).show();
        }
        
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is missing", Toast.LENGTH_SHORT).show();
        }
        
        new LoginTask(this).execute(email, password);
    }

    public void doRegister(View v) {
        
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
    
    private void onLogin(JSONObject json) {
        // Checks if the API has returned an error flag
        boolean authenticationSucceeded = true;
        try {
            authenticationSucceeded = !json.getBoolean("error");
        } catch (JSONException e) {
            authenticationSucceeded = false;
        }

        if(!authenticationSucceeded) {
            Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Read the message key containing the authenticated user
        JSONObject roomyData = null;
        try {
            roomyData = json.getJSONObject("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        _roomy = new Roomy(roomyData);

        // Redirects to the appropriate activity
        if(_roomy.hasCollocation()) {
            new GetRoomysCollocation(this).execute(_roomy.getToken(), _roomy.getCollocation().getUuid());
        } else {
            Intent i = new Intent(this, CollocationsListActivity.class);
            i.putExtra(CollocationsListActivity.ROOMY, _roomy);
            startActivity(i);
        }
    }
    
    public void redirectToCollocationPage(JSONObject json) {
        JSONObject collocationData = null;
        try {
            collocationData = json.getJSONObject("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        Collocation c = new Collocation(collocationData);
        
        Intent i = new Intent(this, CollocationActivity.class);
        i.putExtra(CollocationsListActivity.ROOMY, _roomy);
        i.putExtra(CollocationsListActivity.COLLOCATION, c);
        startActivity(i);
        
    }
    
    private class GetRoomysCollocation extends AsyncTask<String, Integer, JSONObject> {
        private Activity _activity;

        private GetRoomysCollocation(Activity _activity) {
            this._activity = _activity;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return new APIManager()
                    .setMethod("GET")
                    .addHeader("Authorization", params[0])
                    .setResource("/api/collocations/" + params[1])
                    .execute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            ((LoginActivity) _activity).redirectToCollocationPage(jsonObject);
        }
    }
    
    private class LoginTask extends AsyncTask<String, Integer, JSONObject> {

        private LoginActivity _activity;

        public LoginTask(LoginActivity activity) {
            this._activity = activity;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return new APIManager()
                .setMethod("GET")
                .setResource("/api/auth")
                .addHeader("authorization", params[0] + ":" + params[1])
                .execute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            this._activity.onLogin(jsonObject);
        }
    }


}
