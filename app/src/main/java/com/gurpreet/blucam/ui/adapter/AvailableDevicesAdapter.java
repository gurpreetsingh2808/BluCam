package com.gurpreet.blucam.ui.adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gurpreet.blucam.R;

import java.util.List;

/**
 * Created by Gurpreet on 04-03-2017.
 */

public class AvailableDevicesAdapter extends RecyclerView.Adapter<AvailableDevicesAdapter.ViewHolder> {

    private static final String TAG = AvailableDevicesAdapter.class.getSimpleName();
    private List<BluetoothDevice> mItems;
    private Activity activity;
    private ItemListener mListener;

    public AvailableDevicesAdapter(List<BluetoothDevice> items, ItemListener listener, Activity activity) {
        mItems = items;
        mListener = listener;
        this.activity = activity;
    }

    public void setListener(ItemListener listener) {
        mListener = listener;
    }

    public void clearList() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public BluetoothDevice getItemAtPos(int pos) {
        if (mItems.size() > 0) {
            return mItems.get(pos);
        } else {
            return null;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bluetooth_device, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public void add(List<BluetoothDevice> items) {
        int previousDataSize = this.mItems.size();
        this.mItems.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDeviceName;
        BluetoothDevice item;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvDeviceName = (TextView) itemView.findViewById(R.id.tvDeviceName);
        }

        public void setData(final BluetoothDevice item) {
            this.item = item;
            tvDeviceName.setText(item.getName());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(item, getAdapterPosition());
            }
        }
    }

    public interface ItemListener {
        void onItemClick(BluetoothDevice item, int position);
    }

}
