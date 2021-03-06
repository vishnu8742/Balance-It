package com.vishnu.anon.balanceit;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.vishnu.anon.balanceit.data.db_contract;

import static android.content.ContentValues.TAG;

public class transAdapter extends CursorAdapter {


    public transAdapter(Context context, Cursor c) {
        super(context, c);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.transactions, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView amount = (TextView) view.findViewById(R.id.id_amount);
        TextView commission = (TextView) view.findViewById(R.id.id_commission);
        TextView bank_name = (TextView) view.findViewById(R.id.id_bank);
        TextView service = (TextView) view.findViewById(R.id.id_service);
        TextView cash_in_hand = (TextView) view.findViewById(R.id.id_cashinhand);
        TextView cash_at_bank = (TextView) view.findViewById(R.id.id_cashatbank);
        TextView date = (TextView) view.findViewById(R.id.id_date);


        int amount_index = cursor.getColumnIndex(db_contract.trans.AMOUNT);
        Log.d(TAG, "bindView: " + amount_index);
        int comm_index = cursor.getColumnIndex(db_contract.trans.COMMISSION);
        Log.d(TAG, "bindView: " + comm_index);
        int bank_index = cursor.getColumnIndex(db_contract.trans.BANK_NAME);
        Log.d(TAG, "bindView: " + bank_index);
        int serv_index = cursor.getColumnIndex(db_contract.trans.SERVICE);
        Log.d(TAG, "bindView: " + serv_index);
        int cash_index = cursor.getColumnIndex(db_contract.trans.CASH_IN_HAND);
        Log.d(TAG, "bindView: " + cash_index);
        int cash_bankindex = cursor.getColumnIndex(db_contract.trans.CASH_AT_BANK);
        Log.d(TAG, "bindView: " + cash_bankindex);
        int date_index = cursor.getColumnIndex(db_contract.trans.TIME);
        Log.d(TAG, "bindView: " + date_index);

        String amount_value = cursor.getString(amount_index);
        Log.d(TAG, "bindView: " + amount_value);
        String comm_value = cursor.getString(comm_index);
        Log.d(TAG, "bindView: " + comm_value);
        String bank_value = cursor.getString(bank_index);
        Log.d(TAG, "bindView: " + bank_value);
        String serv_value = cursor.getString(serv_index);
        Log.d(TAG, "bindView: " + serv_value);
        String cash_value = cursor.getString(cash_index);
        Log.d(TAG, "bindView: " + cash_value);
        String cash_bank_value = cursor.getString(cash_bankindex);
        Log.d(TAG, "bindView: " + cash_bank_value);
        String date_value = cursor.getString(date_index);
        Log.d(TAG, "bindView: " + date_value);

        amount.setText(amount_value);
        commission.setText(comm_value);
        bank_name.setText(bank_value);
        service.setText(serv_value);
        cash_in_hand.setText(cash_value);
        cash_at_bank.setText(cash_bank_value);
        date.setText(date_value);

    }
}
