package org.techtown.just.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class BookInfo_Added implements Serializable, Parcelable {

    //book 속성
    public String isbn;
    public String book_name;
    public String author;
    public String contents;
    public String thumbnail;

    public String tags;
    public String country;

    protected BookInfo_Added(Parcel in) {
        isbn = in.readString();
        tags = in.readString();
        book_name = in.readString();
        author = in.readString();
        country = in.readString();
        contents = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<BookInfo_Added> CREATOR = new Creator<BookInfo_Added>() {
        @Override
        public BookInfo_Added createFromParcel(Parcel in) {
            return new BookInfo_Added(in);
        }

        @Override
        public BookInfo_Added[] newArray(int size) {
            return new BookInfo_Added[size];
        }
    };

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "BookInfo_Added{" +
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

    public BookInfo_Added(String book_name){
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
