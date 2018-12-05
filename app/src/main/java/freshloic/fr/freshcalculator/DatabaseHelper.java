package freshloic.fr.freshcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " TEXT)"
            ;

    private static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + FeedReaderCategory.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderCategory.FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderCategory.FeedEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    "UNIQUE(" + FeedReaderCategory.FeedEntry.COLUMN_NAME_TITLE + ") ON CONFLICT replace)"
            ;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    private static final String SQL_DELETE_CATEGORIES =
            "DROP TABLE IF EXISTS " + FeedReaderCategory.FeedEntry.TABLE_NAME;

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Calculs.db";
    private static DatabaseHelper mInstance = null;

    DatabaseHelper(Context context){
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static DatabaseHelper getInstance(Context ctx) {
        return (mInstance == null) ? mInstance = new DatabaseHelper(ctx.getApplicationContext()) : mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORIES);
        ContentValues insertValues = new ContentValues();
        insertValues.put(FeedReaderCategory.FeedEntry.COLUMN_NAME_TITLE, "Defaut");
        sqLiteDatabase.insert(FeedReaderCategory.FeedEntry.TABLE_NAME, null, insertValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_CATEGORIES);
        onCreate(db);
//        String sql = "DROP TABLE IF EXISTS ";
//        sqLiteDatabase.execSQL(sql + DB_TABLE);
//        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    void insertData(String title, String subtitle, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, date);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY, "defaut");


         db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
         db.close();
    }

    boolean insertCategories(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderCategory.FeedEntry.COLUMN_NAME_TITLE, title);

        long result = db.insert(FeedReaderCategory.FeedEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    boolean deleteData(String title, Boolean deleteAll) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows;

        if (!deleteAll) {// Issue SQL statement.
            String selection =  FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {title};
            deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        }else {
            deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);
        }

        db.close();

        return deletedRows != -1;
    }

    /*Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + FeedReaderContract.FeedEntry._ID
                + " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME
                + " WHERE " + FeedReaderContract.FeedEntry._ID
                + " = '" + name + "'";
        return db.rawQuery(query,null);
    }*/

    boolean updateCategorieCalcul(String date, String updateTo) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection =  FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {date};
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY,updateTo);
        int deletedRows = db.update(FeedReaderContract.FeedEntry.TABLE_NAME,cv ,selection, selectionArgs);

        db.close();

        return deletedRows != -1;
    }

    Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dbQuery;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY
        };

        dbQuery = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        return dbQuery;
    }
//
    Cursor searchCalcul(String text){
        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "Select * from " +DB_TABLE + " Where " + NAME + " Like '%" + text + "%'";
//
//        return db.rawQuery(query,null);
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " = ?";
        String[] selectionArgs = { text };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedReaderContract.FeedEntry._ID + " DESC";

        return db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
    }

     String[] viewCategory(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT title FROM category", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("title")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[0]);
    }

/*    Cursor getLastIDCalcul() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + FeedReaderContract.FeedEntry._ID
                + " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME
                + " ORDER BY " + FeedReaderContract.FeedEntry._ID + " DESC LIMIT 1";
        return db.rawQuery(query,null);
    }*/
}
