package com.kaayotee.fbaspotter.json;

/**
 * Created by maaz_khan on 10/22/16.
 */
public class MergeFieldEntry {
    String tag;
    String name;
    String type = "text";

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}