package org.techtown.just.model;

import java.io.Serializable;

public class Tag implements Serializable {
    int tag_id;
    String tag_name;

    @Override
    public String toString() {
        return tag_id + " : " + tag_name;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}
