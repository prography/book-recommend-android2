package org.techtown.just.model;

import java.io.Serializable;
import java.util.List;

public class BookInfoList_NotAdded implements Serializable {
    List<BookInfo_NotAdded> data;

    public List<BookInfo_NotAdded> getData() {
        return data;
    }

    public void setData(List<BookInfo_NotAdded> data) {
        this.data = data;
    }
}
