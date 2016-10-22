package com.feeder.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
@Entity
public class Subscription {
    @Id
    private Long id;

    @Index(unique = true)
    private String key;
    
    private String title;

    private String iconUrl;

    private String url;

    private Long time;

    private String siteUrl;

    private String desc;

    private String category;

    private String sortid;

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSiteUrl() {
        return this.siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSortid() {
        return this.sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Generated(hash = 1919070078)
    public Subscription(Long id, String key, String title, String iconUrl, String url,
            Long time, String siteUrl, String desc, String category, String sortid) {
        this.id = id;
        this.key = key;
        this.title = title;
        this.iconUrl = iconUrl;
        this.url = url;
        this.time = time;
        this.siteUrl = siteUrl;
        this.desc = desc;
        this.category = category;
        this.sortid = sortid;
    }

    @Generated(hash = 1800298428)
    public Subscription() {
    }
}
