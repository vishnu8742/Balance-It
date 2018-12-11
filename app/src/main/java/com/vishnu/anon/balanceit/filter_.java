package com.vishnu.anon.balanceit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

public class filter_ extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button filter;
    Spinner bank, type;
    EditText fromdate, todate;
    String bank_name, type_name, from_date, to_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        bank = findViewById(R.id.bank_spinner);
        type = findViewById(R.id.type_spinner);

        getSupportActionBar().setTitle("Filter");
        filter = findViewById(R.id.filterbutton);

        fromdate = findViewById(R.id.fromdate);
        todate = findViewById(R.id.todate);

        loadBanks();
        loadTypes();

        bank.setOnItemSelectedListener(this);
        type.setOnItemSelectedListener(this);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from_date = fromdate.getText().toString().trim();
                to_date = todate.getText().toString().trim();

                if (bank_name.equals("SELECT BANK")) {
                    bank_name = null;
                }
                if (type_name.equals("SELECT TYPE")) {
                    type_name = null;
                }

                if (from_date.isEmpty() || to_date.isEmpty()) {
                    from_date = null;
                    to_date = null;
                }

                Intent intent = new Intent(getApplicationContext(), trans_list.class);
                intent.putExtra("bank_name", bank_name);
                intent.putExtra("type_name", type_name);
                intent.putExtra("from_date", from_date);
                intent.putExtra("to_date", to_date);
                startActivity(intent);
            }
        });
    }

    private void loadBanks() {
        List<String> banks = new ArrayList<>();

        String[] projection = {
                db_contract.trans.BANK_NAMES
        };

        // This loader will execute the ContentProvider's query method on a background thread
        CursorLoader cursor = new CursorLoader(this,   // Parent activity context
                db_contract.trans.CONTENT_BANK_URI,   // Provider content URI to query
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
            ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, banks);
            //Type of spinner
            bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            bank.setAdapter(bankAdapter);

        }else{
            Toast.makeText(getApplicationContext(), "No Banks Added Yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTypes(){
        List<String> types = new ArrayList<>();
        types.add("SELECT TYPE");
        types.add("deposit");
        types.add("Bank Added");
        types.add("withdraw");
        types.add("payment");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        //Type of spinner
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        type.setAdapter(typeAdapter);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner type_spinner = (Spinner)parent;
        Spinner bank_spinner = (Spinner)parent;

        if (bank_spinner.getId() == R.id.bank_spinner ){
            TextView tv = (TextView)view;
            bank_name = tv.getText().toString();
        }else if (type_spinner.getId() == R.id.type_spinner){
            TextView sv = (TextView)view;
            type_name = sv.getText().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
