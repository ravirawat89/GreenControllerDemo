package com.netcommlabs.greencontroller.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.netcommlabs.greencontroller.services.BleAdapterService;

/**
 * Created by Android on 12/5/2017.
 */

public class DialogConfirm  {

    private void dialogConfirmAction(String title,String msg,Context mContext) {

        android.support.v7.app.AlertDialog.Builder builder;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {*/
        builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        //}
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Stop", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
