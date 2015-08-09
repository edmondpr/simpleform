package com.simpleform.clientapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simpleform.clientapp.R;
import com.simpleform.clientapp.models.FormTemplate;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class SearchAdapter extends ArrayAdapter<FormTemplate> implements StickyListHeadersAdapter {
    private Activity activity;
    private ArrayList<FormTemplate> formTemplates;
    private int textViewResourceId;
    private static LayoutInflater inflater = null;

    public SearchAdapter(Activity activity, int textViewResourceId, ArrayList<FormTemplate> formTemplates) {
        super(activity, textViewResourceId, formTemplates);
        try {
            this.activity = activity;
            this.formTemplates = formTemplates;
            this.textViewResourceId = textViewResourceId;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return formTemplates.size();
    }

    public FormTemplate getItem(FormTemplate position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView formName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(textViewResourceId, null);
                holder = new ViewHolder();

                holder.formName = (TextView) vi.findViewById(R.id.form_name);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
                /*if (((ListView)parent).getSelectedItemPosition() == ListView.INVALID_POSITION) {
                    parent.requestFocus();
                }*/
            }

            if (StringUtils.isNotBlank(formTemplates.get(position).getName())) {
                holder.formName.setText(formTemplates.get(position).getName());
            } else {
                holder.formName.setText(formTemplates.get(position).getOwner());
            }
            holder.formName.setId(position);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String headerText = "";
        if (StringUtils.isNotBlank(formTemplates.get(position).getName())) {
            headerText = "My saved forms";
        } else {
            headerText = "Other forms";
        }
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        long id;
        if (StringUtils.isNotBlank(formTemplates.get(position).getName())) {
            id = 1;
        } else {
            id = 2;
        }
        return id;
    }

    class HeaderViewHolder {
        TextView text;
    }

}