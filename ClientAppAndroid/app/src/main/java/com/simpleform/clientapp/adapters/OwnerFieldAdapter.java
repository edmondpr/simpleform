package com.simpleform.clientapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.simpleform.clientapp.R;
import com.simpleform.clientapp.models.OwnerField;

import java.util.ArrayList;


public class OwnerFieldAdapter extends ArrayAdapter<OwnerField> {
    private Activity activity;
    private ArrayList<OwnerField> ownerFields;
    private int textViewResourceId;
    private static LayoutInflater inflater = null;

    public OwnerFieldAdapter(Activity activity, int textViewResourceId, ArrayList<OwnerField> ownerFields) {
        super(activity, textViewResourceId, ownerFields);
        try {
            this.activity = activity;
            this.ownerFields = ownerFields;
            this.textViewResourceId = textViewResourceId;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return ownerFields.size();
    }

    public OwnerField getItem(OwnerField position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public MaterialEditText field;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(textViewResourceId, null);
                holder = new ViewHolder();

                holder.field = (MaterialEditText) vi.findViewById(R.id.field);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
                /*if (((ListView)parent).getSelectedItemPosition() == ListView.INVALID_POSITION) {
                    parent.requestFocus();
                }*/
            }

            holder.field.setHint(ownerFields.get(position).getLabel());
            holder.field.setFloatingLabelText(ownerFields.get(position).getLabel());
            holder.field.setText(ownerFields.get(position).getConnect());
            holder.field.setId(position);

            // We need to update the adapter once we finish editing
            holder.field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final MaterialEditText field_edit = (MaterialEditText) v;
                        // Prevent cursor under the other edit text fields from persisting after scroll
                        v.dispatchWindowFocusChanged(hasFocus);
                        try {
                            ownerFields.get(position).setPosition(Integer.parseInt(field_edit.getText().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
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