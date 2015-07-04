package com.simpleform.clientapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.simpleform.clientapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ClientTemplate.class);
        ParseObject.registerSubclass(OwnerTemplate.class);
        Parse.initialize(this, "HxlZ3d7O3BuGM6oION0qPLrtrh5TcqnGR1eRecmA", "NP9FyiUzHqbR9LEZXeJ4cgjkfHTTnieMAYJCZkhX");


        /*// ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

            }

        });*/

        ParseQuery<ClientTemplate> queryClientsTemplates = ParseQuery.getQuery(ClientTemplate.class);
        queryClientsTemplates.whereEqualTo("user", "alex1");
        queryClientsTemplates.findInBackground(new FindCallback<ClientTemplate>() {
            @Override
            public void done(List<ClientTemplate> clientsTemplates, ParseException e) {
                ArrayList<ClientField> clientFields = clientsTemplates.get(0).getFields();
                /*ArrayList<String> fields = new ArrayList<String>();
                for (ClientField field : clientFields) {
                    fields.add(field.getLabel());
                }

                ListView listView = (ListView) findViewById(R.id.fields);
                listView.setAdapter(buildAdapter(fields));*/
                ClientFieldAdapter clientFieldAdapter = new ClientFieldAdapter (MainActivity.this, R.layout.field_adapter, clientFields);
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

        ParseQuery<OwnerTemplate> queryOwnersTemplates = ParseQuery.getQuery(OwnerTemplate.class);
        queryOwnersTemplates.findInBackground(new FindCallback<OwnerTemplate>() {
            @Override
            public void done(List<OwnerTemplate> ownersTemplates, ParseException e) {

            }
        });
    }

    public ArrayAdapter buildAdapter(ArrayList<String> fields) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.field_adapter,
                fields);
        return arrayAdapter;
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

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        /*EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*/
        startActivity(intent);
    }
}
