package com.falconssoft.onlinetechsupport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String BD_NAME = "TECH_DB";
    private static int BD_VERSION = 1;
    private static SQLiteDatabase database;

    //******************************************************************
    private static final String SETTINGS_TABLE = "SETTINGS_TABLE";

    private static final String SETTINGS_ID = "ID";
    private static final String SETTINGS_IP_ADDRESS = "IP_ADDRESS";

    //******************************************************************
    private static final String USERS_TABLE = "USERS_TABLE";

    private static final String USERS_ID = "ID";
    private static final String USERS_NAME = "NAME";
    private static final String USERS_PASSWORD = "PASSWORD";
    private static final String USERS_STATE = "STATE";
    private static final String USERS_TYPE = "TYPE";

    //******************************************************************
    private static final String CUSTOMERS_TABLE = "CUSTOMERS_TABLE";

    private static final String CUSTOMERS_NAME = "CUSTOMER";
    private static final String CUSTOMERS_COMPANY = "COMPANY";
    private static final String CUSTOMERS_SYSTEM = "SYSTEM";
    private static final String CUSTOMERS_PHONE = "PHONE";
    private static final String CUSTOMERS_CHICK_IN = "CHECK_IN";
    private static final String CUSTOMERS_STATE = "STATE";
    private static final String CUSTOMERS_ENG_NAME = "ENG_NAME";

    //******************************************************************

    public DatabaseHandler(@Nullable Context context) {
        super(context, BD_NAME, null, BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_SETTINGS = "CREATE TABLE " + SETTINGS_TABLE + "("
                + SETTINGS_ID + " TEXT,"
                + SETTINGS_IP_ADDRESS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_SETTINGS);

        String CREATE_TABLE_USERS = "CREATE TABLE " + USERS_TABLE + "("
                + USERS_ID + " TEXT,"
                + USERS_NAME + " TEXT,"
                + USERS_PASSWORD + " TEXT,"
                + USERS_STATE + " TEXT,"
                + USERS_TYPE + "INTEGER" + ")";
        db.execSQL(CREATE_TABLE_USERS);

        String CREATE_TABLE_CUSTOMERS = "CREATE TABLE " + CUSTOMERS_TABLE + "("
                + CUSTOMERS_NAME + " TEXT,"
                + CUSTOMERS_COMPANY + " TEXT,"
                + CUSTOMERS_SYSTEM + " TEXT,"
                + CUSTOMERS_PHONE + " INTEGER,"
                + CUSTOMERS_CHICK_IN + " TEXT,"
                + CUSTOMERS_STATE + " TEXT,"
                + CUSTOMERS_ENG_NAME + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE_CUSTOMERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        try {
//            db.execSQL("ALTER TABLE SETTINGS_TABLE ADD " + SETTINGS_USER_NO + " TEXT");
//        }catch (Exception e){
//            Log.e("upgrade", "USER NO");
//        }
    }

    //*************************************** ADD **********************************

//    public void addSettings(Settings settings) {
//        db = this.getReadableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(SETTINGS_COMPANY_NAME, settings.getCompanyName());
//        contentValues.put(SETTINGS_IP_ADDRESS, settings.getIpAddress());
//        contentValues.put(SETTINGS_STORE, settings.getStore());
//
////        SettingsFile.companyName = settings.getCompanyName();
////        SettingsFile.ipAddress = settings.getIpAddress();
////        SettingsFile.store = settings.getStore();
//
//        db.insert(SETTINGS_TABLE, null, contentValues);
//        db.close();
//    }

    //*************************************** GET **********************************

//    public Settings getSettings() {
//        Settings settings = new Settings();
//        String selectQuery = "SELECT * FROM " + SETTINGS_TABLE;
//        db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                settings.setCompanyName(cursor.getString(0));
//                settings.setIpAddress(cursor.getString(1));
//                settings.setStore(cursor.getString(2));
//                settings.setUserNo(cursor.getString(3));
//
////                SettingsFile.companyName = cursor.getString(0);
////                SettingsFile.ipAddress = cursor.getString(1);
////                SettingsFile.store = cursor.getString(2);
//            } while (cursor.moveToNext());
//        }
//        return settings;
//    }

    //*************************************** UPDATE **********************************

//    public void updateSettingsUserNo(String userNo) {
//        db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(SETTINGS_USER_NO, userNo);
//        db.update(SETTINGS_TABLE, values, null, null);
//    }

    //*************************************** DELETE **********************************

//    public void deleteSettings() {
//        db = this.getWritableDatabase();
//        db.delete(SETTINGS_TABLE, null, null);
//        db.close();
//    }

}
