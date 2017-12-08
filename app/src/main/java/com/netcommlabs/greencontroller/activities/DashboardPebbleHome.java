package com.netcommlabs.greencontroller.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.adapters.NavListAdapter;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;
import com.netcommlabs.greencontroller.utilities.Navigation_Drawer_Data;
import com.netcommlabs.greencontroller.utilities.RowDataArrays;

import java.util.ArrayList;
import java.util.List;

public class DashboardPebbleHome extends AppCompatActivity {

    private MainActivity mContext;
    private TextView tvPebbleAsset;
    private LinearLayout llMyDevices, llStatistics, llHamburgerIcon;
    private DrawerLayout nav_drawer_layout;
    private RecyclerView nav_revi_slider;
    private List<Navigation_Drawer_Data> listNavDrawerRowDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_pebble);

        initBase();

        initListeners();
    }

    private void initBase() {
        //mContext = this;

        tvPebbleAsset = (TextView) findViewById(R.id.tvPebbleAsset);
        Typeface tvPebbleFont = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf");
        tvPebbleAsset.setTypeface(tvPebbleFont);

        llHamburgerIcon = (LinearLayout) findViewById(R.id.llHamburgerIcon);
        llMyDevices = (LinearLayout) findViewById(R.id.llMyDevices);
        llStatistics = (LinearLayout) findViewById(R.id.llStatistics);

        nav_drawer_layout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        nav_revi_slider = (RecyclerView) findViewById(R.id.nav_revi_slider);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        nav_revi_slider.setLayoutManager(layoutManager);

        listNavDrawerRowDat = new ArrayList<>();

        for (int i = 0; i < new RowDataArrays().flatIconArray.length; i++) {

            listNavDrawerRowDat.add(new Navigation_Drawer_Data(

                    new RowDataArrays().flatIconArray[i],
                    new RowDataArrays().labelArray[i]

            ));
        }

        nav_revi_slider.setAdapter(new NavListAdapter(mContext,listNavDrawerRowDat,nav_drawer_layout));

    }

    private void initListeners() {
        llHamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav_drawer_layout.openDrawer(Gravity.START);
            }
        });

        llMyDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
                List<ModalBLEDevice> listBLEDvcFromDB = databaseHandler.getAllBLEDvcs();

                if (listBLEDvcFromDB != null && listBLEDvcFromDB.size() > 0) {
                    Intent intentDeviceMap = new Intent(mContext, DeviceActivity.class);
                    startActivity(intentDeviceMap);
                    mContext.finish();
                } else {
                    Intent intentDontHave = new Intent(mContext, DontHaveDvc.class);
                    startActivity(intentDontHave);
                    mContext.finish();
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
