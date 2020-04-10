package com.falconssoft.onlinetechsupport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String BD_NAME = "TECH_DB";
    private static int BD_VERSION = 2;
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
    private static final String USERS_TYPE = "TYPE";
    private static final String USERS_STATE = "STATE";

    //******************************************************************
    private static final String CUSTOMERS_TABLE = "CUSTOMERS_TABLE";

    private static final String CUSTOMERS_NAME = "CUSTOMER";
    private static final String CUSTOMERS_COMPANY = "COMPANY";
    private static final String CUSTOMERS_SYSTEM = "SYSTEM";
    private static final String CUSTOMERS_PHONE = "PHONE";
    private static final String CUSTOMERS_CHICK_IN = "CHECK_IN";
    private static final String CUSTOMERS_STATE = "STATE";
    private static final String CUSTOMERS_ENG_NAME = "ENG_NAME";
    private static final String CUSTOMERS_ENG_ID = "ENG_ID";

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
                + USERS_TYPE + " INTEGER,"
                + USERS_STATE + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_USERS);

        String CREATE_TABLE_CUSTOMERS = "CREATE TABLE " + CUSTOMERS_TABLE + "("
                + CUSTOMERS_NAME + " TEXT,"
                + CUSTOMERS_COMPANY + " TEXT,"
                + CUSTOMERS_SYSTEM + " TEXT,"
                + CUSTOMERS_PHONE + " INTEGER,"
                + CUSTOMERS_CHICK_IN + " TEXT,"
                + CUSTOMERS_STATE + " INTEGER,"
                + CUSTOMERS_ENG_NAME + " TEXT,"
                + CUSTOMERS_ENG_ID + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CUSTOMERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        try {
//            db.execSQL("ALTER TABLE SETTINGS_TABLE ADD " + SETTINGS_USER_NO + " TEXT");
//        }catch (Exception e){
//            Log.e("upgrade", "USER NO");
//        }
        try {
        String CREATE_TABLE_SETTINGS = "CREATE TABLE " + SETTINGS_TABLE + "("
                + SETTINGS_ID + " TEXT,"
                + SETTINGS_IP_ADDRESS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_SETTINGS);
        }catch (Exception e){
            Log.e("upgrade", "USER NO");
        }
    }

    //*************************************** ADD **********************************

    public void addLoginInfo(List<EngineerInfo> info) {
        database = this.getReadableDatabase();
        ContentValues contentValues;
        for (int i = 0; i<info.size(); i++) {
             contentValues = new ContentValues();

            contentValues.put(USERS_ID, info.get(i).getId());
            contentValues.put(USERS_NAME, info.get(i).getName());
            contentValues.put(USERS_PASSWORD, info.get(i).getPassword());
            contentValues.put(USERS_STATE, info.get(i).getState());
            contentValues.put(USERS_TYPE, info.get(i).getEng_type());

            database.insert(USERS_TABLE, null, contentValues);
        }
        database.close();
    }


    public void addIPSetting(String ip) {
        database = this.getReadableDatabase();
        ContentValues contentValues;

            contentValues = new ContentValues();

            contentValues.put(SETTINGS_IP_ADDRESS, ip);


            database.insert(SETTINGS_TABLE, null, contentValues);

        database.close();
    }


    //*************************************** GET **********************************

    public List<EngineerInfo> getLoginData() {
        List<EngineerInfo> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + USERS_TABLE;
        database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                EngineerInfo info = new EngineerInfo();
                info.setId(cursor.getString(0));
                info.setName(cursor.getString(1));
                info.setPassword(cursor.getString(2));
                info.setEng_type(cursor.getInt(3));

                list.add(info);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public String getIp() {
       String list = "";
        String selectQuery = "SELECT IP_ADDRESS FROM " + SETTINGS_TABLE;
        database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                list=cursor.getString(0);



            } while (cursor.moveToNext());
        }
        return list;
    }

    //*************************************** UPDATE **********************************

//    public void updateSettingsUserNo(String userNo) {
//        db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(SETTINGS_USER_NO, userNo);
//        db.update(SETTINGS_TABLE, values, null, null);
//    }

    //*************************************** DELETE **********************************

    public void deleteLoginData() {
        database = this.getWritableDatabase();
        database.delete(USERS_TABLE, null, null);
        database.close();
    }

    public void deleteIpData() {
        database = this.getWritableDatabase();
        database.delete(SETTINGS_TABLE, null, null);
        database.close();
    }

}
