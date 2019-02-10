package org.techtown.just.model;

import java.io.Serializable;
import java.util.List;

public class BookInfoWithBool implements Serializable {
    List<BookInfo> bookInfoList;
    Boolean bl;

    public Boolean getBl() {
        return bl;
    }

    public void setBl(Boolean bl) {
        this.bl = bl;
    }

    public List<BookInfo> getBookInfoList() {
        return bookInfoList;
    }

    public void setBookInfoList(List<BookInfo> bookInfoList) {
        this.bookInfoList = bookInfoList;
    }
}
