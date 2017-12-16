package com.netcommlabs.greencontroller.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.netcommlabs.greencontroller.activities.MainActivity;

/**
 * Created by Netcomm on 9/16/2016.
 */
public class AppAlertDialog {
    private static AlertDialog.Builder builder;

    public static void showDialogSelfFinish(Context tmContext, String Title, String Msg) {
        builder = new AlertDialog.Builder(tmContext);
        builder.setTitle(Title).setMessage(Msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public static void showDialogFinishWithActivity(final Context tmContext, String Title, String Msg) {
        builder = new AlertDialog.Builder(tmContext);
        builder.setTitle(Title).setMessage(Msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((Activity) tmContext).finish();
                    }
                })
                .show();
    }

    public static void dialogBLENotConnected(final MainActivity mContext, final Fragment myRequestedFrag , final BLEAppLevel bleAppLevel) {
        AlertDialog.Builder alBui = new AlertDialog.Builder(mContext);
        alBui.setTitle("BLE not connected");
        alBui.setMessage("Check BLE power, operating range and connect again !");
        alBui.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String macAddress = MySharedPreference.getInstance(mContext).getConnectedDvcMacAdd();
                bleAppLevel.disconnectBLECompletely();
                BLEAppLevel.getInstance(mContext,myRequestedFrag, macAddress);
            }
        });
        alBui.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alBui.create().show();
    }
}
