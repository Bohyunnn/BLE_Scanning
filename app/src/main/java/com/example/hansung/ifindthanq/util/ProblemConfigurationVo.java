package com.example.hansung.ifindthanq.util;

import java.io.Serializable;

public class ProblemConfigurationVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int seq;
    private String type;
    private String albumImage;
    private int bleImage;
    private String macs;
    private String bleName;


    public ProblemConfigurationVo() {


    }

    public ProblemConfigurationVo(int seq, String type, String albumImage, int bleImage, String macs, String bleName) {
        this.seq = seq;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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