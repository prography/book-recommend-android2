package org.techtown.just.model;

import java.io.Serializable;

public class BookInfoLittle implements Serializable {

    //book 속성
    public String isbn;
    public String tags;

    public String book_name;

    public String author;
    public String country;
    public String contents;
    public String thumbnail;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    //    public List<Integer> getAllTags() {
//        return tags;
//    }
//
//    public void setAllTags(List<Integer> tags) {
//        this.tags = tags;
//    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "isbn=" + isbn +
                ", tags='" + tags + '\'' +
                ", book_name='" + book_name + '\'' +
                ", author='" + author + '\'' +
                ", country='" + country + '\'' +
                ", contents='" + contents + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BookInfoLittle(String book_name){
        this.book_name = book_name;
    }



}
