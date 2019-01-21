package org.techtown.just.model;

import java.io.Serializable;

public class BookInfo implements Serializable {

    //book 속성
    public int id;
    public String book_name;
    //public String content;

    public BookInfo(String book_name){
        //this.id = id;
        this.book_name = book_name;
    }

}
