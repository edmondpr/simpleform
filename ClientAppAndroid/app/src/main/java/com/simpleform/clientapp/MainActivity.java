package com.simpleform.clientapp;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.simpleform.clientapp.adapters.FormFieldAdapter;
import com.simpleform.clientapp.fragments.CountryFragment;
import com.simpleform.clientapp.fragments.DatePickerFragment;
import com.simpleform.clientapp.helpers.CountryFlagHelper;
import com.simpleform.clientapp.models.ClientField;
import com.simpleform.clientapp.models.ClientTemplate;
import com.simpleform.clientapp.models.FormField;
import com.simpleform.clientapp.models.OwnerField;

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
        String formId = "";
        if (intent != null) {
            formType = intent.getStringExtra("formType");
            formId = intent.getStringExtra("formId");
        }

        if (StringUtils.isBlank(formId)) {
            getMyProfile();
        } else {
            if (formType.equals("client")) {
                getClientFields(formId);
            } else {
                getOwnerFields(formId);
            }
        }

    }

    private void getMyProfile() {
        ParseQuery<ClientTemplate> queryMyProfileId = ParseQuery.getQuery(ClientTemplate.class);
        queryMyProfileId.whereEqualTo("name", "My Profile");
        queryMyProfileId.whereEqualTo("user", Utility.getLoggedInUser());
        queryMyProfileId.findInBackground(new FindCallback<ClientTemplate>() {
            @Override
            public void done(List<ClientTemplate> clientsTemplates, ParseException e) {
                String myProfileId = clientsTemplates.get(0).getObjectId();
                ParseQuery<ClientField> queryMyProfile = ParseQuery.getQuery(ClientField.class);
                queryMyProfile.whereEqualTo("formId", myProfileId);
                queryMyProfile.findInBackground(new FindCallback<ClientField>() {
                    @Override
                    public void done(List<ClientField> myProfileFields, ParseException e) {
                        ArrayList<FormField> myProfileFormFields = new ArrayList<FormField>();
                        for (ClientField clientField : myProfileFields) {
                            FormField formField = new FormField();
                            formField.setLabel(clientField.getLabel());
                            formField.setValue(clientField.getValue());
                            formField.setConnect(clientField.getConnect());
                            formField.setType(clientField.getType());
                            myProfileFormFields.add(formField);
                        }
                        SimpleFormApplication.myProfileFormFields = myProfileFormFields;
                        setFormFieldAdapter(myProfileFormFields);
                    }
                });
            }
        });
    }

    private void getClientFields(String formId) {
        ParseQuery<ClientField> queryClientFields = ParseQuery.getQuery(ClientField.class);
        queryClientFields.whereEqualTo("formId", formId);
        queryClientFields.findInBackground(new FindCallback<ClientField>() {
            @Override
            public void done(List<ClientField> clientFields, ParseException e) {
                ArrayList<FormField> clientFormFields = new ArrayList<FormField>();
                for (ClientField clientField : clientFields) {
                    FormField formField = new FormField();
                    formField.setLabel(clientField.getLabel());
                    formField.setValue(clientField.getValue());
                    formField.setConnect(clientField.getConnect());
                    formField.setType(clientField.getType());
                    clientFormFields.add(formField);
                }
                connectFields(SimpleFormApplication.myProfileFormFields, clientFormFields);
                setFormFieldAdapter(clientFormFields);
            }
        });
    }
    private void getOwnerFields(String formId) {
        ParseQuery<OwnerField> queryOwnerFields = ParseQuery.getQuery(OwnerField.class);
        queryOwnerFields.whereEqualTo("formId", formId);
        queryOwnerFields.findInBackground(new FindCallback<OwnerField>() {
            @Override
            public void done(List<OwnerField> ownerFields, ParseException e) {
                ArrayList<FormField> ownerFormFields = new ArrayList<FormField>();
                OwnerField ownerField1 = ownerFields.get(0);
                for (OwnerField ownerField : ownerFields) {
                    FormField formField = new FormField();
                    //formField.setLabel(ownerField.getLabel());
                    formField.setConnect(ownerField.getConnect());
                    ownerFormFields.add(formField);
                }
                connectFields(SimpleFormApplication.myProfileFormFields, ownerFormFields);
                setFormFieldAdapter(ownerFormFields);
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
