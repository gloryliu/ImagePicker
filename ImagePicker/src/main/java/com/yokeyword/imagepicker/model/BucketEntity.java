package com.yokeyword.imagepicker.model;

/**
 * 相册辑 实体
 * Created by Yokeyword on 2015/12/14.
 */
public class BucketEntity {

    private String name;
    private int count;
    private String path;

    public BucketEntity() {
    }

    public BucketEntity(String name, int count, String path) {
        this.name = name;
        this.count = count;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
