package com.example.krishna.bluetoothmanager;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krishna.bluetoothmanager.R;
import com.example.krishna.bluetoothmanager.data.object.BluetoothDev;
import com.example.krishna.bluetoothmanager.data.object.MusicPlayer;

import java.util.List;

/**
 * Created by Krishna on 10/13/2015.
 */
public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDev>{
    private List<BluetoothDev> bluetoothDevices;

    public BluetoothDeviceAdapter(Context context, int resource, List<BluetoothDev> bluetoothDevices) {
        super(context, resource, bluetoothDevices);
        this.bluetoothDevices = bluetoothDevices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        BluetoothDev bluetoothDevice = bluetoothDevices.get(position);

        ImageView bluetoothDeviceIcon = (ImageView)view.findViewById(R.id.dropdown_icon);
        switch (bluetoothDevice.getDeviceType())
        {
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_car);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_headset);
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_watch);
                break;
        }

        TextView bluetoothDeviceName = (TextView)view.findViewById(R.id.dropdown_name);
        bluetoothDeviceName.setText(bluetoothDevice.getDeviceName());

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

        BluetoothDev bluetoothDevice = bluetoothDevices.get(position);

        ImageView bluetoothDeviceIcon = (ImageView)view.findViewById(R.id.dropdown_item_icon);
        switch (bluetoothDevice.getDeviceType())
        {
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_car);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_headset);
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_watch);
                break;
        }

        TextView bluetoothDeviceName = (TextView)view.findViewById(R.id.dropdown_item_name);
        bluetoothDeviceName.setText(bluetoothDevice.getDeviceName());

        return  view;
    }
}
