package com.example.busra.phonecontacts;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ContactAdapter extends ArrayAdapter<ContactBean> {
    private Activity activity;
    private List<ContactBean> items;
    private int row;
    private ContactBean objBean;


    public ContactAdapter(Activity act, int row, List<ContactBean> items) {
        super(act, row, items);

        this.activity = act;
        this.row = row;
        this.items = items;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);

            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((items == null) || ((position + 1) > items.size()))
            return view;

        objBean = items.get(position);
        holder.imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        holder.contctName = (TextView) view.findViewById(R.id.contactName);
        holder.phoneNo = (TextView) view.findViewById(R.id.phoneNumber);

        holder.imgAvatar.setImageResource(R.drawable.tel);

        if (holder.contctName != null && null != objBean.getName()
                && objBean.getName().trim().length() > 0) {
            holder.contctName.setText(Html.fromHtml(objBean.getName()));
        }
        if (holder.phoneNo != null && null != objBean.getPhoneNo()
                && objBean.getPhoneNo().trim().length() > 0) {
            holder.phoneNo.setText(Html.fromHtml(objBean.getPhoneNo()));
        }

        return view;
    }

    public class ViewHolder {
        public TextView contctName;
        public TextView phoneNo;
        public ImageView imgAvatar;
    }

}
