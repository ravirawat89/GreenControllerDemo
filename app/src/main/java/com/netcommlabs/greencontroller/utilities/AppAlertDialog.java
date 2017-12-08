package com.netcommlabs.greencontroller.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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
}
