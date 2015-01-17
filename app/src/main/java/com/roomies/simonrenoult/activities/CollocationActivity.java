package com.roomies.simonrenoult.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.roomies.simonrenoult.adapters.CollocationPagerAdapter;
import com.roomies.simonrenoult.adapters.MessagesListAdapter;
import com.roomies.simonrenoult.models.Collocation;
import com.roomies.simonrenoult.models.Message;
import com.roomies.simonrenoult.models.Roomy;
import com.roomies.simonrenoult.roomies.R;
import com.roomies.simonrenoult.utils.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class CollocationActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager _viewPager;
    private CollocationPagerAdapter _adapter;
    private ActionBar _actionBar;
    
    private Collocation _collocation;
    private Roomy _roomy;
    private ArrayList<Message> _messages;
    
    private final static String[] TABS_TITLES = {"Board", "Map"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocation);
        
        _roomy = (Roomy) getIntent().getExtras().getSerializable(CollocationsListActivity.ROOMY);
        _collocation = (Collocation) getIntent().getExtras().getSerializable(CollocationsListActivity.COLLOCATION);

        setupView();

        new GetBoardMessagesTaks(this).execute(_roomy.getToken(), _collocation.getUuid());
    }
    
    private void setupView() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CollocationsListActivity.COLLOCATION, _collocation);
        
        _viewPager = (ViewPager) findViewById(R.id.pager);
        _actionBar = getActionBar();
        _adapter   = new CollocationPagerAdapter(getSupportFragmentManager(), bundle);

        _viewPager.setAdapter(_adapter);
        _actionBar.setHomeButtonEnabled(false);
        _actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String tabName: TABS_TITLES) {
            _actionBar.addTab(_actionBar.newTab().setText(tabName).setTabListener(this));
        }

        _viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                _actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void populateBoard(JSONObject json) {
        JSONArray messagesData = null;
        try {
            messagesData = json.getJSONArray("message");
        } catch(JSONException e) {
            e.printStackTrace();
        }
        
        _messages = Message.fromJSON(messagesData);
        Collections.reverse(_messages);
        MessagesListAdapter adapter = new MessagesListAdapter(this, _messages);
        ListView lv = (ListView) findViewById(R.id.collocation_messages_list);
        lv.setAdapter(adapter);
    }

    public void sendNewMessage(View v) {

        EditText newMessageField = (EditText) findViewById(R.id.new_message);
        String newMessage = (newMessageField).getText().toString();

        Message m = new Message(newMessage, _collocation.getId(), _roomy);
        
        new SendNewMessageTask(this).execute(_roomy.getToken(), _collocation.getId(), m);
        newMessageField.setText("");
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        _viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    private class SendNewMessageTask extends  AsyncTask<Object, Integer, JSONObject> {
        
        private Activity _activity;
        
        private SendNewMessageTask(Activity activity) {
            _activity = activity;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {
            Message m = (Message) params[2];
            return new APIManager()
                    .setMethod("POST")
                    .setResource("/api/messages")
                    .addHeader("authorization", params[0].toString())
                    .addBody("content", m.getContent())
                    .addBody("roomyId", m.getRoomy().getId())
                    .addBody("collocationId", (int) params[1])
                    .execute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            ((CollocationActivity) _activity).populateBoard(jsonObject);
        }
    }
    
    private class GetBoardMessagesTaks extends AsyncTask<String, Integer, JSONObject> {
        Activity _activity;
        
        public GetBoardMessagesTaks(Activity activity) {
            _activity = activity;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return new APIManager()
                    .setMethod("GET")
                    .setResource("/api/collocations/" + params[1] + "/messages")
                    .addHeader("authorization", params[0])
                    .execute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            ((CollocationActivity) _activity).populateBoard(jsonObject);
        }
    }
}