package freshloic.fr.freshcalculator;

import android.provider.BaseColumns;

final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    static class FeedEntry implements BaseColumns {
        static final String TABLE_NAME = "Calculs";
        static final String COLUMN_NAME_TITLE = "expression";
        static final String COLUMN_NAME_SUBTITLE = "resultat";
        static final String COLUMN_NAME_DATE = "dateAjout";
        static final String COLUMN_NAME_CATEGORY = "category";
    }
}