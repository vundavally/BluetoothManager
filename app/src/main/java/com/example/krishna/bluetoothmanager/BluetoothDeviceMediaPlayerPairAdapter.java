package com.example.krishna.bluetoothmanager;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Krishna on 10/13/2015.
 */
public class BluetoothDeviceMediaPlayerPairAdapter extends RecyclerView.Adapter<BluetoothDeviceMediaPlayerPairAdapter.ViewHolder> {
    public static final int COL_ID = 0;
    public static final int COL_BLUETOOTH_DEVICE_ADDRESS = 1;
    public static final int COL_BLUETOOTH_DEVICE_NAME = 2;
    public static final int COL_BLUETOOTH_DEVICE_TYPE = 3;
    public static final int COL_MEDIA_PLAYER_NAME = 4;
    public static final int COL_MEDIA_PLAYER_PACKAGE_NAME = 5;
    public static final int COL_MEDIA_PLAYER_VOLUME = 6;

    private Cursor mCursor;
    final private Context mContext;
    final BluetoothDeviceAdapterOnclickListener mClickHandler;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mBluetoothDeviceIcon;
        public final TextView mBluetoothDeviceName;
        public final ImageView mMediaDeviceIcon;
        public final TextView mMediaDeviceName;
        public final ImageView mVolumeIcon;
        public final ProgressBar mVolumeProgressBar;

        public ViewHolder(View view)
        {
            super(view);
            mBluetoothDeviceIcon = (ImageView)view.findViewById(R.id.list_item_bluetooth_device_icon);
            mBluetoothDeviceName = (TextView)view.findViewById(R.id.list_item_bluetooth_device_name);
            mMediaDeviceIcon = (ImageView) view.findViewById(R.id.list_item_media_player_icon);
            mMediaDeviceName = (TextView)view.findViewById(R.id.list_item_media_player_name);
            mVolumeIcon = (ImageView)view.findViewById(R.id.volume_icon_id);
            mVolumeProgressBar = (ProgressBar)view.findViewById(R.id.volume_progress_bar);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(this, mCursor);
        }
    }

    public static interface BluetoothDeviceAdapterOnclickListener {
        void onClick(ViewHolder viewHolder, Cursor cursor);
    }

    public BluetoothDeviceMediaPlayerPairAdapter(Context context, BluetoothDeviceAdapterOnclickListener clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if(viewGroup instanceof RecyclerView)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_bluetooth_media_pair, viewGroup, false);
            view.setFocusable(true);
            return new ViewHolder(view);
        }
        else
        {
            throw new RuntimeException("Not bound to recycler view selection.");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        mCursor.moveToPosition(position);

        int bluetoothDeviceType = mCursor.getInt(COL_BLUETOOTH_DEVICE_TYPE);
        viewHolder.mBluetoothDeviceIcon.setImageResource(Utility.getBluetoothDeviceIconResource(bluetoothDeviceType));

        String bluetoothDeviceName = mCursor.getString(COL_BLUETOOTH_DEVICE_NAME);
        viewHolder.mBluetoothDeviceName.setText(bluetoothDeviceName);

        // Use package name to retrieve media player icon
        String mediaPlayerPackage = mCursor.getString(COL_MEDIA_PLAYER_PACKAGE_NAME);
        Drawable mediaPlayerDrawable = null;
        try {
            mediaPlayerDrawable = mContext.getPackageManager().getApplicationIcon(mediaPlayerPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(mediaPlayerDrawable != null) {
            viewHolder.mMediaDeviceIcon.setImageDrawable(mediaPlayerDrawable);
        }
        else
        {
            // TODO change it to a fallback icon
            viewHolder.mMediaDeviceIcon.setImageResource(R.mipmap.ic_headset);
        }

        String mediaPlayerName = mCursor.getString(COL_MEDIA_PLAYER_NAME);
        viewHolder.mMediaDeviceName.setText(mediaPlayerName);

        int progress = mCursor.getInt(COL_MEDIA_PLAYER_VOLUME);
        viewHolder.mVolumeProgressBar.setProgress(progress);

        // Change volume icon if progress is zero.
        if(progress > 0){
            viewHolder.mVolumeIcon.setImageResource(R.mipmap.ic_volume_up);
        }
        else {
            viewHolder.mVolumeIcon.setImageResource(R.mipmap.ic_volume_off);
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor == null)
        {
            return 0;
        }
        else
        {
            return mCursor.getCount();
        }
    }

    public void swapCursor(Cursor newCursor)
    {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor()
    {
        return mCursor;
    }
}
