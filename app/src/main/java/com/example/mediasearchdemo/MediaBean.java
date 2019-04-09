package com.example.mediasearchdemo;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaBean implements Parcelable {

    private String name;
    private String path;
    private long createTime;
    private String mimeType;
    private long size;

    public MediaBean(String name, String path, long createTime, String mimeType, long size) {
        this.name = name;
        this.path = path;
        this.createTime = createTime;
        this.mimeType = mimeType;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setParentPath(long createTime) {
        this.createTime = createTime;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.createTime);
        dest.writeString(this.mimeType);
        dest.writeLong(this.size);
    }

    public MediaBean() {
    }

    protected MediaBean(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.createTime = in.readLong();
        this.mimeType = in.readString();
        this.size = in.readLong();
    }

    public static final Creator<MediaBean> CREATOR = new Creator<MediaBean>() {
        @Override
        public MediaBean createFromParcel(Parcel source) {
            return new MediaBean(source);
        }

        @Override
        public MediaBean[] newArray(int size) {
            return new MediaBean[size];
        }
    };
}
