package com.example.krishna.bluetoothmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krishna.bluetoothmanager.data.object.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kv026205 on 10/12/2015.
 */
public class MusicPlayerAdapter extends ArrayAdapter<MusicPlayer>{

    private List<MusicPlayer> musicPlayers;

    public MusicPlayerAdapter(Context context, int resource, List<MusicPlayer> musicPlayers) {
        super(context, resource, musicPlayers);
        this.musicPlayers = musicPlayers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        MusicPlayer musicPlayer = musicPlayers.get(position);

        ImageView musicPlayerIcon = (ImageView)view.findViewById(R.id.dropdown_icon);
        if(musicPlayer.getPlayerIcon() != null) {
            musicPlayerIcon.setImageDrawable(musicPlayer.getPlayerIcon());
        }

        TextView musicPlayerName = (TextView)view.findViewById(R.id.dropdown_name);
        musicPlayerName.setText(musicPlayer.toString());

        return  view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        MusicPlayer musicPlayer = musicPlayers.get(position);

        ImageView musicPlayerIcon = (ImageView)view.findViewById(R.id.dropdown_item_icon);
        if(musicPlayer.getPlayerIcon() != null) {
            musicPlayerIcon.setImageDrawable(musicPlayer.getPlayerIcon());
        }

        TextView musicPlayerName = (TextView)view.findViewById(R.id.dropdown_item_name);
        musicPlayerName.setText(musicPlayer.toString());

        return  view;
    }
}
