package com.example.krishna.bluetoothmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.krishna.bluetoothmanager.data.BluetoothDBContract.BluetoothMediaPairEntry;

/**
 * Created by Krishna on 10/13/2015.
 */
public class BluetoothDBHelper extends SQLiteOpenHelper{

    private static final int DTABASE_VERSION = 2;

    public static final String DATABASE_NAME = "bluetoothkv.db";

    public BluetoothDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DTABASE_VERSION);
    }

    //TODO ADD ALL THE FIELDS IN CONTRACT
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_BLUETOOTH_MEDIA_PAIR =
                "CREATE TABLE " + BluetoothMediaPairEntry.TABLE_NAME + " ("
                + BluetoothMediaPairEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_ADDRESS + " TEXT UNIQUE NOT NULL, "
                + BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_NAME + " TEXT NOT NULL, "
                + BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_TYPE + " INTEGER NOT NULL, "
                + BluetoothMediaPairEntry.COLUMN_MEDIA_PLAYER_NAME + " TEXT NOT NULL, "
                + BluetoothMediaPairEntry.COLUMN_MEDIA_PLAYER_PACKAGE_NAME + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_BLUETOOTH_MEDIA_PAIR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "BLUETOOTH_MEDIA_PAIR");
    }
}
