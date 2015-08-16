package com.simpleform.clientapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.simpleform.clientapp.adapters.FormFieldAdapter;
import com.simpleform.clientapp.fragments.CountryFragment;
import com.simpleform.clientapp.fragments.DatePickerFragment;
import com.simpleform.clientapp.helpers.CountryFlagHelper;
import com.simpleform.clientapp.models.FormField;
import com.simpleform.clientapp.models.ClientTemplate;
import com.simpleform.clientapp.models.OwnerTemplate;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, CountryFlagHelper {

    LinearLayout titleLinearLayout;
    public int selectedField;
    FormFieldAdapter formFieldAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleLinearLayout = (LinearLayout) findViewById(R.id.title_linear_layout);
        titleLinearLayout.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.fields);

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
            //Parse.enableLocalDatastore(this);
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

    private void executeClientParseQuery(final boolean isMyProfile, final String objectId, final boolean isForOwnerTemplate) {
        ParseQuery<ClientTemplate> queryMyProfile = ParseQuery.getQuery(ClientTemplate.class);
        queryMyProfile.whereEqualTo("name", "My Profile");
        ParseQuery<ClientTemplate> querySavedClientTemplate = ParseQuery.getQuery(ClientTemplate.class);
        queryMyProfile.whereEqualTo("objectId", objectId);
        List<ParseQuery<ClientTemplate>> queries = new ArrayList<ParseQuery<ClientTemplate>>();
        queries.add(queryMyProfile);
        queries.add(querySavedClientTemplate);

        ParseQuery<ClientTemplate> queryClientsTemplates;
        if (isMyProfile) {
            queryClientsTemplates = ParseQuery.getQuery(ClientTemplate.class);
            queryClientsTemplates.whereEqualTo("user", Utility.getLoggedInUser());
            queryClientsTemplates.whereEqualTo("name", "My Profile");
        } else {
            queryClientsTemplates = ParseQuery.or(queries);
            queryClientsTemplates.whereEqualTo("user", Utility.getLoggedInUser());
        }
        queryClientsTemplates.findInBackground(new FindCallback<ClientTemplate>() {
            @Override
            public void done(List<ClientTemplate> clientsTemplates, ParseException e) {
                ArrayList<FormField> clientFields = new ArrayList<FormField>();
                ArrayList<FormField> myProfileFields = new ArrayList<FormField>();
                clientFields = clientsTemplates.get(0).getFields();

                if (!isForOwnerTemplate) {
                    if (!isMyProfile) {
                        if (clientsTemplates.get(0).getName().equals("My Profile")) {
                            myProfileFields = clientsTemplates.get(0).getFields();
                            clientFields = clientsTemplates.get(1).getFields();
                        } else {
                            clientFields = clientsTemplates.get(0).getFields();
                            myProfileFields = clientsTemplates.get(1).getFields();
                        }
                        connectFields(myProfileFields, clientFields);
                    }
                    setFormFieldAdapter(clientFields);
                } else {
                    executeOwnerParseQuery(objectId, clientFields);
                }
            }
        });
    }

    private void executeOwnerParseQuery(String objectId, final ArrayList<FormField> myProfileFields) {
        ParseQuery<OwnerTemplate> queryOwnersTemplates = ParseQuery.getQuery(OwnerTemplate.class);
        queryOwnersTemplates.whereEqualTo("objectId", objectId);
        queryOwnersTemplates.findInBackground(new FindCallback<OwnerTemplate>() {
            @Override
            public void done(List<OwnerTemplate> ownersTemplates, ParseException e) {
                ArrayList<FormField> ownerFields = new ArrayList<FormField>();
                ownerFields = ownersTemplates.get(0).getFields();
                connectFields(myProfileFields, ownerFields);
                setFormFieldAdapter(ownerFields);
            }
        });
    }

    public void connectFields(ArrayList<FormField> sourceFields, ArrayList<FormField> destFields) {
        for (int i = 0; i < destFields.size(); i++) {
            for (int j = 0; j < sourceFields.size(); j++) {
                String connect = "";
                if (StringUtils.isNotBlank(destFields.get(i).getConnect())) {
                    connect = destFields.get(i).getConnect();
                    connect = connect.substring(2, connect.length() - 2);
                }
                if (connect.equals(sourceFields.get(j).getLabel())) {
                    destFields.get(i).setValue(sourceFields.get(j).getValue());
                    j = destFields.size() - 1;
                }
            }
        }
    }

    public void setFormFieldAdapter(ArrayList<FormField> formFields) {
        formFieldAdapter = new FormFieldAdapter(MainActivity.this, R.layout.field_adapter, formFields);
        listView.setAdapter(formFieldAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                EditText focusField = (EditText) findViewById(R.id.focus_field);
                focusField.requestFocus();
                if (scrollState != 0) {
                    Utility.hideKeyboard(MainActivity.this);
                }
            }
        });
    }

    // Show DatePicker when clicking on field in ListView
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // Update ListView adapter with selected date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = String.format("%02d", day) + "/" + String.format("%02d", month + 1) + "/" + year;
        FormField formField = (FormField) listView.getAdapter().getItem(selectedField);
        formField.setValue(date);
        ((FormFieldAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public String getCountryFlag() {
        Utility.hideKeyboard(this);
        if (CountryFragment.isSelected) {
            String countryName = CountryFragment.countryName;
            CountryFragment.isSelected = false;
            FormField formField = (FormField) listView.getAdapter().getItem(selectedField);
            formField.setValue(countryName);
            ((FormFieldAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
        return null;
    }

    public void setFragment(CountryFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.frame_for_fragments, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void getClosedCountryPicker() {

    }

    @Override
    public void onClick(View view) {

    }
}
