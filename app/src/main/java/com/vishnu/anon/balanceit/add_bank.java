package com.vishnu.anon.balanceit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.vishnu.anon.balanceit.data.db_contract.trans;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class add_bank extends AppCompatActivity {
    Button submit;
    EditText bank_name, amount;
    EditText service_name;
    RelativeLayout layout;
    int total_balance, total_cash;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank);
        bank_name = findViewById(R.id.bank_name);
        service_name = findViewById(R.id.service_name);
        amount = findViewById(R.id.initamount);
        layout = (RelativeLayout) findViewById(R.id.relative_layout);
        submit = findViewById(R.id.bank_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String bank = bank_name.getText().toString().trim();
              String service = service_name.getText().toString().trim();
              int initamount = Integer.parseInt(amount.getText().toString().trim());
              if (initamount < 0){
                  initamount = 0;
              }
              if(bank.isEmpty() && service.isEmpty()){
                  Toast.makeText(getApplicationContext(), "Dont Treat Me Like A Stupid", Toast.LENGTH_LONG).show();
              }else if(bank.isEmpty() && !service.isEmpty()){
                 AddService(service);
              }else if (!bank.isEmpty() && service.isEmpty()){
                  AddBank(bank);
                  AddBankBalance(bank, initamount);
              }else if (!bank.isEmpty() && !service.isEmpty()){
                 AddBank(bank);
                 AddBankBalance(bank, initamount);
                 AddService(service);
              }

            }
        });
    }

    private void AddBank(String bank){
        String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
        bank = bank.toUpperCase();
        ContentValues values = new ContentValues();
        values.put(trans.BANK_NAMES, bank);
        values.put(trans.TIME, time);
        Uri newUri = getContentResolver().insert(trans.CONTENT_BANK_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getApplicationContext(), "Failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(getApplicationContext(),"Success",
                    Toast.LENGTH_SHORT).show();
            bank_name.getText().clear();
            amount.setText("0");
            service_name.getText().clear();
        }
    }

    private void AddBankBalance(String bank, int initamount){
        String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
        bank = bank.toUpperCase();
        ContentValues values = new ContentValues();
        values.put(trans.ACCOUNT_NAMES, bank);
        values.put(trans.ACCOUNT_BALANCE, initamount);
        values.put(trans.TIME, time);
        Uri newUri = getContentResolver().insert(trans.CONTENT_BALANCE_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getApplicationContext(), "Failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(getApplicationContext(),"Success",
                    Toast.LENGTH_SHORT).show();
            bank_name.getText().clear();
            amount.setText("0");
            service_name.getText().clear();
        }

        String[] tables = {
                trans.ACCOUNT_NAMES,
                trans.ACCOUNT_BALANCE
        };
        String from = trans.ACCOUNT_NAMES + "=?";
        String[] bank_amount = new String[] {"CASH AT BANK"};

        CursorLoader cursor =  new CursorLoader(getApplicationContext(),
                trans.CONTENT_BALANCE_URI,
                tables,
                from,
                bank_amount,
                null);
        Cursor getdata = cursor.loadInBackground();
        if (getdata != null && getdata.getCount() > 0) {
            if (getdata.moveToFirst()) {
                do {
                    total_balance = Integer.parseInt(getdata.getString(1));
                } while (getdata.moveToNext());
            }
        }
        cursor.cancelLoad();
        int total_in_bank = initamount + total_balance;

        String[] cash_tables = {
                trans.ACCOUNT_NAMES,
                trans.ACCOUNT_BALANCE
        };
        String cash_from = trans.ACCOUNT_NAMES + "=?";
        String[] cash_args = new String[] {"CASH IN HAND"};

        CursorLoader cursor2 =  new CursorLoader(getApplicationContext(),
                trans.CONTENT_BALANCE_URI,
                tables,
                from,
                bank_amount,
                null);
        Cursor getcash = cursor2.loadInBackground();
        if (getcash != null && getcash.getCount() > 0) {
            if (getcash.moveToFirst()) {
                do {
                    total_cash = Integer.parseInt(getcash.getString(1));
                } while (getcash.moveToNext());
            }
        }
        cursor2.cancelLoad();



        ContentValues update_values = new ContentValues();
        update_values.put(trans.ACCOUNT_BALANCE, total_in_bank);

        int rowsAffected = getContentResolver().update(trans.CONTENT_BALANCE_URI, update_values, from, bank_amount);

        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(getApplicationContext(), "Failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(getApplicationContext(), "Success",
                    Toast.LENGTH_SHORT).show();
        }

        ContentValues transaction = new ContentValues();
        transaction.put(trans.AMOUNT, initamount);
        transaction.put(trans.COMMISSION, "0");
        transaction.put(trans.TYPE, "Bank Added");
        transaction.put(trans.BANK_NAME, bank);
        transaction.put(trans.SERVICE, "None");
        transaction.put(trans.CASH_AT_BANK, total_in_bank);
        transaction.put(trans.CASH_IN_HAND, total_cash);
        transaction.put(trans.TIME, time);

        Uri transAdd = getContentResolver().insert(trans.CONTENT_TRANS_URI, transaction);

        // Show a toast message depending on whether or not the insertion was successful.
        if (transAdd == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getApplicationContext(), "Failed" ,
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(getApplicationContext(),"Success",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void AddService(String service){
        String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
        service = service.toUpperCase();
        ContentValues values = new ContentValues();
        values.put(trans.SECTIONS, service);
        values.put(trans.TIME, time);
        Uri newUri = getContentResolver().insert(trans.CONTENT_SERV_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getApplicationContext(), "Failed" ,
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(getApplicationContext(),"Success",
                    Toast.LENGTH_SHORT).show();
            bank_name.getText().clear();
            amount.setText("0");
            service_name.getText().clear();
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
            return true;
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
}
