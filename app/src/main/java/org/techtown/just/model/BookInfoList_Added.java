package org.techtown.just.model;

import java.io.Serializable;
import java.util.List;

public class BookInfoListAdded implements Serializable {
    List<BookInfoAdded> data;

    public List<BookInfoAdded> getData() {
        return data;
    }

    public void setData(List<BookInfoAdded> data) {
        this.data = data;
    }
}
