package com.example.mediasearchdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FileFolderBean implements Parcelable {

    private String name;
    private String path;
    private List<FileBean> children = new ArrayList<>();

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

    public List<FileBean> getChildren() {
        if (children == null) {
            return new ArrayList<FileBean>();
        }
        return children;
    }

    public void setChildren(List<FileBean> children) {
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

    public FileFolderBean() {
    }

    protected FileFolderBean(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.children = in.createTypedArrayList(FileBean.CREATOR);
    }

    public static final Creator<FileFolderBean> CREATOR = new Creator<FileFolderBean>() {
        @Override
        public FileFolderBean createFromParcel(Parcel source) {
            return new FileFolderBean(source);
        }

        @Override
        public FileFolderBean[] newArray(int size) {
            return new FileFolderBean[size];
        }
    };
}
