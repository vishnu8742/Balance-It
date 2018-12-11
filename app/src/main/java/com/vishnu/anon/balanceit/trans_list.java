package com.vishnu.anon.balanceit;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.vishnu.anon.balanceit.data.db_contract;

public class trans_list extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int transact_list = 101;
    String bank_name, type_name, from_date, to_date;
    transAdapter transactionAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_list);
        getIncomingIntent();
        getLoaderManager().initLoader(transact_list, null, this);
        ListView translist = (ListView) findViewById(R.id.trans_list);
        transactionAdapter = new transAdapter(this, null);
        translist.setAdapter(transactionAdapter);
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("bank_name")) {
            bank_name =  getIntent().getStringExtra("bank_name");
        }else{
            bank_name = "";
        }
        if(getIntent().hasExtra("type_name")) {
            type_name =  getIntent().getStringExtra("type_name");
        }else{
            type_name = "";
        }
        if(getIntent().hasExtra("from_date")) {
            from_date =  getIntent().getStringExtra("from_date");
        }else{
            from_date = "";
        }
        if(getIntent().hasExtra("to_date")) {
            to_date =  getIntent().getStringExtra("to_date");
        }else{
            to_date = "";
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
//        String where = db_contract.trans.TYPE + "=? AND "+ db_contract.trans.BANK_NAME + "=? AND " + db_contract.trans.TIME+ ">=? AND "+ db_contract.trans.TIME + "<=?";
//        String[] whereArgs = new String[]{type_name, bank_name, from_date, to_date};

        // This loader will execute the ContentProvider's query method on a background thread
        return new android.content.CursorLoader(this,   // Parent activity context
                db_contract.trans.CONTENT_TRANS_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        transactionAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        transactionAdapter.swapCursor(null);
    }
}
