package com.simpleform.clientapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.simpleform.clientapp.adapters.ClientFieldAdapter;
import com.simpleform.clientapp.models.ClientField;
import com.simpleform.clientapp.models.ClientTemplate;
import com.simpleform.clientapp.models.OwnerTemplate;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ClientTemplate.class);
        ParseObject.registerSubclass(OwnerTemplate.class);
        Parse.initialize(this, "HxlZ3d7O3BuGM6oION0qPLrtrh5TcqnGR1eRecmA", "NP9FyiUzHqbR9LEZXeJ4cgjkfHTTnieMAYJCZkhX");

        ParseQuery<ClientTemplate> queryClientsTemplates = ParseQuery.getQuery(ClientTemplate.class);
        queryClientsTemplates.whereEqualTo("user", "alex");
        queryClientsTemplates.findInBackground(new FindCallback<ClientTemplate>() {
            @Override
            public void done(List<ClientTemplate> clientsTemplates, ParseException e) {
                ArrayList<ClientField> clientFields = new ArrayList<ClientField>();
                for (int i = 0; i < clientsTemplates.size(); i++) {
                    if (StringUtils.isBlank(clientsTemplates.get(i).getOwner())) {
                        clientFields = clientsTemplates.get(i).getFields();
                        break;
                    }
                }
                ClientFieldAdapter clientFieldAdapter = new ClientFieldAdapter(MainActivity.this, R.layout.field_adapter, clientFields);
                ListView listView = (ListView) findViewById(R.id.fields);
                listView.setAdapter(clientFieldAdapter);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        // TODO Auto-generated method stub
                    }

                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        EditText searchField = (EditText) findViewById(R.id.searchField);
                        searchField.requestFocus();
                        if (scrollState != 0) {
                            InputMethodManager inputMethodManager = (InputMethodManager)
                                    getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
