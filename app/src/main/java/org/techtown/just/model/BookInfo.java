package org.techtown.just.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class BookInfo implements Serializable, Parcelable {

    //book 속성
    public String isbn;
    public String tags;

    public String book_name;

    public String author;
    public String country;
    public String contents;
    public String thumbnail;

    protected BookInfo(Parcel in) {
        isbn = in.readString();
        tags = in.readString();
        book_name = in.readString();
        author = in.readString();
        country = in.readString();
        contents = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel in) {
            return new BookInfo(in);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };

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

    public BookInfo(String book_name){
        this.book_name = book_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(isbn);
        parcel.writeString(tags);
        parcel.writeString(book_name);
        parcel.writeString(author);
        parcel.writeString(country);
        parcel.writeString(contents);
        parcel.writeString(thumbnail);
    }
}
