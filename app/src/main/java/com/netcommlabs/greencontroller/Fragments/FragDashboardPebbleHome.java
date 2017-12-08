package com.netcommlabs.greencontroller.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.activities.DeviceActivity;
import com.netcommlabs.greencontroller.activities.DontHaveDvc;
import com.netcommlabs.greencontroller.activities.MainActivity;
import com.netcommlabs.greencontroller.adapters.NavListAdapter;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;
import com.netcommlabs.greencontroller.utilities.Constant;
import com.netcommlabs.greencontroller.utilities.Navigation_Drawer_Data;
import com.netcommlabs.greencontroller.utilities.RowDataArrays;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 12/6/2017.
 */

public class FragDashboardPebbleHome extends Fragment {

    private MainActivity mContext;
    private View view;
    private TextView tvPebbleAsset;
    private LinearLayout llMyDevices, llStatistics;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.navigation_pebble, null);

        initBase(view);
        initListeners();

        return view;
    }


    private void initBase(View view) {
        tvPebbleAsset = view.findViewById(R.id.tvPebbleAsset);
        Typeface tvPebbleFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/CaviarDreams_Bold.ttf");
        tvPebbleAsset.setTypeface(tvPebbleFont);

        llMyDevices = view.findViewById(R.id.llMyDevices);
        llStatistics = view.findViewById(R.id.llStatistics);
    }

    private void initListeners() {
        llMyDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
                List<ModalBLEDevice> listBLEDvcFromDB = databaseHandler.getAllBLEDvcs();

                if (listBLEDvcFromDB != null && listBLEDvcFromDB.size() > 0) {
                    //Adding Fragment(FragMyDevices)
                    MyFragmentTransactions.replaceFragment(mContext, new FragMyDevices(), Constant.DEVICE_MAP, mContext.frm_lyt_container_int, true);
                } else {
                    //Adding Fragment(FragDontHvDevice)
                    MyFragmentTransactions.replaceFragment(mContext, new FragDontHvDevice(), Constant.DO_NOT_HAVE_DEVICE, mContext.frm_lyt_container_int, true);
                }


            }
        });

        llStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Statistics", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
