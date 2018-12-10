package freshloic.fr.freshcalculator;

import android.provider.BaseColumns;

final class FeedReaderTask {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderTask() {}

    /* Inner class that defines the table contents */
    static class FeedEntry implements BaseColumns {
        static final String TABLE_NAME = "Task";
        static final String COLUMN_NAME_TITLE = "TaskName";
    }
}