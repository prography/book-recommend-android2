package org.techtown.just.model;

import java.io.Serializable;
import java.util.List;

public class BookInfoWithBool implements Serializable {
    Boolean isExist;
    List<BookInfo> data;

    public Boolean getIsExist() {
        return isExist;
    }

    public void setIsExist(Boolean isExist) {
        this.isExist = isExist;
    }

    public List<BookInfo> getData() {
        return data;
    }

    public void setData(List<BookInfo> data) {
        this.data = data;
    }
}
