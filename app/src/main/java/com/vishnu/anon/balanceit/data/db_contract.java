package com.vishnu.anon.balanceit.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class db_contract implements BaseColumns {

    private db_contract() {}

    public static final class trans implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_BANK_URI = Uri.withAppendedPath(db_provider.BASE_CONTENT_URI, db_provider.BANK_URI);

        public static final Uri CONTENT_SERV_URI = Uri.withAppendedPath(db_provider.BASE_CONTENT_URI, db_provider.SERVICE_URI);

        public static final Uri CONTENT_BALANCE_URI = Uri.withAppendedPath(db_provider.BASE_CONTENT_URI, db_provider.BALANCE_URI);

        public static final Uri CONTENT_TRANS_URI = Uri.withAppendedPath(db_provider.BASE_CONTENT_URI, db_provider.TRANSACTION_URI);

        public final static String TABLE_NAME_TRANS = "transactions";

        public final static String _ID = BaseColumns._ID;

        public final static String AMOUNT ="amount";

        public final static String COMMISSION = "commission";

        public final static String TYPE = "type";

        public final static String SERVICE = "service";

        public final static String BANK_NAME = "bank_name";

        public final static String TIME = "time";

        public final static String TABLE_SECTION = "services";

        public final static String SECTIONS = "names";

        public final static String TABLE_BALANCE = "balances";

        public final static String CASH_IN_HAND = "cash_in_hand";

        public final static String CASH_AT_BANK = "cash_at_bank";

        public final static String TABLE_BANK = "banks";

        public final static String BANK_NAMES = "names";


    }
}
