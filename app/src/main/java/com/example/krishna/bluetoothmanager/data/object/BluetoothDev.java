package com.example.krishna.bluetoothmanager.data.object;

/**
 * Created by Krishna on 10/13/2015.
 */
public class BluetoothDev {
    private String deviceName;
    private int deviceType;

    public BluetoothDev(String deviceName, int deviceType) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
