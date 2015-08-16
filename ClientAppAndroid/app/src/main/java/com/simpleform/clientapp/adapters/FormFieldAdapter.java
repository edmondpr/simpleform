package com.simpleform.clientapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.simpleform.clientapp.MainActivity;
import com.simpleform.clientapp.R;
import com.simpleform.clientapp.models.FormField;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;


public class FormFieldAdapter extends ArrayAdapter<FormField> {
    private Activity activity;
    private ArrayList<FormField> formFields;
    private int textViewResourceId;
    private static LayoutInflater inflater = null;

    public FormFieldAdapter(Activity activity, int textViewResourceId, ArrayList<FormField> formFields) {
        super(activity, textViewResourceId, formFields);
        try {
            this.activity = activity;
            this.formFields = formFields;
            this.textViewResourceId = textViewResourceId;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return formFields.size();
    }

    public FormField getItem(FormField position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public MaterialEditText fieldEditText;
        public TextView fieldTextView;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(textViewResourceId, null);
                holder = new ViewHolder();

                holder.fieldTextView = (TextView) vi.findViewById(R.id.fieldTextView);
                holder.fieldEditText = (MaterialEditText) vi.findViewById(R.id.fieldEditText);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
                /*if (((ListView)parent).getSelectedItemPosition() == ListView.INVALID_POSITION) {
                    parent.requestFocus();
                }*/
            }

            holder.fieldEditText.setHint(formFields.get(position).getLabel());
            holder.fieldEditText.setFloatingLabelText(formFields.get(position).getLabel());
            holder.fieldEditText.setText(formFields.get(position).getValue());
            holder.fieldEditText.setId(position);

            // We need to update the adapter once we finish editing
            holder.fieldEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    final int position = v.getId();
                    if (!hasFocus) {
                        final MaterialEditText fieldEdit = (MaterialEditText) v;
                        // Prevent cursor under the other edit text fields from persisting after scroll
                        v.dispatchWindowFocusChanged(hasFocus);
                        try {
                            formFields.get(position).setValue(fieldEdit.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (formFields.get(position).getType().equals("Date")) {
                            ((MainActivity) activity).selectedField = position;
                            holder.fieldEditText.clearFocus();
                            ((MainActivity) activity).showDatePickerDialog(v);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }
}