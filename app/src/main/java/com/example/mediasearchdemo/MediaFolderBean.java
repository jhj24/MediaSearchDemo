package com.example.mediasearchdemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kotlin.contracts.Returns;

public class MediaFolderBean implements Parcelable {

    private String name;
    private String path;
    private List<MediaBean> children = new ArrayList<>();

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

    public List<MediaBean> getChildren() {
        if (children == null) {
            return new ArrayList<MediaBean>();
        }
        return children;
    }

    public void setChildren(List<MediaBean> children) {
        this.children = children;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeTypedList(this.children);
    }

    public MediaFolderBean() {
    }

    protected MediaFolderBean(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.children = in.createTypedArrayList(MediaBean.CREATOR);
    }

    public static final Creator<MediaFolderBean> CREATOR = new Creator<MediaFolderBean>() {
        @Override
        public MediaFolderBean createFromParcel(Parcel source) {
            return new MediaFolderBean(source);
        }

        @Override
        public MediaFolderBean[] newArray(int size) {
            return new MediaFolderBean[size];
        }
    };
}
