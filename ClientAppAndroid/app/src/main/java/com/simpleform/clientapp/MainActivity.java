package com.simpleform.clientapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.simpleform.clientapp.adapters.FormFieldAdapter;
import com.simpleform.clientapp.adapters.FormFieldAdapter;
import com.simpleform.clientapp.models.FormField;
import com.simpleform.clientapp.models.ClientTemplate;
import com.simpleform.clientapp.models.FormField;
import com.simpleform.clientapp.models.OwnerTemplate;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    String loggedInUser = "edmondpr@gmail.com";
    LinearLayout titleLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleLinearLayout = (LinearLayout) findViewById(R.id.title_linear_layout);
        titleLinearLayout.setOnClickListener(this);

        titleLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent searchIntent = new Intent(MainActivity.this,
                        SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        Intent intent = getIntent();
        String formType = "client";
        String objectId = "";
        if (intent != null) {
            formType = intent.getStringExtra("formType");
            objectId = intent.getStringExtra("objectId");
        }

        if (StringUtils.isBlank(objectId)) {
            Parse.enableLocalDatastore(this);
            ParseObject.registerSubclass(ClientTemplate.class);
            ParseObject.registerSubclass(OwnerTemplate.class);
            Parse.initialize(this, "HxlZ3d7O3BuGM6oION0qPLrtrh5TcqnGR1eRecmA", "NP9FyiUzHqbR9LEZXeJ4cgjkfHTTnieMAYJCZkhX");
            boolean isMyProfile = true;
            String objectIdParam = "";
            boolean isForOwnerTemplate = false;
            executeClientParseQuery(isMyProfile, objectIdParam, isForOwnerTemplate);
        } else {
            if (formType.equals("client")) {
                boolean isMyProfile = false;
                boolean isForOwnerTemplate = false;
                executeClientParseQuery(isMyProfile, objectId, isForOwnerTemplate);
            } else {
                boolean isMyProfile = true;
                boolean isForOwnerTemplate = true;
                executeClientParseQuery(isMyProfile, objectId, isForOwnerTemplate);
            }
        }

    }

    private void executeClientParseQuery(boolean isMyProfile, final String objectId, final boolean isForOwnerTemplate) {
        ParseQuery<ClientTemplate> queryClientsTemplates = ParseQuery.getQuery(ClientTemplate.class);
        if (isMyProfile) {
            queryClientsTemplates.whereEqualTo("user", loggedInUser);
        } else {
            queryClientsTemplates.whereEqualTo("objectId", objectId);
        }
        queryClientsTemplates.findInBackground(new FindCallback<ClientTemplate>() {
            @Override
            public void done(List<ClientTemplate> clientsTemplates, ParseException e) {
                ArrayList<FormField> clientFields = new ArrayList<FormField>();
                for (int i = 0; i < clientsTemplates.size(); i++) {
                    if (StringUtils.isBlank(clientsTemplates.get(i).getOwner())) {
                        clientFields = clientsTemplates.get(i).getFields();
                        break;
                    }
                }
                if (!isForOwnerTemplate) {
                    FormFieldAdapter formFieldAdapter = new FormFieldAdapter(MainActivity.this, R.layout.field_adapter, clientFields);
                    ListView listView = (ListView) findViewById(R.id.fields);
                    listView.setAdapter(formFieldAdapter);

                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            // TODO Auto-generated method stub
                        }

                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            EditText focusField = (EditText) findViewById(R.id.focus_field);
                            focusField.requestFocus();
                            if (scrollState != 0) {
                                InputMethodManager inputMethodManager = (InputMethodManager)
                                        getSystemService(Activity.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            }
                        }
                    });
                } else {
                    executeOwnerParseQuery(objectId, clientFields);
                }
            }
        });
    }

    private void executeOwnerParseQuery(String objectId, final ArrayList<FormField> clientFields) {
        ParseQuery<OwnerTemplate> queryOwnersTemplates = ParseQuery.getQuery(OwnerTemplate.class);
        queryOwnersTemplates.whereEqualTo("objectId", objectId);
        queryOwnersTemplates.findInBackground(new FindCallback<OwnerTemplate>() {
            @Override
            public void done(List<OwnerTemplate> ownersTemplates, ParseException e) {
                ArrayList<FormField> ownerFields = new ArrayList<FormField>();
                ownerFields = ownersTemplates.get(0).getFields();
                for (int i = 0; i < ownerFields.size(); i++) {
                    for (int j = 0; j < clientFields.size(); j++) {
                        String connect = "";
                        if (StringUtils.isNotBlank(ownerFields.get(i).getConnect())) {
                            connect = ownerFields.get(i).getConnect();
                            connect = connect.substring(2, connect.length() - 2);
                        }
                        if (connect.equals(clientFields.get(j).getLabel())) {
                            ownerFields.get(i).setValue(clientFields.get(j).getValue());
                            j = ownerFields.size() - 1;
                        }
                    }
                }
                FormFieldAdapter formFieldAdapter = new FormFieldAdapter(MainActivity.this, R.layout.field_adapter, ownerFields);
                ListView listView = (ListView) findViewById(R.id.fields);
                listView.setAdapter(formFieldAdapter);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        // TODO Auto-generated method stub
                    }

                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        EditText focusField = (EditText) findViewById(R.id.focus_field);
                        focusField.requestFocus();
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

    @Override
    public void onClick(View view) {

    }
}
