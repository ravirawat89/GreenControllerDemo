package com.netcommlabs.greencontroller.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netcommlabs.greencontroller.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Netcomm on 11/24/2017.
 */

public class DeviceAddressAdapter extends RecyclerView.Adapter<DeviceAddressAdapter.MyViewHolder> {
    List<View> listViews;
    Context mContext;
    List<String> listLocAddressType;
    //List<DeviceAddressModel> listLocAddressType;

    public DeviceAddressAdapter(Context mContext, List<String> listLocAddressType /*List<DeviceAddressModel> listLocAddressType*/) {
        this.mContext = mContext;
        this.listLocAddressType = listLocAddressType;
        listViews = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAddress;
        LinearLayout ll_bg;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            ll_bg = (LinearLayout) itemView.findViewById(R.id.ll_bg);
            listViews.add(ll_bg);
            listViews.get(0).setBackgroundResource(R.drawable.device_bg_select);

            ll_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosi = getAdapterPosition();
                    for (int i = 0; i < listViews.size(); i++) {
                        listViews.get(i).setBackgroundResource(R.drawable.device_bg);
                    }
                    listViews.get(clickedPosi).setBackgroundResource(R.drawable.device_bg_select);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_map_addresss, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String address = listLocAddressType.get(position);
        holder.tvAddress.setText(address);

    }

    @Override
    public int getItemCount() {
        return listLocAddressType.size();
    }
}