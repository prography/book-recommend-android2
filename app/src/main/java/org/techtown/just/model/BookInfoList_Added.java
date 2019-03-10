package org.techtown.just.model;

import java.io.Serializable;
import java.util.List;

public class BookInfoList_Added implements Serializable {
    List<BookInfo_Added> data;

    public List<BookInfo_Added> getData() {
        return data;
    }

    public void setData(List<BookInfo_Added> data) {
        this.data = data;
    }
}
