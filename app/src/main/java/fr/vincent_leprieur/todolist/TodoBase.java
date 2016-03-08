package fr.vincent_leprieur.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by phil on 24/02/15.
 */
public class TodoBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo.db";
    public static final String TABLE_NAME = "items";
    public static final String KEY_ID = "_id";
    public static final String KEY_LABEL = "label";
    private static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_LABEL + " TEXT);";

    TodoBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing here as there is only one version for now
    }

    // Méthode ajoutée lors de l'ajout de la classe AndroidDatabaseManager
    // (requis dans les étapes d'installation)
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
                String maxQuery = Query ;
                //execute the query results will be save in Cursor c
                Cursor c = sqlDB.rawQuery(maxQuery, null);


                //add value to cursor2
                Cursor2.addRow(new Object[] { "Success" });

                alc.set(1,Cursor2);
                if (null != c && c.getCount() > 0) {


                        alc.set(0,c);
                        c.moveToFirst();

                        return alc ;
                }
                return alc;
        } catch(SQLException sqlEx){
                Log.d("printing exception", sqlEx.getMessage());
                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
                alc.set(1,Cursor2);
                return alc;
        } catch(Exception ex){

                Log.d("printing exception", ex.getMessage());

                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+ex.getMessage() });
                alc.set(1,Cursor2);
                return alc;
        }
    }

    public static SQLiteDatabase getDB(Activity activity) {
        return (new TodoBase(activity)).getWritableDatabase();
    }

    public static void addItem(Activity activity, String s) {
        ContentValues cont = new ContentValues();
        cont.put(KEY_LABEL, s);
        SQLiteDatabase db = getDB(activity);
        db.insert(TABLE_NAME, null, cont);
        db.close();
    }

    public static Cursor fetchAllItems(Activity activity, boolean sorted) {
        SQLiteDatabase db = getDB(activity);
        String sortLabel = (sorted)? KEY_LABEL + " ASC" : null;
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_LABEL}, null, null, null, null, sortLabel);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
}
