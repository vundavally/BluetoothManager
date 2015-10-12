package com.example.krishna.bluetoothmanager.data.object;

/**
 * Created by Krishna on 10/11/2015.
 */
public class MusicPlayer {
    private String playerName;
    private String packageName;

    public MusicPlayer(String playerName, String packageName) {
        this.playerName = playerName;
        this.packageName = packageName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return playerName;
    }
}
