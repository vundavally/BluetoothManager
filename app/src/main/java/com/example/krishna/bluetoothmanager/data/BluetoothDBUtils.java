package com.example.krishna.bluetoothmanager.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.krishna.bluetoothmanager.data.BluetoothDBContract.BluetoothMediaPairEntry;

import com.example.krishna.bluetoothmanager.data.object.DeviceMusicPlayerPair;

/**
 * Created by Krishna on 10/13/2015.
 */
public class BluetoothDBUtils {

    public static long insertBluetoothMediaPair(BluetoothDBHelper dbHelper, DeviceMusicPlayerPair deviceMusicPlayerPair)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_ADDRESS,
                deviceMusicPlayerPair.getBluetoothDevice().getDeviceAddress());
        values.put(BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_NAME,
                deviceMusicPlayerPair.getBluetoothDevice().getDeviceName());
        values.put(BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_TYPE,
                deviceMusicPlayerPair.getBluetoothDevice().getDeviceType());
        values.put(BluetoothMediaPairEntry.COLUMN_MEDIA_PLAYER_NAME,
                deviceMusicPlayerPair.getMusicPlayer().getPlayerName());
        values.put(BluetoothMediaPairEntry.COLUMN_MEDIA_PLAYER_PACKAGE_NAME,
                deviceMusicPlayerPair.getMusicPlayer().getPackageName());
        values.put(BluetoothMediaPairEntry.COLUMN_MEDIA_PLAYER_VOLUME,
                deviceMusicPlayerPair.getMusicPlayer().getPlayerVolume());

        long newRowId = db.insert(BluetoothMediaPairEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    public static Cursor getBluetoothMediaPairs(BluetoothDBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(BluetoothMediaPairEntry.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public static Cursor getMediaPlayerByDeviceAddress(BluetoothDBHelper dbHelper, String bluetoothDeviceAddress)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String whereClause = BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_ADDRESS + " LIKE ?";
        String[] whereClauseArgs = { bluetoothDeviceAddress };
        Cursor cursor = db.query(BluetoothMediaPairEntry.TABLE_NAME, null, whereClause, whereClauseArgs, null, null, null);
        return cursor;
    }

    public static int deleteBluetoothMediaPair(BluetoothDBHelper dbHelper, String bluetoothDeviceAddress)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_ADDRESS + " LIKE ?";
        String[] whereClauseArgs = { bluetoothDeviceAddress };
        int numberOfRowsDeleted = db.delete(BluetoothMediaPairEntry.TABLE_NAME, whereClause, whereClauseArgs);
        return numberOfRowsDeleted;
    }

    public static int updateBluetoothMediaPair(BluetoothDBHelper dbHelper, DeviceMusicPlayerPair deviceMusicPlayerPair)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BluetoothMediaPairEntry.COLUMN_MEDIA_PLAYER_NAME,
                deviceMusicPlayerPair.getMusicPlayer().getPlayerName());
        values.put(BluetoothMediaPairEntry.COLUMN_MEDIA_PLAYER_PACKAGE_NAME,
                deviceMusicPlayerPair.getMusicPlayer().getPackageName());
        values.put(BluetoothMediaPairEntry.COLUMN_MEDIA_PLAYER_VOLUME,
                deviceMusicPlayerPair.getMusicPlayer().getPlayerVolume());

        String whereClause = BluetoothMediaPairEntry.COLUMN_BLUETOOTH_DEVICE_ADDRESS + " LIKE ?";
        String[] whereClauseArgs = { deviceMusicPlayerPair.getBluetoothDevice().getDeviceAddress() };

        int numberOfRowsAffected = db.update(BluetoothMediaPairEntry.TABLE_NAME, values, whereClause, whereClauseArgs);
        return numberOfRowsAffected;
    }
}
