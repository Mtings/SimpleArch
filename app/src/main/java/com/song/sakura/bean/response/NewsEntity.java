package com.song.sakura.bean.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: com.song.sakura.entity
 * Description:
 * Copyright:Copyright(c) 2020
 * CreateTime:2020/06/24 14:27
 *
 * @author SogZiw
 * @version 1.0
 */
public class NewsEntity implements Parcelable {
    public String path;
    public String image;
    public String title;
    public String passtime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.image);
        dest.writeString(this.title);
        dest.writeString(this.passtime);
    }

    public NewsEntity() {
    }

    protected NewsEntity(Parcel in) {
        this.path = in.readString();
        this.image = in.readString();
        this.title = in.readString();
        this.passtime = in.readString();
    }

    public static final Parcelable.Creator<NewsEntity> CREATOR = new Parcelable.Creator<NewsEntity>() {
        @Override
        public NewsEntity createFromParcel(Parcel source) {
            return new NewsEntity(source);
        }

        @Override
        public NewsEntity[] newArray(int size) {
            return new NewsEntity[size];
        }
    };
}
