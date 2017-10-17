package com.example.dmitro.chatapp.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dmitro on 12.10.17.
 */

public class ContractClass {


    public static final String AUTHORITY = "com.example.dmitro.chatapp.data.provider.ContractClass";

    private ContractClass() {
    }

    public static final class Messages implements BaseColumns {

        public static final String TABLE_NAME = "message";
        private static final String SCHEME = "content://";
        private static final String PATH_OVERVIEW = "/message";
        private static final String PATH_OVERVIEW_ID = "/message/";


        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_OVERVIEW);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_OVERVIEW_ID);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.overview";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.overview";

        public static final String COLUMN_NAME_LOGIN = "login";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_BODY = "body";
        public static final String COLUMN_NAME_TYPE = "type";


        public static final int MESSAGE_ID_PATH_POSITION = 1;


        public static final String[] DEFAULT_PROJECTION = new String[]{
                Messages._ID,
                Messages.COLUMN_NAME_LOGIN,
                Messages.COLUMN_NAME_TIME,
                Messages.COLUMN_NAME_BODY,
                Messages.COLUMN_NAME_TIME};


    }

}
