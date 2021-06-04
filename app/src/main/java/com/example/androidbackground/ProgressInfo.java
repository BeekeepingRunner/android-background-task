package com.example.androidbackground;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgressInfo implements Parcelable {

    public static final int IN_PROGRESS = 1;
    public static final int FINISHED = 2;
    public static final int ERROR = -1;

    private int fileSize;
    private int status;
    private int bytesFetched;

    public ProgressInfo(int fileSize, int status) {
        this.fileSize = fileSize;
        this.status = status;
        this.bytesFetched = 0;
    }

    protected ProgressInfo(Parcel in) {
        fileSize = in.readInt();
        status = in.readInt();
        bytesFetched = in.readInt();
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBytesFetched() {
        return bytesFetched;
    }

    public void addBytesFetched(int bytes) {
        this.bytesFetched += bytes;
        if (bytesFetched >= fileSize)
            setStatus(FINISHED);
    }

    // Returns an integer number in range [0:100]
    public int getProgress() {
        if (fileSize != 0) {
            return (int) ((bytesFetched / (fileSize * 1.0) * 100));
        }
        else return 0;
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
