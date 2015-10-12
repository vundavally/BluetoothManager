package com.example.krishna.bluetoothmanager.data.object;

import android.graphics.drawable.Drawable;

/**
 * Created by Krishna on 10/11/2015.
 */
public class MusicPlayer {
    private String playerName;
    private String packageName;
    private Drawable playerIcon;

    public MusicPlayer(String playerName, String packageName, Drawable playerIcon) {
        this.playerName = playerName;
        this.packageName = packageName;
        this.playerIcon = playerIcon;
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

    @Override
    public String toString() {
        return playerName;
    }
}
