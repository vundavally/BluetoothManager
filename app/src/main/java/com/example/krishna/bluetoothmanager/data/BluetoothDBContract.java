package com.example.krishna.bluetoothmanager.data;

import android.provider.BaseColumns;

/**
 * Created by Krishna on 10/13/2015.
 */
public class BluetoothDBContract {

    public static final class BluetoothMediaPairEntry implements BaseColumns{
        public static final String TABLE_NAME = "bluetooth_media_pair";
        public static final String COLUMN_BLUETOOTH_DEVICE_ADDRESS = "bluetooth_device_address";
        public static final String COLUMN_BLUETOOTH_DEVICE_NAME = "bluetooth_device_name";
        public static final String COLUMN_BLUETOOTH_DEVICE_TYPE = "bluetooth_device_type";
        public static final String COLUMN_MEDIA_PLAYER_NAME = "media_player_name";
        public static final String COLUMN_MEDIA_PLAYER_PACKAGE_NAME = "media_player_package_name";
    }
}
