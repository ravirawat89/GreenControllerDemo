package com.netcommlabs.greencontroller.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.utilities.Navigation_Drawer_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/13/2017.
 */

public class NavListAdapter extends RecyclerView.Adapter<NavListAdapter.MyViewHolder> {

    private List<Navigation_Drawer_Data> listNavDrawerRowDat;

    public NavListAdapter(List<Navigation_Drawer_Data> listNavDrawerRowDat) {

        this.listNavDrawerRowDat = listNavDrawerRowDat;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNavIcon;
        TextView tvNavName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivNavIcon = (ImageView) itemView.findViewById(R.id.ivNavIcon);
            tvNavName = (TextView) itemView.findViewById(R.id.tvNavName);
        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                           int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_nav_dashboard, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.ivNavIcon.setImageResource(listNavDrawerRowDat.get(position).getflat_icon_drawer());
        holder.tvNavName.setText(listNavDrawerRowDat.get(position).getlabel_drawer());
    }

    @Override
    public int getItemCount() {
        return listNavDrawerRowDat.size();
    }
}
