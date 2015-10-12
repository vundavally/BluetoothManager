package com.example.krishna.bluetoothmanager.data.object;

/**
 * Created by Krishna on 10/11/2015.
 */
public class DeviceMusicPlayerPair {
    private String bluetoothDeviceName;
    private MusicPlayer musicPlayer;

    public DeviceMusicPlayerPair(String bluetoothDeviceName, MusicPlayer musicPlayer) {
        this.bluetoothDeviceName = bluetoothDeviceName;
        this.musicPlayer = musicPlayer;
    }

    public String getBluetoothDeviceName() {
        return bluetoothDeviceName;
    }

    public void setBluetoothDeviceName(String bluetoothDeviceName) {
        this.bluetoothDeviceName = bluetoothDeviceName;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }
}
