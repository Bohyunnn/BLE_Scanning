package com.example.hansung.ifindthanq.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hansung.ifindthanq.BLEDistanceActivity;
import com.example.hansung.ifindthanq.addBLE.BLESearchActivity;
import com.example.hansung.ifindthanq.R;

import com.example.hansung.ifindthanq.util.ProblemConfigurationVo;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by kimbohyun on 2018-03-09.
 */

//MainActivity의 MYBLE Adapter list
public class MyBLEAdapter extends RecyclerView.Adapter<MyBLEAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProblemConfigurationVo> myBLEList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView bleImage;
        public TextView bleName;

        public MyViewHolder(View view) {
            super(view);
            bleImage = (ImageView) view.findViewById(R.id.bleImage);
            bleName = (TextView) view.findViewById(R.id.bleName);
        }
    }

    public MyBLEAdapter(Context mContext, List<ProblemConfigurationVo> myBLEList) {
        this.mContext = mContext;
        this.myBLEList = myBLEList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProblemConfigurationVo myBLE = myBLEList.get(position);
        holder.bleName.setText(myBLE.getBleName());

        if(myBLE.getBleImage()!=0){
            Glide.with(mContext).load(myBLE.getBleImage()).into(holder.bleImage);
        }else{
            if(myBLE.getAlbumImage()!=null){
                byte[] decodedByteArray = Base64.decode(myBLE.getAlbumImage(), Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                decodedBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                Glide.with(mContext).load(stream.toByteArray()).asBitmap().into(holder.bleImage);
            }
        }

        holder.bleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, myBLE.getBleName() + "을 클릭함", Toast.LENGTH_SHORT).show();
                if (myBLE.getBleName() == " ") {
                    //BEL 찾기 Activity
                    Intent intent = new Intent(mContext, BLESearchActivity.class);
                    mContext.startActivity(intent);
                } else {
                    // BLE 상세보기 Activity
                    Intent intent = new Intent(mContext, BLEDistanceActivity.class);
                    intent.putExtra("bleName", myBLE.getBleName());
                    intent.putExtra("bleMac", myBLE.getMacs());
                    intent.putExtra("bleImage", myBLE.getBleImage());
                    intent.putExtra("albumImage", myBLE.getAlbumImage());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myBLEList.size();
    }
}
