package com.liu.happygrow.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 记录android项目的类
 * Created by Liu on 2016/3/5.
 */
public class GanHuo {
    @SerializedName("_id")
    private String objectId;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private String who;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    @Override
    public String toString() {
        return type+":"+url;
    }
}
