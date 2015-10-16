package com.example.krishna.bluetoothmanager.data.object;

import android.graphics.drawable.Drawable;

/**
 * Created by Krishna on 10/11/2015.
 */
public class MusicPlayer {
    private String playerName;
    private String packageName;
    private Drawable playerIcon;
    private int playerVolume;

    public MusicPlayer(String playerName, String packageName, Drawable playerIcon) {
        this.playerName = playerName;
        this.packageName = packageName;
        this.playerIcon = playerIcon;
        // Initializing default volume
        this.playerVolume = 5;
    }

    /**
     * Initialize music player.
     * This will return a null Drawable
     * @param playerName
     * @param packageName
     * @param playerVolume
     */
    public MusicPlayer(String playerName, String packageName, int playerVolume) {
        this.playerName = playerName;
        this.packageName = packageName;
        this.playerVolume = playerVolume;
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

    public Drawable getPlayerIcon() {
        return playerIcon;
    }

    public void setPlayerIcon(Drawable playerIcon) {
        this.playerIcon = playerIcon;
    }

    public int getPlayerVolume() {
        return playerVolume;
    }

    public void setPlayerVolume(int playerVolume) {
        this.playerVolume = playerVolume;
    }

    @Override
    public String toString() {
        return playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicPlayer that = (MusicPlayer) o;

        if (!playerName.equals(that.playerName)) return false;
        return packageName.equals(that.packageName);

    }

    @Override
    public int hashCode() {
        int result = playerName.hashCode();
        result = 31 * result + packageName.hashCode();
        return result;
    }
}
