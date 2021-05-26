package com.example.androidbackground;

public class FileInfo {

    private int fileSize;
    private String fileType;

    public FileInfo(int fileSize, String fileType) {
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
