package com.netcommlabs.greencontroller.adapters;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netcommlabs.greencontroller.Fragments.FragAvailableDevices;
import com.netcommlabs.greencontroller.R;

import java.util.List;

public class AdptrAvailableDVCs extends RecyclerView.Adapter<AdptrAvailableDVCs.MyViewHolder> {

    Context mContext;
    List<BluetoothDevice> listAvailbleDvcs;
    BluetoothAdapter mBluetoothAdapter;
    FragAvailableDevices fragAvailableDevices;


    public AdptrAvailableDVCs(Context mContext, FragAvailableDevices fragAvailableDevices, List<BluetoothDevice> listAvailbleDvcs, BluetoothAdapter mBluetoothAdapter) {
        this.mContext = mContext;
        this.listAvailbleDvcs = listAvailbleDvcs;
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.fragAvailableDevices=fragAvailableDevices;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llAvailDvcRow;
        TextView tvDvcName, tvDvcMacAdd;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvDvcName = itemView.findViewById(R.id.tvDvcName);
            tvDvcMacAdd = itemView.findViewById(R.id.tvDvcMacAdd);
            llAvailDvcRow = itemView.findViewById(R.id.llAvailDvcRow);

            llAvailDvcRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    int clickedPosi = getAdapterPosition();
                    String dvcName = listAvailbleDvcs.get(clickedPosi).getName();
                    String dvcAddress = listAvailbleDvcs.get(clickedPosi).getAddress();
                    fragAvailableDevices.onRecyclerItemClickedNameAdress(dvcName,dvcAddress);
                   /* Toast.makeText(mContext, "Clicked " + dvcName + "\n" + dvcAddress, Toast.LENGTH_LONG).show();

                    *//*Intent intentAddWtrngProfile = new Intent(mContext, ConnectedQRAct.class);
                    mContext.startActivity(intentAddWtrngProfile);
                    mContext.finish();*//*

                    //Intent intentAddWtrngProfile = new Intent(mContext, AddEditSessionPlan.class);
                    Intent intentAddWtrngProfile = new Intent(mContext, AddEditSessionPlan.class);
                    intentAddWtrngProfile.putExtra(AddEditSessionPlan.EXTRA_NAME, dvcName);
                    intentAddWtrngProfile.putExtra(AddEditSessionPlan.EXTRA_ID, dvcAddress);
                    mContext.startActivity(intentAddWtrngProfile);
                    //mContext.finish();*/


                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_list_avail_divice, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String dvcName = listAvailbleDvcs.get(position).getName();
        String dvcAddress = listAvailbleDvcs.get(position).getAddress();
        if (dvcName != null) {
            holder.tvDvcName.setText(dvcName);
        } else {
            holder.tvDvcName.setText("Unknown Device");
        }
        holder.tvDvcMacAdd.setText(dvcAddress);
    }

    @Override
    public int getItemCount() {
        return listAvailbleDvcs.size();
    }
}
