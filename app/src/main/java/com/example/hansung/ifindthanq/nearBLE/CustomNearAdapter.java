package com.example.hansung.ifindthanq.nearBLE;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hansung.ifindthanq.R;
import com.example.hansung.ifindthanq.model.NearBLE;

import java.util.ArrayList;

public class CustomNearAdapter extends RecyclerView.Adapter<CustomNearAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<NearBLE> nearBLEs;

    //Adapter 초기화 및 생성자로 받은 데이터기반으로 Adapter 내 데이터 세팅
    public CustomNearAdapter(ArrayList<NearBLE> nearBLE) {
        this.nearBLEs = nearBLE;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bleName;
        public TextView macAddress;
        public TextView update;
        public TextView rssiResult;
        public TextView distance;

        private CustomNearAdapter mContacts;
        private Context context;

        public MyViewHolder(Context context, View view, CustomNearAdapter mContacts) {
            super(view);
            bleName = (TextView) view.findViewById(R.id.bleName);
            macAddress = (TextView) view.findViewById(R.id.macAddress);
            update = (TextView) view.findViewById(R.id.update);
            rssiResult = (TextView) view.findViewById(R.id.rssiResult);
            distance = (TextView) view.findViewById(R.id.distance);

            this.mContacts = mContacts;
            this.context = context;
        }

    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public CustomNearAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_near_cardview, parent, false);
        return new CustomNearAdapter.MyViewHolder(mContext, itemView, this);
    }

    @Override
    public void onBindViewHolder(CustomNearAdapter.MyViewHolder holder, int position) {
        NearBLE nearBLE = nearBLEs.get(position);

        TextView macAddress = holder.macAddress;
        macAddress.setText(nearBLE.getMacAddress());

        TextView bleName = holder.bleName;
        bleName.setText(nearBLE.getBleName());

        TextView update = holder.update;
        update.setText(nearBLE.getDate());

        TextView rssiResult = holder.rssiResult;
        rssiResult.setText(nearBLE.getRssi());

        TextView distance = holder.distance;
        distance.setText(nearBLE.getDistance());

    }

    @Override
    public int getItemCount() {
        return nearBLEs.size();
    }
}