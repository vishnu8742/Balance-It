package com.vishnu.anon.balanceit;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.vishnu.anon.balanceit.data.db_contract.trans;

import java.util.ArrayList;
import java.util.List;

public class transaction extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button deposit, withdraw;
    EditText amount, commisiion;
    Spinner service, bank;
    private String bank_name, service_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction);
        loadBanks();
        loadServices();
        getSupportActionBar().setTitle("Add Transaction");
        deposit = (Button) findViewById(R.id.deposit_button);
        withdraw = (Button) findViewById(R.id.withdraw_button);
        bank.setOnItemSelectedListener(this);
        service.setOnItemSelectedListener(this);
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.getText().toString().trim();
                commisiion.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "CLICKED ON DEPOSIT", Toast.LENGTH_SHORT).show();
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
        if(c.moveToFirst())
        {
            do {
                banks.add(c.getString(0));
            }while (c.moveToNext());
        }

        if(banks.isEmpty()){
            Toast.makeText(this, "No Banks Listed", Toast.LENGTH_SHORT).show();
        }else {
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, banks);
            //Type of spinner
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            bank.setAdapter(dataAdapter);
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
        if(c.moveToFirst())
        {
            do {
                servs.add(c.getString(0));
            }while (c.moveToNext());
        }

        if(servs.isEmpty()){
            Toast.makeText(this, "No Cats Available", Toast.LENGTH_SHORT).show();
        }else {

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, servs);
            //Type of spinner
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            service.setAdapter(dataAdapter);
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
