package org.techtown.just.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TagNames implements Serializable {
    //private String[] allTags = {"행복", "슬픔", "힐링", "스릴러", "고전", "SF", "드라마", "따뜻함", "시리즈물", "고전명작", "한국", "영국", "미스터리"};
    private List<Tag> allTags;
    private List<Tag> selectedTags = new ArrayList<Tag>();

    public List<Tag> getAllTags() {
        return allTags;
    }

    public void setAllTags(List<Tag> allTags) {
        this.allTags = allTags;
    }

    public void addSelectedTag(Tag tag) {
        selectedTags.add(tag);
    }

    public void clearSelectedTags() {
        selectedTags.clear();
    }

    public void updateSelectedTags(List<Tag> tags) {
        selectedTags.clear();
        selectedTags.addAll(tags);
    }

    public void addSelectedTags(List<Tag> tags) {
        selectedTags.addAll(tags);
    }

    public List<Tag> getSelectedTags() {
        return selectedTags;
    }

    public TagNames() {

    }
}
