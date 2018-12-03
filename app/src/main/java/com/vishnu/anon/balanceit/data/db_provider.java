package com.vishnu.anon.balanceit.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class db_provider extends ContentProvider {
//    Content Authority
    public static final String CONTENT_AUTHORITY = "com.vishnu.anon.balanceit.bank";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String BALANCE_URI = "balance_uri";

    public static final String BANK_URI = "bank_uri";

    public static final String SERVICE_URI = "service_uri";

    public static final String TRANSACTION_URI = "transaction_uri";


    //URI For Getting all the TRANSACTIONS
    private static final int TRANSACTIONS = 1001;

    //URI For Getting all the BALANCES
    private static final int BALANCES = 1002;

    //URI For Getting all the BANKS LISTED
    private static final int BANKS = 1003;

    //URI For Getting all the SERVICES LISTED
    private static final int SERVICES = 1004;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String LOG_TAG = "DB_provider";

    static {

        sUriMatcher.addURI(CONTENT_AUTHORITY, TRANSACTION_URI, TRANSACTIONS);

        sUriMatcher.addURI(CONTENT_AUTHORITY, BALANCE_URI, BALANCES);

        sUriMatcher.addURI(CONTENT_AUTHORITY, BANK_URI, BANKS);

        sUriMatcher.addURI(CONTENT_AUTHORITY, SERVICE_URI, SERVICES);
    }

    private db_helper DBhelper;

    public static final String CONTENT_BALANCE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + BALANCE_URI;

    public static final String CONTENT_BANKS =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + BANK_URI;

    public static final String CONTENT_SERVICES =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + SERVICE_URI;

    public static final String CONTENT_TRANSACTIONS =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TRANSACTION_URI;
    @Override
    public boolean onCreate() {
        DBhelper = new db_helper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = DBhelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BALANCES:
                cursor = database.query(db_contract.trans.TABLE_BALANCE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TRANSACTIONS:
                // Cursor containing that row of the table.
                cursor = database.query(db_contract.trans.TABLE_NAME_TRANS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BANKS:
                // Cursor containing that row of the table.
                cursor = database.query(db_contract.trans.TABLE_BANK, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case SERVICES:
                // Cursor containing that row of the table.
                cursor = database.query(db_contract.trans.TABLE_SECTION, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TRANSACTIONS:
                return CONTENT_TRANSACTIONS;
            case BALANCES:
                return CONTENT_BALANCE;
            case BANKS:
                return CONTENT_BANKS;
            case SERVICES:
                return CONTENT_SERVICES;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    @Override
    public Uri insert( Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TRANSACTIONS:
                return insertTrans(uri, values);
            case BALANCES:
                return insertBal(uri, values);
            case BANKS:
                return insertBanks(uri, values);
            case SERVICES:
                return insertServ(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertTrans(Uri uri, ContentValues values) {
        return uri;
    }

    private Uri insertBal(Uri uri, ContentValues values) {

        Log.d("InsertBal", "Insert: " + uri);
        String name = values.getAsString(db_contract.trans.ACCOUNT_NAMES);
        if (name == null) {
            throw new IllegalArgumentException("Name Must Man");
        }
        SQLiteDatabase database = DBhelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(db_contract.trans.TABLE_BALANCE, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertBanks(Uri uri, ContentValues values) {
        Log.d("insertBanks", "insertBanks: " + uri);
        String name = values.getAsString(db_contract.trans.BANK_NAMES);
        if (name == null) {
            throw new IllegalArgumentException("Name Must Man");
        }
        SQLiteDatabase database = DBhelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(db_contract.trans.TABLE_BANK, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertServ(Uri uri, ContentValues values) {
        Log.d("InsertServices", "insertServices: " + uri);
        String name = values.getAsString(db_contract.trans.SECTIONS);
        if (name == null) {
            throw new IllegalArgumentException("Name Must Man");
        }
        SQLiteDatabase database = DBhelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(db_contract.trans.TABLE_SECTION, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(Uri uri,  String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri,  ContentValues values, String selection,  String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TRANSACTIONS:
                return updateBal(uri, values, selection, selectionArgs);
            case BALANCES:
                return updateBal(uri, values, selection, selectionArgs);
            case BANKS:
                return updateBal(uri, values, selection, selectionArgs);
            case SERVICES:
                return updateBal(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private int updateBal(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = DBhelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(db_contract.trans.TABLE_BALANCE, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
