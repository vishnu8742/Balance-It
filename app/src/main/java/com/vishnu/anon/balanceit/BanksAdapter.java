package com.vishnu.anon.balanceit;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.vishnu.anon.balanceit.data.db_contract;

public class BanksAdapter extends CursorAdapter {


    public BanksAdapter(Context context, Cursor c) {
        super(context, c);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.banks_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.banknameid);
        TextView amountTextView = (TextView) view.findViewById(R.id.amountid);

        int nameindex = cursor.getColumnIndex(db_contract.trans.ACCOUNT_NAMES);
        int balanceindex = cursor.getColumnIndex(db_contract.trans.ACCOUNT_BALANCE);

        String bankname = cursor.getString(nameindex);
        String balance = cursor.getString(balanceindex);

        nameTextView.setText(bankname);
        amountTextView.setText(balance);

    }
}
