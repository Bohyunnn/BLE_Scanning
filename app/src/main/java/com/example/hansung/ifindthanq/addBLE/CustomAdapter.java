package com.example.hansung.ifindthanq.addBLE;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansung.ifindthanq.R;
import com.example.hansung.ifindthanq.model.SearchBLE;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SearchBLE> searchBLES;

    //Adapter 초기화 및 생성자로 받은 데이터기반으로 Adapter 내 데이터 세팅
    public CustomAdapter(ArrayList<SearchBLE> searchBLES) {
        this.searchBLES = searchBLES;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView macAddress;
        public TextView bleName;
        public Button trackButton;
        private CustomAdapter mContacts;
        private Context context;

        public MyViewHolder(Context context, View view, CustomAdapter mContacts) {
            super(view);
            macAddress = (TextView) view.findViewById(R.id.macAddress);
            bleName = (TextView) view.findViewById(R.id.bleName);
            trackButton = (Button) view.findViewById(R.id.trackButton);

            this.mContacts = mContacts;
            this.context = context;

            trackButton.setOnClickListener(this); //버튼에 이벤트리스너 추가
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); //Get item position
            Toast.makeText(context, macAddress.getText(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(mContext, AddBLEActivity.class);
            intent.putExtra("macs", macAddress.getText());
            intent.putExtra("name", bleName.getText());
            mContext.startActivity(intent);
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
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, int position) {
        SearchBLE searchBLE = searchBLES.get(position);

        TextView macAddress = holder.macAddress;
        macAddress.setText(searchBLE.getMacAddress());

        TextView bleName = holder.bleName;
        bleName.setText(searchBLE.getBleName());

    }

    @Override
    public int getItemCount() {
        return searchBLES.size();
    }
}