package org.techtown.just.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class BookFlag implements Serializable, Parcelable {
    int had_read;
    int be_interested;


    public BookFlag(int had_read, int be_interested) {
        this.had_read = had_read;
        this.be_interested = be_interested;
    }

    protected BookFlag(Parcel in) {
        had_read = in.readInt();
        be_interested = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(had_read);
        dest.writeInt(be_interested);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookFlag> CREATOR = new Creator<BookFlag>() {
        @Override
        public BookFlag createFromParcel(Parcel in) {
            return new BookFlag(in);
        }

        @Override
        public BookFlag[] newArray(int size) {
            return new BookFlag[size];
        }
    };

    public int getHad_read() {
        return had_read;
    }

    public void setHad_read(int had_read) {
        this.had_read = had_read;
    }

    public int getBe_interested() {
        return be_interested;
    }

    public void setBe_interested(int be_interested) {
        this.be_interested = be_interested;
    }


}
