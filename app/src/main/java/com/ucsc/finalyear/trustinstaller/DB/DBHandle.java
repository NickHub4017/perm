package com.ucsc.finalyear.trustinstaller.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ucsc.finalyear.trustinstaller.Util.Friend;

import java.util.ArrayList;

/**
 * Created by nrv on 1/21/17.
 */
public class DBHandle extends SQLiteOpenHelper {

    public DBHandle(Context context) {
        super(context, "trust.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String cr_plc = "CREATE TABLE metadata (" +
                "  frID INTEGER AUTO INCREMENT," +
                "  FrName VARCHAR(255) DEFAULT NULL," +
                "  FrPhone VARCHAR(10) NOT NULL" +
                ")";
        db.execSQL(cr_plc);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Friend> getAllFriends() { //Retrive all categories and mae arry of strings with results-- for set adapter in serach window
        SQLiteDatabase database = this.getReadableDatabase();

        String select_item_cat_Query = "SELECT * FROM metadata";
        Cursor cursor = database.rawQuery(select_item_cat_Query, null);
        ArrayList<Friend> Friendslist = new ArrayList<Friend>();
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                Friendslist.add(new Friend(cursor.getString(cursor.getColumnIndex("FrName")),cursor.getString(cursor.getColumnIndex("FrPhone"))));
                i++;
            } while (cursor.moveToNext());
        }
        return Friendslist;
    }


    public void insertNewData(Friend frdata) {//insert dummy data for debug purpose
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FrName", frdata.getName());
        values.put("FrPhone", frdata.getPhone());
        database.insert("metadata", null, values);
        values.clear();
    }

    public boolean deleteFriend(Friend fr)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        int val=database.delete("metadata", "FrPhone" + "='" + fr.getPhone()+"'", null);
        Log.e("Data"," --- "+val);
        return val>0;
    }
}
