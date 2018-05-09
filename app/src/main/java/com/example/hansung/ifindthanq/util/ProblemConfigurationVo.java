package com.example.hansung.ifindthanq.util;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ProblemConfigurationVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int seq;
    private String albumImage;
    private int bleImage;
    private String macs;
    private String bleName;


    public ProblemConfigurationVo() {


    }

    public ProblemConfigurationVo(String albumImage, int bleImage, String macs, String bleName) {
        this.albumImage = albumImage;
        this.bleImage = bleImage;
        this.macs = macs;
        this.bleName = bleName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public int getBleImage() {
        return bleImage;
    }

    public void setBleImage(int bleImage) {
        this.bleImage = bleImage;
    }

    public String getMacs() {
        return macs;
    }

    public void setMacs(String macs) {
        this.macs = macs;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }
}