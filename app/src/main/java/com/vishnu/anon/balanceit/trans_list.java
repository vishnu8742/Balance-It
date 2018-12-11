package com.vishnu.anon.balanceit;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.vishnu.anon.balanceit.data.db_contract;

import java.util.Arrays;

public class trans_list extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int transact_list = 101;
    String bank_name, type_name, from_date, to_date, where;
    String[] whereArgs;
    transAdapter transactionAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_list);
        getIncomingIntent();
        ListView translist = (ListView) findViewById(R.id.trans_list);
        transactionAdapter = new transAdapter(this, null);
        translist.setAdapter(transactionAdapter);
        getLoaderManager().initLoader(transact_list, null, this);
    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra("bank_name")) {

            bank_name = getIntent().getStringExtra("bank_name");

        }else {

            bank_name = "";

        }

        if (getIntent().hasExtra("type_name")) {

            type_name = getIntent().getStringExtra("type_name");

        }else {

            type_name = "";

        }

        if (getIntent().hasExtra("from_date") && getIntent().hasExtra("to_date")) {

            from_date = getIntent().getStringExtra("from_date");

            to_date = getIntent().getStringExtra("to_date");

        }else {
            from_date = "";
            to_date = "";
        }


        if(!type_name.isEmpty() && !bank_name.isEmpty() && !from_date.isEmpty() && !to_date.isEmpty()) {

            from_date = from_date + " 00-00-00"; to_date = to_date + " 23-59-59";

            where = db_contract.trans.TYPE + "=? AND " + db_contract.trans.BANK_NAME + "=? AND " + db_contract.trans.TIME+ ">=? AND " + db_contract.trans.TIME + "<=?";

            whereArgs = new String[]{type_name, bank_name, from_date, to_date};

        }else if(!type_name.isEmpty() && !bank_name.isEmpty() && from_date.isEmpty() && to_date.isEmpty()) {

            where = db_contract.trans.TYPE + "=? AND " + db_contract.trans.BANK_NAME + "=?";

            whereArgs = new String[]{type_name, bank_name};


        }else if(!type_name.isEmpty() && bank_name.isEmpty() && from_date.isEmpty() && to_date.isEmpty()) {

            where = db_contract.trans.TYPE + "=?";

            whereArgs = new String[]{type_name};

        }else if(type_name.isEmpty() && !bank_name.isEmpty() && from_date.isEmpty() && to_date.isEmpty()) {

            where = db_contract.trans.BANK_NAME + " =?";

            whereArgs = new String[]{String.valueOf(bank_name)};

        }else if (type_name.isEmpty() && bank_name.isEmpty() && !from_date.isEmpty() && !to_date.isEmpty()){

            from_date = from_date + " 00-00-00"; to_date = to_date + " 23-59-59";

            where = db_contract.trans.TIME + ">=? AND " + db_contract.trans.TIME + "<=?";

            whereArgs = new String[]{from_date, to_date};

        }else if(!type_name.isEmpty() && bank_name.isEmpty() && !from_date.isEmpty() && !to_date.isEmpty()){

            from_date = from_date + " 00-00-00"; to_date = to_date + " 23-59-59";

            where = db_contract.trans.TYPE + "=? AND "  + db_contract.trans.TIME+ ">=? AND " + db_contract.trans.TIME + "<=?";

            whereArgs = new String[]{type_name, from_date, to_date};


        } else if(type_name.isEmpty() && !bank_name.isEmpty() && !from_date.isEmpty() && !to_date.isEmpty()){

            from_date = from_date + " 00-00-00"; to_date = to_date + " 23-59-59";

            where = db_contract.trans.BANK_NAME + "=? AND "  + db_contract.trans.TIME+ ">=? AND " + db_contract.trans.TIME + "<=?";

            whereArgs = new String[]{bank_name, from_date, to_date};

        }else {
            where = null;

            whereArgs = null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter){
            Intent intent = new Intent(getApplicationContext(), filter_.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {db_contract.trans._ID, db_contract.trans.AMOUNT, db_contract.trans.COMMISSION,
                db_contract.trans.TYPE, db_contract.trans.BANK_NAME, db_contract.trans.SERVICE,
                db_contract.trans.CASH_IN_HAND, db_contract.trans.CASH_AT_BANK, db_contract.trans.TIME };


        Log.d("TAG", "onCreateLoader: " + where );
        // This loader will execute the ContentProvider's query method on a background thread
        return new android.content.CursorLoader(this,   // Parent activity context
                db_contract.trans.CONTENT_TRANS_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                where,                   // No selection clause
                whereArgs,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        transactionAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        transactionAdapter.swapCursor(null);
    }
}
