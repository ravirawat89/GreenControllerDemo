package com.netcommlabs.greencontroller.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.netcommlabs.greencontroller.activities.MainActivity;

import org.w3c.dom.Text;

/**
 * Created by Android on 12/6/2017.
 */

public class MyFragmentTransactions {
    static TextView tvClearEditDataLocal;

    public static void replaceFragment(Context context, Fragment fragment, String tag, int layout, Boolean isAddFrag) {
        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        ft.replace(layout, fragment, tag);
        ft.addToBackStack(tag);

        if (!isAddFrag) {
            if (((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount() > 1) {
                for (int i = ((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount(); i > 1; i--) {
                    ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();
                }
            }

        }
        Log.e("FragmentCount", "" + ((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount() + " Tag " + tag);
        ft.commit();

        ((MainActivity) context).tvToolbar_title.setText(tag);
        ((MainActivity) context).tvDesc_txt.setText("");
        tvClearEditDataLocal = ((MainActivity) context).tvClearEditData;
        if (tvClearEditDataLocal.getVisibility() == View.VISIBLE) {
            tvClearEditDataLocal.setVisibility(View.GONE);
        }
    }

}
