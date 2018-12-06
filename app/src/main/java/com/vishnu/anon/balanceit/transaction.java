package com.vishnu.anon.balanceit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class transaction extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button deposit, withdraw, payment;
    EditText amount, commisiion;
    Spinner service, bank;
    private int amount_value,bank_present_cash, commission_value, present_balance, new_balance, total_bank_balance, new_total_bank_balance, previous_cash, new_cash;
    private String bank_name, service_name, type;
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
        payment = findViewById(R.id.payment);
        amount = findViewById(R.id.amount);
        commisiion = findViewById(R.id.commission);
        bank.setOnItemSelectedListener(this);
        service.setOnItemSelectedListener(this);
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    type = "deposit";
                    amount_value = 0; commission_value = 0;
                    amount_value = Integer.parseInt(amount.getText().toString().trim());
                    commission_value = Integer.parseInt(commisiion.getText().toString().trim());
                }catch (Exception e){
                    Log.d("", String.valueOf(e));
                }
                Log.d("", "onClick: " + amount_value + "   " + commission_value + " " + bank_name + " " + service_name);
            if(amount_value >= 1  && !bank_name.equals("SELECT BANK") && !service_name.equals("SELECT SERVICE")){
                String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
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
                            present_balance = Integer.parseInt(getbalance.getString(1));
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
                    Cursor getdata = cursor.loadInBackground();
                    if (getdata != null && getdata.getCount() > 0) {
                        if (getdata.moveToFirst()) {
                            do {
                                total_bank_balance = Integer.parseInt(getdata.getString(1));
                            } while (getdata.moveToNext());
                        }
                    }
                    cursor.cancelLoad();
                    Log.d("", "total_bank_balance" + total_bank_balance);

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

                    Cursor get_cash = cash_cursor.loadInBackground();
                    if (get_cash != null && get_cash.getCount() > 0) {
                        if (get_cash.moveToFirst()) {
                            do {
                                previous_cash = Integer.parseInt(get_cash.getString(1));
                            } while (get_cash.moveToNext());
                        }
                    }
                    cursorLoader.cancelLoad();

                    new_cash = previous_cash + amount_value + commission_value;

                    new_total_bank_balance = total_bank_balance - amount_value;

                    Log.d("", "New cash " + new_cash + " Total Bank " + new_total_bank_balance);

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

                    String[] update_where = new String[] {"CASH AT BANK"};

                    ContentValues update_total_values = new ContentValues();
                    update_total_values.put(trans.ACCOUNT_BALANCE, new_total_bank_balance);

                    int rowsUpdated = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_total_values, from, update_where);

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

                    ContentValues transaction = new ContentValues();
                    transaction.put(trans.AMOUNT, amount_value);
                    transaction.put(trans.COMMISSION, commission_value);
                    transaction.put(trans.TYPE, type);
                    transaction.put(trans.BANK_NAME, bank_name);
                    transaction.put(trans.SERVICE, service_name);
                    transaction.put(trans.CASH_AT_BANK, total_bank_balance);
                    transaction.put(trans.CASH_IN_HAND, new_cash);
                    transaction.put(trans.TIME, time);

                    Uri newUri = getContentResolver().insert(trans.CONTENT_TRANS_URI, transaction);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(getApplicationContext(), "Failed" ,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(getApplicationContext(),"Success",
                                Toast.LENGTH_SHORT).show();
                        amount.getText().clear();
                        commisiion.getText().clear();
                        bank.setSelection(0);
                        service.setSelection(0);
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
                try {
                    type = "withdraw";
                    amount_value = 0; commission_value = 0;
                    amount_value = Integer.parseInt(amount.getText().toString().trim());
                    commission_value = Integer.parseInt(commisiion.getText().toString().trim());
                }catch (Exception e){
                    Log.d("", String.valueOf(e));
                }
                Log.d("", "onClick: " + amount_value + "   " + commission_value + " " + bank_name + " " + service_name);
                if(amount_value >= 1  && !bank_name.equals("SELECT BANK") && !service_name.equals("SELECT SERVICE")){
                    String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
                    String[] projection = {
                            trans.ACCOUNT_NAMES,
                            trans.ACCOUNT_BALANCE
                    };
                    String where = trans.ACCOUNT_NAMES + "=?";
                    String[] whereArgs = new String[] {"CASH IN HAND"};

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
                                present_balance = Integer.parseInt(getbalance.getString(1));
                            } while (getbalance.moveToNext());
                        }
                    }
                    cursorLoader.cancelLoad();
                    Log.d("", "Present Balance " + bank_name + " : " + present_balance);
                    if (present_balance >= amount_value){
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
                        Cursor getdata = cursor.loadInBackground();
                        if (getdata != null && getdata.getCount() > 0) {
                            if (getdata.moveToFirst()) {
                                do {
                                    total_bank_balance = Integer.parseInt(getdata.getString(1));
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

                        Cursor get_cash = cash_cursor.loadInBackground();
                        if (get_cash != null && get_cash.getCount() > 0) {
                            if (get_cash.moveToFirst()) {
                                do {
                                    previous_cash = Integer.parseInt(get_cash.getString(1));
                                } while (get_cash.moveToNext());
                            }
                        }
                        cursorLoader.cancelLoad();

                        String[] bank_projection = {
                                trans.ACCOUNT_NAMES,
                                trans.ACCOUNT_BALANCE
                        };
                        String bank_where = trans.ACCOUNT_NAMES + "=?";
                        String[] bank_where_args = new String[] {String.valueOf(bank_name)};

                        CursorLoader bank_cursor =  new CursorLoader(getApplicationContext(),
                                trans.CONTENT_BALANCE_URI,
                                bank_projection,
                                bank_where,
                                bank_where_args,
                                null);

                        Cursor get_bank = bank_cursor.loadInBackground();
                        if (get_bank != null && get_bank.getCount() > 0) {
                            if (get_bank.moveToFirst()) {
                                do {
                                    bank_present_cash = Integer.parseInt(get_bank.getString(1));
                                } while (get_bank.moveToNext());
                            }
                        }
                        cursorLoader.cancelLoad();


                        new_cash = previous_cash - amount_value;

                        new_total_bank_balance = total_bank_balance + amount_value + commission_value;

                        int new_bank_total_cash = bank_present_cash + amount_value + commission_value;

                        Log.d("", " " +bank_name + " : " + new_bank_total_cash);

                        ContentValues update_bank_values = new ContentValues();
                        update_bank_values.put(trans.ACCOUNT_BALANCE, new_bank_total_cash);

                        int bankrowsAffected = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_bank_values, bank_where, bank_where_args);

                        if (bankrowsAffected == 0) {
                            // If no rows were affected, then there was an error with the update.
                            Toast.makeText(getApplicationContext(), "Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the update was successful and we can display a toast.
                            Toast.makeText(getApplicationContext(), "Success",
                                    Toast.LENGTH_SHORT).show();
                        }



                        Log.d("", "New cash " + new_cash + " Total Bank " + new_total_bank_balance);

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

                        String[] update_where = new String[] {"CASH AT BANK"};

                        ContentValues update_total_values = new ContentValues();
                        update_total_values.put(trans.ACCOUNT_BALANCE, new_total_bank_balance);

                        int rowsUpdated = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_total_values, from, update_where);

                        if (rowsUpdated == 0) {
                            // If no rows were affected, then there was an error with the update.
                            Toast.makeText(getApplicationContext(), "Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the update was successful and we can display a toast.
                            Toast.makeText(getApplicationContext(), "Success",
                                    Toast.LENGTH_SHORT).show();
                        }

//                        ContentValues update_total_cash = new ContentValues();
//                        update_total_cash.put(trans.ACCOUNT_BALANCE, new_cash);
//
//                        int cashUpdated = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_total_cash, cash_where, cash_where_args);
//
//                        if (cashUpdated == 0) {
//                            // If no rows were affected, then there was an error with the update.
//                            Toast.makeText(getApplicationContext(), "Failed",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            // Otherwise, the update was successful and we can display a toast.
//                            Toast.makeText(getApplicationContext(), "Success",
//                                    Toast.LENGTH_SHORT).show();
//                        }

                        ContentValues transaction = new ContentValues();
                        transaction.put(trans.AMOUNT, amount_value);
                        transaction.put(trans.COMMISSION, commission_value);
                        transaction.put(trans.TYPE, type);
                        transaction.put(trans.BANK_NAME, bank_name);
                        transaction.put(trans.SERVICE, service_name);
                        transaction.put(trans.CASH_AT_BANK, total_bank_balance);
                        transaction.put(trans.CASH_IN_HAND, new_balance);
                        transaction.put(trans.TIME, time);

                        Uri newUri = getContentResolver().insert(trans.CONTENT_TRANS_URI, transaction);

                        // Show a toast message depending on whether or not the insertion was successful.
                        if (newUri == null) {
                            // If the new content URI is null, then there was an error with insertion.
                            Toast.makeText(getApplicationContext(), "Failed" ,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the insertion was successful and we can display a toast.
                            Toast.makeText(getApplicationContext(),"Success",
                                    Toast.LENGTH_SHORT).show();
                            amount.getText().clear();
                            commisiion.getText().clear();
                            bank.setSelection(0);
                            service.setSelection(0);
                            finish();
                        }

                    }else {
                        Toast.makeText(getApplicationContext(), " Cash is less in your hand", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "ADD DETAILS MAN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    type = "payment";
                    amount_value = 0;
                    commission_value = 0;
                    amount_value = Integer.parseInt(amount.getText().toString().trim());
                    commission_value = Integer.parseInt(commisiion.getText().toString().trim());
                } catch (Exception e) {
                    Log.d("", String.valueOf(e));
                }
                Log.d("", "onClick: " + amount_value + "   " + commission_value + " " + bank_name + " " + service_name);
                if (amount_value >= 1 && !bank_name.equals("SELECT BANK") && !service_name.equals("SELECT SERVICE")) {
                    String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
                    String[] projection = {trans.ACCOUNT_NAMES, trans.ACCOUNT_BALANCE};
                    String where = trans.ACCOUNT_NAMES + "=?";
                    String[] whereArgs = new String[]{String.valueOf(bank_name)};

                    CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), trans.CONTENT_BALANCE_URI, projection, where, whereArgs, null);
                    Cursor getbalance = cursorLoader.loadInBackground();
                    if (getbalance != null && getbalance.getCount() > 0) {
                        if (getbalance.moveToFirst()) {
                            do {
                                present_balance = Integer.parseInt(getbalance.getString(1));
                            } while (getbalance.moveToNext());
                        }
                    }
                    cursorLoader.cancelLoad();
                    Log.d("", "Present Balance " + bank_name + " : " + present_balance);
                    if (present_balance > amount_value) {
                        new_balance = present_balance - amount_value;
                        String[] tables = {trans.ACCOUNT_NAMES, trans.ACCOUNT_BALANCE};
                        String from = trans.ACCOUNT_NAMES + "=?";
                        String[] values = new String[]{"CASH AT BANK"};

                        CursorLoader cursor = new CursorLoader(getApplicationContext(), trans.CONTENT_BALANCE_URI, tables, from, values, null);
                        Cursor getdata = cursor.loadInBackground();
                        if (getdata != null && getdata.getCount() > 0) {
                            if (getdata.moveToFirst()) {
                                do {
                                    total_bank_balance = Integer.parseInt(getdata.getString(1));
                                } while (getdata.moveToNext());
                            }
                        }
                        cursor.cancelLoad();

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

                        String[] update_where = new String[] {"CASH AT BANK"};

                        ContentValues update_total_values = new ContentValues();
                        update_total_values.put(trans.ACCOUNT_BALANCE, new_total_bank_balance);

                        int rowsUpdated = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_total_values, from, update_where);

                        if (rowsUpdated == 0) {
                            // If no rows were affected, then there was an error with the update.
                            Toast.makeText(getApplicationContext(), "Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the update was successful and we can display a toast.
                            Toast.makeText(getApplicationContext(), "Success",
                                    Toast.LENGTH_SHORT).show();
                        }

                        String[] cash_tables = {trans.ACCOUNT_NAMES, trans.ACCOUNT_BALANCE};
                        String cash_from = trans.ACCOUNT_NAMES + "=?";
                        String[] cash_values = new String[]{"CASH AT BANK"};

                        CursorLoader cash_cursor = new CursorLoader(getApplicationContext(), trans.CONTENT_BALANCE_URI, tables, from, values, null);
                        Cursor cashdata = cash_cursor.loadInBackground();
                        if (cashdata != null && cashdata.getCount() > 0) {
                            if (cashdata.moveToFirst()) {
                                do {
                                    new_cash = Integer.parseInt(cashdata.getString(1));
                                } while (cashdata.moveToNext());
                            }
                        }
                        cash_cursor.cancelLoad();

                        ContentValues transaction = new ContentValues();
                        transaction.put(trans.AMOUNT, amount_value);
                        transaction.put(trans.COMMISSION, commission_value);
                        transaction.put(trans.TYPE, type);
                        transaction.put(trans.BANK_NAME, bank_name);
                        transaction.put(trans.SERVICE, service_name);
                        transaction.put(trans.CASH_AT_BANK, new_total_bank_balance);
                        transaction.put(trans.CASH_IN_HAND, new_cash);
                        transaction.put(trans.TIME, time);

                        Uri newUri = getContentResolver().insert(trans.CONTENT_TRANS_URI, transaction);

                        // Show a toast message depending on whether or not the insertion was successful.
                        if (newUri == null) {
                            // If the new content URI is null, then there was an error with insertion.
                            Toast.makeText(getApplicationContext(), "Failed" ,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the insertion was successful and we can display a toast.
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            amount.getText().clear();
                            commisiion.getText().clear();
                            bank.setSelection(0);
                            service.setSelection(0);
                        }
                    }
                }else if (amount_value >= 1 && bank_name.equals("SELECT BANK") && !service_name.equals("SELECT SERVICE"))
                {
                    String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
                    String[] projection = {trans.ACCOUNT_NAMES, trans.ACCOUNT_BALANCE};
                    String where = trans.ACCOUNT_NAMES + "=?";
                    String[] whereArgs = new String[]{"CASH IN HAND"};

                    CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), trans.CONTENT_BALANCE_URI, projection, where, whereArgs, null);
                    Cursor getbalance = cursorLoader.loadInBackground();
                    if (getbalance != null && getbalance.getCount() > 0) {
                        if (getbalance.moveToFirst()) {
                            do {
                                present_balance = Integer.parseInt(getbalance.getString(1));
                            } while (getbalance.moveToNext());
                        }
                    }
                    cursorLoader.cancelLoad();
                    Log.d("", "Present Balance " + bank_name + " : " + present_balance);
                    if (present_balance > amount_value) {
                        new_balance = present_balance - amount_value;
                        String[] tables = {trans.ACCOUNT_NAMES, trans.ACCOUNT_BALANCE};
                        String from = trans.ACCOUNT_NAMES + "=?";
                        String[] values = new String[]{"CASH AT BANK"};

                        CursorLoader cursor = new CursorLoader(getApplicationContext(), trans.CONTENT_BALANCE_URI, tables, from, values, null);
                        Cursor getdata = cursor.loadInBackground();
                        if (getdata != null && getdata.getCount() > 0) {
                            if (getdata.moveToFirst()) {
                                do {
                                    total_bank_balance = Integer.parseInt(getdata.getString(1));
                                } while (getdata.moveToNext());
                            }
                        }
                        cursor.cancelLoad();

                        ContentValues update_values = new ContentValues();
                        update_values.put(trans.ACCOUNT_BALANCE, new_balance);

                        int rowsAffected = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_values, where, whereArgs);

                        if (rowsAffected == 0) {
                            // If no rows were affected, then there was an error with the update.
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the update was successful and we can display a toast.
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        }


                        ContentValues transaction = new ContentValues();
                        transaction.put(trans.AMOUNT, amount_value);
                        transaction.put(trans.COMMISSION, commission_value);
                        transaction.put(trans.TYPE, type);
                        transaction.put(trans.BANK_NAME, "NONE");
                        transaction.put(trans.SERVICE, service_name);
                        transaction.put(trans.CASH_AT_BANK, total_bank_balance);
                        transaction.put(trans.CASH_IN_HAND, new_balance);
                        transaction.put(trans.TIME, time);

                        Uri newUri = getContentResolver().insert(trans.CONTENT_TRANS_URI, transaction);

                        // Show a toast message depending on whether or not the insertion was successful.
                        if (newUri == null) {
                            // If the new content URI is null, then there was an error with insertion.
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the insertion was successful and we can display a toast.
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            amount.getText().clear();
                            commisiion.getText().clear();
                            bank.setSelection(0);
                            service.setSelection(0);
                        }
                    }
                }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_button) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else if (id == R.id.add_service_button){
            Intent intent = new Intent(getApplicationContext(), add_bank.class);
            startActivity(intent);
        }else if (id == R.id.delete_service_button){
            return true;
        }else if (id == R.id.add_bank_option){
            Intent intent = new Intent(getApplicationContext(), add_bank.class);
            startActivity(intent);
        }else if (id == R.id.import_export){
            Intent intent = new Intent(getApplicationContext(), import_export.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
