package org.techtown.just.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TagNames implements Serializable {
    private String[] tags = {"행복", "슬픔", "힐링", "스릴러", "고전", "SF", "드라마", "따뜻함", "시리즈물", "고전명작", "한국", "영국", "미스터리"};
    private ArrayList<String> selectedTags = new ArrayList<>();

    public String[] getTags() {
        return tags;
    }

    public void addSelectedTag(String tag) {
        selectedTags.add(tag);
    }

    public void clearSelectedTags() {
        selectedTags.clear();
    }

    public void updateSelectedTags(ArrayList<String> tags) {
        selectedTags.clear();
        selectedTags.addAll(tags);
    }

    public void addSelectedTags(ArrayList<String> tags) {
        selectedTags.addAll(tags);
    }

    public ArrayList<String> getSelectedTags() {
        return selectedTags;
    }

    public TagNames() {

    }
}
