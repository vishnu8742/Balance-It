package com.vishnu.anon.balanceit;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;

import com.vishnu.anon.balanceit.data.db_contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    BanksAdapter banksAdapter;

    private static final int bankloader = 1001;

    boolean added = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBanks();
        CheckServs();
        CheckAccounts();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), transaction.class);
                startActivity(intent);
            }
        });

        ListView bankslist = (ListView) findViewById(R.id.list_view);
        banksAdapter = new BanksAdapter(this, null);
        bankslist.setAdapter(banksAdapter);


    }
    private void CheckBanks(){
        Log.d("CheckBanks", "CheckBanks: ");
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

        Cursor cursor1 = cursor.loadInBackground();
        if (cursor1 == null || cursor1.getCount() < 1){
            Log.d("", "add bank ");
            AddBank();
        }
        cursor1.close();
    }

    private void CheckServs(){
        Log.d("Servs", "CheckServs: ");
        String[] projection = {
                db_contract.trans.SECTIONS
        };

        // This loader will execute the ContentProvider's query method on a background thread
        CursorLoader cursor = new CursorLoader(this,   // Parent activity context
                db_contract.trans.CONTENT_SERV_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);

        Cursor cursor2 = cursor.loadInBackground();
        if (cursor2 == null || cursor2.getCount() < 1){
            Log.d("", "add serv");
            AddService();
        }
        cursor2.close();
    }
    private void CheckAccounts(){
        Log.d("Check ACCS", "ACCS: ");
        String[] projection = {
                db_contract.trans.ACCOUNT_NAMES
        };

        // This loader will execute the ContentProvider's query method on a background thread
        CursorLoader cursor = new CursorLoader(this,   // Parent activity context
                db_contract.trans.CONTENT_BALANCE_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);

        Cursor cursor3 = cursor.loadInBackground();
        if (cursor3 == null || cursor3.getCount() < 1){
            Log.d("", "add bank ");
            AddAccount();
        }else {
            added = true;
            initLoader();
        }
        cursor3.close();
    }

    private void AddAccount(){
        Log.d("", "Add Account: ");
        String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
        List<String> list = new ArrayList<String>();
        list.add("CASH AT BANK");
        list.add("CASH IN HAND");
        List<Integer> bal = new ArrayList<>();
        bal.add(0);
        bal.add(0);
        List<String> times = new ArrayList<>();
        times.add(time);
        times.add(time);
        for (int i = 0; i <= 1; i++){
            ContentValues values = new ContentValues();
            values.put(db_contract.trans.ACCOUNT_NAMES, list.get(i));
            values.put(db_contract.trans.ACCOUNT_BALANCE, bal.get(i));
            values.put(db_contract.trans.TIME, times.get(i));

            Uri newUri = getContentResolver().insert(db_contract.trans.CONTENT_BALANCE_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(getApplicationContext(), "Default Sections Added", Toast.LENGTH_SHORT).show();
                added = true;
                initLoader();
            }
        }
    }

    private void AddBank(){
        Log.d("", "AddBank: ");
        String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
        ContentValues values = new ContentValues();
        values.put(db_contract.trans.BANK_NAMES, "SELECT BANK");
        values.put(db_contract.trans.TIME, time);
        Uri newUri = getContentResolver().insert(db_contract.trans.CONTENT_BANK_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getApplicationContext(), "Failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(getApplicationContext(),"Default Sections Added",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void AddService(){
        Log.d("", "AddService: ");
        String time = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss").format(new Date());
        ContentValues values = new ContentValues();
        values.put(db_contract.trans.SECTIONS, "SELECT SERVICE");
        values.put(db_contract.trans.TIME, time);
        Uri newUri = getContentResolver().insert(db_contract.trans.CONTENT_SERV_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getApplicationContext(), "Failed" ,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initLoader(){
        if (added){
            getLoaderManager().initLoader(bankloader, null, this);
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


    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                db_contract.trans._ID,
                db_contract.trans.ACCOUNT_NAMES,
                db_contract.trans.ACCOUNT_BALANCE
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                db_contract.trans.CONTENT_BALANCE_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor cursor) {
        banksAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset( Loader<Cursor> loader) {
        banksAdapter.swapCursor(null);
    }
}
