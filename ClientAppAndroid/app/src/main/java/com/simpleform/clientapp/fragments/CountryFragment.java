package com.simpleform.clientapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.countrypicker.Country;
import com.countrypicker.CountryListAdapter;
import com.simpleform.clientapp.R;
import com.simpleform.clientapp.Utility;
import com.simpleform.clientapp.helpers.CountryFlagHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CountryFragment extends Fragment{

	View view;
	EditText searchEditText;
	ListView countryListView;
	private List<Country> allCountriesList = new ArrayList<Country>();
	private List<Country> selectedCountriesList = new ArrayList<Country>();
	CountryListAdapter adapter ;
	public static String countryName = "";
	public static boolean isSelected = false;
	public static boolean isClicked = false;
    CountryFlagHelper countryFlagHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.country_picker, container, false);
		searchEditText = (EditText) view.findViewById(R.id.country_picker_search); 
		countryListView =(ListView) view.findViewById(R.id.country_picker_listview);
        countryFlagHelper = (CountryFlagHelper)getActivity();

		getAllCountries();

		// Set adapter
		adapter = new CountryListAdapter(getActivity(), selectedCountriesList);
		countryListView.setAdapter(adapter);

		// Inform listener
		countryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isSelected = true;
				isClicked = true;
				Country country = selectedCountriesList.get(position);
				countryName = country.getName();
                countryFlagHelper.getCountryFlag();
				getActivity().getSupportFragmentManager().popBackStack();

			}
		});

        countryListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != 0) {
                    Utility.hideKeyboard(getActivity());
                }
            }
        });

		// Search for which countries matched user query
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				search(s.toString());
			}
		});

		return view;
	}




	@SuppressLint("DefaultLocale")
	private void search(String text) {
		selectedCountriesList.clear();

		for (Country country : allCountriesList) {
			if (country.getName().toLowerCase(Locale.ENGLISH)
					.contains(text.toLowerCase())) {
				selectedCountriesList.add(country);
			}
		}

		adapter.notifyDataSetChanged();
	}

	/**
	 * Get all countries with code and name from res/raw/countries.json
	 * 
	 * @return
	 */
	public List<Country> getAllCountries() {
		if (allCountriesList.isEmpty()) {
			try {
				allCountriesList = new ArrayList<Country>();

				// Read from local file
				String allCountriesString = readFileAsString(getActivity());
				Log.d("countrypicker", "country: " + allCountriesString);
				JSONObject jsonObject = new JSONObject(allCountriesString);
				Log.d("countrypicker", "country: " + jsonObject);
				Log.d("countrypicker", "country: " + jsonObject);
				Iterator<?> keys = jsonObject.keys();

				// Add the data to all countries list
				while (keys.hasNext()) {
					String key = (String) keys.next();
					if (!key.equals("GB") && !key.equals("US")) {
						Country country = new Country();
						country.setCode(key);
						country.setName(jsonObject.getString(key));
						allCountriesList.add(country);
					}
				}

				// Sort the all countries list based on country name
				Collections.sort(allCountriesList, new FishNameComparator());

                Country country = new Country();
                String key = "GB";
                country = new Country();
                country.setCode(key);
                country.setName(jsonObject.getString(key));
                allCountriesList.add(0, country);
                key = "US";
                country = new Country();
                country.setCode(key);
                country.setName(jsonObject.getString(key));
                allCountriesList.add(1, country);

				// Initialize selected countries with all countries
				selectedCountriesList = new ArrayList<Country>();
				selectedCountriesList.addAll(allCountriesList);

				// Return
				return allCountriesList;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * R.string.countries is a json string which is Base64 encoded to avoid
	 * special characters in XML. It's Base64 decoded here to get original json.
	 * 
	 * @param context
	 * @return
	 * @throws java.io.IOException
	 */
	private static String readFileAsString(Context context)
			throws java.io.IOException {
		String base64 = context.getResources().getString(R.string.countries);
		byte[] data = Base64.decode(base64, Base64.DEFAULT);
		return new String(data, "UTF-8");
	}

	public class FishNameComparator implements Comparator<Country> {

		@Override
		public int compare(Country lhs, Country rhs) {
			return lhs.getName().compareTo(rhs.getName());
		}
	}
}
