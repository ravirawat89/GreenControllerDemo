package com.netcommlabs.greencontroller;

import com.netcommlabs.greencontroller.model.DataTransferModel;

import java.util.List;

/**
 * Created by Android on 11/6/2017.
 */

public interface InterfaceValveAdapter {

    void clickPassDataToAct(List<DataTransferModel> listValveDataSingle,String clickedValveName);
    void onRecyclerItemClickedNameAdress(String name,String address);
}
