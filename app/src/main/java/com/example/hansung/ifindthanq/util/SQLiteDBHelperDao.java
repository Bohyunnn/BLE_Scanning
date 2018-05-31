package com.example.hansung.ifindthanq.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hansung.ifindthanq.mapBLE.MapLocation;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


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

    // Contacts table name
    private static final String TABLE_LOC = "item_loc";

    // Location Table Columns names
    private static final String KEY_SEQ_LOC = "seq";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LON = "longitude";
    private static final String KEY_TIME = "time";
    private static final String KEY_NAME = "name";

    // Setting Table name
    private static final String TABLE_SET = "item_setting";

    // Setting Table Columns names
    private static final String KEY_SEQ_SET = "seq";
    private static final String KEY_METER = "meter";

    private AtomicInteger openCounter = new AtomicInteger();

    public SQLiteDBHelperDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BLE = "CREATE TABLE " + TABLE_BLE + "(" + KEY_SEQ + " INTEGER PRIMARY KEY," + KEY_ALBUMIMAGE + " VARCHAR, " + KEY_BLEIMAGE + " INTEGER, " + KEY_MACS + " VARCHAR, " + KEY_BLENAME + " VARCHAR) ";
        String CREATE_TABLE_LOC = "CREATE TABLE " + TABLE_LOC + "(" + KEY_SEQ_LOC + " INTEGER PRIMARY KEY," + KEY_NAME + " VARCHAR, " + KEY_LAT + " VARCHAR, " + KEY_LON + " VARCHAR, " + KEY_TIME + " VARCHAR) ";
        String CREATE_TABLE_SET = "CREATE TABLE " + TABLE_SET + "(" + KEY_SEQ_SET + " INTEGER PRIMARY KEY," + KEY_METER + " INTEGER) ";
        db.execSQL(CREATE_TABLE_BLE);
        db.execSQL(CREATE_TABLE_LOC);
        db.execSQL(CREATE_TABLE_SET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SET);
        onCreate(db);
    }

    public ProblemConfigurationVo getConfigurations(int seq) {
        ProblemConfigurationVo contact = new ProblemConfigurationVo();
        String selectQuery = "SELECT  * FROM " + TABLE_BLE + " where " + KEY_SEQ + "=" + seq;

        SQLiteDatabase db = openDb();
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
            closeDb(db);
        }
        return contact;
    }

    public ArrayList<ProblemConfigurationVo> getConfigurationsAll() {
        ArrayList<ProblemConfigurationVo> contactList = new ArrayList<ProblemConfigurationVo>();

        String selectQuery = "SELECT  * FROM " + TABLE_BLE;

        SQLiteDatabase db = openDb();
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
            closeDb(db);
        }
        return contactList;
    }

    public ArrayList<MapLocation> getConfigurationsLocAll(SQLiteDatabase db) {
        ArrayList<MapLocation> contactList = new ArrayList<MapLocation>();

        String selectQuery = "SELECT  * FROM " + TABLE_LOC;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MapLocation location = new MapLocation();
                location.setBle_name(cursor.getString(1));
                location.setLatitude(Double.parseDouble(cursor.getString(2)));
                location.setLongitude(Double.parseDouble(cursor.getString(3)));
                location.setTime(cursor.getString(4));
                contactList.add(location);
            } while (cursor.moveToNext());
        }
//        if (db != null) {
//            closeDb(db);
//        }
        return contactList;
    }

    public ArrayList<MapLocation> getConfigurationsLocAll() {
        ArrayList<MapLocation> contactList = new ArrayList<MapLocation>();

        String selectQuery = "SELECT  * FROM " + TABLE_LOC;

        SQLiteDatabase db = openDb();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MapLocation location = new MapLocation();
                location.setBle_name(cursor.getString(1));
                location.setLatitude(Double.parseDouble(cursor.getString(2)));
                location.setLongitude(Double.parseDouble(cursor.getString(3)));
                location.setTime(cursor.getString(4));
                contactList.add(location);
            } while (cursor.moveToNext());
        }
        if (db != null) {
            closeDb(db);
        }
        return contactList;
    }

    public int getConfigurationsMeter() {
        int meter = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_SET;

        SQLiteDatabase db = openDb();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                meter = cursor.getInt(1);
            } while (cursor.moveToNext());
        }
        if (db != null) {
            closeDb(db);
        }
        return meter;
    }

    public int getConfigurationsMeter(SQLiteDatabase db) {
        int meter = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_SET;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                meter = cursor.getInt(1);
            } while (cursor.moveToNext());
        }
        if (db != null) {
            closeDb(db);
        }
        return meter;
    }

    public void addConfiguration(ProblemConfigurationVo problemConfigurationVo) {
        SQLiteDatabase db = openDb();
        addConfiguration(db, problemConfigurationVo);
        closeDb(db);
    }

    public void addConfiguration(SQLiteDatabase db, ProblemConfigurationVo problemConfigurationVo) {
        ContentValues values = new ContentValues();

        values.put(KEY_ALBUMIMAGE, problemConfigurationVo.getAlbumImage());
        values.put(KEY_BLEIMAGE, problemConfigurationVo.getBleImage());
        values.put(KEY_MACS, problemConfigurationVo.getMacs());
        values.put(KEY_BLENAME, problemConfigurationVo.getBleName());

        db.insert(TABLE_BLE, null, values);
    }

    public void addConfigurationLoc(MapLocation location) {
        SQLiteDatabase db = openDb();
        addConfigurationLoc(db, location);
        closeDb(db);
    }

    //    위치 데이터 insert, update
    public void addConfigurationLoc(SQLiteDatabase db, MapLocation location) {
//      데이터중 추가하려는 ble_name을 가진 row가 있는지 여부
        boolean isEquals = false;

        ArrayList<MapLocation> list = new ArrayList<>();
        list = getConfigurationsLocAll(db);
        if (list.size() != 0) {
//          update 해야하는지 검사
            for (int i = 0; i < list.size(); i++) {
                if (location.getBle_name().equals(list.get(i).getBle_name())) {
                    isEquals = true;
                }
            }

            if (isEquals) {
                ContentValues values = new ContentValues();

                values.put(KEY_LAT, location.getLatitude());
                values.put(KEY_LON, location.getLongitude());
                values.put(KEY_TIME, location.getTime());

                db.update(TABLE_LOC, values, "name=?", new String[]{location.getBle_name()});
            } else {
                ContentValues values = new ContentValues();

                values.put(KEY_NAME, location.getBle_name());
                values.put(KEY_LAT, location.getLatitude());
                values.put(KEY_LON, location.getLongitude());
                values.put(KEY_TIME, location.getTime());

                db.insert(TABLE_LOC, null, values);
            }
        } else { //데이터를 처음 넣을경우
            ContentValues values = new ContentValues();

            values.put(KEY_NAME, location.getBle_name());
            values.put(KEY_LAT, location.getLatitude());
            values.put(KEY_LON, location.getLongitude());
            values.put(KEY_TIME, location.getTime());

            db.insert(TABLE_LOC, null, values);
        }
    }

    public void addConfigurationMeter(int meter) {
        SQLiteDatabase db = this.getWritableDatabase();
        addConfigurationMeter(db, meter);
        closeDb(db);
    }

    //    미터 데이터 insert, update
    public void addConfigurationMeter(SQLiteDatabase db, int meter) {

        int meter_store = getConfigurationsMeter(db);

        if (meter_store!=0) {
            ContentValues values = new ContentValues();

            values.put(KEY_METER, meter);

            db.update(TABLE_SET, values, "seq=?", new String[]{"1"});
        } else { //데이터를 처음 넣을경우
            ContentValues values = new ContentValues();

            values.put(KEY_METER, meter);

            db.insert(TABLE_SET, null, values);
        }
    }

    public synchronized SQLiteDatabase openDb(){
        SQLiteDatabase db = null;
        if(openCounter.incrementAndGet()==1){
            db = this.getWritableDatabase();
        }
        return db;
    }

    public synchronized void closeDb(SQLiteDatabase db){
        if(openCounter.decrementAndGet()==0){
            db.close();
        }
    }

    //등록된 블루투스 삭제 기능
    public void deleteConfiguration(String bleName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_BLE+" WHERE bleName='" + bleName + "';");
        db.execSQL("DELETE FROM "+TABLE_LOC+" WHERE name='" + bleName + "';");
        db.close();
    }
}