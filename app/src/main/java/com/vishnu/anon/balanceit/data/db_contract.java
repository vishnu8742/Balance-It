package com.vishnu.anon.balanceit.data;

import android.provider.BaseColumns;

public class db_contract implements BaseColumns {

    private db_contract() {}

    public static final class trans implements BaseColumns {

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

        public final static String UPDATED = "updates_at";

        public final static String TABLE_BANK = "banks";

        public final static String BANK_NAMES = "names";

        public final static String BANK_UPDATED = "updated_at";

    }
}
