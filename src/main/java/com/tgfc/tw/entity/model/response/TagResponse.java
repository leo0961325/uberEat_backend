package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Tag;

import java.util.Map;

import java.util.Objects;

public class TagResponse {
    private int id;
    private String name;

    public TagResponse(){

    }

    public TagResponse(Map<String, Object> map){
        this.id = (int)map.get("id");
        this.name = String.valueOf(map.get("name"));
    }

    public TagResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagResponse(Tag tag){
        this.id = tag.getId();
        this.name = tag.getName();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagResponse that = (TagResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
