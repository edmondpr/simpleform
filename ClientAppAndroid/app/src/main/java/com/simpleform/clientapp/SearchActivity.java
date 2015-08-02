package com.simpleform.clientapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.simpleform.clientapp.adapters.SearchAdapter;
import com.simpleform.clientapp.models.OwnerTemplate;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends FragmentActivity implements TextWatcher {

    ArrayList<OwnerTemplate> ownersTemplatesGlobal = new ArrayList<OwnerTemplate>();
    ArrayList<OwnerTemplate> searchOwnersTemplates = new ArrayList<OwnerTemplate>();
    ListView ownersListViewGlobal;
    EditText searchField;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchField = (EditText) findViewById(R.id.search_field);
        searchField.addTextChangedListener(this);
        ownersListViewGlobal = (ListView) findViewById(R.id.templates);

        ParseQuery<OwnerTemplate> queryOwnersTemplates = ParseQuery.getQuery(OwnerTemplate.class);
        queryOwnersTemplates.findInBackground(new FindCallback<OwnerTemplate>() {
            @Override
            public void done(List<OwnerTemplate> ownersTemplates, ParseException e) {
                ownersTemplatesGlobal = (ArrayList<OwnerTemplate>) ownersTemplates;
                searchAdapter = new SearchAdapter(SearchActivity.this, R.layout.search_adapter, ownersTemplatesGlobal);
                final ListView ownersListView = ownersListViewGlobal;
                ownersListView.setAdapter(searchAdapter);

                // ListView Item Click Listener
                ownersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition     = position;

                        // ListView Clicked item value
                        OwnerTemplate  itemValue    = (OwnerTemplate) ownersListView.getItemAtPosition(position);

                        // Show Alert
                        Toast.makeText(getApplicationContext(),
                                "Position: " + itemPosition + "  ListItem: " + itemValue.getOwner(), Toast.LENGTH_LONG)
                                .show();

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

    // Search filter methods
    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!ownersTemplatesGlobal.isEmpty()) {
            if (searchOwnersTemplates != null && !searchOwnersTemplates.isEmpty()) {
                searchOwnersTemplates.clear();
            }
            for (int i = 0; i < ownersTemplatesGlobal.size(); i++) {
                if ((ownersTemplatesGlobal.get(i)
                        .getOwner().toLowerCase())
                        .contains(searchField.getText()
                                .toString().toLowerCase())) {
                    searchOwnersTemplates.add(ownersTemplatesGlobal.get(i));
                }
            }

            if (searchOwnersTemplates != null && !searchOwnersTemplates.isEmpty()) {
                searchAdapter = new SearchAdapter(SearchActivity.this, R.layout.search_adapter, searchOwnersTemplates);
            }
            ownersListViewGlobal.setAdapter(searchAdapter);
        }
    }
}
