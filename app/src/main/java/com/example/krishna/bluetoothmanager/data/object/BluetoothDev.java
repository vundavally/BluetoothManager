package com.example.krishna.bluetoothmanager.data.object;

/**
 * Created by Krishna on 10/13/2015.
 */
public class BluetoothDev {
    private String deviceAddress;
    private String deviceName;
    private int deviceType;

    public BluetoothDev(String deviceAddress, String deviceName, int deviceType) {
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BluetoothDev that = (BluetoothDev) o;

        return deviceAddress.equals(that.deviceAddress);

    }

    @Override
    public int hashCode() {
        return deviceAddress.hashCode();
    }
}
