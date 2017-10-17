package com.example.dmitro.chatapp.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by dmitro on 12.10.17.
 */

public class MessageProvider extends ContentProvider {
    private static final int DATABASE_VERSION = 1;
    private static HashMap<String, String> messageProjectionMap;


    private static final int MESSAGE = 1;
    private static final int MESSAGE_ID = 2;

    private static final UriMatcher uriMatcher;
    private DatabaseHelper dbHelper;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContractClass.AUTHORITY, "message", MESSAGE);
        uriMatcher.addURI(ContractClass.AUTHORITY, "message/#", MESSAGE_ID);


        messageProjectionMap = new HashMap<String, String>();
        for (int i = 0; i < ContractClass.Messages.DEFAULT_PROJECTION.length; i++) {
            messageProjectionMap.put(
                    ContractClass.Messages.DEFAULT_PROJECTION[i],
                    ContractClass.Messages.DEFAULT_PROJECTION[i]);
        }

    }


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case MESSAGE:
                qb.setTables(ContractClass.Messages.TABLE_NAME);
                qb.setProjectionMap(messageProjectionMap);
                break;
            case MESSAGE_ID:
                qb.setTables(ContractClass.Messages.TABLE_NAME);
                qb.setProjectionMap(messageProjectionMap);
                qb.appendWhere(ContractClass.Messages._ID + "=" + uri.getPathSegments().get(ContractClass.Messages.MESSAGE_ID_PATH_POSITION));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {
            case MESSAGE:
                return ContractClass.Messages.CONTENT_TYPE;
            case MESSAGE_ID:
                return ContractClass.Messages.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues message) {
        if (uriMatcher.match(uri) != MESSAGE) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;
        if (message != null) {
            values = new ContentValues(message);
        } else {
            values = new ContentValues();
        }
        long rowId = -1;
        Uri rowUri = Uri.EMPTY;

        if (uriMatcher.match(uri) == MESSAGE) {
            if (values.containsKey(ContractClass.Messages.COLUMN_NAME_IS_NEW_USER) == false) {
                values.put(ContractClass.Messages.COLUMN_NAME_IS_NEW_USER, "");
            }

            if (values.containsKey(ContractClass.Messages.COLUMN_NAME_LOGIN) == false) {
                values.put(ContractClass.Messages.COLUMN_NAME_LOGIN, "");
            }

            if (values.containsKey(ContractClass.Messages.COLUMN_NAME_MESSAGE) == false) {
                values.put(ContractClass.Messages.COLUMN_NAME_MESSAGE, "");
            }


            rowId = db.insert(ContractClass.Messages.TABLE_NAME, null, values);


            if (rowId > 0) {
                rowUri = ContentUris.withAppendedId(ContractClass.Messages.CONTENT_ID_URI_BASE, rowId);
                getContext().getContentResolver().notifyChange(rowUri, null);
            }
        }


        return rowUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String finalWhere;
        int count;
        switch (uriMatcher.match(uri)) {
            case MESSAGE:
                count = db.delete(ContractClass.Messages.TABLE_NAME, selection, selectionArgs);
                break;
            case MESSAGE_ID:
                finalWhere = ContractClass.Messages._ID + " = " + uri.getPathSegments().get(ContractClass.Messages.MESSAGE_ID_PATH_POSITION);
                if (selection != null) {
                    finalWhere = finalWhere + " AND " + selection;
                }
                count = db.delete(ContractClass.Messages.TABLE_NAME, finalWhere, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        String finalWhere;
        String id;
        switch (uriMatcher.match(uri)) {
            case MESSAGE:
                count = db.update(ContractClass.Messages.TABLE_NAME, values, where, whereArgs);
                break;
            case MESSAGE_ID:
                id = uri.getPathSegments().get(ContractClass.Messages.MESSAGE_ID_PATH_POSITION);
                finalWhere = ContractClass.Messages._ID + " = " + id;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(ContractClass.Messages.TABLE_NAME, values, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "chat_database";
        public static final String DATABASE_TABLE_MESSAGE = ContractClass.Messages.TABLE_NAME;
        public static final String KEY_ROWID = "_id";

        private static final String DATABASE_CREATE_TABLE_MESSAGE =
                "create table " + DATABASE_TABLE_MESSAGE + " ("
                        + KEY_ROWID + " integer primary key autoincrement, "
                        + ContractClass.Messages.COLUMN_NAME_LOGIN + " string , "
                        + ContractClass.Messages.COLUMN_NAME_TIME + " integer , "
                        + ContractClass.Messages.COLUMN_NAME_IS_NEW_USER + " string , "
                        + ContractClass.Messages.COLUMN_NAME_MESSAGE + " string , "
                        + ContractClass.Messages.COLUMN_NAME_FILE_URI + " string , "
                        + " UNIQUE ( " + KEY_ROWID + " ) ON CONFLICT IGNORE" + ");";


        private Context ctx;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            ctx = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_TABLE_MESSAGE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_MESSAGE);
            onCreate(db);
        }
    }

}
