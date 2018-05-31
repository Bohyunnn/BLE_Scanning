package com.example.hansung.ifindthanq.addBLE;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hansung.ifindthanq.R;
import com.example.hansung.ifindthanq.model.SearchBLE;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SearchBLE> searchBLES;
    private String bleName, macAddress;

    //Adapter 초기화 및 생성자로 받은 데이터기반으로 Adapter 내 데이터 세팅
    public CustomAdapter(ArrayList<SearchBLE> searchBLES) {
        this.searchBLES = searchBLES;
    }

    public CustomAdapter(Context mContext, ArrayList<SearchBLE> searchBLES) {
        this.mContext = mContext;
        this.searchBLES = searchBLES;
    }

    public CustomAdapter(Context mContext, String bleName, String macAddress) {
        this.mContext = mContext;
        this.bleName = bleName;
        this.macAddress = macAddress;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView macAddress;
        public TextView bleName;
        public Button trackButton;

        public MyViewHolder(Context context, View view, CustomAdapter mContacts) {
            super(view);
            macAddress = (TextView) view.findViewById(R.id.macAddress);
            bleName = (TextView) view.findViewById(R.id.bleName);
            trackButton = (Button) view.findViewById(R.id.trackButton);
        }

    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_cardview, parent, false);
        return new CustomAdapter.MyViewHolder(mContext, itemView, this);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, final int position) {
        final SearchBLE searchBLE = searchBLES.get(position);

        holder.macAddress.setText(searchBLE.getMacAddress());

        holder.bleName.setText(searchBLE.getBleName());

        holder.trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddBLEActivity.class);
                intent.putExtra("macs", searchBLE.getMacAddress() + "");
                intent.putExtra("name", searchBLE.getBleName() + "");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchBLES.size();
    }
}