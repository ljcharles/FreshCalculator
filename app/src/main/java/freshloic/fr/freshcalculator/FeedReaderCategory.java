package freshloic.fr.freshcalculator;

import android.provider.BaseColumns;

final class FeedReaderCategory {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderCategory() {}

    /* Inner class that defines the table contents */
    static class FeedEntry implements BaseColumns {
        static final String TABLE_NAME = "Category";
        static final String COLUMN_NAME_TITLE = "title";
    }
}