package com.example.krishna.bluetoothmanager;

import android.bluetooth.BluetoothClass;

/**
 * Created by kv026205 on 10/29/2015.
 */
public class Utility {
    public static int getBluetoothDeviceIconResource(int deviceType)
    {
        switch (deviceType)
        {
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                return R.mipmap.ic_car;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                return R.mipmap.ic_headset;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                return R.mipmap.ic_watch;
            default:
                // fallback icon
                return R.mipmap.ic_headset;
        }
    }
}
