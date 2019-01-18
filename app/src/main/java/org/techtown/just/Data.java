package org.techtown.just;

public class Data {
    //json 파싱
    int postId;
    int id;
    String name;
    String mail;
    String body;

    public String toString() {
        String s = "";
        s += postId + "/";
        s += id + "/";
        s += name + "/";
        s += mail + "/";
        s += body + "/";
        return s;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
