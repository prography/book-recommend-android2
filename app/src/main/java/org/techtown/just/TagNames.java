package org.techtown.just;

import java.io.Serializable;

public class TagNames implements Serializable {
    private String[] tags = {"행복", "슬픔", "힐링"};
    private int[] tagIndex;

    public String[] getTags() { return tags; }
    public int[] getTagIndex() { return tagIndex; }

    public void setTagIndex(int i) {
        tagIndex[i] = 1;
    }

    public TagNames() {
        tagIndex = new int[tags.length];

        for (int i = 0; i < tags.length; i++)
            tagIndex[i] = 0;
    }
}
