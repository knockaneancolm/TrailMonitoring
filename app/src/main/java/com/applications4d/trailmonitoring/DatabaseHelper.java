package com.applications4d.trailmonitoring;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.net.URI;

/**
 * Created by colmforde on 4/9/18.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

//    public static final String DATABASE_NAME = "Trails.db";
    public static final String TAG = "DatabaseHelper";

    public static final String TABLE_NAME = "problems_table";
    public static final String COL1= "ID";
    public static final String COL2= "County";
    public static final String COL3= "TrailName";
    public static final String COL4= "MonitorID";
    public static final String COL5= "ProblemType";
    public static final String COL6= "ProblemDetail";
    public static final String COL7= "LatLng";
    public static final String COL8= "Comment";
    public static final String COL9= "Photo";
    public static final String COL10= "DateFound";



    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//             String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 +" TEXT, " + COL3 +" TEXT, " + COL4 +" TEXT, "
//                + COL5 +" TEXT, " + COL6 +" TEXT, " + COL7 +" TEXT, " + COL8 +" TEXT, " + COL9 +" TEXT)";

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 +" TEXT, " + COL3 +" TEXT, " + COL4 +" TEXT, " + COL5 +" TEXT, " + COL6 +" TEXT, " + COL7 +" TEXT, " + COL8 +" TEXT, " + COL9 +" TEXT, " + COL10 +" TEXT)";

        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData (String county, String trailName, String monitorID, String problemType, String problemDetail, String latLng, String comment, byte[] photoAsBytes, String formattedDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, county);
        contentValues.put(COL3, trailName);
        contentValues.put(COL4, monitorID);
        contentValues.put(COL5, problemType);
        contentValues.put(COL6, problemDetail);
        contentValues.put(COL7, latLng);
        contentValues.put(COL8, comment);
        contentValues.put(COL9, photoAsBytes);
        contentValues.put(COL10, formattedDate);



        // Log.d(TAG, "addData: Adding " + county + " "+ trailName + " " + monitorID + " " + photo + "to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //negative 1 if not inserted corrrectly
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM "+ TABLE_NAME + " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query,null);
        return data;
    }



    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

    public void deleteRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + id + " from database.");
        db.execSQL(query);
    }

}
