package com.netcommlabs.greencontroller.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netcommlabs.greencontroller.Fragments.FragDeviceDetails;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.model.DataTransferModel;
import com.netcommlabs.greencontroller.model.ModalVlNameSelect;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/1/2017.
 */

public class ValvesListAdapter extends RecyclerView.Adapter<ValvesListAdapter.MyViewHolder> {

    //private List<String> listValves;
    //ArrayList<ModalVlNameSelect> listModalValveNameSelect;
    private Context mContext;
    private List<View> listViewsCollection;
    private DatabaseHandler databaseHandler;
    private String dvcMacAdd;
    private ArrayList<DataTransferModel> listValveDataSingle;
    private FragDeviceDetails fragDeviceDetails;
    private int clickedPosition;
    private int posiViewHolder = 0;
    ModalVlNameSelect modalVlNameSelect1;

    public ValvesListAdapter(Context mContext, FragDeviceDetails fragDeviceDetails, String dvcMacAdd, int clickedPosition) {
        this.mContext = mContext;
        //this.listModalValveNameSelect = listModalValveNameSelect;
        this.dvcMacAdd = dvcMacAdd;
        this.fragDeviceDetails = fragDeviceDetails;
        this.clickedPosition = clickedPosition;
        listViewsCollection = new ArrayList<>();
        databaseHandler = new DatabaseHandler(mContext);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llValveNameColor;
        TextView tvValveName;

        public MyViewHolder(View itemView) {
            super(itemView);
            llValveNameColor = itemView.findViewById(R.id.llValveNameColor);
            tvValveName = itemView.findViewById(R.id.tvValveName);

           /* listViewsCollection.add(llValveNameColor);
            if (clickedPosition == posiViewHolder) {
                //---- First Item selected----
                listViewsCollection.get(clickedPosition).setBackgroundResource(R.drawable.volve_bg_shadow_select);
                //posiViewHolder=0;
            }
            posiViewHolder++;*/
            /*llValveNameColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tvClickedValveName = v.findViewById(R.id.tvValveName);
                    String clickedValveName = tvClickedValveName.getText().toString();
                    //mContext.clickPassDataToAct(clickedValveName);
                    Log.e("@@@VALVE NAME", clickedValveName);
                    for (int i = 0; i < listViewsCollection.size(); i++) {
                        listViewsCollection.get(i).setBackgroundResource(R.drawable.volve_bg_shadow);
                    }
                    int pos = getAdapterPosition();
                    listViewsCollection.get(pos).setBackgroundResource(R.drawable.volve_bg_shadow_select);
                    //DB work for valve selection
                    listValveDataSingle = databaseHandler.getValveDataWithMACValveName(dvcMacAdd, clickedValveName);
                    fragDeviceDetails.clickPassDataToAct(listValveDataSingle, clickedValveName, pos);
                }
            });*/
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_valves_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ModalVlNameSelect modalVlNameSelect = FragDeviceDetails.listModalValveNameSelect.get(position);
        String clickedVlvName = modalVlNameSelect.getValveName();
        holder.tvValveName.setText(clickedVlvName);
        final boolean isValveSelected = modalVlNameSelect.getSelected();

        if (isValveSelected) {
            holder.llValveNameColor.setBackgroundResource(R.drawable.volve_bg_shadow_select);
        } else {
            holder.llValveNameColor.setBackgroundResource(R.drawable.volve_bg_shadow);
        }

        holder.llValveNameColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModalVlNameSelect modalVlNameSelectClicked = FragDeviceDetails.listModalValveNameSelect.get(position);
                String clickedVlvName = modalVlNameSelectClicked.getValveName();
                //Deselecting all views
                for (int i = 0; i < FragDeviceDetails.listModalValveNameSelect.size(); i++) {
                    FragDeviceDetails.listModalValveNameSelect.get(i).setSelected(false);
                    //modalVlNameSelectFrEch.setSelected(false);
                    holder.llValveNameColor.setBackgroundResource(R.drawable.volve_bg_shadow);
                }
                //Item showing as selected
                //modalVlNameSelect1 = listModalValveNameSelect.get(position);
                boolean isValveSelected = modalVlNameSelectClicked.getSelected();
                if (!isValveSelected) {
                    modalVlNameSelectClicked.setSelected(true);
                }
                holder.llValveNameColor.setBackgroundResource(R.drawable.volve_bg_shadow_select);
                notifyDataSetChanged();
                //Toast.makeText(mContext, "POS " + position+" NAME "+clickedVlvName, Toast.LENGTH_SHORT).show();
                //DB work for valve selection
                listValveDataSingle = databaseHandler.getValveDataWithMACValveName(dvcMacAdd, clickedVlvName);
                fragDeviceDetails.clickedPassDataToParent(listValveDataSingle, clickedVlvName);

            }
        });

       /* if (clickedPosition==position){
            //---- First Item selected----
            listViewsCollection.get(clickedPosition).setBackgroundResource(R.drawable.volve_bg_shadow_select);
        }else {
            listViewsCollection.get(position).setBackgroundResource(R.drawable.volve_bg_shadow);
        }*/
    }

    @Override
    public int getItemCount() {
        return FragDeviceDetails.listModalValveNameSelect.size();
    }

}
