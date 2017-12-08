package com.netcommlabs.greencontroller.adapters;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.Fragments.FragAvailableDevices;
import com.netcommlabs.greencontroller.Fragments.FragFAQHelp;
import com.netcommlabs.greencontroller.Fragments.FragFeedback;
import com.netcommlabs.greencontroller.Fragments.FragMeterDevice;
import com.netcommlabs.greencontroller.Fragments.FragMyDevices;
import com.netcommlabs.greencontroller.Fragments.FragMyProfile;
import com.netcommlabs.greencontroller.Fragments.FragRecomm;
import com.netcommlabs.greencontroller.Fragments.FragSavedAddress;
import com.netcommlabs.greencontroller.Fragments.FragStatistics;
import com.netcommlabs.greencontroller.Fragments.MyFragmentTransactions;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.activities.MainActivity;
import com.netcommlabs.greencontroller.utilities.Constant;
import com.netcommlabs.greencontroller.utilities.Navigation_Drawer_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/13/2017.
 */

public class NavListAdapter extends RecyclerView.Adapter<NavListAdapter.MyViewHolder> {

    private List<Navigation_Drawer_Data> listNavDrawerRowDat;
    MainActivity mContext;
    DrawerLayout nav_drawer_layout;

    public NavListAdapter(MainActivity mContext, List<Navigation_Drawer_Data> listNavDrawerRowDat, DrawerLayout nav_drawer_layout) {
        this.mContext = mContext;
        this.listNavDrawerRowDat = listNavDrawerRowDat;
        this.nav_drawer_layout = nav_drawer_layout;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNavIcon;
        TextView tvNavName;
        LinearLayout llAvailDvcRow;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivNavIcon = (ImageView) itemView.findViewById(R.id.ivNavIcon);
            tvNavName = (TextView) itemView.findViewById(R.id.tvNavName);
            llAvailDvcRow = itemView.findViewById(R.id.llAvailDvcRow);

            llAvailDvcRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("@CLICKED ONE ", "" + getAdapterPosition());
                    TextView tvClickedNavItem = v.findViewById(R.id.tvNavName);
                    String clickedNavItem = tvClickedNavItem.getText().toString();

                    switch (clickedNavItem) {
                        case "My Profile":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragMyProfile(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;
                        case "My Devices":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragMyDevices(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;
                        case "Add New Device":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragAvailableDevices(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;
                        case "Meter Devices":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragMeterDevice(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;
                        case "Recommendations":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragRecomm(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;
                        case "Statistics":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragStatistics(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;
                        case "Saved Address":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragSavedAddress(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;
                        case "Feedback":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragFeedback(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;
                        case "FAQ & Help":
                            //Replacing Fragment(FragAddAddress)
                            MyFragmentTransactions.replaceFragment(mContext, new FragFAQHelp(), Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, false);
                            break;

                    }

                    nav_drawer_layout.closeDrawers();

                }
            });
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
