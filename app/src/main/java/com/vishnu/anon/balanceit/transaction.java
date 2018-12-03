package com.vishnu.anon.balanceit;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnu.anon.balanceit.data.db_contract;
import com.vishnu.anon.balanceit.data.db_contract.trans;

import java.util.ArrayList;
import java.util.List;

public class transaction extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button deposit, withdraw;
    EditText amount, commisiion;
    Spinner service, bank;
    private int amount_value, commission_value, present_balance, new_balance, total_bank_balance, new_total_bank_balance, previous_cash, new_cash;
    private String bank_name, service_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction);
        bank = findViewById(R.id.bank);
        service = findViewById(R.id.service);
        loadBanks();
        loadServices();
        getSupportActionBar().setTitle("Add Transaction");
        deposit = (Button) findViewById(R.id.deposit_button);
        withdraw = (Button) findViewById(R.id.withdraw_button);
        amount = findViewById(R.id.amount);
        commisiion = findViewById(R.id.commission);
        bank.setOnItemSelectedListener(this);
        service.setOnItemSelectedListener(this);
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    amount_value = 0; commission_value = 0;
                    amount_value = Integer.parseInt(amount.getText().toString().trim());
                    commission_value = Integer.parseInt(commisiion.getText().toString().trim());
                }catch (Exception e){
                    Log.d("", String.valueOf(e));
                }
                Log.d("", "onClick: " + amount_value + "   " + commission_value + " " + bank_name + " " + service_name);
            if(amount_value > 1  && !bank_name.equals("SELECT BANK") && !service_name.equals("SELECT SERVICE")){
                Toast.makeText(getApplicationContext(), "All Good", Toast.LENGTH_SHORT).show();
                String[] projection = {
                        trans.ACCOUNT_NAMES,
                        trans.ACCOUNT_BALANCE
                };
                String where = trans.ACCOUNT_NAMES + "=?";
                String[] whereArgs = new String[] {String.valueOf(bank_name)};

                CursorLoader cursorLoader =  new CursorLoader(getApplicationContext(),
                        trans.CONTENT_BALANCE_URI,
                        projection,
                        where,
                        whereArgs,
                        null);
                Cursor getbalance = cursorLoader.loadInBackground();
                if (getbalance != null && getbalance.getCount() > 0) {
                    if (getbalance.moveToFirst()) {
                        do {
                            present_balance = Integer.parseInt(getbalance.getString(0));
                        } while (getbalance.moveToNext());
                    }
                }
                cursorLoader.cancelLoad();
                Log.d("", "Present Balance " + bank_name + " : " + present_balance);
                if (present_balance > amount_value){
                    new_balance = present_balance - amount_value;
                    String[] tables = {
                            trans.ACCOUNT_NAMES,
                            trans.ACCOUNT_BALANCE
                    };
                    String from = trans.ACCOUNT_NAMES + "=?";
                    String[] values = new String[] {"CASH AT BANK"};

                    CursorLoader cursor =  new CursorLoader(getApplicationContext(),
                            trans.CONTENT_BALANCE_URI,
                            tables,
                            from,
                            values,
                            null);
                    Cursor getdata = cursorLoader.loadInBackground();
                    if (getdata != null && getdata.getCount() > 0) {
                        if (getdata.moveToFirst()) {
                            do {
                                total_bank_balance = Integer.parseInt(getdata.getString(0));
                            } while (getdata.moveToNext());
                        }
                    }
                    cursor.cancelLoad();

                    String[] cash_projection = {
                            trans.ACCOUNT_NAMES,
                            trans.ACCOUNT_BALANCE
                    };
                    String cash_where = trans.ACCOUNT_NAMES + "=?";
                    String[] cash_where_args = new String[] {"CASH IN HAND"};

                    CursorLoader cash_cursor =  new CursorLoader(getApplicationContext(),
                            trans.CONTENT_BALANCE_URI,
                            cash_projection,
                            cash_where,
                            cash_where_args,
                            null);
                    Cursor get_cash = cursorLoader.loadInBackground();
                    if (get_cash != null && get_cash.getCount() > 0) {
                        if (get_cash.moveToFirst()) {
                            do {
                                previous_cash = Integer.parseInt(get_cash.getString(0));
                            } while (get_cash.moveToNext());
                        }
                    }
                    cursorLoader.cancelLoad();

                    new_cash = previous_cash + amount_value;

                    new_total_bank_balance = total_bank_balance - amount_value;

                    ContentValues update_values = new ContentValues();
                    update_values.put(trans.ACCOUNT_BALANCE, new_balance);

                    int rowsAffected = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_values, where, whereArgs);

                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(getApplicationContext(), "Failed",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(getApplicationContext(), "Success",
                                Toast.LENGTH_SHORT).show();
                    }

                    ContentValues update_total_values = new ContentValues();
                    update_values.put(trans.ACCOUNT_BALANCE, new_total_bank_balance);

                    int rowsUpdated = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_total_values, from, values);

                    if (rowsUpdated == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(getApplicationContext(), "Failed",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(getApplicationContext(), "Success",
                                Toast.LENGTH_SHORT).show();
                    }

                    ContentValues update_total_cash = new ContentValues();
                    update_total_cash.put(trans.ACCOUNT_BALANCE, new_cash);

                    int cashUpdated = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_total_cash, cash_where, cash_where_args);

                    if (cashUpdated == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(getApplicationContext(), "Failed",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(getApplicationContext(), "Success",
                                Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), " Funds are less in " + bank_name + " account", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "ADD DETAILS MAN", Toast.LENGTH_SHORT).show();
            }
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.getText().toString().trim();
                commisiion.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "CLICKED ON WITHDRAW", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadBanks() {
        List<String> banks = new ArrayList<>();

        String[] projection = {
                trans.BANK_NAMES
        };

        // This loader will execute the ContentProvider's query method on a background thread
        CursorLoader cursor = new CursorLoader(this,   // Parent activity context
                trans.CONTENT_BANK_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);

        Cursor c = cursor.loadInBackground();
        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    banks.add(c.getString(0));
                } while (c.moveToNext());
            }

                // Creating adapter for spinner
                ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, banks);
                //Type of spinner
                bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                bank.setAdapter(bankAdapter);

        }else{
            Toast.makeText(getApplicationContext(), "No Banks Added Yet", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadServices() {
        List<String> servs = new ArrayList<>();

        String[] projection = {
                trans.SECTIONS
        };

        // This loader will execute the ContentProvider's query method on a background thread
        CursorLoader cursor = new CursorLoader(this,   // Parent activity context
                trans.CONTENT_SERV_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);

        Cursor c = cursor.loadInBackground();
        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    servs.add(c.getString(0));
                } while (c.moveToNext());
            }


                // Creating adapter for spinner
                ArrayAdapter<String> servAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, servs);
                //Type of spinner
                servAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                service.setAdapter(servAdapter);

        }else{
            Toast.makeText(getApplicationContext(), "No Services Added Yet", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner bank_spinner = (Spinner)parent;
        Spinner service_spinner = (Spinner)parent;
        if (bank_spinner.getId() == R.id.bank ){
            TextView tv = (TextView)view;
            bank_name = tv.getText().toString();
        }else if (service_spinner.getId() == R.id.service){
            TextView sv = (TextView)view;
            service_name = sv.getText().toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
