package com.example.androidbackground;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgressInfo implements Parcelable {

    private final int IN_PROGRESS = 1;
    private final int FINISHED = 2;
    private final int ERROR = -1;

    private int fileSize;
    private int status;
    private int bytesFetched;

    protected ProgressInfo(Parcel in) {
        fileSize = in.readInt();
        status = in.readInt();
        bytesFetched = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fileSize);
        dest.writeInt(status);
        dest.writeInt(bytesFetched);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProgressInfo> CREATOR = new Creator<ProgressInfo>() {

        @Override
        public ProgressInfo createFromParcel(Parcel in) {
            return new ProgressInfo(in);
        }

        @Override
        public ProgressInfo[] newArray(int size) {
            return new ProgressInfo[size];
        }
    };
}
