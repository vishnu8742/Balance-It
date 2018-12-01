package com.vishnu.anon.balanceit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class db_helper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "balance_it.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public db_helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the  table
        String BANK_TRANSACTIONS_TABLE =  "CREATE TABLE " + db_contract.trans.TABLE_NAME_TRANS + " ("
                + db_contract.trans._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + db_contract.trans.AMOUNT + " INTEGER NOT NULL, "
                + db_contract.trans.COMMISSION + " INTEGER NOT NULL, "
                + db_contract.trans.TYPE + " TEXT NOT NULL, "
                + db_contract.trans.BANK_NAME + " TEXT NOT NULL, "
                + db_contract.trans.SERVICE + " TEXT NOT NULL, "
                + db_contract.trans.TIME + " TEXT NOT NULL);";

        // Create a String that contains the SQL statement to create the  table
        String BANK_SERVICES_TABLE =  "CREATE TABLE " + db_contract.trans.TABLE_SECTION + " ("
                + db_contract.trans._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + db_contract.trans.SECTIONS + " TEXT NOT NULL, "
                + db_contract.trans.TIME + " TEXT NOT NULL);";

        // Create a String that contains the SQL statement to create the  table
        String BANKS_TABLE =  "CREATE TABLE " + db_contract.trans.TABLE_BANK + " ("
                + db_contract.trans._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + db_contract.trans.BANK_NAMES + " TEXT NOT NULL, "
                + db_contract.trans.TIME + " TEXT NOT NULL);";

        // Create a String that contains the SQL statement to create the  table
        String BANK_BALANCE =  "CREATE TABLE " + db_contract.trans.TABLE_BALANCE + " ("
                + db_contract.trans._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + db_contract.trans.ACCOUNT_NAMES + " TEXT NOT NULL, "
                + db_contract.trans.ACCOUNT_BALANCE + " INTEGER NOT NULL, "
                + db_contract.trans.TIME + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(BANK_BALANCE);
        db.execSQL(BANK_SERVICES_TABLE);
        db.execSQL(BANK_TRANSACTIONS_TABLE);
        db.execSQL(BANKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
