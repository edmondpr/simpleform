package com.simpleform.clientapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simpleform.clientapp.R;
import com.simpleform.clientapp.models.OwnerTemplate;

import java.util.ArrayList;


public class SearchAdapter extends ArrayAdapter<OwnerTemplate> {
    private Activity activity;
    private ArrayList<OwnerTemplate> ownerTemplates;
    private int textViewResourceId;
    private static LayoutInflater inflater = null;

    public SearchAdapter(Activity activity, int textViewResourceId, ArrayList<OwnerTemplate> ownerTemplates) {
        super(activity, textViewResourceId, ownerTemplates);
        try {
            this.activity = activity;
            this.ownerTemplates = ownerTemplates;
            this.textViewResourceId = textViewResourceId;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return ownerTemplates.size();
    }

    public OwnerTemplate getItem(OwnerTemplate position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView ownerName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(textViewResourceId, null);
                holder = new ViewHolder();

                holder.ownerName = (TextView) vi.findViewById(R.id.owner_name);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
                /*if (((ListView)parent).getSelectedItemPosition() == ListView.INVALID_POSITION) {
                    parent.requestFocus();
                }*/
            }

            holder.ownerName.setText(ownerTemplates.get(position).getOwner());
            holder.ownerName.setId(position);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }
}