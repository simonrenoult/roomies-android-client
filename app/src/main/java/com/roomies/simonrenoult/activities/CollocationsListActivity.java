package com.roomies.simonrenoult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.roomies.simonrenoult.adapters.CollocationsListAdapter;
import com.roomies.simonrenoult.models.Collocation;
import com.roomies.simonrenoult.models.Roomy;
import com.roomies.simonrenoult.roomies.R;
import com.roomies.simonrenoult.utils.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CollocationsListActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String ROOMY = "com.roomies.simonrenoult.CollocationListActivity.roomy";
    public static final String COLLOCATION = "com.roomies.simonrenoult.CollocationListActivity.collocation";;

    private Roomy _roomy;
    private Collocation _selectedCollocation;
    private ArrayList<Collocation> _collocations;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocations_list);

        _roomy = (Roomy) getIntent().getExtras().getSerializable(CollocationsListActivity.ROOMY);

        new ListCollocationsTask(this).execute(_roomy.getToken());
    }

    public void populateCollocations(JSONObject json) {
        JSONArray collocationsData = null;
        try {
            collocationsData = json.getJSONArray("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        if(collocationsData == null) {
            // TODO: Display the collocation creation form.
            return;
        }

        _collocations = Collocation.fromJSON(collocationsData);
        CollocationsListAdapter adapter = new CollocationsListAdapter(this, _collocations);
        ListView lv = (ListView) findViewById(R.id.collocations_list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedUuid = view.getTag().toString();
        _selectedCollocation = null;
        for(Collocation c : _collocations) {
            if(c.getUuid() == selectedUuid) {
                _selectedCollocation = c;
                break;
            }
        }
        
        new AttributeRoomyToCollocation(this).execute(String.valueOf(_selectedCollocation.getId()), _roomy.getUuid(), _roomy.getToken());
    }
    
    public void redirectToCollocationPage() {
        Intent i = new Intent(this, CollocationActivity.class);
        i.putExtra(ROOMY, _roomy);
        i.putExtra(COLLOCATION, _selectedCollocation);
        startActivity(i);
    }

    private class AttributeRoomyToCollocation extends AsyncTask<String, Integer, JSONObject> {
        private Activity _activity;
        
        public AttributeRoomyToCollocation (Activity activity) {
            _activity = activity;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            APIManager req = new APIManager()
                    .setMethod("PUT")
                    .setBase("https://secret-shelf-2410.herokuapp.com")
                    .setResource("/api/roomies/" + params[1])
                    .addHeader("authorization", params[2])
                    .addBody("collocationId", Integer.valueOf(params[0]));
            
            Log.i("REQUETE", req.toString());
            return req.execute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            ((CollocationsListActivity) _activity).redirectToCollocationPage();
        }
    }
    
    private class ListCollocationsTask extends AsyncTask<String, Integer, JSONObject> {
        
        private Activity _activity;
        
        public ListCollocationsTask(Activity activity) {
            _activity = activity;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return new APIManager()
                    .setMethod("GET")
                    .setResource("/api/collocations")
                    .addHeader("authorization", params[0])
                    .execute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            ((CollocationsListActivity) _activity).populateCollocations(jsonObject);
        }
    }
}
