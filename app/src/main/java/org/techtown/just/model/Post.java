package org.techtown.just.model;

import java.io.Serializable;

public class Post implements Serializable {
    int userId;
    int id;
    String title;
    String body;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        String s = "";
        s += userId + "/";
        s += id + "/";
        s += title + "/";
        s += body + "/";
        return s;
    }
}
