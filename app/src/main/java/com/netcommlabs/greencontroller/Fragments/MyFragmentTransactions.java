package com.netcommlabs.greencontroller.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
        MainActivity mContext = (MainActivity) context;

        FragmentManager myFragmentManager = mContext.getSupportFragmentManager();
        FragmentTransaction ft = myFragmentManager.beginTransaction();
        ft.replace(layout, fragment, tag);
        ft.addToBackStack(tag);

        int backStackCount = myFragmentManager.getBackStackEntryCount();
        if (!isAddFrag) {
            if (backStackCount > 1) {
                for (int i = backStackCount; i > 1; i--) {
                    myFragmentManager.popBackStack();
                }
            }
        }
        ft.commit();

        mContext.tvToolbar_title.setText(tag);
        mContext.tvDesc_txt.setText("");
        tvClearEditDataLocal = mContext.tvClearEditData;
        if (tvClearEditDataLocal.getVisibility() == View.VISIBLE) {
            tvClearEditDataLocal.setVisibility(View.GONE);
        }

        myFragmentManager.executePendingTransactions();
        Log.e("$$$ FRAG COUNT", "" + myFragmentManager.getBackStackEntryCount() + ", TAG: " + tag);

    }

}
