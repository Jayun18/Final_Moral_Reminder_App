package cs499_uab_capstone_project.moralreminders;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.InputStream;
import java.io.Serializable;

/*
 * This class basically handles the back end part of the app.
 * It store different quotes and also retrieves them when needed.
 */
public class Moral_Database extends SQLiteOpenHelper implements Serializable {

    public static final String DATABASE_NAME = "Moral_database";
    public static String DATABASE_FILE_PATH = "raw\\data.csv";
    private static InputStream data_stream;

    public Moral_Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Resources res = context.getResources();
    }

    /*
     * It creates an SQL that stores all the quotes, and displays them according to the mood clicked.
     */
    @Override
    public void onCreate(SQLiteDatabase myDatabase) {
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Happy(Message TEXT, Author TEXT)");
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Sad(Message TEXT, Author TEXT)");
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Angry(Message TEXT, Author TEXT)");
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Faith(Message TEXT, Author TEXT)");
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Lonely(Message TEXT, Author TEXT)");
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Love(Message TEXT, Author TEXT)");
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS SAVED(Message TEXT, Author TEXT)");
    }

    /*
     * This method pulls out a random message from database
     */
    public String[] getMessage(String mood, Boolean all) {
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        String[] message = new String[2];
        Cursor result = null;

        if (myDatabase != null) {
            if (all == true) {
                result = myDatabase.rawQuery("Select DISTINCT * from " + mood, null);
                result.moveToFirst();
                int length = result.getCount() * 2;
                message = new String[length];
                for (int i = 0; i < length; i++){
                    message[i] = result.getString(0);
                    i = i + 1;
                    message[i] = result.getString(1);
                    result.moveToNext();
                }
            }
            else {
                result = myDatabase.rawQuery("Select * from " + mood + " ORDER BY RANDOM() LIMIT 1", null);
                result.moveToFirst();
                message[0] = result.getString(0);
                message[1] = result.getString(1);
            }

        } else {
            message[0] = "Error!";
            message[1] = "Something broke!";
        }
        return message;
    }

    /*
     * It updates the database when the app is opened. It is mostly used when new quotes are added.
     */
    public void updateDatabase(String mood, String message, String author){
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        if (myDatabase != null){
            myDatabase.execSQL("INSERT OR REPLACE into " + mood + " VALUES('" + message +
            "', '" + author + "')");
        }
    }

    /*
     * It saves quotes that is liked by user or that is saved by user.
     */
    public boolean saveQuote(String message, String author){
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        if (myDatabase != null){
            myDatabase.execSQL("INSERT OR REPLACE into SAVED VALUES('" + message + "', '" + author + "')");
            return true;
        }
        else return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
