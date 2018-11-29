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

public class db_provider extends ContentProvider {
//    Content Authority
    public static final String CONTENT_AUTHORITY = "com.vishnu.anon.balanceit.bank";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String BALANCE = "balance";

    //URI For Getting all the TRANSACTIONS
    private static final int TRANSACTIONS = 1001;

    //URI For Getting all the BALANCES
    private static final int BALANCES = 1002;

    //URI For Getting all the BANKS LISTED
    private static final int BANKS = 1003;

    //URI For Getting all the SERVICES LISTED
    private static final int SERVICES = 1004;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(CONTENT_AUTHORITY, BALANCE, TRANSACTIONS);

        sUriMatcher.addURI(CONTENT_AUTHORITY, BALANCE, BALANCES);

        sUriMatcher.addURI(CONTENT_AUTHORITY, BALANCE, BANKS);

        sUriMatcher.addURI(CONTENT_AUTHORITY, BALANCE, SERVICES);
    }

    private db_helper DBhelper;

    public static final String CONTENT_BALANCE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + BALANCE;
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
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TRANSACTIONS:
                return db_contract.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    @Override
    public Uri insert( Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri,  String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri,  ContentValues values, String selection,  String[] selectionArgs) {
        return 0;
    }
}
