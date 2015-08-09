package com.simpleform.clientapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.simpleform.clientapp.adapters.SearchAdapter;
import com.simpleform.clientapp.models.ClientTemplate;
import com.simpleform.clientapp.models.FormTemplate;
import com.simpleform.clientapp.models.OwnerTemplate;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class SearchActivity extends FragmentActivity implements TextWatcher {

    ArrayList<FormTemplate> formsTemplatesGlobal = new ArrayList<FormTemplate>();
    ArrayList<FormTemplate> searchFormsTemplates = new ArrayList<FormTemplate>();
    StickyListHeadersListView formsListViewGlobal;
    EditText searchField;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchField = (EditText) findViewById(R.id.search_field);
        searchField.addTextChangedListener(this);
        formsListViewGlobal = (StickyListHeadersListView) findViewById(R.id.templates);

        ParseQuery<ClientTemplate> queryClientsTemplates = ParseQuery.getQuery(ClientTemplate.class);
        queryClientsTemplates.whereEqualTo("user", Utility.getLoggedInUser());
        queryClientsTemplates.findInBackground(new FindCallback<ClientTemplate>() {
            @Override
            public void done(List<ClientTemplate> clientsTemplates, ParseException e) {
                ArrayList<ClientTemplate> clientsTemplatesSorted = new ArrayList<ClientTemplate>();
                Set<String> types = new HashSet<String>();
                for (ClientTemplate clientTemplate : clientsTemplates) {
                    if (types.add(clientTemplate.getName())) {
                        clientsTemplatesSorted.add(clientTemplate);
                    }
                }

                // Add client template from sorted list to global templates
                for (ClientTemplate clientTemplate : clientsTemplatesSorted) {
                    FormTemplate formTemplate = new FormTemplate();
                    if (!clientTemplate.getName().equals("My Profile")) {
                        formTemplate.setObjectId(clientTemplate.getObjectId());
                        formTemplate.setName(clientTemplate.getName());
                        formTemplate.setOwner(clientTemplate.getOwner());
                        formTemplate.setType(clientTemplate.getType());
                        formTemplate.setFields(clientTemplate.getFields());
                        formsTemplatesGlobal.add(formTemplate);
                    }
                }

                ParseQuery<OwnerTemplate> queryOwnersTemplates = ParseQuery.getQuery(OwnerTemplate.class);
                queryOwnersTemplates.findInBackground(new FindCallback<OwnerTemplate>() {
                    @Override
                    public void done(final List<OwnerTemplate> ownersTemplates, ParseException e) {
                        ArrayList<OwnerTemplate> ownersTemplatesSorted = new ArrayList<OwnerTemplate>();
                        Set<String> types = new HashSet<String>();
                        for (OwnerTemplate ownerTemplate : ownersTemplates) {
                            if (types.add(ownerTemplate.getOwner())) {
                                ownersTemplatesSorted.add(ownerTemplate);
                            }
                        }

                        // Add owner template from sorted list to global templates
                        for (OwnerTemplate ownerTemplate : ownersTemplatesSorted) {
                            FormTemplate formTemplate = new FormTemplate();
                            formTemplate.setObjectId(ownerTemplate.getObjectId());
                            formTemplate.setOwner(ownerTemplate.getOwner());
                            formTemplate.setType(ownerTemplate.getType());
                            formTemplate.setFields(ownerTemplate.getFields());
                            formsTemplatesGlobal.add(formTemplate);
                        }

                        searchAdapter = new SearchAdapter(SearchActivity.this, R.layout.search_adapter, formsTemplatesGlobal);
                        final StickyListHeadersListView formsListView = formsListViewGlobal;
                        formsListView.setAdapter(searchAdapter);

                        // ListView Item Click Listener
                        formsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                FormTemplate formTemplate = (FormTemplate) formsListView.getItemAtPosition(position);
                                int numberOfForms = 0;
                                ArrayList<OwnerTemplate> ownersTemplatesGroup = new ArrayList<OwnerTemplate>();
                                if (StringUtils.isBlank(formTemplate.getName())) {
                                    for (OwnerTemplate ownerTemplateIterator : ownersTemplates) {
                                        if (ownerTemplateIterator.getOwner().equals(formTemplate.getOwner())) {
                                            numberOfForms++;
                                            ownersTemplatesGroup.add(ownerTemplateIterator);
                                        }
                                    }
                                }
                                if (numberOfForms == 0) {
                                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                                    intent.putExtra("formType", "client");
                                    intent.putExtra("objectId", formTemplate.getObjectId());
                                    startActivity(intent);
                                }  else if (numberOfForms == 1) {
                                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                                    intent.putExtra("formType", "owner");
                                    intent.putExtra("objectId", formTemplate.getObjectId());
                                    startActivity(intent);
                                } else {
                                    openFormSelector(ownersTemplatesGroup);
                                }
                            }

                        });
                    }
                });
            }
        });
    }

    public void openFormSelector(final ArrayList<OwnerTemplate> ownersTemplatesGroup) {
        CharSequence forms[] = new CharSequence[ownersTemplatesGroup.size()];
        for (int i=0; i<ownersTemplatesGroup.size(); i++) {
            forms[i] = ownersTemplatesGroup.get(i).getType();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please select a form");
        builder.setItems(forms, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i=0; i<ownersTemplatesGroup.size(); i++) {
                    if (i == which) {
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        intent.putExtra("formType", "owner");
                        intent.putExtra("objectId", ownersTemplatesGroup.get(i).getObjectId());
                        startActivity(intent);
                    }
                }
            }
        });
        builder.show();
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
        if (!formsTemplatesGlobal.isEmpty()) {
            if (searchFormsTemplates != null && !searchFormsTemplates.isEmpty()) {
                searchFormsTemplates.clear();
            }
            for (int i = 0; i < formsTemplatesGlobal.size(); i++) {
                if (StringUtils.isNotBlank(formsTemplatesGlobal.get(i).getName())) {
                    if ((formsTemplatesGlobal.get(i)
                            .getName().toLowerCase())
                            .contains(searchField.getText()
                                    .toString().toLowerCase())) {
                        searchFormsTemplates.add(formsTemplatesGlobal.get(i));
                    }
                } else {
                    if ((formsTemplatesGlobal.get(i)
                            .getOwner().toLowerCase())
                            .contains(searchField.getText()
                                    .toString().toLowerCase())) {
                        searchFormsTemplates.add(formsTemplatesGlobal.get(i));
                    }
                }
            }

            if (searchFormsTemplates != null && !searchFormsTemplates.isEmpty()) {
                searchAdapter = new SearchAdapter(SearchActivity.this, R.layout.search_adapter, searchFormsTemplates);
            }
            formsListViewGlobal.setAdapter(searchAdapter);
        }
    }
}
