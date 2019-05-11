package id.ac.umn.projectuts_00000012802;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BookDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static String DB_PATH = "";
    private static final String DB_NAME = "books.db";
    private static final String TABLE_BOOK = "Book";
    private static final String COLUMN_BOOK_ASIN = "ASIN";
    private static final String COLUMN_BOOK_GROUP = "GROUP";
    private static final String COLUMN_BOOK_FORMAT = "FORMAT";
    private static final String COLUMN_BOOK_TITLE = "TITLE";
    private static final String COLUMN_BOOK_AUTHOR = "AUTHOR";
    private static final String COLUMN_BOOK_PUBLISHER = "PUBLISHER";
    private static final String COLUMN_BOOK_FAVORITE = "FAVORITE";

    private SQLiteDatabase db;
    private Context mContext;

    public BookDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_PATH = "/data/data/"+context.getPackageName()+"/databases/";

        mContext = context;
        openDatabase();
    }

//    START CONFIGURE DATABASE
    private boolean checkDataBase(){
        return mContext.getDatabasePath(DB_NAME).exists();
    }

    public void copyDatabase(){
        try{
            InputStream externalDBStream = mContext.getAssets().open(DB_NAME);
            OutputStream localDBStream = new FileOutputStream(mContext.getDatabasePath(DB_NAME));
            byte[] buffer = new byte[externalDBStream.available()];

            externalDBStream.read(buffer);
            localDBStream.write(buffer);
            localDBStream.flush();
            localDBStream.close();
            externalDBStream.close();
        }
        catch(IOException e){
            Log.e(this.getClass().toString(), "Copying database error");
        }
    }

    public void openDatabase(){
        String path = DB_PATH+DB_NAME;
        if(db == null){
            createDatabase();
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public void createDatabase(){
        boolean isDBExist = checkDataBase();
        if(!isDBExist){
            this.getReadableDatabase();
            copyDatabase();
        }
        else{
            Log.i(this.getClass().toString(), "Database already exist!");
        }
    }

//    UPDATE DATABASE BASED ON CONTENTVALUES AND ASIN
    public void updateDatabase(ContentValues cv, String asin){
        SQLiteDatabase tempdb = this.getWritableDatabase();
        tempdb.update(TABLE_BOOK, cv, COLUMN_BOOK_ASIN+" = ?", new String[] {asin});
    }

//    CLOSE DATABASE
    @Override
    public synchronized void close() {
        if(db != null)
            db.close();
        super.close();
    }
//END CONFIGURE DATABASE

//    GET ALL BOOK OBJECTS, EXCLUDING EMPTY AUTHOR AND EMPTY PUBLISHER, AND LIMIT 300 DATA TO REDUCE LAG
    public List<Book> getAllBooks(){
        List<Book> results = new ArrayList<>();
        SQLiteDatabase tempdb = this.getWritableDatabase();
        Cursor cursor;
        try{
            cursor = tempdb.query(
                    TABLE_BOOK, null,COLUMN_BOOK_AUTHOR+" != ? AND "+COLUMN_BOOK_PUBLISHER+" != ?", new String[]{"", ""},
                    null, null, null, "300"
            );
            if(cursor == null) return null;

            cursor.moveToFirst();
            do{
                Book book = new Book(
                    cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_ASIN)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_GROUP)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_FORMAT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_PUBLISHER)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_BOOK_FAVORITE))
                );
                results.add(book); // add to array list
            }while(cursor.moveToNext());
            cursor.close();
        }
        catch(Exception e){
            Log.e(this.getClass().toString(), "Failed Get Data!");
        }

        return results;
    }

//    GET 1 BOOK OBJECT BASED ON ASIN PARAMETER
    public Book getBook(String asin){
        Book result = new Book();
        SQLiteDatabase tempdb = this.getWritableDatabase();
        Cursor cursor;
        try{
            cursor = tempdb.query(
                    TABLE_BOOK, null,
                    COLUMN_BOOK_ASIN+"= ?", new String[] {asin},
                    null, null, null, null
            );
            if(cursor == null) return null;
            cursor.moveToFirst();
            result = new Book(
                cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_ASIN)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_GROUP)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_FORMAT)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_PUBLISHER)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_BOOK_FAVORITE))
            );
            cursor.close();
        }
        catch(Exception e){
            Log.e(this.getClass().toString(), "Failed Get Data!");
        }

        return result;
    }

//    GET FAVORITE BOOK OBJECTS
    public List<Book> getFavoriteBooks(){
        List<Book> results = new ArrayList<>();
        SQLiteDatabase tempdb = this.getWritableDatabase();
        Cursor cursor;
        try{
            cursor = tempdb.query(TABLE_BOOK, null,COLUMN_BOOK_FAVORITE+"= ?", new String[] {"1"}, null, null, null, "250");
            if(cursor == null) return null;

            cursor.moveToFirst();
            do{
                Book book = new Book(
                        cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_ASIN)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_GROUP)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_FORMAT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_AUTHOR)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_PUBLISHER)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_BOOK_FAVORITE))
                );
                results.add(book); // add to array list
            }while(cursor.moveToNext());
            cursor.close();
        }
        catch(Exception e){
            Log.e(this.getClass().toString(), "Failed Get Data!");
        }

        return results;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
