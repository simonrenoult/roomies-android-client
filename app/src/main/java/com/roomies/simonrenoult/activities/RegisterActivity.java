package com.roomies.simonrenoult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.roomies.simonrenoult.models.Roomy;
import com.roomies.simonrenoult.roomies.R;
import com.roomies.simonrenoult.utils.APIManager;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        EditText emailText = (EditText) findViewById(R.id.emailText);
        EditText pseudoText = (EditText) findViewById(R.id.pseudoText);
        EditText pwdText = (EditText) findViewById(R.id.pwdText);

        if (emailText.getText().toString().isEmpty())
        {
            Toast.makeText(this, "email is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pseudoText.getText().toString().isEmpty())
        {
            Toast.makeText(this, "pseudoname is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwdText.getText().toString().isEmpty())
        {
            Toast.makeText(this, "password is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        new RegisterTask(this).execute(emailText.getText().toString(), pseudoText.getText().toString(), pwdText.getText().toString());
    }

    private void registerIsValid (JSONObject jsonObject)
    {
        String error = null;
        String message = null;
        try {
            error = jsonObject.getString("error");
            message = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (error != "false")
        {
            Toast.makeText(this, "Inscription problem : " + message, Toast.LENGTH_SHORT).show();
            return;
        }
        
        JSONObject roomyData = null;
        try {
            roomyData = jsonObject.getJSONObject("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
       
        Roomy roomy = new Roomy(roomyData);
        Intent i = new Intent(this, CollocationsListActivity.class);
        i.putExtra(CollocationsListActivity.ROOMY, roomy);
        this.startActivity(i);
    }

    private class RegisterTask extends AsyncTask<String, Integer, JSONObject> {

        private Activity _activity;

        public RegisterTask(Activity activity) {
            this._activity = activity;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return new APIManager()
                    .setMethod("POST")
                    .setResource("/api/roomies")
                    .addBody("email", params[0])
                    .addBody("username", params[1])
                    .addBody("password", params[2])
                    .execute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            ((RegisterActivity) this._activity).registerIsValid(jsonObject);
        }
    }
}