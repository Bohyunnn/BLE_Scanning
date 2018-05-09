package com.example.hansung.ifindthanq.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class SQLiteDBHelperDao extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "BLE";
    // Contacts table name
    private static final String TABLE_BLE = "item_main";

    // Contacts Table Columns names
    private static final String KEY_SEQ = "seq";
    private static final String KEY_ALBUMIMAGE = "albumImage";
    private static final String KEY_BLEIMAGE = "bleImage";
    private static final String KEY_MACS = "macs";
    private static final String KEY_BLENAME = "bleName";

    public SQLiteDBHelperDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BLE = "CREATE TABLE " + TABLE_BLE + "(" + KEY_SEQ + " INTEGER PRIMARY KEY," + KEY_ALBUMIMAGE + " VARCHAR, " + KEY_BLEIMAGE + " INTEGER, " + KEY_MACS + " VARCHAR, " + KEY_BLENAME + " VARCHAR) ";
        db.execSQL(CREATE_TABLE_BLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLE);
        onCreate(db);
    }

    public ProblemConfigurationVo getConfigurations( int seq) {
        ProblemConfigurationVo contact = new ProblemConfigurationVo();
        String selectQuery = "SELECT  * FROM " +TABLE_BLE + " where "+KEY_SEQ +"="+seq;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                contact.setSeq(Integer.parseInt(cursor.getString(0)));
                contact.setAlbumImage(cursor.getString(1));
                contact.setBleImage(cursor.getInt(2));
                contact.setMacs(cursor.getString(3));
                contact.setBleName(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        if (db != null) {
            db.close();
        }
        return contact;
    }

    public ArrayList<ProblemConfigurationVo> getConfigurationsAll() {
        ArrayList<ProblemConfigurationVo> contactList = new ArrayList<ProblemConfigurationVo>();

        String selectQuery = "SELECT  * FROM " + TABLE_BLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProblemConfigurationVo contact = new ProblemConfigurationVo();
                contact.setSeq(Integer.parseInt(cursor.getString(0)));
                contact.setAlbumImage(cursor.getString(1));
                contact.setBleImage(cursor.getInt(2));
                contact.setMacs(cursor.getString(3));
                contact.setBleName(cursor.getString(4));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        if (db != null) {
            db.close();
        }
        return contactList;
    }


    public void addConfiguration(ProblemConfigurationVo problemConfigurationVo) {
        SQLiteDatabase db = this.getReadableDatabase();
        addConfiguration(db, problemConfigurationVo);
        db.close(); // Closing database connection
    }

    public void addConfiguration( SQLiteDatabase db, ProblemConfigurationVo problemConfigurationVo) {
        ContentValues values = new ContentValues();

        values.put(KEY_ALBUMIMAGE, problemConfigurationVo.getAlbumImage());
        values.put(KEY_BLEIMAGE, problemConfigurationVo.getBleImage());
        values.put(KEY_MACS, problemConfigurationVo.getMacs());
        values.put(KEY_BLENAME, problemConfigurationVo.getBleName());

        db.insert(TABLE_BLE, null, values);
    }
}