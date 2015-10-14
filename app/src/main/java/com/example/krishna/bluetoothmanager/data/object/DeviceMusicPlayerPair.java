package com.example.krishna.bluetoothmanager.data.object;

/**
 * Created by Krishna on 10/11/2015.
 */
public class DeviceMusicPlayerPair {
    private BluetoothDev bluetoothDev;
    private MusicPlayer musicPlayer;

    public DeviceMusicPlayerPair(BluetoothDev bluetoothDev, MusicPlayer musicPlayer) {
        this.bluetoothDev = bluetoothDev;
        this.musicPlayer = musicPlayer;
    }

    public BluetoothDev getBluetoothDevice() {
        return bluetoothDev;
    }

    public void setBluetoothDevice(BluetoothDev bluetoothDev) {
        this.bluetoothDev = bluetoothDev;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }
}
