package com.netcommlabs.greencontroller;

import com.netcommlabs.greencontroller.model.DataTransferModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/6/2017.
 */

public interface InterfaceValveAdapter {

    //void clickPassDataToAct(ArrayList<DataTransferModel> listValveDataSingle, String clickedValveName,int position);
    void onRecyclerItemClickedNameAdress(String name,String address);
}
